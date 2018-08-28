package com.sqltest.db;

import com.sqltest.model.UserInfo;
import io.github.cotide.dapper.core.attr.Ignore;
import io.github.cotide.dapper.core.unit.info.PocoColumn;
import io.github.cotide.dapper.core.unit.info.PocoData;
import io.github.cotide.dapper.core.unit.info.TableInfo;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Arrays;
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




    /**
     * 实体字段映射测试
     */
    @Test
    public void columnInfo_Test(){

        UserInfo userInfo = new UserInfo();
        userInfo.setId(1000);
        userInfo.setName("test");
        userInfo.setPwd("123456");
        userInfo.setLogin(6666);



        Field field = Arrays.stream(UserInfo.class.getDeclaredFields())
                .filter(x->x.getName()=="other").findFirst().orElse(null);
        assert (field!=null)
                :"column mapper login not found";
        assert (field.isAnnotationPresent(Ignore.class))
                :"column mapper login not found";

    }



}
