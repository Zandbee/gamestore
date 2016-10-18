package org.strokova.gamestore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.strokova.gamestore.exception.ApplicationNotFoundException;
import org.strokova.gamestore.model.Application;
import org.strokova.gamestore.model.Category;
import org.strokova.gamestore.model.User;
import org.strokova.gamestore.repository.ApplicationRepository;
import org.strokova.gamestore.repository.UserRepository;
import org.strokova.gamestore.util.PathsManager;

import java.io.*;
import java.nio.channels.IllegalSelectorException;
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
public class ApplicationService {

    private static final int POPULAR_PAGE_SIZE = 5;
    private static final int PAGE_SIZE = 3;
    private static final String APPLICATION_FIELD_NAME_DOWNLOAD_NUMBER = "downloadNumber";
    private static final String APPLICATION_FIELD_NAME_TIME_UPLOADED = "timeUploaded";

    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;

    @Autowired
    public ApplicationService(ApplicationRepository applicationRepository, UserRepository userRepository) {
        this.applicationRepository = applicationRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Application saveApplicationWithUser(Application application, int userId) {

        User user = userRepository.findOne(userId);
        application.setUser(user);
        user.getApplications().add(application);

        return applicationRepository.save(application);
    }

    @Transactional(readOnly = true)
    public Application getApplicationById(int applicationId) {
        Application application = applicationRepository.findOne(applicationId);

        if (application == null) {
            throw new ApplicationNotFoundException("Application with ID = " + applicationId + " was not found");
        }

        return application;
    }

    @Transactional(readOnly = true)
    public Page<Application> findMostPopularApplications() {
        PageRequest request = new PageRequest(0, POPULAR_PAGE_SIZE, Sort.Direction.DESC,
                APPLICATION_FIELD_NAME_DOWNLOAD_NUMBER, APPLICATION_FIELD_NAME_TIME_UPLOADED);
        return applicationRepository.findAll(request);
    }

    @Transactional(readOnly = true)
    public Application findByPackageAndName(String appPackage, String name) {
        return applicationRepository.findByAppPackageAndName(appPackage, name);
    }

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
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
}
