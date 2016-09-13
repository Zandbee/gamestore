package org.strokova.gamestore.service;

import org.springframework.stereotype.Service;
import org.strokova.gamestore.model.Application;

/**
 * @author vstrokova, 12.09.2016.
 */
@Service
public interface ApplicationService {
    Application saveUploadedApplication(String userGivenName, String description);
}
