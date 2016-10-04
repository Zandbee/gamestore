package org.strokova.gamestore.service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.strokova.gamestore.model.Application;
import org.strokova.gamestore.model.Category;

import java.io.IOException;
import java.util.List;

/**
 * @author vstrokova, 12.09.2016.
 */
@Service
public interface ApplicationService {
    Application saveUploadedApplication(String userGivenName, String description, Category category, MultipartFile file) throws IOException;
    Page<Application> findMostPopularApplications();
    Page<Application> findApplicationsPage(int pageNum, String category);
    int getPageCount(String category);
}
