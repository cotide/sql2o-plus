package io.github.cotide.dapper.repository.sql.base;

import io.github.cotide.dapper.core.unit.IUnitOfWork;
import io.github.cotide.dapper.core.unit.Sql2oUnitOfWork;
import sql2o.Query;

/**
 * @author cotide
 */
public abstract class SqlBase {


    protected final Sql2oUnitOfWork UnitOfWork;

    public SqlBase(IUnitOfWork unitOfWork){
        this.UnitOfWork = (Sql2oUnitOfWork)unitOfWork;
    }

    protected Query createQuery(String sql, Object... parm )
    {
        UnitOfWork.getOpenConnection();
        Query sqlBuild = UnitOfWork.dbConnection.createQuery(sql)
                .setAutoDeriveColumnNames(false)
                .throwOnMappingFailure(false)
                .withParams(parm);

        return sqlBuild;
    }


}
