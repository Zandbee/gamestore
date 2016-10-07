package org.strokova.gamestore.exception;

/**
 * @author vstrokova, 07.10.2016.
 */
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
