package com.sqltest.db;

import com.sqltest.base.BaseTest;
import com.sqltest.dto.UserInfoDetailDto;
import com.sqltest.dto.UserInfoDto;
import com.sqltest.model.UserInfo;
import com.sqltest.model.UserType;
import io.github.cotide.dapper.Database;
import io.github.cotide.dapper.basic.collections.PageList;
import io.github.cotide.dapper.query.Sql;
import io.github.cotide.dapper.query.enums.OrderBy;
import io.github.cotide.dapper.repository.inter.IRepository;
import org.junit.Test;

public class PageListTest  extends BaseTest {


    @Test
    public void  getPageListTest(){
        long startTime = System.currentTimeMillis();
        Database db = getDatabase();
        long endTime=System.currentTimeMillis();
        System.out.println("Database 初始化时间： "+(endTime-startTime)+"ms");
        long startTime1 = System.currentTimeMillis();
        IRepository<UserInfo> userInfoRepository = db.getRepository(UserInfo.class);
        //  getPageList(int pageIndex, int pageSize, Sql inter)
        PageList<UserInfo> result = userInfoRepository.getPageList(
                1,
                10,
                Sql.builder()
                        .select()
                        .from(UserInfo.class)
                        .order(UserInfo::getId, OrderBy.DESC));

        long endTime1=System.currentTimeMillis();
        System.out.println("Database 查询总时间： "+(endTime1-startTime)+"ms");
        System.out.println("Database 查询时间： "+(endTime1-startTime1)+"ms");

        //assert (result.getTotalCount() > 0) : "result value is error";
        System.out.println(">>>>>>>>>> Result <<<<<<<<<<");
        System.out.println("pageIndex:" + result.getPageIndex());
        System.out.println("pageSize:" + result.getPageSize());
        System.out.println("totalCount:" + result.getTotalCount());
        System.out.println("totalPage:" + result.getTotalPage());
        for (UserInfo item : result.getItems()) {
            System.out.println("id:" + item.getId());
            System.out.println("user_Name:" + item.getName());
            System.out.println("login:" + item.getLogin());
        }
    }


    @Test
    public void  getPageList2Test(){
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

        System.out.println("Sql语句:");
        System.out.println(sql.getFinalSql());
        System.out.println("SQL参数值:");
        sql.getFinalArgs().forEach(System.out::println);


        PageList<UserInfoDetailDto> result =  db.getSqlQuery()
                .getPageDtoList(
                        UserInfoDetailDto.class,
                        1,
                        10,
                        sql);
        assert (result.getTotalCount() > 0);
        assert (result.getItems().size()>0);
        System.out.println(">>>>>>>>>> Result <<<<<<<<<<");
        System.out.println("totalCount:"+result.getTotalCount());
        for (UserInfoDetailDto item : result.getItems()) {
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
    public void  getPageList3Test(){

        Database db = getDatabase();
        //  getPageList(int pageIndex, int pageSize, Sql inter)
        PageList<UserInfoDto> result = db.getSqlQuery().getPageDtoList(
                UserInfoDto.class,
                1,
                10,
                Sql.builder().select().from(UserInfo.class));
        assert (result.getTotalCount() > 0) : "result value is error";
        System.out.println(">>>>>>>>>> Result <<<<<<<<<<");
        System.out.println("pageIndex:" + result.getPageIndex());
        System.out.println("pageSize:" + result.getPageSize());
        System.out.println("totalCount:" + result.getTotalCount());
        System.out.println("totalPage:" + result.getTotalPage());
        for (UserInfoDto item : result.getItems()) {
            System.out.println("id:" + item.getId());
            System.out.println("user_Name:" + item.getName());
            System.out.println("login:" + item.getLogin());
        }
    }
}
