package com.sqltest.db;

import com.sqltest.base.BaseTest;
import com.sqltest.dto.UserInfoDto;
import com.sqltest.model.UserInfo;
import org.dapper.Database;
import org.dapper.query.Sql;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExecuteTest extends BaseTest {

    @Test
    public void  Execute(){

       try(Database db = getDatabase()){
           db.beginTransaction();
           final  String insertSql  =
                   "INSERT INTO user_info (user_Name,password,login,create_time) VALUES (@0,@1,@2,@3)";

           int id =  db.getSqlRun().execute(
                   insertSql,
                   "Execute Test",
                   "123456",
                   10086,
                   new Date()).asInt();
           System.out.println("Object is :"+id);
           assert (id>0):"insert is error";
           final String updateSql  =
                   "UPDATE user_info set user_Name = @0 WHERE user_id = @1";
           db.getSqlRun().execute(updateSql,"Execute Test2",id);
           db.commit();
           Sql sql = Sql.builder()
                   .select("user_id as id, user_Name as name")
                   .from(UserInfo.class).where("user_id  = @0", id);
           UserInfoDto resultDto = db.getSqlQuery().getDto(UserInfoDto.class,sql);
           assert (resultDto!=null&&resultDto.getName().equals("Execute Test2")):"update get is error";
           System.out.println(">>>>>>>>>> Result <<<<<<<<<<");
           System.out.println(resultDto.getId());
           System.out.println(resultDto.getName());

       }

    }
}
