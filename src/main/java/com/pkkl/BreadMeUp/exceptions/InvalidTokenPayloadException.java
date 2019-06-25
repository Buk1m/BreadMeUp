package com.pkkl.BreadMeUp.exceptions;

public class InvalidTokenPayloadException extends AuthorizationException {

    public InvalidTokenPayloadException() {
    }

    public InvalidTokenPayloadException(String message) {
        super(message);
    }

    public InvalidTokenPayloadException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidTokenPayloadException(Throwable cause) {
        super(cause);
    }

    public InvalidTokenPayloadException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
