package io.github.cotide.dapper.repository.base;

import io.github.cotide.dapper.Database;
import io.github.cotide.dapper.basic.domain.Entity;
import io.github.cotide.dapper.core.unit.IUnitOfWork;
import io.github.cotide.dapper.core.unit.Sql2oCache;
import io.github.cotide.dapper.core.unit.Sql2oUnitOfWork;
import sql2o.Query;

import java.io.InputStream;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
            List<Object> parm)
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
            List<Object> parm)
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
