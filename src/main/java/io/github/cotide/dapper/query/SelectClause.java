package io.github.cotide.dapper.query;

import io.github.cotide.dapper.basic.domain.Entity;
import io.github.cotide.dapper.core.unit.Sql2oCache;
import io.github.cotide.dapper.core.utility.Guard;
import io.github.cotide.dapper.core.utility.StringUtils;
import io.github.cotide.dapper.query.base.BaseClause;

public class SelectClause  extends BaseClause {


    public SelectClause(Sql sql) {
        super(sql);
    }

    public <T extends Entity> Sql from(Class<T> modelClass){
        return from(modelClass,null);
    }

    public <T extends Entity> Sql from(Class<T> modelClass, String asName){
        return from(Sql2oCache.getTableName(modelClass),asName);
    }

    public Sql from(String table)
    {
        return from(table,null);
    }

    public Sql from(String table,String asName)
    {
        if(!StringUtils.isNullOrEmpty(asName))
        {
            _sql.tableName = table+" " + asName;
        }else
        {
            _sql.tableName = table;
        }
        return _sql;
    }

    public Sql fromDb(String dbName,String table,String asName)
    {
        Guard.isNotNullOrEmpty(dbName,"dbName");
        Guard.isNotNullOrEmpty(table,"table");
        return from(dbName+"."+table+" " + asName);
    }

    public Sql fromDb(String dbName,String table)
    {
        return fromDb(dbName,table,"");
    }

    public <T extends Entity> Sql fromDb(String dbName,Class<T> modelClass)
    {
        return fromDb(dbName,Sql2oCache.getTableName(modelClass));
    }

    public <T extends Entity> Sql fromDb(String dbName,Class<T> modelClass,String asName)
    {
        return fromDb(dbName,Sql2oCache.getTableName(modelClass),asName);
    }
}
