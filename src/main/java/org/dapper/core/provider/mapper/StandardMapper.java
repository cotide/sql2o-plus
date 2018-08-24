package org.dapper.core.provider.mapper;

import org.dapper.core.unit.info.PocoColumn;
import org.dapper.core.unit.info.TableInfo;

import java.lang.reflect.Field;

/**
 * 获取数据库对象的实体映射信息实现
 */
public class StandardMapper implements IMapper {

    @Override
    public TableInfo GetTableInfo(Class<?> type) {
        return TableInfo.fromPoco(type);
    }

    @Override
    public PocoColumn GetColumnInfo(Field field) {
         return  PocoColumn.fromField(field);
    }
}
