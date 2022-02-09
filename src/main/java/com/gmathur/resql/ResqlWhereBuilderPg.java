package com.gmathur.resql;

import com.gmathur.resql.adapaters.postgres.PgQueryWhereBuilder;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.util.Optional;

public class ResqlWhereBuilderPg extends ResqlWhereBuilder {
    public Optional<String> process(final String input) {
        final ParseTree parseTree = parseTree(input);
        PgQueryWhereBuilder pgQueryWhereBuilder = new PgQueryWhereBuilder();

        ParseTreeWalker.DEFAULT.walk(pgQueryWhereBuilder, parseTree);

        return pgQueryWhereBuilder.get();
    }
}
