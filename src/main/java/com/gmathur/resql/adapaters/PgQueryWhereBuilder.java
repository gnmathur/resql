package com.gmathur.resql.adapaters;

import com.gmathur.resql.ResqlLangParser;
import com.gmathur.resql.models.ComputedObj;
import com.gmathur.resql.models.ResqlListenerDataModels;
import com.gmathur.resql.models.ResqlListenerDataModels.IntTuple;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTreeProperty;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.gmathur.resql.models.ResqlListenerDataModels.IntTupleBldr;
import static com.gmathur.resql.models.ResqlListenerDataModels.StringWrapperBldr;

public class PgQueryWhereBuilder extends QueryWhereBuilder {
    private static final Logger logger = LoggerFactory.getLogger(PgQueryWhereBuilder.class);
    private final ParseTreeProperty<ComputedObj> expressions = new ParseTreeProperty<>();

    public String chkAndExtractStr(ComputedObj o) {
        if (!(o instanceof ResqlListenerDataModels.StringWrapper)) {
            throw new IllegalStateException("Expecting String but got " + o.getClass());
        }
        return ((ResqlListenerDataModels.StringWrapper) o).getS();
    }
    public IntTuple chkAndExtractTuple(ComputedObj o) {
        if (!(o instanceof IntTuple)) {
            throw new IllegalStateException("Expecting String but got " + o.getClass());
        }
        return (IntTuple) o;
    }

    @Override
    public void enterQexp(ResqlLangParser.QexpContext ctx) {
        logger.debug(ctx.getText());
    }

    @Override
    public void exitQexp(ResqlLangParser.QexpContext ctx) {
        where = chkAndExtractStr(expressions.get(ctx.subexp()));
    }

    @Override
    public void enterSubexp(ResqlLangParser.SubexpContext ctx) {
        logger.debug(ctx.getText());
    }

    @Override
    public void exitSubexp(ResqlLangParser.SubexpContext ctx) {
        logger.debug(ctx.getText());
        if (ctx.equal() != null) {
            expressions.put(ctx, expressions.get(ctx.equal()));
        } else if (ctx.notequal() != null) {
            expressions.put(ctx, expressions.get(ctx.notequal()));
        } else if (ctx.AND() != null) {
            StringBuilder q = new StringBuilder();
            q
                    .append(chkAndExtractStr(expressions.get(ctx.subexp(0))))
                    .append(" AND ")
                    .append(chkAndExtractStr(expressions.get(ctx.subexp(1))));
            expressions.put(ctx, StringWrapperBldr(q.toString()));
            logger.info(q.toString());
        } else if (ctx.OR() != null) {
            final StringBuilder q = new StringBuilder();
            q
                    .append(chkAndExtractStr(expressions.get(ctx.subexp(0))))
                    .append(" OR ")
                    .append(chkAndExtractStr(expressions.get(ctx.subexp(1))));
            expressions.put(ctx, StringWrapperBldr(q.toString()));
            logger.info(q.toString());
        } else if (ctx.OPENPAREN() != null && ctx.CLOSEPAREN() != null) {
            expressions.put(ctx, StringWrapperBldr("(" + chkAndExtractStr(expressions.get(ctx.subexp(0))) + ")"));
        } else if (ctx.gtexp() != null) {
            expressions.put(ctx, expressions.get(ctx.gtexp()));
        } else if (ctx.ltexp() != null) {
            expressions.put(ctx, expressions.get(ctx.ltexp()));
        } else if (ctx.in() != null) {
            expressions.put(ctx, expressions.get(ctx.in()));
        } else if (ctx.between() != null) {
            expressions.put(ctx, expressions.get(ctx.between()));
        }
    }

    @Override
    public void enterGtexp(ResqlLangParser.GtexpContext ctx) {
        logger.debug(ctx.getText());
    }

    @Override
    public void exitGtexp(ResqlLangParser.GtexpContext ctx) {
        logger.debug(ctx.getText());
        StringBuffer q = new StringBuffer();
        q.append(ctx.FIELD().getText()) .append(" > ") .append(ctx.NUMBER().getText());
        expressions.put(ctx, StringWrapperBldr(q.toString()));
        logger.info(q.toString());
    }

    @Override
    public void enterLtexp(ResqlLangParser.LtexpContext ctx) { }

    @Override
    public void exitLtexp(ResqlLangParser.LtexpContext ctx) {
        logger.debug(ctx.getText());
        StringBuffer q = new StringBuffer();
        q.append(ctx.FIELD().getText()).append(" < ").append(ctx.NUMBER().getText());
        expressions.put(ctx, StringWrapperBldr(q.toString()));
        logger.info(q.toString());
    }

    @Override
    public void enterLteexp(ResqlLangParser.LteexpContext ctx) { }

    @Override
    public void exitLteexp(ResqlLangParser.LteexpContext ctx) {
        logger.debug(ctx.getText());
        StringBuffer q = new StringBuffer();
        q.append(ctx.FIELD().getText()).append(" <= ").append(ctx.NUMBER().getText());
        expressions.put(ctx, StringWrapperBldr(q.toString()));
        logger.info(q.toString());
    }

    @Override
    public void enterGteexp(ResqlLangParser.GteexpContext ctx) { }

    @Override
    public void exitGteexp(ResqlLangParser.GteexpContext ctx) {
        logger.debug(ctx.getText());
        StringBuffer q = new StringBuffer();
        q.append(ctx.FIELD().getText()).append(" >= ").append(ctx.NUMBER().getText());
        expressions.put(ctx, StringWrapperBldr(q.toString()));
        logger.info(q.toString());
    }

    @Override
    public void enterEqual(ResqlLangParser.EqualContext ctx) { }

    @Override
    public void exitEqual(ResqlLangParser.EqualContext ctx) {
        logger.debug(ctx.getText());
        boolean isNumber = ctx.NUMBER() != null;

        StringBuffer q = new StringBuffer();
        q.append(ctx.FIELD().getText())
                .append(" = ")
                .append(isNumber? ctx.NUMBER().getText() : ctx.STRING().getText());
        expressions.put(ctx, StringWrapperBldr(q.toString()));
        logger.info(q.toString());
    }

    @Override
    public void enterNotequal(ResqlLangParser.NotequalContext ctx) {

    }

    @Override
    public void exitNotequal(ResqlLangParser.NotequalContext ctx) {
        logger.debug(ctx.getText());
        boolean isNumber = ctx.NUMBER() != null;

        StringBuffer q = new StringBuffer();
        q.append(ctx.FIELD().getText())
                .append(" != ")
                .append(isNumber? ctx.NUMBER().getText() : ctx.STRING().getText());
        expressions.put(ctx, StringWrapperBldr(q.toString()));
        logger.info(q.toString());

    }

    @Override
    public void enterBetween(ResqlLangParser.BetweenContext ctx) {

    }

    @Override
    public void exitBetween(ResqlLangParser.BetweenContext ctx) {
        logger.debug(ctx.getText());
        final StringBuilder s = new StringBuilder();
        IntTuple t = chkAndExtractTuple(expressions.get(ctx.tuple()));
        final String l = ctx.FIELD() + " >= " + t.get_0();
        final String r = ctx.FIELD() + " < " + t.get_1();
        s.append("(").append(l).append(" AND ").append(r).append(")");
        expressions.put(ctx, StringWrapperBldr(s.toString()));
    }

    @Override
    public void enterIn(ResqlLangParser.InContext ctx) {

    }

    @Override
    public void exitIn(ResqlLangParser.InContext ctx) {
        logger.debug(ctx.getText());
        String inExp = ctx.FIELD().getText() + " IN " + chkAndExtractStr(expressions.get(ctx.array()));
        logger.info(inExp);
        expressions.put(ctx, StringWrapperBldr(inExp));
    }

    @Override
    public void enterArray(ResqlLangParser.ArrayContext ctx) { }

    @Override
    public void exitArray(ResqlLangParser.ArrayContext ctx) {
        logger.debug(ctx.getText());
        final StringBuffer sb = new StringBuffer();
        sb.append("(");
        for (TerminalNode n:  ctx.NUMBER()) {
            sb.append(n.toString()).append(",");
        }
        sb.setCharAt(sb.length()-1, ')');
        expressions.put(ctx, new ResqlListenerDataModels.StringWrapper(sb.toString()));
        logger.debug(sb.toString());
    }

    @Override
    public void enterTuple(ResqlLangParser.TupleContext ctx) {

    }

    @Override
    public void exitTuple(ResqlLangParser.TupleContext ctx) {
        logger.debug(ctx.getText());
        final StringBuilder sb = new StringBuilder();

        IntTuple t = IntTupleBldr(
                Integer.parseInt(ctx.NUMBER().get(0).getText()),
                Integer.parseInt(ctx.NUMBER().get(1).getText()));
        expressions.put(ctx, t);
    }

    @Override
    public void visitTerminal(TerminalNode node) {

    }

    @Override
    public void visitErrorNode(ErrorNode node) {

    }

    @Override
    public void enterEveryRule(ParserRuleContext ctx) {

    }

    @Override
    public void exitEveryRule(ParserRuleContext ctx) {

    }
}
