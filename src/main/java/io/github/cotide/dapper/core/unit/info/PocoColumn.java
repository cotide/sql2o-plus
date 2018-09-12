package io.github.cotide.dapper.core.unit.info;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

import io.github.cotide.dapper.basic.enums.IEnum;
import io.github.cotide.dapper.basic.enums.IntegerEnum;
import io.github.cotide.dapper.basic.enums.StringEnum;
import io.github.cotide.dapper.core.attr.Column;
import io.github.cotide.dapper.core.attr.Ignore;
import io.github.cotide.dapper.core.attr.PrimaryKey;
import io.github.cotide.dapper.basic.enums.EnumMapping;
import lombok.NonNull;
import io.github.cotide.dapper.basic.domain.Entity;
import io.github.cotide.dapper.exceptions.SqlBuildException;

/**
 * 表字段信息
 */
@lombok.Getter
@lombok.Setter
public class PocoColumn {

    /**
     * 字段名
     */
    @NonNull
    private String columnName;

    /**
     * 字段值
     */
    private Field field;


    /**
     * 是否主键
     */
    private boolean isPrimaryKey = false;


    public <T extends Entity>  Object getValue(T model) {
        return getValue(model,true);
    }

        public <T extends Entity>  Object getValue(T model, boolean allowNull) {

            field.setAccessible(true);
            try {
                Object value = field.get(model);
                if (null != value) {

                    if (value instanceof Enum) {

                        if(value instanceof StringEnum)
                        {
                            return value.toString();
                        }else if(value instanceof IntegerEnum)
                        {
                           return  ((IEnum)value).getCode();
                        }



                        EnumMapping enumMapping = field.getAnnotation(EnumMapping.class);
                        if (null == enumMapping) {
                            return value.toString();
                        } else {
                            if (enumMapping.value().equals(EnumMapping.TO_STRING)) {
                                return  value.toString();
                            }
                            if (enumMapping.value().equals(EnumMapping.ORDINAL)) {
                                return  ((Enum) value).ordinal();
                            }
                        }
                    } else {
                       return  value;
                    }
                } else if (allowNull) {
                   return null;
                }
            } catch (IllegalArgumentException | IllegalAccessException e) {
                throw new SqlBuildException("illegal argument or Access:"+e.getMessage());
            }
            return  null;
    }




    public static List<PocoColumn> fromFields(Class<?> modelClass){
        return Arrays.stream(
                modelClass.getDeclaredFields())
                .map(PocoColumn::fromField)
                .collect(Collectors.toList());
    }

    public static PocoColumn fromField(Field field)
    {
        // 自定义属性注解,检查是否有Column或者PrimaryKey注解描述
        Column column = field.getAnnotation(Column.class);
        PocoColumn result = new PocoColumn();
        if (null != column) {
            result.columnName = column.value();
            return result;
        }else{
            PrimaryKey primaryKey = field.getAnnotation(PrimaryKey.class);
            if (null != primaryKey) {
                result.columnName = primaryKey.value();
                return result;
            }
            // 忽略属性注解
            if(field.getAnnotation(Ignore.class)!=null)
            {
                return null;
            }
        }
        result.columnName = toColumnName(field.getName());
        return  result;
    }

    // region Helper

    private static String toColumnName(String propertyName) {
        return toColumnName(propertyName,false);
    }

    private static String toColumnName(String propertyName,Boolean isAuto) {
        if(isAuto)
        {
            StringBuilder result = new StringBuilder();
            if (propertyName != null && propertyName.length() > 0) {
                result.append(propertyName.substring(0, 1).toLowerCase());
                for (int i = 1; i < propertyName.length(); i++) {
                    String s = propertyName.substring(i, i + 1);
                    if (s.equals(s.toUpperCase())) {
                        result.append("_");
                        result.append(s.toLowerCase());
                    } else {
                        result.append(s);
                    }
                }
            }
            return result.toString();
        }else{
            return  propertyName.toLowerCase();
        }
    }
    // endregion
}
