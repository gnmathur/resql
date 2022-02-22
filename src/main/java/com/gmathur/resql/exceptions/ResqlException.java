package com.gmathur.resql.exceptions;

public abstract class ResqlException extends RuntimeException {
    public ResqlException(String message) {
        super(message);
    }
}
