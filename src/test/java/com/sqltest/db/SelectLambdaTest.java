package com.sqltest.db;

import com.sqltest.base.BaseTest;
import com.sqltest.dto.UserInfoDto;
import com.sqltest.model.UserInfo;
import com.sqltest.model.UserType;
import io.github.cotide.dapper.query.Sql;
import io.github.cotide.dapper.query.enums.OrderBy;
import org.junit.Test;
import java.util.Arrays;

public class SelectLambdaTest extends BaseTest {


    @Test
    public void sqlSelectLambda() {

        Sql sql = Sql.builder().select(
                UserInfo::getId,
                UserInfo::getName)
                .from(UserInfo.class)
                .where(UserInfo::getName,"Test")
                .whereIn(UserInfo::getId,1,2)
                .order(UserInfo::getCreateTime, OrderBy.DESC);
        System.out.println("Sql语句:");
        System.out.println(sql.getFinalSql());
        System.out.println("SQL参数值:");
        sql.getFinalArgs().forEach(System.out::println);
    }


    @Test
    public void sqlSelectLambda1() {

        Sql sql = Sql.builder().select(
                UserInfo::getId)
                .from(UserInfo.class)
                .where(UserInfo::getName,"Test")
                .whereIn(UserInfo::getId,1,2)
                .order(UserInfo::getCreateTime, OrderBy.DESC);

        System.out.println("Sql语句:");
        System.out.println(sql.getFinalSql());
        System.out.println("SQL参数值:");
        sql.getFinalArgs().forEach(System.out::println);
    }

}
