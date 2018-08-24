package org.dapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.dapper.basic.domain.base.BaseEntityByType;
import org.dapper.core.enums.DbType;
import org.dapper.core.exceptions.DataAccessException;
import org.dapper.core.repository.IRepository;
import org.dapper.core.unit.IUnitOfWork;
import org.dapper.core.unit.Sql2oUnitOfWork;
import org.dapper.core.utility.Guard;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.logging.LocalLoggerFactory;
import org.sql2o.logging.Logger;

import javax.sql.DataSource;
import java.io.Closeable;
import java.io.IOException;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 数据库对象
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Database {

    private final static Logger logger = LocalLoggerFactory.getLogger(Database.class);

    /**
     * 初始化数据库
     * @param dataSource 数据源
     */
    public Database(DataSource dataSource)
    {
        instance = new Database();
        instance.setSql2o(new Sql2o(dataSource));
    }

    /**
     * 初始化数据库
     * @param url
     * @param username
     * @param password
     */
    public Database(String url, String username, String password) {
        // DbType dbType  =  getDbType(url);
        instance = new Database();
        instance.setSql2o(new Sql2o(url,username,password));
    }


    /**
     * 数据源
     */
    protected static Database instance;

    @Setter
    private Sql2o sql2o;

    protected IUnitOfWork _unitOfWork;


    protected IUnitOfWork getUnitOfWork()
    {

        if (_unitOfWork == null)
        {
            _unitOfWork = new Sql2oUnitOfWork(instance.sql2o.getDataSource());
        }
        return _unitOfWork;
    }

    /**
     * 获取仓储对象
     */
    public  <TEntity extends BaseEntityByType> IRepository getRepository(Class<TEntity> returnType)
    {
        return this.getUnitOfWork().getRepository(returnType);
    }


    //#region Helper

    /**
     * 根据URL 获取数据库类型
     * @param url
     * @return
     * @throws DataAccessException
     */
    private DbType getDbType(String url)
            throws DataAccessException {
        Guard.isNotNullOrEmpty(url,"url");
        String pattern = "(:)(\\w*)(:)";
        // 创建 Pattern 对象
        Pattern r = Pattern.compile(pattern);
        // 现在创建 matcher 对象
        Matcher m = r.matcher(url);
        if(m.find())
        {
            String dbType = m.group().replaceAll(":","").toLowerCase();
            if ("mysql".equals(dbType)) {
                return DbType.MySql;
            } else if ("sqlserver".equals(dbType)) {
                return DbType.SqlServer;
            } else if ("postgresql".equals(dbType)) {
                return DbType.PostgreSql;
            } else if ("oracle".equals(dbType)) {
                return DbType.Oracle;
            }
        }
        return DbType.MySql;
    }

    //#endregion
}
