package org.strokova.gamestore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.strokova.gamestore.model.Application;
import org.strokova.gamestore.repository.ApplicationRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @author vstrokova, 12.09.2016.
 */
@Service
public class ApplicationServiceImpl implements ApplicationService {

    private static final String UPLOAD_ZIP_PATH = "D:/Temp/gamestore/uploads/zip"; // TODO: move to config?
    private static final String UPLOAD_IMAGE_PATH = "D:/Temp/gamestore/uploads/images"; // TODO: how write file correctly to work in diff os?
    private static final String UPLOADS_TEMP_PATH = "D:/Temp/gamestore/uploads/tmp";
    private static final String ZIP_FILE_EXTENSION = ".zip";
    private static final String ZIP_INNER_FILE_SEPARATOR = "/";
    private static final String ENCODING_UTF_8 = "UTF-8";

    @Autowired
    private ApplicationRepository applicationRepository;

    @Override
    public Application saveUploadedApplication(String userGivenName, String description, MultipartFile file) {
        // save uploaded zip to temp dir
        Path tempDirectory = prepareTempDirectory(userGivenName);
        String zipFileTmpPath = saveApplicationZipToTempDirectory(file, tempDirectory);

        handleZip(getZipInputStreamFrom(zipFileTmpPath), tempDirectory);

        // delete temp dir

        // return application
        return null; // TODO: return application
    }

    @Transactional
    private Application saveApplication(Application application) {
        return applicationRepository.save(application);
    }

    // return saved file location
    private String saveApplicationZipToTempDirectory(MultipartFile file, Path tempDir) {
        String tempFilePath = null;
        try {
            String fileName = file.getOriginalFilename();
            if (fileName != null && !fileName.isEmpty() && fileName.endsWith(ZIP_FILE_EXTENSION)) { // TODO: do smth if not zip (already restricting in html?
                tempFilePath = tempDir + File.separator + fileName;
                file.transferTo(new File(tempFilePath));
            }
        } catch (IOException e) {
            // TODO
        }
        return tempFilePath;
    }

    private File saveInputStreamAsFileToDirectory(InputStream is, Path dir, String fileName) {
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

    private void handleZip(ZipInputStream zis, Path tempDir) {
        ZipEntry entry;
        String entryName;
        ZipDescriptor zipDescriptor = new ZipDescriptor();
        Map<String, File> entryFiles = new HashMap<>();

        try {
            while ((entry = zis.getNextEntry()) != null) {
                entryName = entry.getName();
                if (isTxt(entryName)) {

                } else {
                    entryFiles.put(entryName, saveInputStreamAsFileToDirectory(zis, tempDir, entryName));
                }
            }
        } catch (IOException e) {
            // TODO
        }
    }

    private static ZipInputStream getZipInputStreamFrom(String filePath) {
        ZipInputStream zis = null;
        try {
            zis = new ZipInputStream(new BufferedInputStream(new FileInputStream(filePath)));
        } catch (FileNotFoundException e) {
            // TODO
        }
        return zis;
    }

    private static Path prepareTempDirectory(String userGivenName) {
        Path tempDir = Paths.get(UPLOADS_TEMP_PATH + File.separator + userGivenName);
        if (Files.notExists(tempDir)) {
            try {
                Files.createDirectories(tempDir);
            } catch (IOException e) {
                // TODO
            }
        }
        return tempDir;
    }

    private static boolean isTxt(String fileName) {
        return fileName.endsWith(".txt");
    }

    private static boolean isImage(Path imagePath) throws IOException {
        return Files.probeContentType(imagePath).startsWith("image"); // TODO: "image/"?;
    }

    private class ZipDescriptor {
        private String name;
        private String appPackage;
        private String image128Path;
        private String image512Path;
        private File image128File;
        private File image512File;

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

        public String getImage128Path() {
            return image128Path;
        }

        public void setImage128Path(String image128Path) {
            this.image128Path = image128Path;
        }

        public String getImage512Path() {
            return image512Path;
        }

        public void setImage512Path(String image512Path) {
            this.image512Path = image512Path;
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
    }
}
