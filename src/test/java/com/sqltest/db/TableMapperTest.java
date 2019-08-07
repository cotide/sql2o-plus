package com.sqltest.db;

import com.sqltest.model.UserInfo;
import io.github.cotide.dapper.core.attr.Ignore;
import org.junit.Test;
import java.lang.reflect.Field;
import java.util.Arrays;

public class TableMapperTest {

    /**
     * 实体字段映射测试
     */
    @Test
    public void columnInfo_Test(){

        UserInfo userInfo = new UserInfo();
        userInfo.setId(1000);
        userInfo.setName("test");
        userInfo.setPwd("123456");
        userInfo.setLogin(6666);

        Field field = Arrays.stream(UserInfo.class.getDeclaredFields())
                .filter(x->x.getName()=="other").findFirst().orElse(null);
        assert (field!=null)
                :"column mapper login not found";
        assert (field.isAnnotationPresent(Ignore.class))
                :"column mapper login not found";

    }
}
