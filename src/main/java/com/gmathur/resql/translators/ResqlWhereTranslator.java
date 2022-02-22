package com.gmathur.resql.translators;

import com.gmathur.resql.ResqlLangListener;
import com.gmathur.resql.exceptions.ResqlException;
import com.gmathur.resql.exceptions.ResqlExceptionHandler;

import java.util.Optional;

/**
 * Abstract class that will be implemented by specific Database translators to build the where clause as the parse tree
 * is being walked over. This extends the generated ANTLR listener implementation to add a {@code get} to retrieve
 * the result of the parse tree walk. The class instance is expected to the attached to the Database speocfic
 * core processor defined in {@code ResqlWhereProcessor}
 *
 * @author Gaurav Mathur (gnmathur)
 */
public abstract class ResqlWhereTranslator implements ResqlLangListener {
    protected String where = null;
    protected ResqlExceptionHandler exceptionHandler;

    public ResqlWhereTranslator(final ResqlExceptionHandler resqlException) {
        this.exceptionHandler = resqlException;
    }

    /**
     * Return the result of the parse tree walk
     *
     * @return Optional where clause as a String
     */
    public Optional<String> get() {
        return Optional.of(where);
    }
}
