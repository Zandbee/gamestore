package org.strokova.gamestore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.strokova.gamestore.exception.InternalErrorException;
import org.strokova.gamestore.exception.InvalidApplicationFileException;
import org.strokova.gamestore.model.Application;
import org.strokova.gamestore.model.Category;
import org.strokova.gamestore.model.User;
import org.strokova.gamestore.util.FileUtils;
import org.strokova.gamestore.util.PathUtils;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * author: Veronika, 10/6/2016.
 */
@Service
public class ApplicationPackageService {

    private static final String ZIP_INNER_FILE_SEPARATOR = "/";
    private static final String ENCODING_UTF_8 = "UTF-8";
    private static final int MAX_ZIP_ENTRIES_NUMBER = 3;

    private final UserService userService;
    private final ApplicationService applicationService;

    @Autowired
    public ApplicationPackageService (UserService userService, ApplicationService applicationService) {
        this.userService = userService;
        this.applicationService = applicationService;
    }

    public Application saveUploadedApplication(String userGivenName, String description, Category appCategory, MultipartFile file, String username) {
        User user = userService.getUserByUsername(username);

        validateSaveUploadedApplicationRequiredParameters(userGivenName, appCategory, file);

        // save uploaded zip to temp dir
        Path tempDirectory = FileUtils.prepareTempDirectory(userGivenName);
        Path zipFileTmpPath = saveApplicationZipToTempDirectory(file, tempDirectory);

        ZipDescriptor zipDescriptor = handleZip(zipFileTmpPath, tempDirectory);

        try {
            String packageName = zipDescriptor.getAppPackage();
            String appName = zipDescriptor.getName();

            Path permanentApplicationDirectory = FileUtils.preparePermanentDirectory(packageName, appName);
            Path permanentZipPath = FileUtils.copyFile(zipFileTmpPath, permanentApplicationDirectory);

            Application application = applicationService.findByPackageAndName(packageName, appName);
            if (application == null) {
                application = new Application();
            }
            setupApplication(application, userGivenName, description, appCategory, packageName, appName, permanentZipPath);

            // copy app images from temp to permanent dir, if they exist
            application.setImage128Path(copyTo(zipDescriptor.getImage128File(), permanentApplicationDirectory));
            application.setImage512Path(copyTo(zipDescriptor.getImage512File(), permanentApplicationDirectory));

            applicationService.saveApplicationWithUser(application, user.getId());

            return application;
        } finally {
            FileUtils.deleteFile(tempDirectory);
        }
    }

    private static void setupApplication(Application application, String userGivenName, String description, Category appCategory, String packageName, String appName, Path permanentZipPath) {
        application
                .setAppPackage(packageName)
                .setName(appName)
                .setUserGivenName(userGivenName)
                .setDescription(description)
                .setCategory(appCategory)
                .setFilePath(getRelativePathInUploads(permanentZipPath).toString());
    }

    private static String copyTo(File f, Path dir) {
        if (f == null) {
            return null;
        }
        Path p = FileUtils.copyFile(f.toPath(), dir);
        return getRelativePathInUploads(p).toString();
    }

    private static void validateSaveUploadedApplicationRequiredParameters(String userGivenName, Category appCategory, MultipartFile file) {
        if (userGivenName == null || userGivenName.isEmpty()) {
            throw new IllegalArgumentException("userGivenName is null or empty");
        }
        if (appCategory == null) {
            throw new IllegalArgumentException("No application category");
        }
        if (file == null) {
            throw new IllegalArgumentException("No application file");
        }
    }

    private static Path getRelativePathInUploads(Path absolutePath) {
        return Paths.get(PathUtils.UPLOADS_DIR).relativize(absolutePath);
    }

    // return saved file location
    private static Path saveApplicationZipToTempDirectory(MultipartFile file, Path tempDir) {
        try {
            String fileName = file.getOriginalFilename();
            if (fileName == null || fileName.isEmpty()) {
                throw new IllegalArgumentException("Cannot read uploaded file name");
            }
            if (!FileUtils.isZip(fileName)) {
                throw new IllegalArgumentException("Uploaded file is not zip");
            }
            Path filePath = Paths.get(tempDir + File.separator + fileName);
            file.transferTo(new File(filePath.toString()));
            return filePath;
        } catch (IOException e) {
            throw new InternalErrorException("Cannot save application zip to temporary directory", e);
        }
    }

    private static File saveInputStreamAsImageToDirectory(InputStream is, Path dir, String fileName) {
        if (isInsideInnerFolderInZip(fileName)) {
            int pos = fileName.lastIndexOf(ZIP_INNER_FILE_SEPARATOR);
            dir = Paths.get(dir.toString() +
                    fileName.substring(0, pos).replace(ZIP_INNER_FILE_SEPARATOR, File.separator));
            fileName = fileName.substring(pos + 1);
        }

        dir = FileUtils.createDirectoryIfNotExist(dir);

        File file = new File(dir + File.separator + fileName);

        if (!FileUtils.isImage(file.toPath())) {
            throw new InvalidApplicationFileException("There are odd files in an application - zip entry is not an image");
        }


        byte[] buf = new byte[2048];
        try (FileOutputStream fos = new FileOutputStream(file)) {
            int length;
            while ((length = is.read(buf)) > 0) {
                fos.write(buf, 0, length);
            }
        } catch (IOException e) {
            throw new InternalErrorException("Cannot save file: " + file, e);
        }

        return file;
    }

    private static boolean isInsideInnerFolderInZip(String fileName) {
        return fileName.contains(ZIP_INNER_FILE_SEPARATOR);
    }

    private ZipDescriptor handleZip(Path zipFilePath, Path tempDir) {
        ZipEntry entry;
        String entryName;
        ZipDescriptor zipDescriptor = new ZipDescriptor();
        String txtImage128, txtImage512;
        Map<String, File> entryFiles = new HashMap<>();
        int zipEntriesCounter = 0;

        try (ZipInputStream zis =
                     new ZipInputStream(new BufferedInputStream
                             (new FileInputStream(zipFilePath.toString())))) {
            while ((entry = zis.getNextEntry()) != null) {
                if (containsOddFiles(++zipEntriesCounter)) {
                    throw new InvalidApplicationFileException("Application zip contains more files than needed");
                }
                entryName = entry.getName();
                if (FileUtils.isTxt(entryName)) {
                    processTxtAndAddToZipDescriptor(zis, zipDescriptor);
                    if (zipDescriptor.getName() == null || zipDescriptor.getAppPackage() == null) {
                        throw new InvalidApplicationFileException("Text file in application zip must contain 'name' and 'package' fields");
                    }

                    // images are optional
                    txtImage128 = zipDescriptor.getImage128Name();
                    txtImage512 = zipDescriptor.getImage512Name();

                    if (txtImage128 != null || txtImage512 != null) {
                        // check already extracted zip entries
                        processAlreadyReadEntriesAndAddToZipDescriptor(entryFiles, zipDescriptor, txtImage128, txtImage512);
                        // check remaining zip entries
                        processRemainingEntriesAndAddToZipDescriptor(zis, zipEntriesCounter, zipDescriptor, txtImage128, txtImage512, tempDir);
                    }

                } else {
                    // assume it is an image while saving
                    entryFiles.put(entryName, saveInputStreamAsImageToDirectory(zis, tempDir, entryName));
                }
            }
        } catch (IOException e) {
            throw new InternalErrorException("Error parsing application zip");
        } catch (IllegalArgumentException e) {
            throw new InvalidApplicationFileException("Something wrong with zip file", e);
        }

        return zipDescriptor;
    }

    private static ZipDescriptor processTxtAndAddToZipDescriptor(
            ZipInputStream txtFileInputStream, ZipDescriptor zipDescriptor) {
        String line;
        try {
            // not closing BufferedReader here in order not to close ZipInputStream. closing ZipInputSteam in handleZip()
            BufferedReader reader = new BufferedReader(new InputStreamReader(txtFileInputStream, ENCODING_UTF_8));
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(ZipDescriptor.TXT_NAME)) {
                    zipDescriptor.setName(extractFieldValue(line, ZipDescriptor.TXT_NAME));
                } else if (line.startsWith(ZipDescriptor.TXT_PACKAGE)) {
                    zipDescriptor.setAppPackage(extractFieldValue(line, ZipDescriptor.TXT_PACKAGE));
                } else if (line.startsWith(ZipDescriptor.TXT_IMAGE_128)) {
                    zipDescriptor.setImage128Name(extractFieldValue(line, ZipDescriptor.TXT_IMAGE_128));
                } else if (line.startsWith(ZipDescriptor.TXT_IMAGE_512)) {
                    zipDescriptor.setImage512Name(extractFieldValue(line, ZipDescriptor.TXT_IMAGE_512));
                }
            }
        } catch (IOException e) {
            throw new InternalErrorException("Error reading text file in application zip");
        }

        return zipDescriptor;
    }

    private static String extractFieldValue(String txtLine, String field) {
        return txtLine.substring(field.length()).trim();
    }

    private static ZipDescriptor processAlreadyReadEntriesAndAddToZipDescriptor(
            Map<String, File> zipEntryFiles, ZipDescriptor zipDescriptor, String txtImage128Name, String txtImage512Name) {

        for (Map.Entry<String, File> entryFile : zipEntryFiles.entrySet()) {
            processFileFromZip(zipDescriptor, txtImage128Name, txtImage512Name, entryFile);
        }

        return zipDescriptor;
    }

    private static void processFileFromZip(
            ZipDescriptor zipDescriptor, String txtImage128Name, String txtImage512Name, Map.Entry<String, File> entryFile) {
        String entryFileName = entryFile.getKey();
        if (imageNameFromTxtEqualsZipEntryName(txtImage128Name, entryFileName)) {
            zipDescriptor.setImage128Name(txtImage128Name);
            zipDescriptor.setImage128File(entryFile.getValue());
        } else if (imageNameFromTxtEqualsZipEntryName(txtImage512Name, entryFileName)) {
            zipDescriptor.setImage512Name(txtImage512Name);
            zipDescriptor.setImage512File(entryFile.getValue());
        } else {
            throw new InvalidApplicationFileException
                    ("Application zip contains odd files - According to TXT, there should be no image with name: " + entryFileName);
        }
    }

    private static ZipDescriptor processRemainingEntriesAndAddToZipDescriptor(
            ZipInputStream zis, int zipEntriesCounter, ZipDescriptor zipDescriptor, String txtImage128Name, String txtImage512Name, Path dir) {

        ZipEntry entry;
        try {
            while ((entry = zis.getNextEntry()) != null) {
                if (containsOddFiles(++zipEntriesCounter)) {
                    throw new InvalidApplicationFileException("Application zip contains more files than needed");
                }
                processEntryFromZip(zis, zipDescriptor, txtImage128Name, txtImage512Name, dir, entry);
            }
        } catch (IOException e) {
            throw new InternalErrorException("Error parsing application zip", e);
        }

        return zipDescriptor;
    }

    private static void processEntryFromZip(
            ZipInputStream zis, ZipDescriptor zipDescriptor, String txtImage128Name, String txtImage512Name, Path dir, ZipEntry entry) {
        String entryName = entry.getName();
        if (imageNameFromTxtEqualsZipEntryName(txtImage128Name, entryName)) {
            zipDescriptor.setImage128Name(txtImage128Name);
            zipDescriptor.setImage128File(saveInputStreamAsImageToDirectory(zis, dir, entryName));
        } else if (imageNameFromTxtEqualsZipEntryName(txtImage512Name, entryName)) {
            zipDescriptor.setImage512Name(txtImage512Name);
            zipDescriptor.setImage512File(saveInputStreamAsImageToDirectory(zis, dir, entryName));
        } else {
            throw new InvalidApplicationFileException
                    ("Application zip contains odd files - According to TXT, there should be no image with name: " + entryName);
        }
    }

    private static boolean imageNameFromTxtEqualsZipEntryName(String nameFromTxt, String nameFromZip) {
        String nameFromZipWithoutExtension = nameFromZip.substring(0, nameFromZip.lastIndexOf("."));
        return nameFromTxt != null &&
                (nameFromTxt.equals(nameFromZipWithoutExtension) || nameFromTxt.equals(nameFromZip));
    }

    // checks if zip file contains more entries than needed
    private static boolean containsOddFiles(int zipEntriesCounter) {
        return zipEntriesCounter > MAX_ZIP_ENTRIES_NUMBER;
    }

    private static class ZipDescriptor {
        private String name;
        private String appPackage;
        private String image128Name;
        private String image512Name;
        private File image128File;
        private File image512File;

        private static final String TXT_NAME = "name:";
        private static final String TXT_PACKAGE = "package:";
        private static final String TXT_IMAGE_128 = "picture_128:";
        private static final String TXT_IMAGE_512 = "picture_512:";

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        private String getAppPackage() {
            return appPackage;
        }

        private void setAppPackage(String appPackage) {
            this.appPackage = appPackage;
        }

        private File getImage128File() {
            return image128File;
        }

        private void setImage128File(File image128File) {
            this.image128File = image128File;
        }

        private File getImage512File() {
            return image512File;
        }

        private void setImage512File(File image512File) {
            this.image512File = image512File;
        }

        private String getImage128Name() {
            return image128Name;
        }

        private void setImage128Name(String image128Name) {
            this.image128Name = image128Name;
        }

        private String getImage512Name() {
            return image512Name;
        }

        private void setImage512Name(String image512Name) {
            this.image512Name = image512Name;
        }
    }
}
