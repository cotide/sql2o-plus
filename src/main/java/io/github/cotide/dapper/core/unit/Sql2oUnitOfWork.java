package io.github.cotide.dapper.core.unit;

import io.github.cotide.dapper.repository.inter.IRepository;
import io.github.cotide.dapper.basic.domain.Entity;
import io.github.cotide.dapper.repository.DapperRepositoryBase;
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
    protected boolean isTran =false;

    /**
     * 是否调试模式
     */
    protected boolean isDebug = false;


     /**
     * 构造函数
     * @param dataSource 数据源
     */
    public Sql2oUnitOfWork(Sql2o dataSource){
        this(dataSource,false);
    }

    /**
     * 构造函数
     * @param dataSource 数据源
     * @param isTransaction 是否启用事务
     */
    public Sql2oUnitOfWork(Sql2o dataSource, boolean isTransaction){
        this(dataSource,isTransaction,false);
    }



    /**
     * 构造函数
     * @param dataSource 数据源
     * @param isTransaction 是否启用事务
     * @param isDebug 是否调试状态
     */
    public Sql2oUnitOfWork(Sql2o dataSource, boolean isTransaction,boolean isDebug){
        sql2o = dataSource;
        if(isTransaction)
        {
            dbConnection = sql2o.beginTransaction();
            this.isTran = true;
        }
        this.isDebug = isDebug;
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

    /**
     * 读取当前是否调试状态
     * @return
     */
    public Boolean isDebug(){
        return this.isDebug;
    }

    public void getOpenConnection() {
        if(dbConnection !=null){
            return;
        }
        dbConnection = sql2o.open();
    }
}
