package com.sqltest;

import com.sqltest.model.UserInfo;
import org.dapper.Database;
import org.junit.Test;
import org.sql2o.Connection;
import org.sql2o.Query;
import org.sql2o.Sql2o;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseTest {

    final String url = "jdbc:mysql://192.168.1.100:3307/g_main?useUnicode=true&characterEncoding=utf-8&serverTimezone=GMT%2B8&useSSL=false";
    final String user ="root";
    final String pass ="123456";


    @Test
    public  void  sql2oTest(){
        Sql2o sql2o = new Sql2o(url,user,pass);
        List<UserInfo> users;
        try (Connection con = sql2o.open()) {
            final String query2 =
                    "select  user_id,user_Name,login " +
                            "from user_info " +
                            "where user_id = 1";

            users =  con.createQuery(query2)
                     .setAutoDeriveColumnNames(true)
                    .throwOnMappingFailure(false)
                    .executeAndFetch(UserInfo.class);

            for (UserInfo item : users)
            {
                System.out.println("id:"+item.getId());
                System.out.println("name:"+item.getName());
                System.out.println("login:"+item.getLogin());
                System.out.println("other:"+item.getOther());
            }

        }
    }


    @Test
    public  void sql2oGetOneTest(){
        Sql2o sql2o = new Sql2o(url,user,pass);
        UserInfo users;
        try (Connection con = sql2o.open()) {
            final String query2 =
                    "select  *  from user_info  where user_id = 1";

            users =  con.createQuery(query2)
                    .setAutoDeriveColumnNames(true)
                    .throwOnMappingFailure(false)
                    .executeAndFetchFirst(UserInfo.class);

            System.out.println("id:"+users.getId());
            System.out.println("name:"+users.getName());
            System.out.println("login:"+users.getLogin());
            System.out.println("other:"+users.getOther());

        }
    }



    @Test
    public  void  sql2oInsertTest(){
        Sql2o sql2o = new Sql2o(url,user,pass);
        UserInfo domain = new UserInfo();
        domain.setName("Test");
        domain.setLogin(10086);
        domain.setPwd("123456");
        try (Connection con = sql2o.open()) {
            final String insertSql  =
                    "INSERT INTO user_info (user_Name,password,login,create_time) VALUES (?,?,?,?)";

            List<Object> values = new ArrayList<>();
            values.add("Test");
            values.add("123456");
            values.add(10086);
            values.add(new Date());
            Object result =  con.createQuery(insertSql).withParams(values).executeUpdate().getKey();
            System.out.println("resultKey:"+result);
        }
    }

}
