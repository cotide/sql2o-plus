package com.sqltest.db;


import com.sqltest.model.UserInfo;
import io.github.cotide.dapper.exceptions.SqlBuildException;
import io.github.cotide.dapper.query.OrderBy;
import org.junit.Test;
import io.github.cotide.dapper.query.Sql;

/**
 * SQL对象测试
 */
public class SqlTest {


    @Test
    public void selectEntity() {
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
    public void sqlWhere() {

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
    public void sqlWhereLambda() {

        Sql sql =  Sql.builder().select().from(UserInfo.class)
                .where(UserInfo::getName,"Test");
        System.out.println("Sql语句:");
        System.out.println(sql.getFinalSql());
        for (Object item : sql.getFinalArgs())
        {
            System.out.println("参数值:");
            System.out.println(item.toString());
        }
    }


    @Test
    public void sqlWhereIn()   {

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
    public void sqlLambda() {

        Sql sql = Sql.builder().select().from(UserInfo.class)
                .where(UserInfo::getName,"Test")
                .whereIn(UserInfo::getId,1,2)
                .orderBy(UserInfo::getCreateTime,OrderBy.DESC);

        System.out.println("Sql语句:");
        System.out.println(sql.getFinalSql());
        for (Object item : sql.getFinalArgs())
        {
            System.out.println("参数值:");
            System.out.println(item.toString());
        }
    }

    @Test
    public void sqlWhereInLambda2() {

        Sql sql = Sql.builder().append("select * from user_info ")
                .where(UserInfo::getId,1)
                .whereIn(UserInfo::getId,1)
                .whereIn(UserInfo::getId,1,2,3)
                .whereIn(UserInfo::getName,"A","B");

        System.out.println("Sql语句:");
        System.out.println(sql.getFinalSql());
        for (Object item : sql.getFinalArgs())
        {
            System.out.println("参数值:");
            System.out.println(item.toString());
        }
    }


    @Test
    public void sqlOrderByLambda(){
        Sql sql = Sql.builder().append("select * from user_info ")
              .orderBy("user_id desc")
              .orderBy("user_id", OrderBy.DESC)
              .orderBy(UserInfo::getId)
              .orderBy(UserInfo::getId,OrderBy.DESC);

        System.out.println("Sql语句:");
        System.out.println(sql.getFinalSql());
        for (Object item : sql.getFinalArgs())
        {
            System.out.println("参数值:");
            System.out.println(item.toString());
        }

    }




    @Test
    public  void  parm()
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
