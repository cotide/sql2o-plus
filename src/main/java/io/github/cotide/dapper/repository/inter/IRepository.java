package io.github.cotide.dapper.repository;

import io.github.cotide.dapper.basic.domain.Entity;

/**
 * 持久化 CRUD
 * @param <TEntity>
 */
public interface IRepository<TEntity  extends Entity> extends IReadOnlyRepository<TEntity> {


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
