package com.sqltest.db;

import com.sqltest.base.BaseTest;
import com.sqltest.dto.UserInfoDto;
import com.sqltest.dto.UserInfoSimpleDto;
import com.sqltest.model.UserInfo;
import io.github.cotide.dapper.Database;
import io.github.cotide.dapper.query.Sql;
import org.junit.Test;

import java.util.List;

public class DebugTest extends BaseTest {

    @Test
    public void  getDtoListTest(){

        Database db = getDatabase();
        db.isDebug(false);
        UserInfoSimpleDto user = db.getSqlQuery().getDto(
                UserInfoSimpleDto.class,
                Sql.builder()
                        .select("user_id as id," +
                                "user_name as  name," +
                                "login,level," +
                                "status," +
                                "create_time as createTime")
                        .from(UserInfo.class)
                        .where("user_id=?",1));

        assert (user != null&&user.getId()>0) : "user is null";

        System.out.println("--------- [get] -----------");
        System.out.println("id:" + user.getId());
        System.out.println("level:"+user.getLevel());
        System.out.println("status:"+user.getStatus());
        //System.out.println("user_Name:" + user.getName());
        System.out.println("login:" + user.getLogin());

    }
}
