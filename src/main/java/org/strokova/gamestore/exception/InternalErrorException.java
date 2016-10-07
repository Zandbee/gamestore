package org.strokova.gamestore.exception;

/**
 * @author vstrokova, 07.10.2016.
 */
public class InternalErrorException extends RuntimeException {
    public InternalErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public InternalErrorException(String message) {
        super(message);
    }
}
