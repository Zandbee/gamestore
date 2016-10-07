package org.strokova.gamestore.exception;

/**
 * @author vstrokova, 07.10.2016.
 */
public class AuthorizationRequiredException extends RuntimeException {
    public AuthorizationRequiredException(String message) {
        super(message);
    }
}
