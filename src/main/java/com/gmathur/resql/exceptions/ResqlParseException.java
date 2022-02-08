package com.gmathur.resql.exceptions;

public class ResqlParseException extends RuntimeException {
    public ResqlParseException(String message) {
        super(message);
    }

    public ResqlParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResqlParseException(Throwable cause) {
        super(cause);
    }

    protected ResqlParseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
