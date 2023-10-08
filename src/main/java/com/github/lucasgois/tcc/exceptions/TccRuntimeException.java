package com.github.lucasgois.tcc.exceptions;

public class TccRuntimeException extends RuntimeException {

    public TccRuntimeException() {
    }

    public TccRuntimeException(String message) {
        super(message);
    }

    public TccRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public TccRuntimeException(Throwable cause) {
        super(cause);
    }

    public TccRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
