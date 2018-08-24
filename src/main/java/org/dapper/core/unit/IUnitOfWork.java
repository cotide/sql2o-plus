package org.dapper.core.unit;



import org.dapper.basic.domain.base.BaseEntityByType;
import org.dapper.core.repository.IRepository;

import java.io.Closeable;
import java.sql.SQLException;

/**
 * 业务对象接口
 */
public interface IUnitOfWork  extends Closeable {


    /**
     * 获取仓储对象
     * @param <TEntity> 领域对象
     * @return
     */
    <TEntity extends BaseEntityByType> IRepository getRepository(Class<TEntity> returnType);

    /**
     * 切换数据库
     * @param dbName 数据库名称
     */
    void changeDatabase(String dbName) throws SQLException;


    /**
     * 事务提交
     */
    void commit() throws SQLException;


    /**
     * 事务回滚
     */
    void rollback() throws SQLException;

}
