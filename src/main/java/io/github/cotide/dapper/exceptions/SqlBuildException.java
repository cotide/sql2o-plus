package io.github.cotide.dapper.exceptions;

/**
 * SQL 语句异常
 * @author cotide
 */
public class SqlBuildException extends  RuntimeException {

    /**
     * 构造函数
     * @param message 异常信息
     */
    public SqlBuildException(String message)
    {
        super(message);
    }
}
