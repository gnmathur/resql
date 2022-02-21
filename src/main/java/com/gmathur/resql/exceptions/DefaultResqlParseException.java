package com.gmathur.resql.exceptions;

public class DefaultResqlParseException extends RuntimeException {
    public DefaultResqlParseException(String message) {
        super(message);
    }

    public DefaultResqlParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public DefaultResqlParseException(Throwable cause) {
        super(cause);
    }

    protected DefaultResqlParseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
