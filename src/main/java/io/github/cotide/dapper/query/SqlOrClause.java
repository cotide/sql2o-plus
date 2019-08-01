package io.github.cotide.dapper.query;

public class SqlOrClause extends BaseClause {

    public SqlOrClause(Sql sql) {
        super(sql);
    }


    public SqlWhereClause or()
    {
        return new SqlWhereClause(_sql);
    }

}
