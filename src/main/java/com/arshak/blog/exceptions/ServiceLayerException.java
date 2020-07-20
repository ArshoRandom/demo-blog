package com.arshak.blog.exceptions;

public class ServiceLayerException extends RuntimeException {

    public ServiceLayerException() {
        super();
    }

    public ServiceLayerException(String message) {
        super(message);
    }

    public ServiceLayerException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceLayerException(Throwable cause) {
        super(cause);
    }

    protected ServiceLayerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
