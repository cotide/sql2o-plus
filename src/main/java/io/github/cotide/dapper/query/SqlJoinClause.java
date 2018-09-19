package io.github.cotide.dapper.query;

public class SqlJoinClause {


    private final Sql _sql;

    public SqlJoinClause(Sql sql)
    {
        _sql = sql;
    }


    public Sql on(String onClause,Object... args)
    {
        return _sql.append("ON "+ onClause,args);
    }
}
