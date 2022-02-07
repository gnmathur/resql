package com.gmathur.resql;

import com.gmathur.resql.adapaters.PgQueryWhereBuilder;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class ResqlMain {
    public static void main(String[] args) {
        String input = "(f1 == 10 && (f2 != 11 || f3 == 12) && (f4 > 13)) || (f5 == 'hello') || (f6 ^[321, 11, 17]) || f7 ><(1, 100)";
        String input2 = "f1 == 10 && f2 != 11 && f3 == 12";
        String input3 = "f1 == 10 && f2 == 11";
        String input4 = "f1 == 10 && f2 >< (11, 21)";
        String input5 = "10 == 100";

        final CharStream charStream = CharStreams.fromString(input5);
        ResqlLangLexer resqlLangLexer = null;
        try {
            resqlLangLexer = new ResqlLangLexer(charStream);
        } catch (Exception e) {
            System.out.println(e);
        }
        // create a buffer of tokens pulled from the lexer
        final CommonTokenStream tokens = new CommonTokenStream(resqlLangLexer);
        // create a parser that feeds off the tokens buffer
        ResqlLangParser resqlLangParser = null;
        try {
            resqlLangParser = new ResqlLangParser(tokens);
        } catch (Exception e) {
            System.out.println(e);
        }
        ParseTree tree = resqlLangParser.qexp(); // begin parsing at the qexp rule
        PgQueryWhereBuilder pgQueryWhereBuilder = new PgQueryWhereBuilder();
        ParseTreeWalker.DEFAULT.walk(pgQueryWhereBuilder, tree);
        System.out.println(pgQueryWhereBuilder.get());
    }
}
