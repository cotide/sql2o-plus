package com.sqltest.db;

import com.sqltest.base.BaseTest;
import com.sqltest.model.UserInfo;
import io.github.cotide.dapper.Database;
import io.github.cotide.dapper.query.Sql;
import io.github.cotide.dapper.repository.inter.IRepository;
import org.junit.Test;

public class SelectOneTest extends BaseTest  {


    @Test
    public void get(){

        Database db = getDatabase();
        IRepository<UserInfo> userInfoIRepository =  db.getRepository(UserInfo.class);
        String sql = "select * from user_info where user_id = ? ";
        UserInfo userInfo = userInfoIRepository.get(sql,1);

    }

    @Test
    public void  getDtoTest(){

        Database db = getDatabase();
        String result = db.getSqlQuery().getDto(
                String.class,
                Sql.builder().select(UserInfo::getValue)
                        .from(UserInfo.class)
                        .where(UserInfo::getId,1));
        System.out.println(">>>>>>>>>> Result1 <<<<<<<<<<");
        System.out.println(result);


        String result1 = db.getSqlQuery().getDto(
                String.class,
                Sql.builder().select(UserInfo::getName)
                        .from(UserInfo.class)
                        .where(UserInfo::getId,2));
        System.out.println(">>>>>>>>>> Result2 <<<<<<<<<<");
        System.out.println(result1);

        Integer result2 = db.getSqlQuery().getDto(
                Integer.class,
                Sql.builder().select(UserInfo::getLogin)
                        .from(UserInfo.class)
                        .where(UserInfo::getId,3));
        System.out.println(">>>>>>>>>> Result3 <<<<<<<<<<");
        System.out.println(result2);

        UserInfo result3 = db.getSqlQuery().getDto(
                UserInfo.class,
                Sql.builder().select()
                        .from(UserInfo.class)
                        .where(UserInfo::getId,4));
        System.out.println(">>>>>>>>>> Result4 <<<<<<<<<<");
        System.out.println(result3.getId());
        System.out.println(result3.getName());
        System.out.println(result3.getLogin());
        System.out.println(result3.getValue());
    }

}
