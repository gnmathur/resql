package com.gmathur.resql.unit;

import com.gmathur.resql.ResqlLangLexer;
import com.gmathur.resql.ResqlLangParser;
import com.gmathur.resql.adapaters.PgQueryWhereBuilder;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class ResqlPgAdapterTest {
    private Optional<String> process(final String input) {
        final CharStream charStream = CharStreams.fromString(input);

        // Lexer from byte stream
        ResqlLangLexer resqlLangLexer = new ResqlLangLexer(charStream);

        // Extract Lexer tokens from Lexer
        final CommonTokenStream tokens = new CommonTokenStream(resqlLangLexer);

        // Create parser from the tokens
        ResqlLangParser resqlLangParser = new ResqlLangParser(tokens);

        // Parsed tree
        ParseTree tree = resqlLangParser.qexp();

        PgQueryWhereBuilder pgQueryWhereBuilder = new PgQueryWhereBuilder();

        ParseTreeWalker.DEFAULT.walk(pgQueryWhereBuilder, tree);

        return pgQueryWhereBuilder.get();
    }

    @Test
    public void tc1GreaterThanOnly() {
        final String restWhereArg = "f1    >    10";
        final Optional<String> res = process(restWhereArg);
        assertTrue(res.isPresent()); assertEquals("f1 > 10", res.get());
    }

    @Test
    public void tc2LessThanOnly() {
        final String restWhereArg1 = "f1<17";
        final Optional<String> res = process(restWhereArg1);
        assertTrue(res.isPresent()); assertEquals("f1 < 17", res.get());

        final String restWhereArg2 = "f1   <   17";
        final Optional<String> res2 = process(restWhereArg2);
        assertTrue(res2.isPresent()); assertEquals("f1 < 17", res2.get());
    }

    @Test
    public void tc3InBetweenTest() {
        final String restWhereArg1 = "age ><(20, 31)";
        final String expected1 = "(age >= 20 AND age < 31)";
        final Optional<String> res1 = process(restWhereArg1);
        assertTrue(res1.isPresent()); assertEquals(expected1, res1.get());

        final String restWhereArg2 = "age  ><   (20,   31)";
        final String expected2 = "(age >= 20 AND age < 31)";
        final Optional<String> res2 = process(restWhereArg2);
        assertTrue(res2.isPresent()); assertEquals(expected2, res2.get());
    }

    @Test
    public void tc4ComplexWhere() {
        final String restWhereArg = "(f1 == 10 && (f2 != 11 || f3 == 12) && (f4 > 13)) || (f5 == 'hello') || (f6 ^[321, 11, 17]) || f7 ><(1, 100)";
        final String expected = "(f1 = 10 AND (f2 != 11 OR f3 = 12) AND (f4 > 13)) OR (f5 = 'hello') OR (f6 IN (321,11,17)) OR (f7 >= 1 AND f7 < 100)";

        final Optional<String> res = process(restWhereArg);

        assertTrue(res.isPresent());
        assertEquals(expected, res.get());
    }

}
