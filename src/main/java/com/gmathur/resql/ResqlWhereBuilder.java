package com.gmathur.resql;

import com.gmathur.resql.adapaters.PgQueryWhereBuilder;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.util.Optional;

import static com.gmathur.resql.ResqlErrorListener.RESQL_ERROR_LISTENER;

public abstract class ResqlWhereBuilder {
    abstract public Optional<String> process(final String input);

    protected static ParseTree parseTree(final String input) {
        final CharStream charStream = CharStreams.fromString(input);

        // Lexer from byte stream
        ResqlLangLexer resqlLangLexer = new ResqlLangLexer(charStream);
        resqlLangLexer.removeErrorListeners();
        resqlLangLexer.addErrorListener(RESQL_ERROR_LISTENER);

        // Extract Lexer tokens from Lexer
        final CommonTokenStream tokens = new CommonTokenStream(resqlLangLexer);

        // Create parser from the tokens
        ResqlLangParser resqlLangParser = new ResqlLangParser(tokens);
        resqlLangParser.removeErrorListeners();
        resqlLangParser.addErrorListener(RESQL_ERROR_LISTENER);

        // Parsed tree
        ParseTree tree = resqlLangParser.qexp();

        return tree;
    }
}