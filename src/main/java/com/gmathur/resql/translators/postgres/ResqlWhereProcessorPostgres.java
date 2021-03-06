package com.gmathur.resql.translators.postgres;

import com.github.benmanes.caffeine.cache.Cache;
import com.gmathur.resql.exceptions.ResqlExceptionHandler;
import com.gmathur.resql.translators.ResqlWhereProcessor;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.util.Optional;

public class ResqlWhereProcessorPostgres extends ResqlWhereProcessor {
    private final ResqlExceptionHandler exceptionHandler;

    public ResqlWhereProcessorPostgres(final ResqlExceptionHandler exceptionHandler) {
        super(exceptionHandler);
        this.exceptionHandler = exceptionHandler;
    }

    public ResqlWhereProcessorPostgres(ResqlExceptionHandler exceptionHandler,
                                       Cache<Integer, String> cache) {
        super(exceptionHandler, cache);
        this.exceptionHandler = exceptionHandler;
    }

    public Optional<String> process(final String input) {
        final ParseTree parseTree = parseTree(input);
        ResqlResqlWhereTranslatorPostgres resqlQueryWhereBuilderPostgres =
                new ResqlResqlWhereTranslatorPostgres(exceptionHandler);

        ParseTreeWalker.DEFAULT.walk(resqlQueryWhereBuilderPostgres, parseTree);

        return resqlQueryWhereBuilderPostgres.get();
    }
}
