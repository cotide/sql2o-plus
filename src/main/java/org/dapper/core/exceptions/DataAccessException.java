package org.dapper.core.exceptions;

/**
 * 数据库访问异常
 */
public class DataAccessException  extends RuntimeException  {

    /**
     * 构造函数
     * @param message 异常信息
     */
    public DataAccessException(String message)
    {
        super(message);
    }
}
