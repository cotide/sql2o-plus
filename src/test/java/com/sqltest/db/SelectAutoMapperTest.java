package com.sqltest.db;

import com.sqltest.base.BaseTest;
import com.sqltest.dto.UserInfoAutoDto;
import com.sqltest.dto.UserInfoDto;
import com.sqltest.model.UserInfo;
import com.sqltest.model.UserType;
import io.github.cotide.dapper.Database;
import io.github.cotide.dapper.query.Sql;
import org.junit.Test;

public class SelectAutoMapperTest extends BaseTest {

    @Test
    public void getOne(){
        Database db = getDatabase();
        String sql = "select  *  from user_info  where user_id = ?";
        UserInfoAutoDto dto = db.getSqlQuery().getDto(UserInfoAutoDto.class,new Sql(sql,1));
        System.out.println(dto.getUserName());
    }


    @Test
    public void getOne2(){
        Database db = getDatabase();
        Sql sql = new Sql( "select  *  from user_info  where user_id = ? and user_id = ? ",1,1);
        UserInfoAutoDto dto = db.getSqlQuery().getDto(UserInfoAutoDto.class,sql);
        System.out.println(dto.getUserName());
    }


    @Test
    public void get1(){
        Database db =  getDatabase();

        Sql sql = Sql.builder()
                .select()
                .from(UserInfo.class)
                .where(UserInfo::getId,1)
                .where("user_id").eq(1);
        UserInfo userInfo = db.getRepository(UserInfo.class).get(sql);

    }



    @Test
    public void join(){
        Database db =  getDatabase();

        Sql sql = Sql.builder()
                .select("*")
                .from(UserInfo.class,"a")
                .join(UserType.class,"b")
                .on("a",UserInfo::getUserTypeId,"b",UserType::getId)
                .where("a",UserInfo::getId,1);
        System.out.println("SQL语句:");
        System.out.println(sql.getFinalSql());
        System.out.println("SQL参数值:");
        UserInfoAutoDto dto =  db.getSqlQuery().getDto(UserInfoAutoDto.class,sql);
        System.out.println(dto.getUserName());
    }
}
