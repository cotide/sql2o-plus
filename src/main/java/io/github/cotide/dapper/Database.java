package io.github.cotide.dapper;

import io.github.cotide.dapper.repository.inter.IRepository;
import io.github.cotide.dapper.core.unit.Sql2oUnitOfWork;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.github.cotide.dapper.basic.domain.Entity;
import io.github.cotide.dapper.core.enums.DbType;
import io.github.cotide.dapper.exceptions.DataAccessException;
import io.github.cotide.dapper.repository.SqlExecuteBase;
import io.github.cotide.dapper.repository.SqlQueryBase;
import io.github.cotide.dapper.core.unit.IUnitOfWork;
import io.github.cotide.dapper.core.utility.Guard;
import sql2o.Sql2o;
import sql2o.logging.LocalLoggerFactory;
import sql2o.logging.Logger;

import javax.sql.DataSource;
import java.io.Closeable;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 数据库对象
 * @author cotide
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Database implements  AutoCloseable,Closeable {

    /**
     * 是否调试模式
     */
    private boolean isDebug = false;

    /**
     * 是否调试模式
     * @param debug
     */
    public void isDebug(Boolean debug){
        isDebug = debug;
    }

    private final static Logger logger = LocalLoggerFactory.getLogger(Database.class);

    /**
     * 初始化数据库
     * @param dataSource 数据源
     */
    public Database(DataSource dataSource)
    {
        open(new Sql2o(dataSource));
    }

    /**
     * 初始化数据库
     * @param url
     * @param username
     * @param password
     */
    public Database(String url, String username, String password) {
        open(new Sql2o(url,username,password));
    }



    /**
     * 数据源
     */
    protected Database instance;

    @Setter
    private Sql2o sql2o;

    protected IUnitOfWork _unitOfWork;


    protected IUnitOfWork getUnitOfWork()
    {

        if (_unitOfWork == null)
        {
            _unitOfWork = new Sql2oUnitOfWork(
                    instance.sql2o,
                    false,
                    isDebug);
        }
        return _unitOfWork;
    }


     /**
     * 开始事务
     */
    public void beginTransaction(){
        if(_unitOfWork==null)
        {
            _unitOfWork = new Sql2oUnitOfWork(
                    instance.sql2o,
                    true,
                    isDebug);
        }else{
           _unitOfWork.beginTransation();
        }
    }


     /**
     * 事务提交
     */
     public  void commit()  {
         _unitOfWork.commit();
     }


     /**
     * 事务回滚
     */
    public  void rollback() {
        _unitOfWork.rollback();
    }

    /**
     * 获取仓储
     */
    public  <TEntity extends Entity> IRepository getRepository(Class<TEntity> returnType)
    {
        return this.getUnitOfWork().getRepository(returnType);
    }


    /**
     * 获取只读仓储
     * @return
     */
    public SqlQueryBase getSqlQuery(){
        return new SqlQueryBase(this.getUnitOfWork());
    }


    /**
     * 获取SQL执行仓储
     * @return
     */
    public SqlExecuteBase getSqlRun(){
        return new SqlExecuteBase(this.getUnitOfWork());
    }

    //#region Helper

    private void open(Sql2o sql2o)
    {
        instance = new Database();
        instance.setSql2o(sql2o);
    }

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

    /**
     * Closes this stream and releases any system resources associated
     * with it. If the stream is already closed then invoking this
     * method has no effect.
     *
     * <p> As noted in {@link AutoCloseable#close()}, cases where the
     * close may fail require careful attention. It is strongly advised
     * to relinquish the underlying resources and to internally
     * <em>mark</em> the {@code Closeable} as closed, prior to throwing
     * the {@code IOException}.
     *
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void close()  {
        _unitOfWork.close(true);
    }

    //#endregion
}
