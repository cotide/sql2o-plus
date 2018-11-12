package com.sqltest.db;

import com.sqltest.base.BaseTest;
import com.sqltest.dto.UserInfoAutoDto;
import com.sqltest.dto.UserInfoDto;
import com.sqltest.model.UserInfo;
import com.sqltest.model.UserType;
import io.github.cotide.dapper.Database;
import io.github.cotide.dapper.query.Sql;
import org.junit.Test;
import sql2o.Connection;
import sql2o.Sql2o;

public class SelectAutoMapperTest extends BaseTest {

    @Test
    public void getOne(){
        Database db =  getDatabase();

        final String sql =
                "select  *  from user_info  where user_id = 8";

        UserInfoAutoDto dto =  db.getSqlQuery().getDto(UserInfoAutoDto.class,sql);
        System.out.println(dto.getUserName());

    }


    @Test
    public void join(){
        Database db =  getDatabase();

        Sql sql = Sql.builder()
                .select("*")
                .from(UserInfo.class,"a")
                .join(UserType.class,"b")
                .on("a.user_type_id = b.id");

        UserInfoAutoDto dto =  db.getSqlQuery().getDto(UserInfoAutoDto.class,sql);
        System.out.println(dto.getUserName());
    }
}
