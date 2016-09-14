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
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @author vstrokova, 12.09.2016.
 */
@Service
public class ApplicationServiceImpl implements ApplicationService {

    private static final String UPLOAD_ZIP_PATH = "D:/Temp/gamestore/uploads/zip"; // TODO: move to config?
    private static final String UPLOAD_IMAGE_PATH = "D:/Temp/gamestore/uploads/images"; // TODO: how write file correctly to work in diff os?
    private static final String ZIP_FILE_EXTENSION = ".zip";
    private static final String ENCODING_UTF_8 = "UTF-8";

    @Autowired
    private ApplicationRepository applicationRepository;

    @Override
    public Application saveUploadedApplication(String userGivenName, String description, MultipartFile file) {
        // save file to the file system
        String filePath = saveApplicationFileToFileSystem(file);

        // deal with archive

        // create Application entity
        Application app = new Application(userGivenName, description);

        // save with saveApplication
        app = saveApplication(app);

        // return application
        return app;
    }

    @Transactional
    private Application saveApplication(Application application) {
        return applicationRepository.save(application);
    }

    private String saveApplicationFileToFileSystem(MultipartFile file) {
        Path uploadsPath = Paths.get(UPLOAD_ZIP_PATH);
        String filePath = null;
        try {
            if (Files.notExists(uploadsPath)) {
                Files.createDirectories(uploadsPath);
            }
            String fileName = file.getOriginalFilename();
            if (fileName != null && !fileName.isEmpty() && fileName.endsWith(ZIP_FILE_EXTENSION)) {
                filePath = uploadsPath + File.separator + fileName;
                file.transferTo(new File(filePath));
            }
        } catch (IOException e) {
            // TODO
        }
        return filePath;
    }

    // application file is a zip file
    private ApplicationDataFromTxtFile validateAndSaveApplicationFileContent(String applicationFilePath) {
        ApplicationDataFromTxtFile appData = new ApplicationDataFromTxtFile();

        // 1. find txt
        InputStream fileInputStream = findTxtFileAndGetInputStream(getZipInputStreamFrom(applicationFilePath));

        if (fileInputStream != null) {
            appData = fillApplicationDataFromTextFile(appData, fileInputStream);
            // 2. if txt contains name and package - this is the right file. if not - a wrong txt, user does not need it - reject
            if (appData.getName() != null && appData.getAppPackage() != null) {
                if (appData.getImage128Path() != null || appData.getImage512Path() != null) {
                    // 3. if the right txt contains picture_128 and/or picture_512 - check other entries of zip for their names. if found pic - save, if has other entries - reject


                }
                // return appData with name, package, picture_128/512 paths
                return appData;
            }
        }

        // zip is not valid
        return null;
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

    // @return null if txt file not found
    private static InputStream findTxtFileAndGetInputStream(ZipInputStream zipFileInputStream) {
        ZipEntry entry;
        String entryName;
        InputStream txtFileInputStream = null;

        try {
            while ((entry = zipFileInputStream.getNextEntry()) != null) {
                entryName = entry.getName();
                if (isTxt(entryName)) {
                    txtFileInputStream = zipFileInputStream;
                }
            }
        } catch (IOException e) {
            // TODO
        }

        return txtFileInputStream;
    }

    private static boolean isTxt(String fileName) {
        return fileName.endsWith(".txt");
    }

    private ApplicationDataFromTxtFile fillApplicationDataFromTextFile(
            ApplicationDataFromTxtFile appData, InputStream txtFileInputStream) {
        String line;
        final String nameField = "name:"; // TODO: do this elegantly (move to ApplicationDataFromTxtFile)
        final String packageField = "package:";
        final String image128Field = "picture_128:";
        final String image512Field = "picture_512:";

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(txtFileInputStream, ENCODING_UTF_8));
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(nameField)) {
                    appData.setName(line.substring(nameField.length()).trim());
                }
                if (line.startsWith(packageField)) {
                    appData.setAppPackage(line.substring(packageField.length()).trim());
                }
                if (line.startsWith(image128Field)) {
                    appData.setImage128Path(line.substring(image128Field.length()).trim());
                }
                if (line.startsWith(image512Field)) {
                    appData.setImage512Path(line.substring(image512Field.length()).trim());
                }
            }
        } catch (IOException e) {
            // TODO
        }

        return appData;
    }

    private ApplicationDataFromTxtFile saveImagesAndAddPathToApplicationData(
            ApplicationDataFromTxtFile appData, ZipInputStream zipFileInputStream) {

        String image128Name = appData.getImage128Path();
        String image512Name = appData.getImage512Path();

        // save images
        // update appData with their paths

        return appData;
    }

    private class ApplicationDataFromTxtFile {
        private String name;
        private String appPackage;
        private String image128Path;
        private String image512Path;

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
    }
}
