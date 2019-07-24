package io.github.cotide.dapper.core.unit;



import io.github.cotide.dapper.repository.inter.IRepository;
import io.github.cotide.dapper.basic.domain.Entity;

import java.io.Closeable;
import java.io.IOException;
import java.sql.SQLException;

/**
 * 业务对象接口
 */
public interface IUnitOfWork extends Closeable {



    /**
     * 获取仓储对象
     * @param <TEntity> 领域对象
     * @return
     */
    <TEntity extends Entity>  IRepository<TEntity>  getRepository(Class<TEntity> returnType);

    /**
     * 切换数据库
     * @param dbName 数据库名称
     */
    void changeDatabase(String dbName) throws SQLException;

    /**
     * 开始事务
     */
    void beginTransation();

    /**
     * 事务提交
     */
    void commit();


    /**
     * 事务回滚
     */
    void rollback();


    /**
     * 释放连接,如果开启事务。
     * 请使用close(true)方法进行强制关闭
     * @throws IOException
     */
    @Override
    void close();


    /**
     * 释放连接
     * @throws IOException
     */
    void close(Boolean isClose);


}
