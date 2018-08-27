package com.sqltest.db;

import com.sqltest.base.BaseTest;
import com.sqltest.dto.UserInfoDto;
import com.sqltest.model.UserInfo;
import org.dapper.Database;
import org.dapper.basic.collections.PageList;
import org.dapper.core.attr.Ignore;
import org.dapper.core.repository.IRepository;
import org.dapper.query.Sql;
import org.junit.Test;
import org.omg.CORBA.PUBLIC_MEMBER;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;

public class TranTest extends BaseTest {

    /**
     * 事务测试
     */

    @Test
    public void  insertAndUpdateTran(){

       try(Database db = getDruidDatabase()){
           IRepository<UserInfo> userInfoIRepository =
                   db.getRepository(UserInfo.class);

           db.beginTransaction();
           UserInfo domain = new UserInfo();
           domain.setName("Tran Test");
           domain.setLogin(10086);
           domain.setPwd("123456");
           domain.setCreateTime(new Date());

           UserInfo user = userInfoIRepository.create(domain);
           assert (user != null && user.getId() > 0)
                   : "insert is error";
           user.setName("Tran Test6");
           userInfoIRepository.update(user);
           // by zero error
           //Integer errorValue  = 1/0;
           db.commit();
           assert(user.getId()>0):"database transaction is error";
       }




    }


}
