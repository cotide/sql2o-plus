package io.github.cotide.dapper.core.provider;

import io.github.cotide.dapper.Database;
import io.github.cotide.dapper.core.unit.info.TableInfo;
import io.github.cotide.dapper.core.unit.parm.SQLParts;

import java.sql.PreparedStatement;

/**
 * 根据不同数据库的SQL定义
 */
public interface IProvider {


    IPagingHelper getPagingUtility();

    Boolean getHasNativeGuidSupport();

    String escapeTableName(String tableName);

    String escapeSqlIdentifier(String sqlIdentifier);

    String buildPageQuery(long skip, long take, SQLParts parts, Object... args);

    Object mapParameterValue(Object value);

    void preExecute(PreparedStatement cmd);

    String getExistsSql();

    Object executeInsert(Database database, PreparedStatement cmd, String primaryKeyName);

    String getInsertOutputClause(String primaryKeyName);

    String getParameterPrefix(String connectionString);

    String GetAutoIncrementExpression(TableInfo tableInfo);

}
