package com.sqltest.db;

import com.sqltest.base.BaseTest;
import com.sqltest.model.UserInfo;
import com.sqltest.model.UserType;
import io.github.cotide.dapper.query.Sql;
import io.github.cotide.dapper.repository.inter.IRepository;

public class PageListTest  extends BaseTest {


    public void pageListOrderBy(){
       Sql sql = Sql.builder().select().from(UserInfo.class);
       System.out.println(sql.getFinalSql());
        IRepository<UserInfo> userInfoIRepository =
                getDatabase().getRepository(UserInfo.class);
    }
}
