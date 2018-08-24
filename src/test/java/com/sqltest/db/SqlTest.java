package com.sqltest.db;


import com.sqltest.model.UserInfo;
import org.dapper.core.exceptions.SqlBuildException;
import org.junit.Test;
import org.dapper.query.Sql;

/**
 * SQL对象测试
 */
public class SqlTest {


    @Test
    public void selectEntityTest() {
        Sql sql = Sql.builder().select().from(UserInfo.class)
                .where("user_Name = @0","leo")
                .where("user_Name = @1","leo2");
        System.out.println("SQL语句:");
        System.out.println(sql.getFinalSql());
        System.out.println("SQL参数值:");
        int index = 0;
        for (Object item: sql.getFinalArgs() ) {
            System.out.println("arg"+index +","+item);
            index++;
        }
    }

    @Test
    public void sqlWhereTest() {

        Sql sql = Sql.builder().append("select * from user_info ").where("id = @0",1).where(" id = @0",2);
        System.out.println("Sql语句:");
        System.out.println(sql.getFinalSql());
        for (Object item : sql.getFinalArgs())
        {
            System.out.println("参数值:");
            System.out.println(item.toString());
        }
    }



    @Test
    public void sqlWhereInTest() throws SqlBuildException {

        Sql sql = Sql.builder().append("select * from user_info ")
               .where("id = @0","aaaa")
                //.where(" id = @0","bbbb")
                .whereIn("id",1,2,3)
                .whereIn("id","A","B","C");
                //.where("name = @0 and name = @1","ccc","ddd");
        System.out.println("Sql语句:");
        System.out.println(sql.getFinalSql());
        for (Object item : sql.getFinalArgs())
        {
            System.out.println("参数值:");
            System.out.println(item.toString());
        }
    }


    @Test
    public  void  parmTest()
    {
        getParmType();
        getParmType(1);
        getParmType(1,2,3);
        getParmType("1");
        getParmType("1","2","3");
    }


    // region Helper

    private void getParmType(Object... paras)
    {
        if(paras==null)
        {
            System.out.println("object is null");
        }

        if(paras.length> 0)
        {
            System.out.println("It's a Array."+paras.getClass().getComponentType());
            if(paras[0] instanceof String )
            {
                System.out.println("It's a String");
            }else    if(paras[0] instanceof Integer )
            {
                System.out.println("It's a Integer");
            }
        }
    }

    // endregion
}
