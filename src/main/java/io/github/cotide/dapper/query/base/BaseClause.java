package io.github.cotide.dapper.query.base;

import io.github.cotide.dapper.query.Sql;

public abstract class BaseClause {

    protected final Sql _sql;

    public BaseClause(Sql sql)
    {
        _sql = sql;
    }
}
