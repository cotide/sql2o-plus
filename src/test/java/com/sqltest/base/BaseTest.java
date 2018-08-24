package com.sqltest.base;

import org.dapper.Database;
import org.junit.BeforeClass;

public class BaseTest {

    @BeforeClass
    public static void init() {

    }



    protected Database getDatabase() {
        String url = "jdbc:mysql://192.168.1.100:3307/g_main?useUnicode=true&characterEncoding=utf-8&serverTimezone=GMT%2B8&useSSL=false";
        String user ="test";
        String pass ="123456";
        return new Database(url,user,pass);
    }
}
