package io.github.cotide.dapper.core.unit.info;

import io.github.cotide.dapper.core.attr.Column;
import io.github.cotide.dapper.core.attr.Ignore;
import io.github.cotide.dapper.core.attr.PrimaryKey;
import io.github.cotide.dapper.core.attr.Table;
import lombok.Getter;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;


public class PocoData {

    private  Class<?> modelClass;

    @Getter
    private TableInfo tableInfo;

    @Getter
    private Map<String, PocoColumn> columns;




    @Getter
    private Boolean isHavePrimaryKey = false;

    private static final ReentrantReadWriteLock rrwl = new ReentrantReadWriteLock();
    private static final ReentrantReadWriteLock.ReadLock rl = rrwl.readLock();
    private static final ReentrantReadWriteLock.WriteLock wl = rrwl.writeLock();
    private static Map<Class, PocoData> pojoDatas = new HashMap<>();

    public PocoData() {
        tableInfo = new TableInfo();
        columns = new HashMap<>();
    }

    public static PocoData forType(Class<?> type) {
        rl.lock();
        try {
            if (pojoDatas.containsKey(type)){
                return pojoDatas.get(type);
            }
        } finally {
            rl.unlock();
        }
        wl.lock();
        try {
            if (pojoDatas.containsKey(type))
            {
               return pojoDatas.get(type);
            }
            PocoData pd = new PocoData(type);
            pojoDatas.put(type, pd);
            return pd;
        } finally {
            wl.unlock();
        }
    }

    public PocoData(Class<?> type) {
        modelClass = type;
        String tableName = "";
        boolean autoIncrement = true;
        if (modelClass.isAnnotationPresent(Table.class)) {
            Table tableNameAnn = modelClass.getAnnotation(Table.class);
            if (tableNameAnn != null && tableNameAnn.value().length() > 0) {
                tableName = tableNameAnn.value();
            } else {
                tableName = modelClass.getSimpleName();
            }
        }

        tableInfo = new TableInfo();
        tableInfo.setTableName(tableName);
        tableInfo.setAutoIncrement(autoIncrement);

        columns = new HashMap<>();
        Field[] fields = modelClass.getDeclaredFields();

        for (Field field : fields) {
            if (field.isAnnotationPresent(Ignore.class))
            {
                continue;
            }
            PocoColumn pc = new PocoColumn();

            if (field.isAnnotationPresent(Column.class)) {
                Column columnAnn = field.getAnnotation(Column.class);
                if (columnAnn != null && columnAnn.value().length() > 0) {
                    pc.setColumnName(columnAnn.value());
                } else {
                    pc.setColumnName(field.getName());
                }
            }else  if(field.isAnnotationPresent(PrimaryKey.class))
            {
                PrimaryKey primaryKey = field.getAnnotation(PrimaryKey.class);
                if (primaryKey != null && primaryKey.value().length() > 0) {
                    tableInfo.setPrimaryKey(primaryKey.value());
                    pc.setColumnName(primaryKey.value());
                }else{
                    pc.setColumnName(field.getName());
                }
                isHavePrimaryKey = true;
                pc.setPrimaryKey(true);
            }
            else {
                pc.setColumnName(field.getName());
            }
            pc.setField(field);
            columns.put(field.getName().toLowerCase(), pc);
        }

    }


}
