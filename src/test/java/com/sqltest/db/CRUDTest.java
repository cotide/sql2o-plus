package com.sqltest.db;

import com.sqltest.base.BaseTest;
import com.sqltest.model.UserInfo;
import org.dapper.Database;
import org.dapper.core.repository.IRepository;
import org.dapper.query.Sql;
import org.junit.Test;

import java.util.Date;

public class CRUDTest extends BaseTest {

    @Test
    public  void  createTest(){
            Database db = getDatabase();
            IRepository<UserInfo> userInfoRepository = db.getRepository(UserInfo.class);
            UserInfo domain = new UserInfo();
            domain.setName("Test");
            domain.setLogin(10086);
            domain.setPwd("123456");
            domain.setCreatTime(new Date());
            UserInfo user = userInfoRepository.create(domain);
            assert (user != null) : "userinfo is null";
            System.out.println(">>>>>>>>>> create result <<<<<<<<<<");
            System.out.println(user.getId());
            System.out.println(user.getName());
    }


    @Test
    public  void  updateTest(){
           Database db = getDatabase();
           IRepository<UserInfo> userInfoRepository = db.getRepository(UserInfo.class);
           UserInfo user = userInfoRepository.get(Sql.builder().select().from(UserInfo.class).where("user_id = @0", 3399));
           assert (user != null) : "userinfo is null";
           System.out.println(">>>>>>>>>> get result <<<<<<<<<<");
           System.out.println(user.getId());
           System.out.println(user.getName());
           user.setName("Test_2 ## -- ");
           userInfoRepository.update(user);
           System.out.println(">>>>>>>>>> update result <<<<<<<<<<");
           System.out.println(user.getId());
           System.out.println(user.getName());
    }

    @Test
    public  void deleteTest(){
            Database db = getDatabase();
            IRepository<UserInfo> userInfoRepository = db.getRepository(UserInfo.class);
            UserInfo user = userInfoRepository.get(Sql.builder().select().from(UserInfo.class).where("user_id = @0", 3391));
            assert (user != null) : "userinfo is null";
            System.out.println(">>>>>>>>>> Result <<<<<<<<<<");
            System.out.println(user.getId());
            System.out.println(user.getName());
            userInfoRepository.delete(user);
    }

    @Test
    public void  delete2Test(){
            Database db = getDatabase();
            IRepository<UserInfo> userInfoRepository = db.getRepository(UserInfo.class);
            UserInfo user = new UserInfo();
            user.setId(3398);
            userInfoRepository.delete(user);
    }
}
