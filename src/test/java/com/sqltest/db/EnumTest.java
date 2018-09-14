package com.sqltest.db;

import com.sqltest.base.BaseTest;
import com.sqltest.dto.UserInfoDto;
import com.sqltest.model.UserInfo;
import com.sqltest.model.enums.EnumGroup;
import com.sqltest.model.enums.EnumUserStatus;
import com.sqltest.model.enums.EnumVipLevel;
import io.github.cotide.dapper.Database;
import io.github.cotide.dapper.query.Sql;
import io.github.cotide.dapper.repository.inter.IRepository;
import org.junit.Test;

import java.util.Date;
import java.util.List;

public class EnumTest extends BaseTest {


    @Test
    public  void  createTest(){
        Database db = getDatabase();
        IRepository<UserInfo> userInfoRepository = db.getRepository(UserInfo.class);
        UserInfo domain = new UserInfo();
        domain.setName("Test");
        domain.setLogin(10086);
        domain.setPwd("123456");
        // Set Enums
        domain.setLevel(EnumVipLevel.VIP3);
        domain.setStatus(EnumUserStatus.STOP);
        domain.setGroup(EnumGroup.GROUP2);
        domain.setCreateTime(new Date());
        UserInfo user = userInfoRepository.create(domain);
        assert (user != null&&user.getId()>0) : "user is null";
        assert (user.getLevel() == EnumVipLevel.VIP3) : "user vip is error";
        assert (user.getStatus() == EnumUserStatus.STOP) : "user status is error";
        System.out.println(">>>>>>>>>> create result <<<<<<<<<<");
        System.out.println("id:" + user.getId());
        System.out.println("level:"+user.getLevel());
        System.out.println("status:"+user.getStatus());
        System.out.println("user_Name:" + user.getName());
        System.out.println("login:" + user.getLogin());
    }

    @Test
    public void GetList(){

        Database db = getDatabase();

        IRepository<UserInfo> userInfoIRepository = db.getRepository(UserInfo.class);
        UserInfo user = userInfoIRepository.getById(1);
        assert (user != null&&user.getId()>0) : "user is null";
        assert (user.getLevel() == EnumVipLevel.VIP3) : "user vip is error";
        assert (user.getStatus() == EnumUserStatus.STOP) : "user status is error";

        System.out.println("--------- [get] -----------");
        System.out.println("id:" + user.getId());
        System.out.println("level:"+user.getLevel());
        System.out.println("status:"+user.getStatus());
        System.out.println("group:" + user.getGroup());
        System.out.println("user_Name:" + user.getName());
        System.out.println("login:" + user.getLogin());

        System.out.println("--------- [getList] -----------");
        List<UserInfo> userList  = db.getRepository(UserInfo.class).getList();
        assert (userList != null&&userList.size()>0) : "userList is null";
        for (UserInfo item : userList) {
            System.out.println("id:" + item.getId());
            System.out.println("level:"+item.getLevel());
            System.out.println("status:"+item.getStatus());
            System.out.println("group:" + item.getGroup());
            System.out.println("user_Name:" + item.getName());
            System.out.println("login:" + item.getLogin());
        }

    }


    @Test
    public void GetDtoList(){

        Database db = getDatabase();
        UserInfoDto user = db.getSqlQuery().getDto(
                            UserInfoDto.class,
                            Sql.builder()
                                .select("user_id as id,user_name as  name,login,level,status,create_time as createTime")
                                .from(UserInfo.class)
                                .where("user_id=@0",3));

        assert (user != null&&user.getId()>0) : "user is null";
        //assert (user.getLevel() == EnumVipLevel.VIP3) : "user vip is error";
        //assert (user.getStatus() == EnumUserStatus.STOP) : "user status is error";

        System.out.println("--------- [get] -----------");
        System.out.println("id:" + user.getId());
        System.out.println("level:"+user.getLevel());
        System.out.println("status:"+user.getStatus());
        System.out.println("user_Name:" + user.getName());
        System.out.println("login:" + user.getLogin());

        System.out.println("--------- [getList] -----------");
        List<UserInfoDto> userList  = db.getSqlQuery().getDtoList(
                                        UserInfoDto.class,
                                        Sql.builder()
                                            .select("user_id as id,user_name as  name,login,level,status,create_time as createTime")
                                            .from(UserInfo.class));

        assert (userList != null&&userList.size()>0) : "userList is null";
        for (UserInfoDto item : userList) {
            System.out.println("id:" + item.getId());
            System.out.println("level:"+item.getLevel());
            System.out.println("status:"+item.getStatus());
            System.out.println("user_Name:" + item.getName());
            System.out.println("login:" + item.getLogin());
        }

    }
}
