package com.sqltest.db;

import com.sqltest.base.BaseTest;
import com.sqltest.dto.UserInfoDto;
import com.sqltest.model.UserInfo;
import com.sqltest.model.UserType;
import io.github.cotide.dapper.Database;
import io.github.cotide.dapper.basic.collections.PageList;
import io.github.cotide.dapper.query.Sql;
import org.junit.Test;

import java.util.List;

public class SelectExTest extends BaseTest {




    @Test
    public void  getDtoListTest(){
        Database db = getDatabase();

        // getDtoList(Class<TDto> returnType,Sql inter)
        Sql sql1 = Sql.builder().select(" user_id as id, user_Name as name ").from(UserInfo.class).where("user_id = ?", 1);
        List<UserInfoDto> result = db.getSqlQuery().getDtoList(UserInfoDto.class, sql1);
        assert (result.size() > 0 && result.get(0).getId() > 0) : "UserInfoRepository getDtoList(Class<TDto> returnType,Sql inter) is error";
        System.out.println(">>>>>>>>>> Result <<<<<<<<<<");
        for (UserInfoDto item : result) {
            System.out.println("id:" + item.getId());
            System.out.println("user_Name:" + item.getName());
            System.out.println("login:" + item.getLogin());
        }

        // getDtoList(Class<TDto> returnType,String inter,Object ... param)
        String sql2 = "select user_id as id, user_Name as name from user_info where user_id = ? ";
        List<UserInfoDto> result2 = db.getSqlQuery().getDtoList(UserInfoDto.class, sql2, 1);
        assert (result2.size() > 0 && result2.get(0).getId() > 0) : "UserInfoRepository getDtoList(Class<TDto> returnType,String inter,Object ... param) is error";
        System.out.println(">>>>>>>>>> Result2 <<<<<<<<<<");
        for (UserInfoDto item : result2) {
            System.out.println("id:" + item.getId());
            System.out.println("user_Name:" + item.getName());
            System.out.println("login:" + item.getLogin());
        }
    }

    @Test
    public void  getDtoTest(){

        Database db = getDatabase();
        // getDto(Class<TDto> returnType, Sql inter)
        UserInfoDto result1 = db.getSqlQuery().getDto(
                UserInfoDto.class,
                Sql.builder().select("user_id as id, user_Name as name").from(UserInfo.class).where("user_id  = ?", 1));
        assert (result1 != null && result1.getId() > 0) : "UserInfoRepository getDto(Class<TDto> returnType, Sql inter) is error";
        System.out.println(">>>>>>>>>> Result2 <<<<<<<<<<");
        System.out.println("id:" + result1.getId());
        System.out.println("user_Name:" + result1.getName());
        System.out.println("login:" + result1.getLogin());
    }


    @Test
    public void  getFinalSql1(){

        Sql sql =   Sql.builder()
                       .select()
                       .from(UserInfo.class)
                       .where(UserInfo::getName,"Test");

        System.out.println(sql.getFinalSql());
        System.out.println("------");
        System.out.println(sql.getFinalSql());
        System.out.println("------");
        System.out.println(sql.getFinalSql());

//        Database db = getDatabase();
//        // int count(Sql inter)
//        int result = db.getSqlQuery().count(Sql.builder().select("count(1)").from(UserInfo.class)
//                .where("user_id in (?,?,?)", 1, 2, 3));
//        assert (result > 0) : "result value is error";
//        System.out.println(">>>>>>>>>> Result <<<<<<<<<<");
//        System.out.println("result size:" + result);
    }


    @Test
    public void  getFinalSql2(){

        Sql sql =  new Sql("select * from user_info  as  a ")
                .leftJoin(UserType.class,"b")
                .on(UserInfo::getUserTypeId,UserType::getId)
                .where("a",UserInfo::getName,"Test")
                .where("b",UserInfo::getName,"Test");
//        System.out.println(sql.getFinalSql());
//        System.out.println("------");
//        System.out.println(sql.getFinalSql());
//        System.out.println("------");
//        System.out.println(sql.getFinalSql());
        System.out.println("Sql语句:");
        System.out.println(sql.getFinalSql());
        System.out.println("SQL参数值:");
        sql.getFinalArgs().forEach(System.out::println);
    }


    @Test
    public void  getFinalSql3()
    {
        Sql sql = Sql.builder().select("*")
                .from(UserInfo.class,"a")
                .leftJoin(UserType.class,"b")
                .on("a",UserInfo::getUserTypeId,"b",UserType::getId)
                .leftJoin(UserType.class,"c")
                .on("a",UserInfo::getUserTypeId,"c",UserType::getId)
                .where("a",UserInfo::getName,"Test")
                .where("a",UserInfo::getName,"Test");
        System.out.println("Sql语句:");
        System.out.println(sql.getFinalSql());
        System.out.println("SQL参数值:");
        sql.getFinalArgs().forEach(System.out::println);
    }



    @Test
    public void  getFinalSql4()
    {
        Sql sql = Sql.builder().select("*")
                .from(UserInfo.class,"a")
                .where("a",UserInfo::getName,"Test")
                .where("a",UserInfo::getName,"Test");
        System.out.println("Sql语句:");
        System.out.println(sql.getFinalSql());
        System.out.println("SQL参数值:");
        sql.getFinalArgs().forEach(System.out::println);
    }
}
