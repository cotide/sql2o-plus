package com.sqltest.db;


import com.sqltest.base.BaseTest;
import com.sqltest.model.UserInfo;
import org.dapper.Database;
import org.dapper.basic.collections.PageList;
import org.dapper.core.repository.IRepository;
import org.dapper.query.Sql;
import org.junit.Test;
import  com.sqltest.dto.*;

import java.util.List;

public class SelectTest extends BaseTest {


    @Test
    public void getListTest(){

            Database db = getDatabase();
            IRepository<UserInfo> userInfoRepository = db.getRepository(UserInfo.class);

            // getList()
            List<UserInfo> result = userInfoRepository.getList();
            assert (result.size() > 0 && result.get(0).getId() > 0) : "UserInfoRepository getList() is error";

            // getList(Sql sql)
            List<UserInfo> result2 = userInfoRepository.getList(Sql.builder().select().from(UserInfo.class));
            assert (result2.size() > 0 && result2.get(0).getId() > 0) : "UserInfoRepository getList(Sql sql) is error";

            // getList(String sql,Object ... param)
            String sql = "select * from user_info where user_id = @0 ";
            List<UserInfo> result3 = userInfoRepository.getList(sql, 1);
            assert (result3.size() == 1 && result3.get(0).getId() > 0) : "UserInfoRepository getList(String sql,Object ... param) is error";
    }


    @Test
    public void  getDtoListTest(){
            Database db = getDatabase();
            IRepository<UserInfo> userInfoRepository = db.getRepository(UserInfo.class);

            // getDtoList(Class<TDto> returnType,Sql sql)
            Sql sql1 = Sql.builder().select(" user_id as id, user_Name as name ").from(UserInfo.class).where("user_id = @0", 1);
            List<UserInfoDto> result = userInfoRepository.getDtoList(UserInfoDto.class, sql1);
            assert (result.size() > 0 && result.get(0).getId() > 0) : "UserInfoRepository getDtoList(Class<TDto> returnType,Sql sql) is error";
            System.out.println(">>>>>>>>>> Result <<<<<<<<<<");
            for (UserInfoDto item : result) {
                System.out.println("id:" + item.getId());
                System.out.println("user_Name:" + item.getName());
                System.out.println("login:" + item.getLogin());
            }

            // getDtoList(Class<TDto> returnType,String sql,Object ... param)
            String sql2 = "select user_id as id, user_Name as name from user_info where user_id = @0 ";
            List<UserInfoDto> result2 = userInfoRepository.getDtoList(UserInfoDto.class, sql2, 1);
            assert (result2.size() > 0 && result2.get(0).getId() > 0) : "UserInfoRepository getDtoList(Class<TDto> returnType,String sql,Object ... param) is error";
            System.out.println(">>>>>>>>>> Result2 <<<<<<<<<<");
            for (UserInfoDto item : result2) {
                System.out.println("id:" + item.getId());
                System.out.println("user_Name:" + item.getName());
                System.out.println("login:" + item.getLogin());
            }
    }


    @Test
    public void  getTest(){

            Database db = getDatabase();
            IRepository<UserInfo> userInfoRepository = db.getRepository(UserInfo.class);

            // getById(Object primaryKey)
            UserInfo result = userInfoRepository.getById(1);
            assert (result != null && result.getId() > 0) : "UserInfoRepository getById(Object primaryKey) is error";
            System.out.println(">>>>>>>>>> Result <<<<<<<<<<");
            System.out.println("id:" + result.getId());
            System.out.println("user_Name:" + result.getName());
            System.out.println("login:" + result.getLogin());

            // get(Sql sql)
            UserInfo result2 = userInfoRepository.get(
                    Sql.builder().select().from(UserInfo.class).where("user_id  = @0", 1));
            assert (result2 != null && result2.getId() > 0) : "UserInfoRepository get(Sql sql) is error";
            System.out.println(">>>>>>>>>> Result2 <<<<<<<<<<");
            System.out.println("id:" + result2.getId());
            System.out.println("user_Name:" + result2.getName());
            System.out.println("login:" + result2.getLogin());

            // get(String sql, Object ...  param)
            String sql = "select * from user_info where user_id = @0 ";
            UserInfo result3 = userInfoRepository.get(sql, 1);
            assert (result3 != null && result3.getId() > 0) : "UserInfoRepository get(String sql, Object ...  param) is error";
            System.out.println(">>>>>>>>>> Result3 <<<<<<<<<<");
            System.out.println("id:" + result3.getId());
            System.out.println("user_Name:" + result3.getName());
            System.out.println("login:" + result3.getLogin());
    }


    @Test
    public void  getDtoTest(){

            Database db = getDatabase();
            IRepository<UserInfo> userInfoRepository = db.getRepository(UserInfo.class);

            // getDto(Class<TDto> returnType, Sql sql)
            UserInfoDto result1 = userInfoRepository.getDto(
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
            IRepository<UserInfo> userInfoRepository = db.getRepository(UserInfo.class);
            // int count(Sql sql)
            int result = userInfoRepository.count(Sql.builder().select("count(1)").from(UserInfo.class).where("user_id in (@0,@1,@2)", 1, 2, 3));
            assert (result > 0) : "result value is error";
            System.out.println(">>>>>>>>>> Result <<<<<<<<<<");
            System.out.println("result size:" + result);
    }


    @Test
    public void  pageListTest(){

             Database db = getDatabase();
            IRepository<UserInfo> userInfoRepository = db.getRepository(UserInfo.class);
            //  getPageList(int pageIndex, int pageSize, Sql sql)
            PageList<UserInfo> result = userInfoRepository.getPageList(1, 10, Sql.builder().select().from(UserInfo.class));
            assert (result.getTotalCount() > 0) : "result value is error";
            System.out.println(">>>>>>>>>> Result <<<<<<<<<<");
            System.out.println("pageIndex:" + result.getPageIndex());
            System.out.println("pageSize:" + result.getPageSize());
            System.out.println("totalCount:" + result.getTotalCount());
            System.out.println("totalPage:" + result.getTotalPage());
            for (UserInfo item : result.getItems()) {
                System.out.println("id:" + item.getId());
                System.out.println("user_Name:" + item.getName());
                System.out.println("login:" + item.getLogin());
            }
    }  

}
