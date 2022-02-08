package com.gmathur.resql.adapaters;

import com.gmathur.resql.ResqlLangListener;

import java.util.Optional;

public abstract class QueryWhereBuilder implements ResqlLangListener {
    protected String where = null;

    /**
     *
     * @return Optional where clause as a String
     */
    public Optional<String> get() {
        return Optional.of(where);
    }
}
