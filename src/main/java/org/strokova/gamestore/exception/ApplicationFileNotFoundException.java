package org.strokova.gamestore.exception;

/**
 * @author vstrokova, 07.10.2016.
 */
public class ApplicationFileNotFoundException extends RuntimeException {
    public ApplicationFileNotFoundException(String message) {
        super(message);
    }
}
