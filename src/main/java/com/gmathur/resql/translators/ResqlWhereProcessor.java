package com.gmathur.resql.translators;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.stats.CacheStats;
import com.gmathur.resql.ResqlErrorListener;
import com.gmathur.resql.ResqlLangLexer;
import com.gmathur.resql.ResqlLangParser;
import com.gmathur.resql.exceptions.ResqlExceptionHandler;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * The Database-specific implementation for processing an input stream - parsing it and converting it into a
 * Database-specific WHERE clause for the native query
 *
 * @author Gaurav Mathur (gnmathur)
 */
public abstract class ResqlWhereProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResqlWhereProcessor.class);

    private Cache<Integer, String> cache = null;

    abstract protected Optional<String> process(final String input);

    private String processAndCheck(final String input) {
        Optional<String> dbWhereClauseOptional = process(input);
        if (dbWhereClauseOptional.isEmpty()) {
            exceptionHandler.report("Error translating resql where clause");
        }
        return dbWhereClauseOptional.get();
    }

    public String processInput(final String input) {
        if (cache != null) {
            Integer key = input.hashCode();
            String dbWhereClause = cache.get(key, k -> null);
            if (dbWhereClause == null) {
                dbWhereClause = processAndCheck(input);
                cache.put(key, dbWhereClause);
            }
            return dbWhereClause;
        } else {
            final String dbWhereClause = processAndCheck(input);
            return dbWhereClause;
        }
    }

    public CacheStats getCacheStats() {
        return cache != null ? cache.stats() : null;
    }

    private final ResqlExceptionHandler exceptionHandler;

    public ResqlWhereProcessor(final ResqlExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }

    public ResqlWhereProcessor(final ResqlExceptionHandler exceptionHandler,
                               final Cache<Integer, String> cache) {
        this.exceptionHandler = exceptionHandler;
        this.cache = cache;
    }

    protected ParseTree parseTree(final String input) {
        final CharStream charStream = CharStreams.fromString(input);

        // Lexer from byte stream
        ResqlLangLexer resqlLangLexer = new ResqlLangLexer(charStream);
        resqlLangLexer.removeErrorListeners();
        resqlLangLexer.addErrorListener(new ResqlErrorListener(exceptionHandler));

        // Extract Lexer tokens from Lexer
        final CommonTokenStream tokens = new CommonTokenStream(resqlLangLexer);

        // Create parser from the tokens
        ResqlLangParser resqlLangParser = new ResqlLangParser(tokens);
        resqlLangParser.removeErrorListeners();
        resqlLangParser.addErrorListener(new ResqlErrorListener(exceptionHandler));

        // Parsed tree
        ParseTree tree = resqlLangParser.qexp();

        return tree;
    }
}