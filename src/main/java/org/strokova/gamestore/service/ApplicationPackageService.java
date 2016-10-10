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
import org.strokova.gamestore.util.PathsManager;

import java.io.*;
import java.nio.file.Files;
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

    @Autowired
    private UserService userService;
    @Autowired
    private ApplicationService applicationService;

    public Application saveUploadedApplication(String userGivenName, String description, Category appCategory, MultipartFile file, String username) {

        User user = userService.findUserByUsername(username);

        validateSaveUploadedApplicationRequiredParameters(userGivenName, appCategory, file);

        // save uploaded zip to temp dir
        Path tempDirectory = FileUtils.prepareTempDirectory(userGivenName);
        Path zipFileTmpPath = saveApplicationZipToTempDirectory(file, tempDirectory);
        if (zipFileTmpPath == null) {
            throw new InternalErrorException("Cannot save application zip to temporary directory");
        }

        ZipDescriptor zipDescriptor = handleZip(zipFileTmpPath, tempDirectory);

        Application application = new Application();

        if (isValidZipDescriptor(zipDescriptor)) { // TODO
            String packageName = zipDescriptor.getAppPackage();
            String appName = zipDescriptor.getName();
            Path permanentApplicationDirectory = FileUtils.preparePermanentDirectory(packageName, appName);

            // copy app zip from temp to permanent dir
            Path permanentZipPath = FileUtils.copyFile(zipFileTmpPath, permanentApplicationDirectory);

            application
                    .setAppPackage(packageName)
                    .setName(appName)
                    .setUserGivenName(userGivenName)
                    .setDescription(description)
                    .setCategory(appCategory)
                    .setFilePath(getRelativePathInUploadsWithCorrectSlash(permanentZipPath).toString());

            // copy app images from temp to permanent dir, if they exist
            File image128 = zipDescriptor.getImage128File();
            if (image128 != null) {
                Path permanentImage128Path = FileUtils.copyFile(image128.toPath(), permanentApplicationDirectory);
                application.setImage128Path(getRelativePathInUploadsWithCorrectSlash(permanentImage128Path).toString());
            }
            File image512 = zipDescriptor.getImage512File();
            if (image512 != null) {
                Path permanentImage512Path = FileUtils.copyFile(image512.toPath(), permanentApplicationDirectory);
                application.setImage512Path(getRelativePathInUploadsWithCorrectSlash(permanentImage512Path).toString());
            }

            // save app to DB
            applicationService.saveApplicationWithUser(application, user.getId());
        }

        // delete temp dir
        FileUtils.deleteFile(tempDirectory);

        // return application
        return application;
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

    private static Path getRelativePathInUploadsWithCorrectSlash(Path absolutePath) {
        return Paths.get(PathsManager.UPLOADS_DIR).relativize(absolutePath);
    }

    // return saved file location
    private static Path saveApplicationZipToTempDirectory(MultipartFile file, Path tempDir) {
        Path filePath;
        try {
            String fileName = file.getOriginalFilename();
            if (fileName == null || fileName.isEmpty()) {
                throw new IllegalArgumentException("Cannot read uploaded file name");
            }
            if (!FileUtils.isZip(fileName)) {
                throw new IllegalArgumentException("Uploaded file is not zip");
            }
            filePath = Paths.get(tempDir + File.separator + fileName);
            file.transferTo(new File(filePath.toString()));
        } catch (IOException e) {
            throw new InternalErrorException("Cannot save application zip to temporary directory", e);
        }
        return filePath;
    }

    private static File saveInputStreamAsFileToDirectory(InputStream is, Path dir, String fileName) {
        if (isInsideInnerFolderInZip(fileName)) {
            int pos = fileName.lastIndexOf(ZIP_INNER_FILE_SEPARATOR);
            dir = Paths.get(dir.toString() +
                    fileName.substring(0, pos).replace(ZIP_INNER_FILE_SEPARATOR, File.separator));
            fileName = fileName.substring(pos + 1);
        }

        if (Files.notExists(dir)) {
            try {
                Files.createDirectories(dir);
            } catch (IOException e) {
                throw new InternalErrorException("Cannot create directory: " + dir, e);
            }
        }

        File file = new File(dir + File.separator + fileName);
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

        try (ZipInputStream zis =
                     new ZipInputStream(new BufferedInputStream
                             (new FileInputStream(zipFilePath.toString())))) {
            while ((entry = zis.getNextEntry()) != null) {
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
                        // check entryFiles
                        processAlreadyReadEntriesAndAddToZipDescriptor(entryFiles, zipDescriptor, txtImage128, txtImage512);
                        // check remaining zip entries
                        processRemainingEntriesAndAddToZipDescriptor(zis, zipDescriptor, txtImage128, txtImage512, tempDir);
                    }

                } else {
                    entryFiles.put(entryName, saveInputStreamAsFileToDirectory(zis, tempDir, entryName));
                }
            }
        } catch (IOException e) {
            throw new InternalErrorException("Error parsing application zip");
        }

        return zipDescriptor;
    }

    private static ZipDescriptor processTxtAndAddToZipDescriptor(
            ZipInputStream txtFileInputStream, ZipDescriptor zipDescriptor) {
        String line;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(txtFileInputStream, ENCODING_UTF_8));
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(ZipDescriptor.TXT_NAME)) {
                    zipDescriptor.setName(line.substring(ZipDescriptor.TXT_NAME.length()).trim());
                    continue;
                }
                if (line.startsWith(ZipDescriptor.TXT_PACKAGE)) {
                    zipDescriptor.setAppPackage(line.substring(ZipDescriptor.TXT_PACKAGE.length()).trim());
                    continue;
                }
                if (line.startsWith(ZipDescriptor.TXT_IMAGE_128)) {
                    zipDescriptor.setImage128Name(line.substring(ZipDescriptor.TXT_IMAGE_128.length()).trim());
                    continue;
                }
                if (line.startsWith(ZipDescriptor.TXT_IMAGE_512)) {
                    zipDescriptor.setImage512Name(line.substring(ZipDescriptor.TXT_IMAGE_512.length()).trim());
                    continue;
                }
            }
        } catch (IOException e) {
            throw new InternalErrorException("Error reading text file in application zip");
        }

        return zipDescriptor;
    }

    private static ZipDescriptor processAlreadyReadEntriesAndAddToZipDescriptor(
            Map<String, File> zipEntryFiles, ZipDescriptor zipDescriptor, String image128Name, String image512Name) {

        for (Map.Entry<String, File> entryFile : zipEntryFiles.entrySet()) {
            String entryFileName = entryFile.getKey();
            String entryFileNameWithoutExtension = entryFileName.substring(0, entryFileName.lastIndexOf("."));
            if (image128Name != null && image128Name.equals(entryFileNameWithoutExtension)) {
                zipDescriptor.setImage128Name(image128Name);
                zipDescriptor.setImage128File(entryFile.getValue());
            }
            if (image512Name != null && image512Name.equals(entryFileNameWithoutExtension)) {
                zipDescriptor.setImage512Name(image512Name);
                zipDescriptor.setImage512File(entryFile.getValue());
            }
            // TODO check if is image
        }

        return zipDescriptor;
    }

    private static ZipDescriptor processRemainingEntriesAndAddToZipDescriptor(
            ZipInputStream zis, ZipDescriptor zipDescriptor, String image128Name, String image512Name, Path dir) {

        ZipEntry entry;
        String entryName;

        try {
            while ((entry = zis.getNextEntry()) != null) {
                entryName = entry.getName();
                String entryNameWithoutExtension = entryName.substring(0, entryName.lastIndexOf("."));
                if (image128Name != null && image128Name.equals(entryNameWithoutExtension)) {
                    zipDescriptor.setImage128Name(image128Name);
                    zipDescriptor.setImage128File(saveInputStreamAsFileToDirectory(zis, dir, entryName));
                }
                if (image512Name != null && image512Name.equals(entryNameWithoutExtension)) {
                    zipDescriptor.setImage512Name(image512Name);
                    zipDescriptor.setImage512File(saveInputStreamAsFileToDirectory(zis, dir, entryName));
                }
                // TODO check if is image
            }
        } catch (IOException e) {
            throw new InternalErrorException("Error parsing application zip", e);
        }

        return zipDescriptor;
    }

    private static boolean isValidZipDescriptor(ZipDescriptor zipDescriptor) {
        // TODO: check descriptor here
        return true;
    }

    private class ZipDescriptor {
        private String name;
        private String appPackage;
        private String image128Name;
        private String image512Name;
        private File image128File;
        private File image512File;

        public static final String TXT_NAME = "name:";
        public static final String TXT_PACKAGE = "package:";
        public static final String TXT_IMAGE_128 = "picture_128:";
        public static final String TXT_IMAGE_512 = "picture_512:";

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAppPackage() {
            return appPackage;
        } // TODO: can be private?

        public void setAppPackage(String appPackage) {
            this.appPackage = appPackage;
        }

        public File getImage128File() {
            return image128File;
        }

        private void setImage128File(File image128File) {
            this.image128File = image128File;
        }

        public File getImage512File() {
            return image512File;
        }

        public void setImage512File(File image512File) {
            this.image512File = image512File;
        }

        public String getImage128Name() {
            return image128Name;
        }

        public void setImage128Name(String image128Name) {
            this.image128Name = image128Name;
        }

        public String getImage512Name() {
            return image512Name;
        }

        public void setImage512Name(String image512Name) {
            this.image512Name = image512Name;
        }
    }
}
