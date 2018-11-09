package io.github.cotide.dapper.query;

import io.github.cotide.dapper.basic.domain.Entity;
import io.github.cotide.dapper.core.unit.Sql2oCache;
import io.github.cotide.dapper.core.utility.Guard;

public class SelectClause  extends  BaseClause{


    public SelectClause(Sql sql) {
        super(sql);
    }


    public <T extends Entity> Sql from(Class<T> modelClass){
       return from(Sql2oCache.getTableName(modelClass));
    }


    public <T extends Entity> Sql from(Class<T> modelClass,String asName)
    {
        return from(Sql2oCache.getTableName(modelClass),asName);
    }


    public Sql from(String table)
    {
        return from(table,null);
    }

    public Sql from(String table,String asName)
    {
        Guard.isNotNullOrEmpty(table,"from table");
        return (_sql.append(new Sql("from "+table+" "+(asName!=null?asName:""))));
    }


    public Sql fromDb(String dbName, String table, String asName)
    {
        Guard.isNotNullOrEmpty(dbName,"from dbName");
        Guard.isNotNullOrEmpty(table,"from table");
        return from(dbName+"."+table+" "+ (asName!=null?asName:""));
    }

    public Sql fromDb(String dbName, String table)
    {
        return fromDb(dbName,table,null);
    }

    public <T extends Entity> Sql  fromDb(String dbName, Class<T> modelClass)
    {
        return fromDb(dbName, Sql2oCache.getTableName(modelClass));
    }

    public <T extends Entity>  Sql fromDb(String dbName,Class<T> modelClass,String asName)
    {
        return fromDb(dbName, Sql2oCache.getTableName(modelClass),asName);
    }


    public Sql as(String asName)
    {
        return new Sql(asName!=null?asName:"");
    }
}
