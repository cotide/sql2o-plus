package com.sqltest.db;

import com.sqltest.base.BaseTest;
import com.sqltest.model.UserInfo;
import com.sqltest.model.enums.EnumGroup;
import com.sqltest.model.enums.EnumUserStatus;
import com.sqltest.model.enums.EnumVipLevel;
import io.github.cotide.dapper.Database;
import io.github.cotide.dapper.repository.inter.IRepository;
import org.junit.Test;

import java.util.Date;

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
           domain.setStatus(EnumUserStatus.NORMAL);
           domain.setLevel(EnumVipLevel.VIP2);
           domain.setGroup(EnumGroup.GROUP2);
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
