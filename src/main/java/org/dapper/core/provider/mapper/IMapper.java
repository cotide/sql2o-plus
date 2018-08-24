package org.dapper.core.provider.mapper;

import org.dapper.core.unit.info.PocoColumn;
import org.dapper.core.unit.info.TableInfo;

import java.lang.reflect.Field;

/**
 * 获取数据库对象的实体映射信息接口
 */
public interface IMapper {

    /**
     * 获取表信息
     * @param type
     * @return
     */
    TableInfo GetTableInfo(Class<?>  type);


    /**
     * 获取表字段信息
     * @param field
     * @return
     */
    PocoColumn GetColumnInfo(Field field) ;

}
