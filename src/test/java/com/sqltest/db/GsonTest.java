package com.sqltest.db;

import com.google.gson.*;
import com.sqltest.base.BaseTest;
import com.sqltest.model.UserInfo;
import com.sqltest.model.enums.*;
import io.github.cotide.dapper.Database;
import io.github.cotide.dapper.repository.inter.IRepository;
import org.junit.Test;
import sql2o.extensions.postgres.converters.JSONConverter;

import java.util.Date;

public class GsonTest extends BaseTest {



    @Test
    public void  getByIdJson(){
        Database db = getDatabase();
        IRepository<UserInfo> userInfoIRepository =  db.getRepository(UserInfo.class);
        UserInfo userInfo =   userInfoIRepository.getById(1);
        System.out.println(new Gson().toJson(userInfo));
    }


    @Test
    public void  toJson(){

        UserInfo domain = new UserInfo();
        domain.setName("Test");
        domain.setLogin(10086);
        domain.setPwd("123456");
        domain.setStatus(EnumUserStatus.NORMAL);
        domain.setLevel(EnumVipLevel.VIP3);
        domain.setGroup(EnumGroup.GROUP2);
        domain.setCreateTime(new Date());
        String json =  JSONConverter.createGson().toJson(domain);
        System.out.println(json);
        UserInfo jsonObj =  JSONConverter.createGson().fromJson(json,UserInfo.class);
    }

}
