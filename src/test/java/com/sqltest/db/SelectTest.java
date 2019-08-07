package com.sqltest.db;


import com.sqltest.base.BaseTest;
import com.sqltest.model.UserInfo;
import com.sqltest.model.enums.EnumUserStatus;
import io.github.cotide.dapper.Database;
import io.github.cotide.dapper.basic.collections.PageList;
import io.github.cotide.dapper.query.Ors;
import io.github.cotide.dapper.repository.inter.IRepository;
import io.github.cotide.dapper.query.Sql;
import org.junit.Test;
import  com.sqltest.dto.*;

import java.util.List;

public class SelectTest extends BaseTest {


    @Test
    public  void or()
    {
        Database db = getDatabase();
        IRepository<UserInfo> userInfoIRepository =  db.getRepository(UserInfo.class);
        Sql sql = Sql.builder().select().from(UserInfo.class)
                .whereLike(UserInfo::getName,"Test_2")
                .or(Ors.sql().where(UserInfo::getStatus, EnumUserStatus.NORMAL))
                .whereLike(UserInfo::getName,"Test")
                .order(UserInfo::getId);

        List<UserInfo> result =   userInfoIRepository.getList(sql);
        for (UserInfo item : result) {
            System.out.println("id:" + item.getId());
            System.out.println("user_Name:" + item.getName());
            System.out.println("login:" + item.getLogin());
        }
    }


    @Test
    public  void getWhereIn()
    {
        Database db = getDatabase();
        IRepository<UserInfo> userInfoIRepository =  db.getRepository(UserInfo.class);
        Sql sql = Sql.builder().select().from(UserInfo.class)
                .whereIn("user_id",1,2);

        List<UserInfo> result =   userInfoIRepository.getList(sql);
        for (UserInfo item : result) {
            System.out.println("id:" + item.getId());
            System.out.println("user_Name:" + item.getName());
            System.out.println("login:" + item.getLogin());
        }
    }



    @Test
    public  void getById()
    {
        Database db = getDatabase();
        IRepository<UserInfo> userInfoIRepository =  db.getRepository(UserInfo.class);
        UserInfo UserInfo =   userInfoIRepository.getById(29);
    }


    @Test
    public void getListTest(){

            Database db = getDatabase();
            IRepository<UserInfo> userInfoRepository = db.getRepository(UserInfo.class);

            // getList()
            List<UserInfo> result = userInfoRepository.getList();
            assert (result.size() > 0 && result.get(0).getId() > 0) : "UserInfoRepository getList() is error";

            // getList(Sql sql)
            List<UserInfo> result2 = userInfoRepository.getList(Sql.builder().select().from(UserInfo.class));
            assert (result2.size() > 0 && result2.get(0).getId() > 0) : "UserInfoRepository getList(Sql inter) is error";

            // getList(String sql,Object ... param)
            String sql = "select * from user_info where user_id = ? ";
            List<UserInfo> result3 = userInfoRepository.getList(sql, 1);
            assert (result3.size() == 1 && result3.get(0).getId() > 0) : "UserInfoRepository getList(String inter,Object ... param) is error";
    }


    @Test
    public void  getDtoListTest(){
            Database db = getDatabase();
            IRepository<UserInfo> userInfoRepository = db.getRepository(UserInfo.class);
            // getDtoList(Class<TDto> returnType,Sql inter)
            Sql sql1 = Sql.builder().select(" user_id as id, user_Name as name ").from(UserInfo.class).where("user_id = ?", 1);
            List<UserInfoDto> result = userInfoRepository.getDtoList(UserInfoDto.class, sql1);
            assert (result.size() > 0 && result.get(0).getId() > 0) : "UserInfoRepository getDtoList(Class<TDto> returnType,Sql inter) is error";
            System.out.println(">>>>>>>>>> Result <<<<<<<<<<");
            for (UserInfoDto item : result) {
                System.out.println("id:" + item.getId());
                System.out.println("user_Name:" + item.getName());
                System.out.println("login:" + item.getLogin());
            }

            // getDtoList(Class<TDto> returnType,String inter,Object ... param)
            String sql2 = "select user_id as id, user_Name as name from user_info where user_id = ? ";
            List<UserInfoDto> result2 = userInfoRepository.getDtoList(UserInfoDto.class, sql2, 1);
            assert (result2.size() > 0 && result2.get(0).getId() > 0) : "UserInfoRepository getDtoList(Class<TDto> returnType,String inter,Object ... param) is error";
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
            UserInfo result = userInfoRepository.getById(29);
            assert (result != null && result.getId() > 0) : "UserInfoRepository getById(Object primaryKey) is error";
            System.out.println(">>>>>>>>>> Result <<<<<<<<<<");
            System.out.println("id:" + result.getId());
            System.out.println("user_Name:" + result.getName());
            System.out.println("login:" + result.getLogin());


            // get(Sql inter)
            UserInfo result2 = userInfoRepository.get(
                    Sql.builder().select().from(UserInfo.class).where("user_id  = ?", 1));
            assert (result2 != null && result2.getId() > 0) : "UserInfoRepository get(Sql inter) is error";
            System.out.println(">>>>>>>>>> Result2 <<<<<<<<<<");
            System.out.println("id:" + result2.getId());
            System.out.println("user_Name:" + result2.getName());
            System.out.println("login:" + result2.getLogin());

            // get(String inter, Object ...  param)
            String sql = "select * from user_info where user_id = ? ";
            UserInfo result3 = userInfoRepository.get(sql, 1);
            assert (result3 != null && result3.getId() > 0) : "UserInfoRepository get(String inter, Object ...  param) is error";
            System.out.println(">>>>>>>>>> Result3 <<<<<<<<<<");
            System.out.println("id:" + result3.getId());
            System.out.println("user_Name:" + result3.getName());
            System.out.println("login:" + result3.getLogin());
    }


    @Test
    public void  getDtoTest(){

            Database db = getDatabase();
            IRepository<UserInfo> userInfoRepository = db.getRepository(UserInfo.class);

            // getDto(Class<TDto> returnType, Sql inter)
            UserInfoDto result1 = userInfoRepository.getDto(
                    UserInfoDto.class,
                    Sql.builder().select("user_id as id, user_Name as name").from(UserInfo.class).where("user_id  = ?", 1));
            assert (result1 != null && result1.getId() > 0) : "UserInfoRepository getDto(Class<TDto> returnType, Sql inter) is error";
            System.out.println(">>>>>>>>>> Result2 <<<<<<<<<<");
            System.out.println("id:" + result1.getId());
            System.out.println("user_Name:" + result1.getName());
            System.out.println("login:" + result1.getLogin());
    }


    @Test
    public void  getCountTest(){

            Database db = getDatabase();
            IRepository<UserInfo> userInfoRepository = db.getRepository(UserInfo.class);
            // int count(Sql inter)
            int result = userInfoRepository.count(Sql.builder().select("count(1)").from(UserInfo.class)
                    .whereIn("user_id", 1, 2, 3,5));
            assert (result > 0) : "result value is error";
            System.out.println(">>>>>>>>>> Result <<<<<<<<<<");
            System.out.println("result size:" + result);
    }


    @Test
    public void  pageListTest(){

        Database db = getDatabase();
        IRepository<UserInfo> userInfoRepository = db.getRepository(UserInfo.class);
        //  getPageList(int pageIndex, int pageSize, Sql inter)
        PageList<UserInfo> result = userInfoRepository.getPageList(1, 10, Sql.builder().select().from(UserInfo.class));
        //assert (result.getTotalCount() > 0) : "result value is error";
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


    @Test
    public void whereLikeTest(){

        Database db = getDatabase();
        IRepository<UserInfo> userInfoRepository = db.getRepository(UserInfo.class);

        Sql sql =  Sql.builder()
                .select()
                .from(UserInfo.class)
                .whereLike(UserInfo::getName,"T");
        List<UserInfo> result =  userInfoRepository
                .getList(sql);
        System.out.println(">>>>>>>>>> Result <<<<<<<<<<");
        System.out.println("size:" + result.size());
        for (UserInfo item : result) {
            System.out.println("id:" + item.getId());
            System.out.println("user_Name:" + item.getName());
            System.out.println("login:" + item.getLogin());
        }
    }




}
