package io.github.cotide.core.unit;

import io.github.cotide.core.repository.IRepository;
import io.github.cotide.basic.domain.base.Entity;
import io.github.cotide.core.repository.sql.DapperRepositoryBase;
import sql2o.Connection;
import sql2o.Sql2o;

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
    public Connection dbConnection;

    /**
     * 是否执行事务操作
     */
    public boolean isTran =false;


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
            dbConnection = sql2o.beginTransaction();
            isTran = true;
        }
    }


    @Override
    public <TEntity extends Entity> IRepository getRepository(Class<TEntity> returnType) {
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
        dbConnection.getJdbcConnection().setCatalog(dbName);
    }

    /**
     * 开始事务
     */
    @Override
    public void beginTransation() {
        getOpenConnection();
        dbConnection = sql2o.beginTransaction();
        isTran = true;
    }


    @Override
    public void commit()  {
        getOpenConnection();
        dbConnection.commit();
    }

    @Override
    public void rollback()  {
        getOpenConnection();
        dbConnection.rollback();
    }

    @Override
    public void close()  {
        if(!isTran){
            this.close(true);
        }
    }

    /**
     * 释放连接
     *
     * @param isClose
     */
    @Override
    public void close(Boolean isClose) {

        if(isClose)
        {
            if (dbConnection != null) {
                dbConnection.close();
                dbConnection = null;
            }
        }
    }


    public void getOpenConnection() {
        if(dbConnection !=null){
            return;
        }
        dbConnection = sql2o.open();
    }
}
