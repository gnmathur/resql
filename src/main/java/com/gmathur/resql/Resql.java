package com.gmathur.resql;

import com.gmathur.resql.translators.ResqlWhereProcessor;
import com.gmathur.resql.exceptions.DefaultResqlParseException;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

/**
 * Resql context - users create the context specifying the exception handler, and the database where clause processor
 *
 * @author Gaurav Mathur (gnmathur)
 */
public class Resql {
    /// Class
    private Class<? extends RuntimeException> exception;
    private ResqlWhereProcessor resqlWhereProcessor;

    Resql(final Class<? extends RuntimeException> exception,
          final ResqlWhereProcessor resqlWhereProcessor) {
        this.exception = exception;
        this.resqlWhereProcessor = resqlWhereProcessor;
    }

    public Class<? extends RuntimeException> getException() {
        return exception;
    }

    public ResqlWhereProcessor getResqlWhereProcessor() {
        return resqlWhereProcessor;
    }

    public Optional<String> process(final String resqlWhereClause) {
        return resqlWhereProcessor.process(resqlWhereClause);
    }

    public static ResqlBuilder builder() {
        return new ResqlBuilder();
    }

    /// Builder

    public static class ResqlBuilder {
        private Class<? extends RuntimeException> exception;
        private Class<? extends ResqlWhereProcessor> resqlWhereProcessor = null;

        private ResqlBuilder() {
            exception = DefaultResqlParseException.class;
        }

        public ResqlBuilder withExceptionHandler(final Class<? extends RuntimeException> exception) {
            this.exception = exception;
            return this;
        }

        public ResqlBuilder withWhereBuilder(final Class<? extends ResqlWhereProcessor> resqlWhereProcessor) {
            this.resqlWhereProcessor = resqlWhereProcessor;
            return this;
        }

        public Resql build() {
            if (resqlWhereProcessor == null) {
                throw new RuntimeException("resql needs a WHERE builder for the target database");
            }
            try {
                return new Resql(exception, resqlWhereProcessor.getDeclaredConstructor().newInstance());
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException("Error create resql instance (err: " + e.getMessage() + ")");
            }
        }
    }
}
