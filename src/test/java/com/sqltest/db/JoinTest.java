package com.sqltest.db;

import com.sqltest.base.BaseTest;
import com.sqltest.dto.UserInfoDetailDto;
import com.sqltest.model.UserInfo;
import com.sqltest.model.UserType;
import com.sqltest.model.enums.EnumGroup;
import com.sqltest.model.enums.EnumUserStatus;
import com.sqltest.model.enums.EnumVipLevel;
import io.github.cotide.dapper.Database;
import io.github.cotide.dapper.basic.collections.PageList;
import io.github.cotide.dapper.query.Sql;
import io.github.cotide.dapper.repository.inter.IRepository;
import org.junit.Test;

import java.util.Date;
import java.util.List;

public class JoinTest  extends BaseTest {


    @Test
    public void insertByJoinData(){

        try(Database db = getDatabase()){
            // BeginTransaction
            db.beginTransaction();
            IRepository<UserInfo> userInfoRepository = db.getRepository(UserInfo.class);
            IRepository<UserType> userTypeIRepository = db.getRepository(UserType.class);
            // Create UserType
            UserType userType =  new UserType();
            userType.setName("A组");
            userType.setCreateTime(new Date());
            userTypeIRepository.create(userType);
            assert (userType != null&&userType.getId()>0) : "user is null";
            // Create UserInfo
            UserInfo domain = new UserInfo();
            domain.setName("Test");
            domain.setUserTypeId(userType.getId());
            domain.setLogin(10086);
            domain.setPwd("123456");
            // Set Enums
            domain.setLevel(EnumVipLevel.VIP3);
            domain.setStatus(EnumUserStatus.STOP);
            domain.setGroup(EnumGroup.GROUP2);
            domain.setCreateTime(new Date());
            UserInfo user = userInfoRepository.create(domain);
            db.commit();
            assert (user != null&&user.getId()>0) : "user is null";
            assert (user.getLevel() == EnumVipLevel.VIP3) : "user vip is error";
            assert (user.getStatus() == EnumUserStatus.STOP) : "user status is error";


            System.out.println(">>>>>>>>>> userType result <<<<<<<<<<");
            System.out.println("id:" + userType.getId());
            System.out.println("name:"+userType.getName());
            System.out.println("create_time:"+userType.getCreateTime());


            System.out.println(">>>>>>>>>> userInfo result <<<<<<<<<<");
            System.out.println("id:" + user.getId());
            System.out.println("level:"+user.getLevel());
            System.out.println("status:"+user.getStatus());
            System.out.println("user_Name:" + user.getName());
            System.out.println("login:" + user.getLogin());
        }
    }

    @Test
    public void  getListJoinTest(){

        Database db = getDatabase();
        Sql sql = Sql.builder()
                .select("a.user_id as id," +
                        "a.user_Name as name,"+
                        "b.id as typeId,"+
                        "b.name as typeName,"+
                        "a.login,"+
                        "a.status,"+
                        "a.group,"+
                        "a.level,"+
                        "a.create_time as createTime")
                .from(UserInfo.class,"a")
                .join(UserType.class,"b")
                .on("a.user_type_id = b.id");
        List<UserInfoDetailDto> result =  db.getSqlQuery()
                .getDtoList(UserInfoDetailDto.class,sql);

        assert (result.size() >= 0 && result.get(0).getId() > 0)
                : "UserInfoRepository getDtoList(Class<TDto> returnType,Sql inter) is error";

        for (UserInfoDetailDto item : result) {
            System.out.println(">>>>>>>>>> Result <<<<<<<<<<");
            System.out.println("user_info.id:" + item.getId());
            System.out.println("user_info.user_Name:" + item.getName());
            System.out.println("user_type.id:" + item.getTypeId());
            System.out.println("user_type.typeName:" + item.getTypeName());
            System.out.println("user_info.login:" + item.getLogin());
            System.out.println("user_info.status:" + item.getStatus());
            System.out.println("user_info.group:" + item.getGroup());
            System.out.println("user_info.level:" + item.getLevel());
            System.out.println("user_info.create_time:" + item.getCreateTime());
        }
    }

    @Test
    public void  getListJoin2Test(){

        Database db = getDatabase();
        Sql sql = Sql.builder()
                .select("a.user_id as id," +
                        "a.user_Name as name,"+
                        "b.id as typeId,"+
                        "b.name as typeName,"+
                        "a.login,"+
                        "a.status,"+
                        "a.group,"+
                        "a.level,"+
                        "a.create_time as createTime")
                .fromDb("g_main_test",UserInfo.class,"a")
                .leftJoinDb("g_main_test",UserType.class).as("b")
                .on("a.user_type_id = b.id")
                .innerJoinDb("g_main_test",UserType.class).as("c")
                .on("a.user_type_id = c.id")
                .joinDb("g_main_test",UserType.class).as("d")
                .on("a.user_type_id = d.id")
                .rightJoinDb("g_main_test",UserType.class,"e")
                .on("a.user_type_id = e.id")
                .rightJoin("g_main_test.user_type f")
                .on("a.user_type_id = f.id")
                .where("a",UserInfo::getId,1)
                .where("1","1")
                .where("2","2");


        System.out.println(sql.getFinalSql());
        List<UserInfoDetailDto> result =  db.getSqlQuery()
                .getDtoList(UserInfoDetailDto.class,sql);

//        assert (result.size() >= 0 && result.get(0).getId() > 0)
//                : "UserInfoRepository getDtoList(Class<TDto> returnType,Sql inter) is error";

        System.out.println(result.size());
        for (UserInfoDetailDto item : result) {
            System.out.println(">>>>>>>>>> Result <<<<<<<<<<");
            System.out.println("user_info.id:" + item.getId());
            System.out.println("user_info.user_Name:" + item.getName());
            System.out.println("user_type.id:" + item.getTypeId());
            System.out.println("user_type.typeName:" + item.getTypeName());
            System.out.println("user_info.login:" + item.getLogin());
            System.out.println("user_info.status:" + item.getStatus());
            System.out.println("user_info.group:" + item.getGroup());
            System.out.println("user_info.level:" + item.getLevel());
            System.out.println("user_info.create_time:" + item.getCreateTime());
        }
    }


}
