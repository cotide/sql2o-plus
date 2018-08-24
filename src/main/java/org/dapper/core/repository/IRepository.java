package org.dapper.core.repository;

import org.dapper.basic.domain.base.BaseEntityByType;

/**
 * 持久化 CRUD
 * @param <TEntity>
 */
public interface IRepository<TEntity  extends BaseEntityByType > extends IReadOnlyRepository<TEntity> {


    //#region 持久化

    /**
     * 创建实体
     * @param entity
     * @return
     */
    TEntity create(TEntity entity);


    /**
     * 更新实体
     * @param entity
     * @return
     */
    TEntity update(TEntity entity);

    /**
     * 删除实体
     * @param entity
     * @return
     */
    Boolean delete(TEntity entity);

    //#endregion

}
