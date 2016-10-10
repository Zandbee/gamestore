package org.strokova.gamestore.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.strokova.gamestore.exception.*;

/**
 * @author vstrokova, 07.10.2016.
 */
@ControllerAdvice
public class GamestoreExceptionHandler {

    // TODO: add logger.log(e.stackTrace) in each handler
    // TODO: TRACE logging for DB

    private static final String PAGE_APPLICATION_NOT_FOUND = "error/applicationNotFound";
    private static final String PAGE_FILE_TRANSFER_ERROR = "error/fileTransferError";
    private static final String PAGE_AUTHORIZATION_REQUIRED = "error/authorizationRequired";
    private static final String PAGE_USER_NOT_FOUND = "error/userNotFound";
    private static final String PAGE_SOMETHING_WENT_WRONG = "error/somethingWentWrong";
    private static final String PAGE_INVALID_APPLICATION_FILE = "error/invalidApplicationFile";

    @ExceptionHandler({ApplicationNotFoundException.class, ApplicationFileNotFoundException.class})
    public String handleApplicationNotFound() {
        return PAGE_APPLICATION_NOT_FOUND;
    }

    @ExceptionHandler(FileTransferException.class)
    public String handleFileTransferError() {
        return PAGE_FILE_TRANSFER_ERROR;
    }

    @ExceptionHandler(AuthorizationRequiredException.class)
    public String handleAuthorizationRequiredError() {
        return PAGE_AUTHORIZATION_REQUIRED;
    }

    @ExceptionHandler(UserNotFoundException.class)
    public String handleUserNotFound() {
        return PAGE_USER_NOT_FOUND;
    }

    @ExceptionHandler(InvalidApplicationFileException.class)
    public String handleInvalidApplicationFile() {
        return PAGE_INVALID_APPLICATION_FILE;
    }

    //any other exception handler
    @ExceptionHandler(Exception.class)
    public String handleAnyOtherException(Exception e) {
        e.printStackTrace();
        return PAGE_SOMETHING_WENT_WRONG;
    }
}
