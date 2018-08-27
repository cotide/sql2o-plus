package org.dapper.core.repository.sql;

import org.dapper.basic.collections.PageList;
import org.dapper.basic.domain.base.BaseEntityByType;
import org.dapper.core.attr.Ignore;
import org.dapper.core.repository.IRepository;
import org.dapper.core.unit.Sql2oUnitOfWork;
import org.dapper.core.unit.Sql2oUtils;
import org.dapper.core.unit.info.PocoColumn;
import org.dapper.core.unit.info.PocoData;
import org.dapper.core.unit.info.TableInfo;
import org.dapper.query.Sql;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DapperRepositoryBase<TEntity  extends BaseEntityByType>
        extends SqlQueryBase
        implements IRepository<TEntity> {

    protected Class<TEntity> modelClass;

    /**
     * 构造函数
     *
     * @param unitOfWork
     */
    public DapperRepositoryBase(Class<TEntity> modelClass,Sql2oUnitOfWork unitOfWork) {
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
        return super.getDtoList(modelClass,Sql.builder().select().from(modelClass));
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
                String.format("%s = @0",TableInfo.fromPoco(modelClass).getPrimaryKey()),
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
    private  <T extends BaseEntityByType> T executeInsert(T object) {

            PocoData pocoData = PocoData.forType(object.getClass());
            TableInfo pd = pocoData.getTableInfo();
            String tableName = pd.getTableName();
            // 主键
            PocoColumn primaryKeyFild = null;
            Map<String, PocoColumn> columns =  pocoData.getColumns();
            StringBuilder names = new StringBuilder();
            StringBuilder values = new StringBuilder();
            List<Object> args = new ArrayList<>();
            for (String key : columns.keySet())
            {
                PocoColumn  col = columns.get(key);
                // 检查是否忽略字段类型
                if (col.getField().isAnnotationPresent(Ignore.class))
                {
                    continue;
                }
                // 检查是否主键字段类型
                if(col.isPrimaryKey()){
                    primaryKeyFild = col;
                    if(pd.getAutoIncrement())
                    {
                        continue;
                    }
                }
                names.append(col.getColumnName()).append(",");
                values.append("?,");
                args.add(col.getValue(object));
            }

        String insertSql = String.format("INSERT INTO %s (%s) VALUES (%s);",
                    tableName,
                    names.substring(0, names.length() - 1),
                    values.substring(0, values.length() - 1));

        if(primaryKeyFild!=null)
        {
            try {
                UnitOfWork.getOpenConnection();
                Field keyField = object
                        .getClass()
                        .getDeclaredField(primaryKeyFild.getField().getName());
                Object result = UnitOfWork.dbConnection
                        .createQuery(insertSql)
                        .withParams(args)
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
            UnitOfWork.getOpenConnection();
            UnitOfWork
                    .dbConnection
                    .createQuery(insertSql)
                    .withParams(args)
                    .executeUpdate()
                    .getKey();
            return null;
        }

    }


    /**
     * 新增
     *
     * @param object
     * @return 影响行数
     */
    private  <T extends BaseEntityByType> T executeUpdate(T object) {

        PocoData pocoData = PocoData.forType(object.getClass());
        TableInfo pd = pocoData.getTableInfo();
        String tableName = pd.getTableName();
        String primaryKeyName = null;
        Object primaryKeyValue = null;
        Map<String, PocoColumn> columns =  pocoData.getColumns();
        List<Object> args = new ArrayList<>();
        StringBuffer sb = new StringBuffer();
        for (String key : columns.keySet())
        {
            PocoColumn  col = columns.get(key);
            // 检查是否忽略字段类型
            if (col.getField().isAnnotationPresent(Ignore.class))
            {
                continue;
            }
            // 检查是否主键字段类型
            if(col.isPrimaryKey()){
                primaryKeyName = col.getColumnName();
                primaryKeyValue = col.getValue(object);
                if(pd.getAutoIncrement())
                {
                    continue;
                }
            }
            sb.append(String.format("%s = ? ,", col.getColumnName()));
            args.add(col.getValue(object));
        }

        try {

            String updateSql = String.format("UPDATE %s SET %s WHERE %s = ?",
                    tableName, sb.substring(0, sb.length() - 1), primaryKeyName);
            args.add(primaryKeyValue);
            UnitOfWork.getOpenConnection();
            UnitOfWork.dbConnection
                    .createQuery(updateSql)
                    .withParams(args)
                    .executeUpdate();
            return object;
        } finally {
            this.UnitOfWork.close();
        }
    }


    /**
     * 删除
     * @param object
     * @param <T>
     * @return
     */
    private  <T extends BaseEntityByType> boolean executeDelete(T object){

        if(Sql2oUtils.isNull(object))
        {
            return false;
        }

        PocoData pocoData = PocoData.forType(object.getClass());
        TableInfo pd = pocoData.getTableInfo();
        String primaryKey = pd.getPrimaryKey();
        Object primaryKeyValue = null;
        if (Sql2oUtils.isNotNullOrEmpty(primaryKey)) {

            PocoColumn primaryKeyColumn =
                    pocoData.getColumns()
                            .values()
                            .stream()
                            .filter(e->e.isPrimaryKey())
                            .findFirst()
                            .orElse(null);

            if(primaryKeyColumn!=null)
            {
                primaryKeyValue = primaryKeyColumn.getValue(object);
            }
        }
        if(Sql2oUtils.isNotNull(primaryKeyValue))
        {
             String deleteSql = String.format("DELETE FROM %s WHERE %s = ?", pd.getTableName(), primaryKey);
             try {
                 UnitOfWork.getOpenConnection();
                 return UnitOfWork
                        .dbConnection
                        .createQuery(deleteSql)
                        .withParams(primaryKeyValue)
                        .executeUpdate()
                        .getResult() > 0;
             }finally {
                 this.UnitOfWork.close();
             }
        }
        return false;
    }


}
