package com.sqltest.base;

import com.alibaba.druid.pool.DruidDataSource;
import io.github.cotide.dapper.Database;
import org.junit.BeforeClass;

public class BaseTest {

    String url = "jdbc:mysql://192.168.11.101:3306/g_main_test?useUnicode=true&characterEncoding=utf-8&serverTimezone=GMT%2B8&useSSL=false";
    String user ="root";
    String pass ="123456";

    @BeforeClass
    public static void init() {

    }



    protected Database getDatabase() {

        return new Database(url,user,pass);
    }


    // Druid DataSource
    protected Database getDruidDatabase() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl(url);
        dataSource.setUsername(user);
        dataSource.setPassword(pass);
        return new Database(dataSource);
    }
}
