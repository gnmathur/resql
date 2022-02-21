package com.gmathur.resql.translators.postgres;

import com.gmathur.resql.translators.ResqlWhereProcessor;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.util.Optional;

public class ResqlWhereProcessorPostgres extends ResqlWhereProcessor {
    public Optional<String> process(final String input) {
        final ParseTree parseTree = parseTree(input);
        ResqlResqlWhereTranslatorPostgres resqlQueryWhereBuilderPostgres = new ResqlResqlWhereTranslatorPostgres();

        ParseTreeWalker.DEFAULT.walk(resqlQueryWhereBuilderPostgres, parseTree);

        return resqlQueryWhereBuilderPostgres.get();
    }
}
