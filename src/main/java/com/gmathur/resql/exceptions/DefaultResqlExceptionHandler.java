package com.gmathur.resql.exceptions;

/**
 * Default resql exception handler.
 *
 * @author Gaurav Mathur (gnmathur)
 */
public class DefaultResqlExceptionHandler implements ResqlExceptionHandler {
    @Override
    public void report(final String msg) throws ResqlException {
        throw new DefaultResqlException(msg);
    }
}
