package com.sqltest.dapper;

import com.sqltest.model.UserInfo;
import io.github.cotide.dapper.core.attr.Table;
import io.github.cotide.dapper.core.unit.Sql2oCache;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Sql2oCacheTest {

    @Test
    public void  getTableName(){

        String tableName = Sql2oCache.getTableName(UserInfo.class);
        assert (tableName.equals("user_info"));
    }

    @Test
    public void computeModelFields(){

        List<Field> fields = Sql2oCache.computeModelFields(UserInfo.class);
        for (Field item : fields)
        {
            System.out.println(item.getName());
        }
    }

    @Test
    public void  computeModelColumnMappings(){
        Map<String, String>  colums =  Sql2oCache.computeModelColumnMappings(UserInfo.class);
        for (Map.Entry<String, String> entry : colums.entrySet()) {
            System.out.println("key : " + entry.getKey() + " values : " + entry.getValue());
        }
    }

    @Test
    public void getPKField(){

        String pkField  =  Sql2oCache.getPKField(UserInfo.class);
        System.out.println(pkField);
        assert (pkField.equals("id"));
    }



    @Test
    public void getPKColumn(){

        String pkColumn  =  Sql2oCache.getPKColumn(UserInfo.class);
        System.out.println(pkColumn);
        assert (pkColumn.equals("user_id"));
    }


}
