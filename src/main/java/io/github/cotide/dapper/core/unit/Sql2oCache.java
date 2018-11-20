package io.github.cotide.dapper.core.unit;

import io.github.cotide.dapper.core.attr.Column;
import io.github.cotide.dapper.core.attr.Ignore;
import io.github.cotide.dapper.core.attr.PrimaryKey;
import io.github.cotide.dapper.core.attr.Table;
import sql2o.Sql2o;
import sql2o.Sql2oException;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public final class Sql2oCache {

    /**
     * 值类型数组
     */
    static final Set<Class<?>> WRAPPER_TYPES = getWrapperTypes();

    static final Map<Class<?>,String> CACHE_TABLE_NAME = new HashMap<>();
    static final Map<Class<?>, Map<String, String>> CACHE_MODEL_COLUMN_MAPPINGS = new HashMap<>(8);
     static final Map<Class<?>, String>              CACHE_PK_COLUMN_NAME  = new HashMap<>(8);
    static final Map<Class<?>, Boolean>              CACHE_PK_COLUMN_IS_AUTO  = new HashMap<>(8);
    static final Map<SerializedLambda,String> CACHE_COLUMN_LAMBDA_NAME = new HashMap<>(8);
    static final Map<SerializedLambda,String> DTO_CACHE_COLUMN_LAMBDA_NAME = new HashMap<>(8);


    static final Map<Class<?>, String>              CACHE_PK_FIELD_NAME   = new HashMap<>(8);
    static final Map<Class, List<Field>> CACHE_MODEL_AVAILABLE_FIELDS = new HashMap<>();
    static final Map<String, String>   CACHE_FIELD_COLUMN_NAME  = new HashMap<>();

    public static String getTableName(Class<?> modelClass) {
        return CACHE_TABLE_NAME.computeIfAbsent(modelClass, type -> {
            Table table = type.getAnnotation(Table.class);
            if (null != table && !table.value().isEmpty()) {
                return table.value();
            }
            return type.getSimpleName();
        });
    }

    public static Map<String, String> computeModelColumnMappings(Class<?> modelType) {

       return modelType.isPrimitive() || modelType.equals(String.class)
               ? new HashMap<>()
               : CACHE_MODEL_COLUMN_MAPPINGS.computeIfAbsent(modelType, model -> {
                    List<Field> fields = computeModelFields(model);
                    return fields.stream()
                            .collect(toMap(Sql2oCache::getColumnName, Field::getName));
       });
    }


    public static String getLambdaColumnName(SerializedLambda serializedLambda) {
        return CACHE_COLUMN_LAMBDA_NAME.computeIfAbsent(serializedLambda, lambda -> {
            String className  = serializedLambda.getImplClass().replace("/", ".");
            String methodName = serializedLambda.getImplMethodName();
            String fieldName  = methodToFieldName(methodName);
            try {
                Field field = Class.forName(className).getDeclaredField(fieldName);
                return getColumnName(field);
            } catch (NoSuchFieldException | ClassNotFoundException e) {
                throw new Sql2oException(e);
            }
        });
    }


    public static String getDtoLambdaColumnName(SerializedLambda serializedLambda) {
        return DTO_CACHE_COLUMN_LAMBDA_NAME.computeIfAbsent(serializedLambda, lambda -> {
            String className  = serializedLambda.getImplClass().replace("/", ".");
            String methodName = serializedLambda.getImplMethodName();
            String fieldName  = methodToFieldName(methodName);
            try {
                Field field = Class.forName(className).getDeclaredField(fieldName);
                return field.getName();
            } catch (NoSuchFieldException | ClassNotFoundException e) {
                throw new Sql2oException(e);
            }
        });
    }

    public static List<Field> computeModelFields(Class clazz) {
        return clazz.isPrimitive() || clazz.equals(String.class)
                ? new ArrayList<>()
                : CACHE_MODEL_AVAILABLE_FIELDS.computeIfAbsent(clazz, model -> Stream.of(model.getDeclaredFields())
                        .filter(field -> !isIgnore(field))
                        .collect(toList()));
    }

    public static List<Field> computeNoKeyModelFields(Class clazz) {

        return clazz.isPrimitive() || clazz.equals(String.class)
                ? new ArrayList<>()
                : computeModelFields(clazz).stream()
                        .filter(field-> !isPrimarykey(field))
                        .collect(toList());
    }

    public static String getColumnName(Field field) {
        String fieldName = field.getName();
        String key       = field.getDeclaringClass().getSimpleName() + "_" + fieldName;

        return CACHE_FIELD_COLUMN_NAME.computeIfAbsent(key, f -> {
            Column column = field.getAnnotation(Column.class);
            if (null != column) {
                return column.value();
            }
            PrimaryKey primaryKey = field.getAnnotation(PrimaryKey.class);
            if(null != primaryKey)
            {
                return primaryKey.value();
            }
            return Sql2oUtils.toUnderline(fieldName);
        });
    }




    public static boolean isIgnore(Field field) {
        if (null != field.getAnnotation(Ignore.class)){
            return true;
        }
        return false;
    }

    public static boolean isPrimarykey(Field field) {
        if (null != field.getAnnotation(PrimaryKey.class)){
            return true;
        }
        return false;
    }


    public static String getPKColumn(Class<?> modelClass) {
        String pkColumn = CACHE_PK_COLUMN_NAME.get(modelClass);
        if (null != pkColumn) {
            return pkColumn;
        }

        // 表主键
        Field[] fields = modelClass.getDeclaredFields();
        for (Field field : fields) {
            PrimaryKey primaryKey = field.getDeclaredAnnotation(PrimaryKey.class);
            if (primaryKey != null) {
                pkColumn = primaryKey.value();
                break;
            }
        }
        CACHE_PK_COLUMN_NAME.put(modelClass, pkColumn);
        return pkColumn;
    }

    public static boolean isAutoIncreMent(Class<?> modelClass)
    {
        Boolean isAuto = CACHE_PK_COLUMN_IS_AUTO.get(modelClass);
        if (null != isAuto) {
            return isAuto;
        }
        String pkColumn = Sql2oCache.getPKColumn(modelClass);
        if(pkColumn==null)
        {
            return false;
        }
        Field[] fields = modelClass.getDeclaredFields();
        for (Field field : fields) {
            PrimaryKey primaryKey = field.getDeclaredAnnotation(PrimaryKey.class);
            if(primaryKey!=null && primaryKey.value().equals(pkColumn))
            {
                CACHE_PK_COLUMN_IS_AUTO.put(modelClass, primaryKey.autoIncrement());
                return primaryKey.autoIncrement();
            }
        }
        return  false;
    }

    public static String getPKField(Class<?> modelClass) {
        String pkField = CACHE_PK_FIELD_NAME.get(modelClass);
        if (null != pkField) {
            return pkField;
        }
        String pkColumn = Sql2oCache.getPKColumn(modelClass);
        if(pkColumn==null)
        {
            return null;
        }
        Field[] fields = modelClass.getDeclaredFields();
        for (Field field : fields) {
            PrimaryKey primaryKey = field.getDeclaredAnnotation(PrimaryKey.class);
            if(primaryKey!=null && primaryKey.value().equals(pkColumn))
            {
                pkField = field.getName();
                break;
            }
        }
        CACHE_PK_FIELD_NAME.put(modelClass, pkField);
        return pkField;
    }





    /**
     * 是否为值类型
     * @param clazz
     * @return
     */
    public static Boolean isValueType(Class<?> clazz)
    {
        return WRAPPER_TYPES.contains(clazz);
    }


    // region Helper
    private static Set<Class<?>> getWrapperTypes()
    {
        Set<Class<?>> ret = new HashSet<Class<?>>();
        ret.add(Boolean.class);
        ret.add(Character.class);
        ret.add(Byte.class);
        ret.add(Short.class);
        ret.add(Integer.class);
        ret.add(Long.class);
        ret.add(Float.class);
        ret.add(Double.class);
        ret.add(Void.class);
        return ret;
    }


    private static String methodToFieldName(String methodName) {
        return capitalize(methodName.replace("get", ""));
    }

    private static String capitalize(String input) {
        return input.substring(0, 1).toLowerCase() + input.substring(1);
    }

    // endregion
}
