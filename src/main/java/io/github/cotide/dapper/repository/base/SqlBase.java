package io.github.cotide.dapper.repository.base;

import io.github.cotide.dapper.core.unit.IUnitOfWork;
import io.github.cotide.dapper.core.unit.Sql2oUnitOfWork;
import sql2o.Query;

/**
 * @author cotide
 */
public abstract class SqlBase {

    /**
     * 是否调试模式
     */
    boolean isDebug = true;

    protected final Sql2oUnitOfWork UnitOfWork;

    public SqlBase(IUnitOfWork unitOfWork){
        this.UnitOfWork = (Sql2oUnitOfWork)unitOfWork;
    }

    protected Query createQuery(String sql, Object... parm )
    {
        UnitOfWork.getOpenConnection();
        Query sqlBuild = UnitOfWork.dbConnection.createQuery(sql)
                .setAutoDeriveColumnNames(false)
                .throwOnMappingFailure(isDebug)
                .withParams(parm);

        return sqlBuild;
    }


}
