package com.gmathur.resql;

import com.gmathur.resql.exceptions.DefaultResqlExceptionHandler;
import com.gmathur.resql.exceptions.ResqlException;
import com.gmathur.resql.exceptions.ResqlExceptionHandler;
import com.gmathur.resql.translators.ResqlWhereProcessor;
import com.gmathur.resql.exceptions.DefaultResqlException;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

/**
 * Resql context - users create the context specifying the exception handler, and the database where clause processor
 *
 * @author Gaurav Mathur (gnmathur)
 */
public class Resql {
    /// Class
    private final ResqlWhereProcessor resqlWhereProcessor;

    Resql(final ResqlWhereProcessor resqlWhereProcessor) {
        this.resqlWhereProcessor = resqlWhereProcessor;
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
        private ResqlExceptionHandler exception;
        private Class<? extends ResqlWhereProcessor> resqlWhereProcessor = null;

        private ResqlBuilder() {
            exception = new DefaultResqlExceptionHandler();
        }

        public ResqlBuilder withExceptionHandler(final ResqlExceptionHandler exceptionHandler) {
            this.exception = exceptionHandler;
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
                Class[] clsArgs = new Class[1];
                clsArgs[0] = ResqlExceptionHandler.class;
                return new Resql(resqlWhereProcessor
                        .getDeclaredConstructor(clsArgs)
                        .newInstance(exception));
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException("Error create resql instance (err: " + e.getMessage() + ")");
            }
        }
    }
}
