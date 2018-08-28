package io.github.cotide.dapper.core.unit.info;

import io.github.cotide.dapper.core.attr.PrimaryKey;
import io.github.cotide.dapper.core.attr.Table;
import io.github.cotide.dapper.core.unit.Sql2oCache;
import io.github.cotide.dapper.core.unit.Sql2oUtils;

import java.lang.reflect.Field;

/**
 * 表信息
 */
@lombok.Getter
@lombok.Setter
public class TableInfo {


    /**
     * 表名
     */
    private String tableName;

    /**
     * 主键
     */
    private String primaryKey;

    /**
     * 主键是否自增
     */
    private Boolean autoIncrement;

    private String  sequenceName;


    /**
     * 获取表信息
     * @param modelClass
     * @return
     */
    public static TableInfo fromPoco(Class<?> modelClass)
    {

        TableInfo ti = new TableInfo();

        // 获取表名
        Table tableName = modelClass.getAnnotation(Table.class);
        if (tableName != null && Sql2oUtils.isNotNullOrEmpty(tableName.value())) {
            ti.tableName  = tableName.value();
        }else{
            ti.tableName = modelClass.getSimpleName().toLowerCase();
        }

        // 表主键
        Field[] fields = modelClass.getDeclaredFields();
        for (Field field : fields) {
            PrimaryKey primaryKey = field.getDeclaredAnnotation(PrimaryKey.class);
            if (primaryKey != null) {
                ti.primaryKey  = primaryKey.value();
                ti.sequenceName  = primaryKey.sequenceName();
                ti.autoIncrement = primaryKey.autoIncrement();
                break;
            }
        }

        // 如果指定主键注解，使用自动匹配
        if(Sql2oUtils.isNullOrEmpty(ti.primaryKey))
        {
            Field prop = null;
            for (Field field : fields) {
                if(field.getName().toLowerCase().equals("id"))
                {
                    prop = field;
                    break;
                }
            }
            if(prop!=null)
            {
                ti.primaryKey = prop.getName().toLowerCase();
                ti.autoIncrement = Sql2oCache.isValueType(prop.getType());
            }
        }
        return ti;
    }


}
