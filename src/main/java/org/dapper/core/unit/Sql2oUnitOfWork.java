package org.dapper.core.unit;

import org.dapper.basic.domain.base.BaseEntityByType;
import org.dapper.core.repository.DapperRepositoryBase;
import org.dapper.core.repository.IRepository;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import javax.sql.DataSource;
import java.sql.SQLException;

public class Sql2oUnitOfWork implements IUnitOfWork {


    /**
     * 数据源
     */
    private Sql2o sql2o;

    /**
     * 当前连接对象
     */
    public Connection DbConnection;


    /**
     * 构造函数
     * @param dataSource 数据源
     */
   public Sql2oUnitOfWork(DataSource dataSource){
        this(dataSource,false);
    }

    /**
     * 构造函数
     * @param dataSource 数据源
     * @param isTransaction 是否启用事务
     */
    public Sql2oUnitOfWork(DataSource dataSource, boolean isTransaction){
        sql2o = new Sql2o(dataSource);
        if(isTransaction)
        {
            DbConnection = sql2o.beginTransaction();
        }else{
            DbConnection = sql2o.open();
        }
    }


    @Override
    public <TEntity extends BaseEntityByType> IRepository getRepository(Class<TEntity> returnType) {
        return new DapperRepositoryBase(returnType,this);
    }



    /**
     * 切换数据库
     * @param dbName
     * @throws SQLException
     */
    @Override
    public void changeDatabase(String dbName) throws SQLException {
        getOpenConnection();
        DbConnection.getJdbcConnection().setCatalog(dbName);
    }



    @Override
    public void commit()  {
        getOpenConnection();
        DbConnection.commit();
    }

    @Override
    public void rollback()  {
        getOpenConnection();
        DbConnection.rollback();
    }

    @Override
    public void close()  {
        if (DbConnection != null) {
            DbConnection.close();
        }
        DbConnection = null;
    }


    public  void getOpenConnection() {
        if(DbConnection !=null) return;
        DbConnection =sql2o.open();
    }
}
