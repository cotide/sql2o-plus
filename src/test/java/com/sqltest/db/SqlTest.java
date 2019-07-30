package com.sqltest.db;


import com.sqltest.model.UserInfo;
import com.sqltest.model.UserType;
import io.github.cotide.dapper.query.enums.OrderBy;
import org.junit.Test;
import io.github.cotide.dapper.query.Sql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * SQL对象测试
 */
public class SqlTest {


    @Test
    public void selectEntity() {
        Sql sql = Sql.builder().select().from(UserInfo.class)
                .where("user_Name = ?","leo")
                .where("user_Name = ?","leo2");
        System.out.println("SQL语句:");
        System.out.println(sql.getFinalSql());
        System.out.println("SQL参数值:");
        Arrays.stream(sql.getFinalArgs()).forEach(System.out::println);
    }

    @Test
    public void sqlWhere() {

        Sql sql = Sql.builder().append("select * from user_info ").where("id = ?",1).where(" id = ?",2);
        System.out.println("Sql语句:");
        System.out.println(sql.getFinalSql());
        System.out.println("SQL参数值:");
        Arrays.stream(sql.getFinalArgs()).forEach(System.out::println);
    }


    @Test
    public void sqlWhereLambda() {

        Sql sql =  Sql.builder().select().from(UserInfo.class)
                .where(UserInfo::getName,"Test");
        System.out.println("Sql语句:");
        System.out.println(sql.getFinalSql());
        System.out.println("SQL参数值:");
        Arrays.stream(sql.getFinalArgs()).forEach(System.out::println);
    }


    @Test
    public void sqlWhereIn()   {

        Sql sql = Sql.builder().append("select * from user_info ")
               //.where("id = ?","aaaa")
                //.where(" id = ?","bbbb")
                //.whereIn("id",1,2,3)
                .whereIn("id","A","B","C");
                //.where("name = ? and name = ?","ccc","ddd");

        System.out.println("Sql语句:");
        System.out.println(sql.getFinalSql());
        System.out.println("SQL参数值:");
        Arrays.stream(sql.getFinalArgs()).forEach(System.out::println);
    }




    @Test
    public void sqlWhereIn2()   {

        List<String> parmList = new ArrayList<>();
        parmList.add("A");
        parmList.add("B");
        parmList.add("C");

        Sql sql = Sql.builder().append("select * from user_info ")
                .whereIn("id",parmList);
        System.out.println("Sql语句:");
        System.out.println(sql.getFinalSql());
        System.out.println("SQL参数值:");
        Arrays.stream(sql.getFinalArgs()).forEach(System.out::println);
    }


    @Test
    public void sqlLambda() {

        Sql sql = Sql.builder().select().from(UserInfo.class)
                .where(UserInfo::getName,"Test")
                .whereIn(UserInfo::getId,1,2)
                .orderBy(UserInfo::getCreateTime,OrderBy.DESC);

        System.out.println("Sql语句:");
        System.out.println(sql.getFinalSql());
        System.out.println("SQL参数值:");
        Arrays.stream(sql.getFinalArgs()).forEach(System.out::println);
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
        System.out.println("SQL参数值:");
        Arrays.stream(sql.getFinalArgs()).forEach(System.out::println);
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
        System.out.println("SQL参数值:");
        Arrays.stream(sql.getFinalArgs()).forEach(System.out::println);
    }


    @Test
    public void sqlJoin(){

        Sql sql = Sql.builder().select().from(UserInfo.class,"a")
                .leftJoin(UserType.class,"b")
                .on("a.user_id = b.id")
                .where("a",UserInfo::getId,1);
        System.out.println("Sql语句:");
        System.out.println(sql.getFinalSql());
        System.out.println("SQL参数值:");
        Arrays.stream(sql.getFinalArgs()).forEach(System.out::println);
    }


    @Test
    public void sqlJoin2(){

        Sql sql = Sql.builder().select().from(UserInfo.class,"a")
                .leftJoin(UserType.class,"b")
                .on("a.user_id = b.id")
                .leftJoin(UserType.class,"c")
                .on("c.id = b.id")
                .where("a",UserInfo::getId,1)
                .whereIn("b",UserType::getId,1,2,3)
                .where("c.id = ? ",555);
        System.out.println("Sql语句:");
        System.out.println(sql.getFinalSql());
        System.out.println("SQL参数值:");
        Arrays.stream(sql.getFinalArgs()).forEach(System.out::println);

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
