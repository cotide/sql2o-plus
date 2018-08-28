package io.github.cotide.dapper.repository;

import io.github.cotide.dapper.core.keys.ResultKey;
import io.github.cotide.dapper.repository.base.SqlBase;
import io.github.cotide.dapper.core.unit.IUnitOfWork;
import io.github.cotide.dapper.query.Sql;

import java.util.List;

/**
 * SQL执行
 * @author cotide
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
