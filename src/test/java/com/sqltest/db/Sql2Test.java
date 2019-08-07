package com.sqltest.db;

import com.sqltest.model.UserInfo;
import com.sqltest.model.UserType;
import com.sqltest.model.enums.EnumUserStatus;
import com.sqltest.model.enums.EnumVipLevel;
import io.github.cotide.dapper.query.Sql;
import io.github.cotide.dapper.query.enums.OrderBy;
import io.github.cotide.dapper.query.Ands;
import io.github.cotide.dapper.query.Ors;
import org.junit.Test;

public class Sql2Test {




    @Test
    public void selectWhere(){
        Sql sql =  Sql.builder()
                .select()
                .from(UserInfo.class)
                .where("user_name","Tom")
                .where("level", EnumVipLevel.VIP1);
        System.out.println("SQL语句:");
        System.out.println(sql.getFinalSql());
        System.out.println("SQL参数值:");
        sql.getFinalArgs().forEach(System.out::println);
    }


    @Test
    public void selectWhereIn(){
        Sql sql =  Sql.builder()
                .select()
                .from(UserInfo.class)
                .where(UserInfo::getName,"Tom")
                .where(UserInfo::getStatus).in(
                        EnumUserStatus.NORMAL,
                        EnumUserStatus.STOP)
                .whereIn(UserInfo::getLevel,
                        EnumVipLevel.VIP1,
                        EnumVipLevel.VIP2);
        System.out.println("SQL语句:");
        System.out.println(sql.getFinalSql());
        System.out.println("SQL参数值:");
        sql.getFinalArgs().forEach(System.out::println);
    }


    @Test
    public void eq()
    {
        Sql sql =  Sql.builder()
                .select()
                .from(UserInfo.class)
                .where(UserInfo::getName)
                .eq("xxx")
                .where(UserInfo::getName)
                .notNull();
        System.out.println("SQL语句:");
        System.out.println(sql.getFinalSql());
        System.out.println("SQL参数值:");
        sql.getFinalArgs().forEach(System.out::println);
    }

    @Test
    public void notNull(){
        Sql sql =  Sql.builder()
                .select()
                .from(UserInfo.class)
                .where(UserInfo::getName)
                .notNull();
        System.out.println("SQL语句:");
        System.out.println(sql.getFinalSql());
        System.out.println("SQL参数值:");
        sql.getFinalArgs().forEach(System.out::println);
    }

    @Test
    public void ors(){
        Sql sql  = Sql.builder()
                .select()
                .from(UserInfo.class)
                .where(UserInfo::getName,"apple")
                .or(
                        Ors.sql()
                                .where(UserInfo::getLevel,EnumVipLevel.VIP1)
                                .where(UserInfo::getLevel,EnumVipLevel.VIP1)
                                .where(UserInfo::getStatus,EnumUserStatus.NORMAL)
                );
        System.out.println("SQL语句:");
        System.out.println(sql.getFinalSql());
        System.out.println("SQL参数值:");
        sql.getFinalArgs().forEach(System.out::println);
    }

    @Test
    public void ors2(){
        Sql sql  = Sql.builder()
                .select()
                .from(UserInfo.class)
                .where(UserInfo::getName,"apple")
                .or(
                        Ors.sql().where(UserInfo::getStatus,EnumUserStatus.NORMAL)
                );
        System.out.println("SQL语句:");
        System.out.println(sql.getFinalSql());
        System.out.println("SQL参数值:");
        sql.getFinalArgs().forEach(System.out::println);
    }



    @Test
    public void ors3()
    {
        Sql sql  = Sql.builder()
            .select()
            .from(UserInfo.class)
            .where(UserInfo::getName,"apple")
            .or(
                    Ors.sql()
                            .where(
                                    UserInfo::getLevel,
                                    EnumVipLevel.VIP1)
                            .whereIn(
                                    UserInfo::getStatus,
                                    EnumUserStatus.NORMAL,
                                    EnumUserStatus.STOP)
            )
            .where(UserInfo::getOther).in("a","b","c")
            .where(UserInfo::getValue).notNull();
        System.out.println("SQL语句:");
        System.out.println(sql.getFinalSql());
        System.out.println("SQL参数值:");
        sql.getFinalArgs().forEach(System.out::println);
    }


    @Test
    public void ors4(){
        Sql sql  = Sql.builder()
                .select()
                .from(UserInfo.class)
                .or(
                        Ors.sql()
                                .where(
                                        UserInfo::getLevel,
                                        EnumVipLevel.VIP1)

                );
        System.out.println("SQL语句:");
        System.out.println(sql.getFinalSql());
        System.out.println("SQL参数值:");
        sql.getFinalArgs().forEach(System.out::println);
    }


    @Test
    public void and(){
        Sql sql  = Sql.builder()
                .select()
                .from(UserInfo.class)
                .where(UserInfo::getName,"apple")
                .or(
                        Ors.sql()
                                .where(
                                        UserInfo::getLevel,
                                        EnumVipLevel.VIP1)
                                .whereIn(
                                        UserInfo::getStatus,
                                        EnumUserStatus.NORMAL,
                                        EnumUserStatus.STOP)
                )
                .and(
                        Ands.sql()
                        .whereIn(UserInfo::getLevel,
                                EnumVipLevel.VIP1)
                        .whereIn(
                                UserInfo::getStatus,
                                EnumUserStatus.NORMAL,
                                EnumUserStatus.STOP)
                )
                .where(UserInfo::getOther).in("a","b","c")
                .where(UserInfo::getValue).notNull();
        System.out.println("SQL语句:");
        System.out.println(sql.getFinalSql());
        System.out.println("SQL参数值:");
        sql.getFinalArgs().forEach(System.out::println);
    }

    @Test
    public void order(){
        Sql sql  = Sql.builder()
                .select()
                .from(UserInfo.class)
                .where(UserInfo::getName,"apple")
                .or(
                        Ors.sql()
                                .where(
                                        UserInfo::getLevel,
                                        EnumVipLevel.VIP1)
                                .whereIn(
                                        UserInfo::getStatus,
                                        EnumUserStatus.NORMAL,
                                        EnumUserStatus.STOP)
                )
                .where(UserInfo::getOther).in("a","b","c")
                .where(UserInfo::getValue).notNull()
                .order(UserInfo::getId, OrderBy.DESC);
        System.out.println("SQL语句:");
        System.out.println(sql.getFinalSql());
        System.out.println("SQL参数值:");
        sql.getFinalArgs().forEach(System.out::println);

    }

    @Test
    public void sqlJoin(){

        Sql sql = Sql.builder().select().from(UserInfo.class)
                .innerJoin(UserType.class)
                .on(UserInfo::getUserTypeId,UserType::getId);
        System.out.println("Sql语句:");
        System.out.println(sql.getFinalSql());
        System.out.println("SQL参数值:");
        sql.getFinalArgs().forEach(System.out::println);
    }

    @Test
    public void sqlJoin2(){
        Sql sql = Sql.builder()
                .select()
                .from(UserInfo.class,"a")
                .innerJoin(UserType.class,"b")
                .on("a",UserInfo::getUserTypeId,"b",UserType::getId)
                .where(UserInfo::getName,"Test")
                .or(
                        Ors.sql()
                                .where("a",
                                        UserInfo::getLevel,
                                        EnumVipLevel.VIP1)
                                .whereIn(
                                        "a",
                                        UserInfo::getStatus,
                                        EnumUserStatus.NORMAL,
                                        EnumUserStatus.STOP)
                );
        System.out.println("Sql语句:");
        System.out.println(sql.getFinalSql());
        System.out.println("SQL参数值:");
        sql.getFinalArgs().forEach(System.out::println);
    }


    @Test
    public void or(){
        Sql sql = Sql.builder().select().from(UserInfo.class)
                .where(UserInfo::getId,1)
                .or(Ors.sql()
                       .whereIn(UserInfo::getName,"a")
                       .whereIn(UserInfo::getName,"b","c"))
                .whereLike(UserInfo::getName,"Test_2")
                .order(UserInfo::getId);
        System.out.println("Sql语句:");
        System.out.println(sql.getFinalSql());
        System.out.println("SQL参数值:");
        sql.getFinalArgs().forEach(System.out::println);

    }



    @Test
    public void newSql(){

        Sql sql = new Sql("select * from user_info where user_name = ? ","AA")
                .where("login",1)
                .whereIn("login",2)
                .order(UserInfo::getName,OrderBy.DESC);

        System.out.println("Sql语句:");
        System.out.println(sql.getFinalSql());
        System.out.println("SQL参数值:");
        sql.getFinalArgs().forEach(System.out::println);
    }
}
