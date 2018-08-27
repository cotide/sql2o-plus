package com.sqltest.db;

import com.sqltest.base.BaseTest;
import com.sqltest.dto.UserInfoDto;
import com.sqltest.model.UserInfo;
import org.dapper.Database;
import org.dapper.basic.collections.PageList;
import org.dapper.core.repository.IRepository;
import org.dapper.query.Sql;
import org.junit.Test;

import java.util.List;

public class SelectExTest extends BaseTest {


    @Test
    public void  getDtoListTest(){
        Database db = getDatabase();

        // getDtoList(Class<TDto> returnType,Sql sql)
        Sql sql1 = Sql.builder().select(" user_id as id, user_Name as name ").from(UserInfo.class).where("user_id = @0", 1);
        List<UserInfoDto> result = db.getSqlQuery().getDtoList(UserInfoDto.class, sql1);
        assert (result.size() > 0 && result.get(0).getId() > 0) : "UserInfoRepository getDtoList(Class<TDto> returnType,Sql sql) is error";
        System.out.println(">>>>>>>>>> Result <<<<<<<<<<");
        for (UserInfoDto item : result) {
            System.out.println("id:" + item.getId());
            System.out.println("user_Name:" + item.getName());
            System.out.println("login:" + item.getLogin());
        }

        // getDtoList(Class<TDto> returnType,String sql,Object ... param)
        String sql2 = "select user_id as id, user_Name as name from user_info where user_id = @0 ";
        List<UserInfoDto> result2 = db.getSqlQuery().getDtoList(UserInfoDto.class, sql2, 1);
        assert (result2.size() > 0 && result2.get(0).getId() > 0) : "UserInfoRepository getDtoList(Class<TDto> returnType,String sql,Object ... param) is error";
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
        // getDto(Class<TDto> returnType, Sql sql)
        UserInfoDto result1 = db.getSqlQuery().getDto(
                UserInfoDto.class,
                Sql.builder().select("user_id as id, user_Name as name").from(UserInfo.class).where("user_id  = @0", 1));
        assert (result1 != null && result1.getId() > 0) : "UserInfoRepository getDto(Class<TDto> returnType, Sql sql) is error";
        System.out.println(">>>>>>>>>> Result2 <<<<<<<<<<");
        System.out.println("id:" + result1.getId());
        System.out.println("user_Name:" + result1.getName());
        System.out.println("login:" + result1.getLogin());
    }


    @Test
    public void  getCountTest(){

        Database db = getDatabase();
        // int count(Sql sql)
        int result = db.getSqlQuery().count(Sql.builder().select("count(1)").from(UserInfo.class).where("user_id in (@0,@1,@2)", 1, 2, 3));
        assert (result > 0) : "result value is error";
        System.out.println(">>>>>>>>>> Result <<<<<<<<<<");
        System.out.println("result size:" + result);
    }


    @Test
    public void  pageListTest(){

        Database db = getDatabase();
        //  getPageList(int pageIndex, int pageSize, Sql sql)
        PageList<UserInfoDto> result = db.getSqlQuery().getPageDtoList(
                UserInfoDto.class,
                1,
                10,
                Sql.builder().select().from(UserInfo.class));
        assert (result.getTotalCount() > 0) : "result value is error";
        System.out.println(">>>>>>>>>> Result <<<<<<<<<<");
        System.out.println("pageIndex:" + result.getPageIndex());
        System.out.println("pageSize:" + result.getPageSize());
        System.out.println("totalCount:" + result.getTotalCount());
        System.out.println("totalPage:" + result.getTotalPage());
        for (UserInfoDto item : result.getItems()) {
            System.out.println("id:" + item.getId());
            System.out.println("user_Name:" + item.getName());
            System.out.println("login:" + item.getLogin());
        }
    }
}
