package com.gmathur.resql.translators;

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

    abstract public Optional<String> process(final String input);

    private final ResqlExceptionHandler exceptionHandler;

    public ResqlWhereProcessor(final ResqlExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
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