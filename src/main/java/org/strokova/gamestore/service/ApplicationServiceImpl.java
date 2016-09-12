package org.strokova.gamestore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.strokova.gamestore.model.Application;
import org.strokova.gamestore.repository.ApplicationRepository;

/**
 * @author vstrokova, 12.09.2016.
 */
@Service
public class ApplicationServiceImpl implements ApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Override
    public <S extends Application> S saveUploadedApplication(String userGivenName, String description) {
        // create Application entity
        // save with saveApplication
        // return application
        return null;
    }

    @Transactional
    private <S extends Application> S saveApplication(S application) {
        return applicationRepository.save(application);
    }
}
