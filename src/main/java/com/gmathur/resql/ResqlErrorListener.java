package com.gmathur.resql;

import com.gmathur.resql.exceptions.ResqlParseException;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.Interval;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.BitSet;

public class ResqlErrorListener extends BaseErrorListener {
    public static final ResqlErrorListener RESQL_ERROR_LISTENER = new ResqlErrorListener();

    private static final Logger LOGGER = LoggerFactory.getLogger(ResqlErrorListener.class);

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer,
                            Object offendingSymbol,
                            int line,
                            int charPositionInLine,
                            String msg,
                            RecognitionException e) throws ResqlParseException {
        final String errMsg = "Error parsing input (line " + line + ":" + charPositionInLine + " " + msg + ")";
        LOGGER.error(errMsg);
        throw new ResqlParseException(errMsg);
    }

    @Override
    public void reportAmbiguity(Parser recognizer,
                                DFA dfa,
                                int startIndex,
                                int stopIndex,
                                boolean exact,
                                BitSet ambigAlts,
                                ATNConfigSet configs) {
        String text = recognizer.getTokenStream().getText(Interval.of(startIndex, stopIndex));
        final String errMsg = "Error parsing input (ambiguous " + text + ")";
        LOGGER.error(errMsg);
        throw new ResqlParseException(errMsg);
    }

    @Override
    public void reportAttemptingFullContext(Parser recognizer,
                                            DFA dfa,
                                            int startIndex,
                                            int stopIndex,
                                            BitSet conflictingAlts,
                                            ATNConfigSet configs) {
        String text = recognizer.getTokenStream().getText(Interval.of(startIndex, stopIndex));
        final String errMsg = "Error parsing input (SLL conflict " + text + ")";
        LOGGER.error(errMsg);
        throw new ResqlParseException(errMsg);
    }

    @Override
    public void reportContextSensitivity(Parser recognizer,
                                         DFA dfa,
                                         int startIndex,
                                         int stopIndex,
                                         int prediction,
                                         ATNConfigSet configs) {
        super.reportContextSensitivity(recognizer, dfa, startIndex, stopIndex, prediction, configs);
    }
}
