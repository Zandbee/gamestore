package org.strokova.gamestore.exception;

/**
 * @author vstrokova, 07.10.2016.
 */
public class ApplicationNotFoundException extends RuntimeException {
    public ApplicationNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApplicationNotFoundException(String message) {
        super(message);
    }
}
