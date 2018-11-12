package com.sqltest.db;

import com.sqltest.base.BaseTest;
import com.sqltest.dto.UserInfoDto;
import com.sqltest.model.UserInfo;
import com.sqltest.model.UserType;
import io.github.cotide.dapper.Database;
import io.github.cotide.dapper.query.Sql;
import io.github.cotide.dapper.query.enums.OrderBy;
import io.github.cotide.dapper.query.map.ResultMap;
import org.junit.Test;

import java.util.Arrays;

public class SelectLambdaTest extends BaseTest {


    @Test
    public void sqlSelectMapLambda() {
//        ResultMap column  = new ResultMap();
//        column.put("a",UserInfo::getName,"name")
//              .put("a",UserInfo::getId, UserInfoDto::getId)
//              .put("a",UserInfo::getName, "name")
//              .put("b",UserInfo::getUserTypeId);
        Sql sql = Sql.builder().select(
                         ResultMap.result()
                        .put("a",UserInfo::getName,"name")
                        .put("a",UserInfo::getId, UserInfoDto::getId)
                        .put("a",UserInfo::getName, "name")
                        .put("b",UserInfo::getUserTypeId))
                .from(UserInfo.class,"a")
                .join(UserType.class,"b")
                .on("a",UserInfo::getUserTypeId,
                        "b",UserType::getId)
                //.on("a.user_type_id = b.id")
                .where(UserInfo::getName,"Test")
                .whereIn(UserInfo::getId,1,2)
                .orderBy(UserInfo::getCreateTime, OrderBy.DESC);

        System.out.println("Sql语句:");
        System.out.println(sql.getFinalSql());
        System.out.println("SQL参数值:");
        Arrays.stream(sql.getFinalArgs()).forEach(System.out::println);
    }



    @Test
    public void sqlSelectDtoMapLambda() {

        Sql sql = Sql.builder()
                .selectTo(UserInfoDto.class)
                .from(UserInfo.class)
                .where(UserInfo::getName,"Test")
                .whereIn(UserInfo::getId,1,2)
                .orderBy(UserInfo::getCreateTime, OrderBy.DESC);

        System.out.println("Sql语句:");
        System.out.println(sql.getFinalSql());
        System.out.println("SQL参数值:");
        Arrays.stream(sql.getFinalArgs()).forEach(System.out::println);

    }


    @Test
    public void sqlSelectLambda() {

        Sql sql = Sql.builder().select(
                UserInfo::getId,
                UserInfo::getName)
                .from(UserInfo.class)
                .where(UserInfo::getName,"Test")
                .whereIn(UserInfo::getId,1,2)
                .orderBy(UserInfo::getCreateTime, OrderBy.DESC);
        System.out.println("Sql语句:");
        System.out.println(sql.getFinalSql());
        System.out.println("SQL参数值:");
        Arrays.stream(sql.getFinalArgs()).forEach(System.out::println);
    }


    @Test
    public void sqlSelectLambda1() {

        Sql sql = Sql.builder().select(
                UserInfo::getId)
                .from(UserInfo.class)
                .where(UserInfo::getName,"Test")
                .whereIn(UserInfo::getId,1,2)
                .orderBy(UserInfo::getCreateTime, OrderBy.DESC);

        System.out.println("Sql语句:");
        System.out.println(sql.getFinalSql());
        System.out.println("SQL参数值:");
        Arrays.stream(sql.getFinalArgs()).forEach(System.out::println);
    }

}
