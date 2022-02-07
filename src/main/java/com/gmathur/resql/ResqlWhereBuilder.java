package com.gmathur.resql;

import com.gmathur.resql.adapaters.PgQueryWhereBuilder;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.util.Optional;

public abstract class ResqlWhereBuilder {
    abstract public Optional<String> process(final String input);

    protected static ParseTree parseTree(final String input) {
        final CharStream charStream = CharStreams.fromString(input);

        // Lexer from byte stream
        ResqlLangLexer resqlLangLexer = new ResqlLangLexer(charStream);

        // Extract Lexer tokens from Lexer
        final CommonTokenStream tokens = new CommonTokenStream(resqlLangLexer);

        // Create parser from the tokens
        ResqlLangParser resqlLangParser = new ResqlLangParser(tokens);

        // Parsed tree
        ParseTree tree = resqlLangParser.qexp();

        return tree;
    }
}
