package com.sqltest.db;

import com.sqltest.model.UserInfo;
import org.dapper.core.unit.info.PocoColumn;
import org.dapper.core.unit.info.PocoData;
import org.dapper.core.unit.info.TableInfo;
import org.junit.Test;

import java.util.Map;

public class TableMapperTest {

    /**
     * 实体表映射测试
     */
    @Test
    public void tableInfoTest(){
        // 表名
        String tableName = "user_info";
        String primaryKey = "user_id";
        TableInfo result =   TableInfo.fromPoco(UserInfo.class);
        // 判断获取表名
        System.out.println(result.getTableName());
        assert(result.getTableName().equals(tableName) );
        // 判断主键
        System.out.println(result.getPrimaryKey());
        assert(result.getPrimaryKey().equals(primaryKey));
    }


    @Test
    public void  pocoDataTest(){


        UserInfo userInfo = new UserInfo();
        userInfo.setId(1000);
        userInfo.setName("test");
        userInfo.setPwd("123456");
        userInfo.setLogin(6666);

        PocoData result = PocoData.forType(UserInfo.class);
        TableInfo tableInfo = result.getTableInfo();
        System.out.println(">>>>>>>>>> tableInfo result <<<<<<<<<<");
        System.out.println(tableInfo.getTableName());
        System.out.println(tableInfo.getPrimaryKey());

        Map<String, PocoColumn> columns = result.getColumns();
        System.out.println(">>>>>>>>>> columns result <<<<<<<<<<");
        for (String key : columns.keySet())
        {
            PocoColumn col = columns.get(key);
            Object value = col.getValue(userInfo,true);
            System.out.println(String.format("[key:%s],[columnName:%s],[value:%s]",key,col.getColumnName(),value));
        }
    }


//    /**
//     * 实体字段映射测试
//     */
//    @Test
//    public void columnInfo_Test(){
//        Field[] fields = UserInfo.class.getDeclaredFields();
//        for (Field field : fields) {
//            PocoColumn result = PocoColumn(field);
//            if(result != null)
//            {
//                System.out.println(result.getColumnName());
//            }
//        }
//    }
}
