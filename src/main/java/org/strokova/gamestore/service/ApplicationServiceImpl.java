package org.strokova.gamestore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.strokova.gamestore.model.Application;
import org.strokova.gamestore.repository.ApplicationRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author vstrokova, 12.09.2016.
 */
@Service
public class ApplicationServiceImpl implements ApplicationService {

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
        Path uploadsPath = Paths.get("D:/Temp/gamestore/uploads");
        String filePath = null;
        try {
            if (Files.notExists(uploadsPath)) {
                Files.createDirectories(uploadsPath);
            }
            filePath = uploadsPath + "/" + file.getOriginalFilename();
            file.transferTo(new File(filePath));
        } catch (IOException e) {
            // TODO
        }
        return filePath;
    }
}
