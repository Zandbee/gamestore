package org.strokova.gamestore.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.strokova.gamestore.exception.*;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author vstrokova, 07.10.2016.
 */
@ControllerAdvice
public class GamestoreExceptionHandler {

    private static final Logger logger = Logger.getLogger(GamestoreExceptionHandler.class.getName());

    private static final String PAGE_APPLICATION_NOT_FOUND = "error/applicationNotFound";
    private static final String PAGE_FILE_TRANSFER_ERROR = "error/fileTransferError";
    private static final String PAGE_AUTHORIZATION_REQUIRED = "error/authorizationRequired";
    private static final String PAGE_USER_NOT_FOUND = "error/userNotFound";
    private static final String PAGE_SOMETHING_WENT_WRONG = "error/somethingWentWrong";
    private static final String PAGE_INVALID_APPLICATION_FILE = "error/invalidApplicationFile";

    @ExceptionHandler({ApplicationNotFoundException.class, ApplicationFileNotFoundException.class})
    public String handleApplicationNotFound(Exception e) {
        logger.log(Level.SEVERE, "Application not found", e);
        return PAGE_APPLICATION_NOT_FOUND;
    }

    @ExceptionHandler(FileTransferException.class)
    public String handleFileTransferError(Exception e) {
        logger.log(Level.SEVERE, "File transfer error", e);
        return PAGE_FILE_TRANSFER_ERROR;
    }

    @ExceptionHandler(AuthorizationRequiredException.class)
    public String handleAuthorizationRequiredError(Exception e) {
        logger.log(Level.SEVERE, "Authorization required", e);
        return PAGE_AUTHORIZATION_REQUIRED;
    }

    @ExceptionHandler(UserNotFoundException.class)
    public String handleUserNotFound(Exception e) {
        logger.log(Level.SEVERE, "User not found", e);
        return PAGE_USER_NOT_FOUND;
    }

    @ExceptionHandler(InvalidApplicationFileException.class)
    public String handleInvalidApplicationFile(Exception e) {
        logger.log(Level.SEVERE, "Invalid application file", e);
        return PAGE_INVALID_APPLICATION_FILE;
    }

    //any other exception handler
    @ExceptionHandler(Exception.class)
    public String handleAnyOtherException(Exception e) {
        logger.log(Level.SEVERE, "Error occurred", e);
        return PAGE_SOMETHING_WENT_WRONG;
    }
}
