package io.github.cotide.dapper.repository.inter;

import io.github.cotide.dapper.basic.domain.Entity;
import io.github.cotide.dapper.query.operations.Update;

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
     * 更新实体
     * @param entity
     * @return
     */
    TEntity update(TEntity entity, Update<TEntity> tEntityUpdate);


    /**
     * 创建更新对象
     * @return
     */
    Update<TEntity> createUpdate();

    /**
     * 删除实体
     * @param entity
     * @return
     */
    boolean delete(TEntity entity);

    //#endregion

}
