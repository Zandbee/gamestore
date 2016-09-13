package org.strokova.gamestore.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.strokova.gamestore.model.Application;

import java.io.IOException;

/**
 * @author vstrokova, 12.09.2016.
 */
@Service
public interface ApplicationService {
    Application saveUploadedApplication(String userGivenName, String description, MultipartFile file) throws IOException;
}
