package com.gmathur.resql;

import com.gmathur.resql.exceptions.DefaultResqlException;
import com.gmathur.resql.exceptions.ResqlException;
import com.gmathur.resql.exceptions.ResqlExceptionHandler;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.Interval;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.BitSet;

public class ResqlErrorListener extends BaseErrorListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResqlErrorListener.class);

    private final ResqlExceptionHandler exceptionHandler;

    public ResqlErrorListener(final ResqlExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer,
                            Object offendingSymbol,
                            int line,
                            int charPositionInLine,
                            String msg,
                            RecognitionException e) throws ResqlException {
        final String errMsg = "Error parsing input (line " + line + ":" + charPositionInLine + " " + msg + ")";
        exceptionHandler.report(errMsg);
    }

    @Override
    public void reportAmbiguity(Parser recognizer,
                                DFA dfa,
                                int startIndex,
                                int stopIndex,
                                boolean exact,
                                BitSet ambigAlts,
                                ATNConfigSet configs) throws ResqlException {
        String text = recognizer.getTokenStream().getText(Interval.of(startIndex, stopIndex));
        final String errMsg = "Error parsing input (ambiguous " + text + ")";
        exceptionHandler.report(errMsg);
    }

    @Override
    public void reportAttemptingFullContext(Parser recognizer,
                                            DFA dfa,
                                            int startIndex,
                                            int stopIndex,
                                            BitSet conflictingAlts,
                                            ATNConfigSet configs) throws ResqlException {
        String text = recognizer.getTokenStream().getText(Interval.of(startIndex, stopIndex));
        final String errMsg = "Error parsing input (SLL conflict " + text + ")";
        exceptionHandler.report(errMsg);
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
