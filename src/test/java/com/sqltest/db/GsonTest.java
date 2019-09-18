package com.sqltest.db;

import com.google.gson.Gson;
import com.sqltest.base.BaseTest;
import com.sqltest.model.UserInfo;
import io.github.cotide.dapper.Database;
import io.github.cotide.dapper.repository.inter.IRepository;
import org.junit.Test;

public class GsonTest extends BaseTest {



    @Test
    public void  getByIdJson(){
        Database db = getDatabase();
        IRepository<UserInfo> userInfoIRepository =  db.getRepository(UserInfo.class);
        UserInfo userInfo =   userInfoIRepository.getById(1);
        System.out.println(new Gson().toJson(userInfo));
    }
}
