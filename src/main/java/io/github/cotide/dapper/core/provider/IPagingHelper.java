package io.github.cotide.dapper.core.provider;

import io.github.cotide.dapper.core.unit.parm.SQLParts;

/**
 * @author cotide
 */
public interface IPagingHelper {


    /**
     * Sql 分页
     * @param sql
     * @param parts
     * @return
     */
    Boolean splitsql(String sql, SQLParts parts);
}
