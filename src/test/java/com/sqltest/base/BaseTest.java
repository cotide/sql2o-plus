package com.sqltest.base;

import com.alibaba.druid.pool.DruidDataSource;
import com.sqltest.dto.UserInfoDto;
import com.sqltest.model.UserInfo;
import io.github.cotide.dapper.Database;
import io.github.cotide.dapper.query.Sql;
import io.github.cotide.dapper.repository.inter.IRepository;
import org.junit.BeforeClass;
import org.junit.Test;

public class BaseTest {

    protected String url = "jdbc:mysql://10.10.10.101:3306/g_main_test?useUnicode=true&characterEncoding=utf-8&serverTimezone=GMT%2B8&useSSL=false";
    protected String user ="root";
    protected  String pass ="123456";

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
