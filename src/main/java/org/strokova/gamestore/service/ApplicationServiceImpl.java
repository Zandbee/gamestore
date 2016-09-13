package org.strokova.gamestore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.strokova.gamestore.model.Application;
import org.strokova.gamestore.repository.ApplicationRepository;

import java.io.File;
import java.io.IOException;

/**
 * @author vstrokova, 12.09.2016.
 */
@Service
public class ApplicationServiceImpl implements ApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Override
    public Application saveUploadedApplication(String userGivenName, String description, MultipartFile file) {
        try {
            file.transferTo(new File("/data/files/" + file.getOriginalFilename()));
        } catch (IOException e) {
            // TODO
        }
        
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
}
