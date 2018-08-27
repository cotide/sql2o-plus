package org.dapper.core.repository.sql.base;

import org.dapper.core.unit.IUnitOfWork;
import org.dapper.core.unit.Sql2oUnitOfWork;
import org.sql2o.Query;

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
