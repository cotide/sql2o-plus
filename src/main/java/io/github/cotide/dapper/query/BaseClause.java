package io.github.cotide.dapper.query;

public abstract class BaseClause {

    protected final Sql _sql;

    public BaseClause(Sql sql)
    {
        _sql = sql;
    }


}
