package org.dapper.core.repository.sql;

import org.dapper.core.keys.ResultKey;
import org.dapper.core.repository.sql.base.SqlBase;
import org.dapper.core.unit.IUnitOfWork;
import org.dapper.query.Sql;
import org.sql2o.Connection;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * SQL执行
 */
public class SqlExecuteBase  extends SqlBase {


    protected List<Object> keys;

    public SqlExecuteBase(IUnitOfWork unitOfWork) {
        super(unitOfWork);
    }



    public ResultKey execute(String sql, Object... param)
    {

        return execute(new Sql(sql,param));
    }


    public  ResultKey execute(Sql sql)
    {
        try {
            UnitOfWork.getOpenConnection();
            return new ResultKey(UnitOfWork
                    .dbConnection
                    .createQuery(sql.getFinalSql())
                    .withParams(sql.getFinalArgs())
                    .executeUpdate().getKeys());
        }finally {
            this.UnitOfWork.close();
        }
    }




}
