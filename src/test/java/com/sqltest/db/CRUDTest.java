package com.sqltest.db;

import com.sqltest.base.BaseTest;
import com.sqltest.model.UserInfo;
import com.sqltest.model.enums.EnumUserStatus;
import com.sqltest.model.enums.EnumVipLevel;
import io.github.cotide.dapper.Database;
import io.github.cotide.dapper.basic.enums.EnumMapping;
import io.github.cotide.dapper.repository.inter.IRepository;
import io.github.cotide.dapper.query.Sql;
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
            domain.setCreateTime(new Date());
            UserInfo user = userInfoRepository.create(domain);
            assert (user != null&&user.getId()>0) : "userinfo is null";
            System.out.println(">>>>>>>>>> create result <<<<<<<<<<");
            System.out.println(user.getId());
            System.out.println(user.getName());
    }


    @Test
    public  void  updateTest(){
           Database db = getDatabase();
           IRepository<UserInfo> userInfoRepository = db.getRepository(UserInfo.class);
           UserInfo user = userInfoRepository.get(
                   Sql.builder()
                           .select()
                           .from(UserInfo.class)
                           .where("user_id = @0", 1));
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
            UserInfo user = userInfoRepository.get(
                    Sql.builder()
                            .select()
                            .from(UserInfo.class)
                            .where("user_id = @0", 1));
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
            Boolean result = userInfoRepository.delete(user);
            assert (result):"repository delete is error";
    }
}
