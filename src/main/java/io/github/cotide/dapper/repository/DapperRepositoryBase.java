package io.github.cotide.dapper.repository;

import io.github.cotide.dapper.core.attr.PrimaryKey;
import io.github.cotide.dapper.core.unit.Sql2oCache;
import io.github.cotide.dapper.core.unit.Sql2oUnitOfWork;
import io.github.cotide.dapper.basic.collections.PageList;
import io.github.cotide.dapper.basic.domain.Entity;
import io.github.cotide.dapper.core.attr.Ignore;
import io.github.cotide.dapper.core.unit.Sql2oUtils;
import io.github.cotide.dapper.core.utility.Guard;
import io.github.cotide.dapper.exceptions.PrimaryKeyException;
import io.github.cotide.dapper.exceptions.SqlBuildException;
import io.github.cotide.dapper.query.Sql;
import io.github.cotide.dapper.repository.SqlQueryBase;
import io.github.cotide.dapper.repository.inter.IRepository;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author cotide
 */
public class DapperRepositoryBase<TEntity  extends Entity>
        extends SqlQueryBase
        implements IRepository<TEntity> {

    protected Class<TEntity> modelClass;

    /**
     * 构造函数
     *
     * @param unitOfWork
     */
    public DapperRepositoryBase(Class<TEntity> modelClass, Sql2oUnitOfWork unitOfWork) {
        super(unitOfWork);
        this.modelClass =  modelClass;
    }

    @Override
    public TEntity create(TEntity entity) {
        return  executeInsert(entity);
    }

    @Override
    public TEntity update(TEntity entity) {
       return executeUpdate(entity);
    }

    @Override
    public Boolean delete(TEntity entity) {
        return  executeDelete(entity);
    }

    @Override
    public List<TEntity> getList() {
        return super.getDtoList(modelClass, Sql.builder().select().from(modelClass));
    }

    @Override
    public List<TEntity> getList(Sql sql) {
       return super.getDtoList(modelClass,sql);
    }

    @Override
    public List<TEntity> getList(String sql, Object... param) {
       return  super.getDtoList(modelClass,sql,param);
    }

    @Override
    public TEntity getById(Object primaryKey) {
        Sql sql = Sql.builder().select().from(modelClass).where(
                String.format("%s = @0", Sql2oCache.getPKColumn(modelClass)),
                primaryKey);
       return super.getDto(modelClass,sql);
    }

    @Override
    public TEntity get(String sql, Object... param) {
         return super.getDto(modelClass,sql,param);
    }

    @Override
    public TEntity get(Sql sql) {
        return  super.getDto(modelClass,sql);
    }

    @Override
    public PageList<TEntity> getPageList(int pageIndex, int pageSize, Sql sql) {
        return getPageDtoList(modelClass,pageIndex,pageSize,sql.getFinalSql(),sql.getFinalArgs());
    }

    @Override
    public PageList<TEntity> getPageList(int pageIndex, int pageSize, String sql, Object... param) {
       return getPageDtoList(modelClass,pageIndex,pageSize,sql,param);
    }



    /**
     * 新增
     *
     * @param object
     * @return 影响行数
     */
    private  <TEntity extends Entity> TEntity executeInsert(TEntity object) {

        Guard.isNotNull(object,"object");
        Class<?> modelClass = object.getClass();
        String tableName = Sql2oCache.getTableName(modelClass);
        String pkField =  Sql2oCache.getPKField(modelClass);
        StringBuilder names = new StringBuilder();
        StringBuilder param = new StringBuilder();
        List<Object> values = new ArrayList<>();

        List<Field> mapperFields =
                Sql2oCache.isAutoIncreMent(modelClass)?
                        Sql2oCache.computeNoKeyModelFields(modelClass):
                        Sql2oCache.computeModelFields(modelClass);

        for (Field field : mapperFields)
        {
            names.append(",").append(Sql2oCache.getColumnName(field));
            param.append(",?");
            values.add(Sql2oUtils.getValue(object,field));
        }

        String insertSql = String.format("INSERT INTO %s (%s) VALUES (%s);",
                    tableName,
                    names.substring(1),
                    param.substring(1));

        if(pkField!=null && !pkField.isEmpty())
        {

            try {

                Field keyField = object
                        .getClass()
                        .getDeclaredField(pkField);
                UnitOfWork.getOpenConnection();
                Object result = UnitOfWork.dbConnection
                        .createQuery(insertSql)
                        .withParams(values)
                        .executeUpdate()
                        .getKey(keyField.getType());
                keyField.setAccessible(true);
                keyField.set(object,result);
                return object;
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }finally {
                UnitOfWork.close();
            }
            return null;
        }else{
            try {
                UnitOfWork.getOpenConnection();
                UnitOfWork
                        .dbConnection
                        .createQuery(insertSql)
                        .withParams(values)
                        .executeUpdate()
                        .getKey();
                return object;
            }finally {
                UnitOfWork.close();
            }
        }
    }


    /**
     * 新增
     *
     * @param object
     * @return 影响行数
     */
    private <T extends Entity> T executeUpdate(T object) {

        Guard.isNotNull(object,"object");
        Class<?> modelClass = object.getClass();
        String tableName = Sql2oCache.getTableName(modelClass);
        String pkField =  Sql2oCache.getPKField(modelClass);
        String pkColumn = Sql2oCache.getPKColumn(modelClass);
        if(pkField==null
            || pkField.isEmpty()
            || pkColumn == null
            || pkColumn.isEmpty())
        {
            throw  new PrimaryKeyException();
        }
        StringBuffer sb = new StringBuffer();
        List<Object> values = new ArrayList<>();
        for (Field field : Sql2oCache.computeNoKeyModelFields(modelClass))
        {
            sb.append(String.format(",%s = ?", Sql2oCache.getColumnName(field)));
            values.add(Sql2oUtils.getValue(object,field));
        }
        try {
            String updateSql = String.format("UPDATE %s SET %s WHERE %s = ?",
                    tableName, sb.substring(1), pkColumn);

            Field keyField = object
                    .getClass()
                    .getDeclaredField(pkField);
            keyField.setAccessible(true);
            values.add(keyField.get(object));
            UnitOfWork.getOpenConnection();
            UnitOfWork.dbConnection
                    .createQuery(updateSql)
                    .withParams(values)
                    .executeUpdate();
            return object;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } finally {
            this.UnitOfWork.close();
        }
        return null;
    }


    /**
     * 删除
     * @param object
     * @param <T>
     * @return
     */
    private  <T extends Entity> boolean executeDelete(T object){

        Guard.isNotNull(object,"object");
        Class<?> modelClass = object.getClass();
        String tableName = Sql2oCache.getTableName(modelClass);
        String pkField =  Sql2oCache.getPKField(modelClass);
        String pkColumn = Sql2oCache.getPKColumn(modelClass);
        if(pkField==null
                || pkField.isEmpty()
                || pkColumn == null
                || pkColumn.isEmpty())
        {
            throw  new PrimaryKeyException();
        }

        String deleteSql = String.format("DELETE FROM %s WHERE %s = ?",tableName, pkColumn);
        try {
            Field keyField = object
                    .getClass()
                    .getDeclaredField(pkField);
            keyField.setAccessible(true);
            Object value = keyField.get(object);
            UnitOfWork.getOpenConnection();
            return UnitOfWork
                    .dbConnection
                    .createQuery(deleteSql)
                    .withParams(value)
                    .executeUpdate()
                    .getResult() > 0;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } finally {
            this.UnitOfWork.close();
        }
        return false;
    }

}
