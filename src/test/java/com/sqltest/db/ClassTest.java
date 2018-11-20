package com.sqltest.db;

import com.sqltest.base.BaseTest;
import com.sqltest.model.UserInfo;
import com.sqltest.model.enums.EnumGroup;
import com.sqltest.model.enums.EnumUserStatus;
import com.sqltest.model.enums.EnumVipLevel;
import io.github.cotide.dapper.Database;
import io.github.cotide.dapper.repository.inter.IRepository;
import org.junit.Test;

import java.util.Date;

public class ClassTest extends BaseTest {


    @Test
    public  void  classCheck(){

        Class<?> c = String.class;
        System.out.println(c.isPrimitive());
        System.out.println(c.equals(String.class));
    }

}
