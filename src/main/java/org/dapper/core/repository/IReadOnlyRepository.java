package org.dapper.core.repository;

import org.dapper.basic.collections.PageList;
import org.dapper.basic.domain.base.BaseEntityByType;
import org.dapper.query.Sql;

import java.util.List;

/**
 * ReadOnly CRUD
 * @param <TEntity>
 */
public interface IReadOnlyRepository<TEntity extends BaseEntityByType>  {

    /**
     * 获取列表数据
     * @return
     */
    List<TEntity> getList();

    /**
     * 获取实体列表数据
     * @param sql sql对象
     * @return
     */
    List<TEntity> getList(Sql sql);

    /**
     * 获取实体列表数据
     * @param sql sql对象
     * @param param 参数值
     * @return
     */
    List<TEntity> getList(String sql, Object ... param);

    /**
     * 获取实体列表数据
     * @param sql sql对象
     * @param <TDto>
     * @return
     */
    <TDto> List<TDto> getDtoList(Class<TDto> returnType,Sql sql) ;

    /**
     * 获取实体列表数据
     * @param sql sql对象
     * @param <TDto>
     * @return
     */
    <TDto> List<TDto> getDtoList(Class<TDto> returnType,String sql,Object ... param);


    /**
     * 获取总数
     * @param sql sql对象
     * @return
     */
    int count(Sql sql);


    //#region 获取单条数据

    /**
     * 根据Id获取实体对象
     * @param primaryKey
     * @return
     */
    TEntity getById(Object primaryKey);

    /**
     * 获取实体数据
     * @param sql SQL对象
     * @return
     */
    TEntity get(Sql sql);


    /**
     * 获取实体数据
     * @param sql SQL对象
     * @param param 参数值
     * @return
     */
    TEntity get(String sql, Object ...  param);



    /**
     * 获取实体数据
     * @param sql SQL对象
     * @return
     */
    <TDto> TDto getDto(Class<TDto> returnType, Sql sql);

    /**
     * 获取实体数据
     * @param sql SQL对象
     * @param param 参数值
     * @param <TDto>
     * @return
     */
    <TDto> TDto getDto(Class<TDto> returnType,String sql, Object ...  param);

    //#endregion


    //#region 分页处理

    /**
     * 分页获取数据
     * @param pageIndex 开始页码
     * @param pageSize 分页大小
     * @param sql SQL对象
     * @return
     */
    PageList<TEntity> getPageList(int pageIndex, int pageSize, Sql sql);



    /**
     * 分页获取数据
     * @param pageIndex 开始页码
     * @param pageSize 分页大小
     * @param sql SQL对象
     * @return
     */
    PageList<TEntity> getPageList(int pageIndex, int pageSize, String sql, Object ...  param);

     /**
     * 分页获取数据
     * @param pageIndex 开始页码
     * @param pageSize 分页大小
     * @param sql SQL对象
     * @param <TDto>
     * @return
     */
     <TDto>  PageList<TDto> getPageDtoList(Class<TDto> returnType, int pageIndex, int pageSize, Sql sql);


    /**
     * 分页获取数据
     * @param pageIndex 开始页码
     * @param pageSize 分页大小
     * @param sql SQL对象
     * @param <TDto>
     * @return
     */
    <TDto>  PageList<TDto> getPageDtoList(Class<TDto> returnType, int pageIndex, int pageSize, String sql, Object ...  param);


    //#endregion


}
