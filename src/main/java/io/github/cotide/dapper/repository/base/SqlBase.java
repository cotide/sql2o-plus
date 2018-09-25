package io.github.cotide.dapper.repository.base;

import io.github.cotide.dapper.Database;
import io.github.cotide.dapper.basic.domain.Entity;
import io.github.cotide.dapper.core.unit.IUnitOfWork;
import io.github.cotide.dapper.core.unit.Sql2oCache;
import io.github.cotide.dapper.core.unit.Sql2oUnitOfWork;
import sql2o.Query;

import java.io.InputStream;

/**
 * @author cotide
 */
public abstract class SqlBase {


    protected final Sql2oUnitOfWork UnitOfWork;

    public SqlBase(IUnitOfWork unitOfWork){
        this.UnitOfWork = (Sql2oUnitOfWork)unitOfWork;
    }



    protected  Query createQuery(
            String sql,
            Object... parm)
    {
        UnitOfWork.getOpenConnection();
        Query sqlBuild = UnitOfWork.dbConnection.createQuery(sql)
                .setAutoDeriveColumnNames(false)
                .throwOnMappingFailure(UnitOfWork.isDebug())
                .withParams(parm);

        return sqlBuild;
    }


    protected <TDto> Query createQuery(
            Class<TDto> returnType,
            String sql,
            Object... parm)
    {
        UnitOfWork.getOpenConnection();
        Query sqlBuild = UnitOfWork.dbConnection.createQuery(sql)
                .setColumnMappings(Sql2oCache.computeModelColumnMappings(returnType))
                .setAutoDeriveColumnNames(false)
                .throwOnMappingFailure(UnitOfWork.isDebug())
                .withParams(parm);

        return sqlBuild;
    }

}
