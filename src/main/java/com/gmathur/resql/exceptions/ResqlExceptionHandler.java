package com.gmathur.resql.exceptions;

public interface ResqlExceptionHandler {
    void report(final String msg) throws ResqlException;
}
