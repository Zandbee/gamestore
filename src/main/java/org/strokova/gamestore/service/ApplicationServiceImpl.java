package org.strokova.gamestore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.strokova.gamestore.model.Application;
import org.strokova.gamestore.model.Category;
import org.strokova.gamestore.repository.ApplicationRepository;
import org.strokova.gamestore.util.PathsManager;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @author vstrokova, 12.09.2016.
 */
@Service
public class ApplicationServiceImpl implements ApplicationService {

    private static final String ZIP_FILE_EXTENSION = ".zip";
    private static final String ZIP_INNER_FILE_SEPARATOR = "/";
    private static final String ENCODING_UTF_8 = "UTF-8";
    private static final int POPULAR_PAGE_SIZE = 5;
    private static final int PAGE_SIZE = 3;
    private static final String APPLICATION_FIELD_NAME_DOWNLOAD_NUMBER = "downloadNumber";
    private static final String APPLICATION_FIELD_NAME_TIME_UPLOADED = "timeUploaded";

    @Autowired
    private ApplicationRepository applicationRepository;

    @Override
    public Application saveUploadedApplication(String userGivenName, String description, Category appCategory, MultipartFile file) {
        // save uploaded zip to temp dir
        Path tempDirectory = prepareTempDirectory(userGivenName);
        Path zipFileTmpPath = saveApplicationZipToTempDirectory(file, tempDirectory);

        ZipDescriptor zipDescriptor = handleZip(zipFileTmpPath, tempDirectory);

        Application application = new Application();

        if (isValidZipDescriptor(zipDescriptor)) {
            String packageName = zipDescriptor.getAppPackage();
            String appName = zipDescriptor.getName();
            Path permanentApplicationDirectory = preparePermanentDirectory(packageName, appName);

            // copy app zip from temp to permanent dir
            Path permanentZipPath = copyFile(zipFileTmpPath, permanentApplicationDirectory);

            application
                    .setAppPackage(packageName)
                    .setName(appName)
                    .setUserGivenName(userGivenName)
                    .setDescription(description)
                    .setCategory(appCategory)
                    .setFilePath(getRelativePathInUploadsWithCorrectSlash(permanentZipPath).toString())
                    .setDownloadNumber(0); // TODO: default value 0 in mysql does not work

            // copy app images from temp to permanent dir, if they exist
            File image128 = zipDescriptor.getImage128File();
            File image512 = zipDescriptor.getImage512File();
            if (image128 != null) {
                Path permanentImage128Path = copyFile(image128.toPath(), permanentApplicationDirectory);
                application.setImage128Path(getRelativePathInUploadsWithCorrectSlash(permanentImage128Path).toString());
            }
            if (image512 != null) {
                Path permanentImage512Path = copyFile(image512.toPath(), permanentApplicationDirectory);
                application.setImage512Path(getRelativePathInUploadsWithCorrectSlash(permanentImage512Path).toString());
            }

            // save app to DB
            saveApplication(application);
        }

        // delete temp dir
        deleteFiles(tempDirectory);

        // return application
        return application;
    }

    private static void deleteFiles(Path dir) {
        deleteFile(new File(dir.toString()));
    }

    private static void deleteFile(File file) {
        File[] content = file.listFiles();
        if (content != null) {
            for (File f : content) {
                deleteFile(f);
            }
        }
        file.delete();
    }

    // returns permanent file path
    private static Path copyFile(Path src, Path dest) {
        Path absoluteFilePath = dest.resolve(src.getFileName());
        try {
            Files.copy(src, absoluteFilePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            // TODO
        }
        return absoluteFilePath;
    }

    private static Path getRelativePathInUploadsWithCorrectSlash(Path absolutePath) {
        return Paths.get(PathsManager.UPLOADS_DIR).relativize(absolutePath);
    }

    @Transactional
    private Application saveApplication(Application application) {
        return applicationRepository.save(application);
    }

    // return saved file location
    private Path saveApplicationZipToTempDirectory(MultipartFile file, Path tempDir) {
        Path filePath = null;
        try {
            String fileName = file.getOriginalFilename();
            if (fileName != null && !fileName.isEmpty() && fileName.endsWith(ZIP_FILE_EXTENSION)) { // TODO: do smth if not zip (already restricting in html?
                filePath = Paths.get(tempDir + File.separator + fileName);
                file.transferTo(new File(filePath.toString()));
            }
        } catch (IOException e) {
            // TODO
        }
        return filePath;
    }

    private static File saveInputStreamAsFileToDirectory(InputStream is, Path dir, String fileName) {
        if (fileName.contains(ZIP_INNER_FILE_SEPARATOR)) { // TODO: this is not pretty
            int pos = fileName.lastIndexOf(ZIP_INNER_FILE_SEPARATOR);
            dir = Paths.get(dir.toString() +
                    fileName.substring(0, pos).replace(ZIP_INNER_FILE_SEPARATOR, File.separator));
            fileName = fileName.substring(pos + 1);
        }

        if (Files.notExists(dir)) {
            try {
                Files.createDirectories(dir);
            } catch (IOException e) {
                // TODO
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
            // TODO
        }

        return file;
    }

    private ZipDescriptor handleZip(Path zipFilePath, Path tempDir) {
        ZipEntry entry;
        String entryName;
        ZipDescriptor zipDescriptor = new ZipDescriptor();
        String txtImage128, txtImage512;
        Map<String, File> entryFiles = new HashMap<>();

        try (ZipInputStream zis =
                     new ZipInputStream(new BufferedInputStream
                             (new FileInputStream(zipFilePath.toString())))){
            while ((entry = zis.getNextEntry()) != null) {
                entryName = entry.getName();
                if (isTxt(entryName)) {
                    processTxtAndAddToZipDescriptor(zis, zipDescriptor);
                    if (zipDescriptor.getName() == null || zipDescriptor.getAppPackage() == null) {
                        // this is not a valid txt - invalid zip - return
                    } else {
                        txtImage128 = zipDescriptor.getImage128Name();
                        txtImage512 = zipDescriptor.getImage512Name();

                        if (txtImage128 != null || txtImage512 != null) {
                            // check entryFiles
                            processAlreadyReadEntriesAndAddToZipDescriptor(entryFiles, zipDescriptor, txtImage128, txtImage512);

                            // check remaining zis' entries
                            processRemainingEntriesAndAddToZipDescriptor(zis, zipDescriptor, txtImage128, txtImage512, tempDir);
                        }
                    }
                } else {
                    entryFiles.put(entryName, saveInputStreamAsFileToDirectory(zis, tempDir, entryName));
                }
            }
        } catch (IOException e) {
            // TODO
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
            // TODO
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
            // TODO
        }

        return zipDescriptor;
    }

    private static Path prepareTempDirectory(String userGivenName) {
        Path tempDir = Paths.get(PathsManager.UPLOADS_TEMP_DIR + File.separator + userGivenName);
        if (Files.notExists(tempDir)) {
            try {
                Files.createDirectories(tempDir);
            } catch (IOException e) {
                // TODO
            }
        }
        return tempDir;
    }

    private static Path preparePermanentDirectory(String packageName, String appName) {
        Path permDir = Paths.get(PathsManager.UPLOADS_DIR +
                File.separator + packageName + File.separator + appName);
        if (Files.notExists(permDir)) {
            try {
                Files.createDirectories(permDir);
            } catch (IOException e) {
                // TODO
            }
        }
        return permDir;
    }

    private static boolean isTxt(String fileName) {
        return fileName.endsWith(".txt");
    }

    private static boolean isImage(Path imagePath) throws IOException {
        return Files.probeContentType(imagePath).startsWith("image"); // TODO: "image/"?;
    }

    private static boolean isValidZipDescriptor(ZipDescriptor zipDescriptor) {
        // TODO: check descriptor here
        return true;
    }

    @Override
    public Page<Application> findMostPopularApplications() {
        PageRequest request = new PageRequest(0, POPULAR_PAGE_SIZE, Sort.Direction.DESC,
                APPLICATION_FIELD_NAME_DOWNLOAD_NUMBER, APPLICATION_FIELD_NAME_TIME_UPLOADED);
        return applicationRepository.findAll(request);
    }

    @Override
    public Page<Application> findApplicationsPage(int pageNum, String category) {
        if (pageNum != 0) {
            --pageNum;
        }
        PageRequest request = new PageRequest(pageNum, PAGE_SIZE, Sort.Direction.DESC, APPLICATION_FIELD_NAME_TIME_UPLOADED);
        if (category == null || category.isEmpty()) {
            return applicationRepository.findAll(request);
        } else {
            return applicationRepository.findByCategory(request, Category.valueOf(category));
        }
    }

    @Override
    public int getPageCount(String category) {
        long applicationsTotalNum;
        if (category == null || category.isEmpty()) {
            applicationsTotalNum = applicationRepository.count();
        } else {
            applicationsTotalNum = applicationRepository.countByCategory(Category.valueOf(category));
        }
        int pageCount = (int) (applicationsTotalNum / PAGE_SIZE);
        if (applicationsTotalNum % PAGE_SIZE != 0) {
            ++pageCount;
        }
        return pageCount;
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
        }

        public void setAppPackage(String appPackage) {
            this.appPackage = appPackage;
        }

        public File getImage128File() {
            return image128File;
        }

        public void setImage128File(File image128File) {
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
