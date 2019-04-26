package com.gdufe.exercise_app.exception;

public class SystemErrorException extends RuntimeException {

    public SystemErrorException() {
    }

    public SystemErrorException(String message) {
        super(message);
    }

    public SystemErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public SystemErrorException(Throwable cause) {
        super(cause);
    }

    public SystemErrorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
