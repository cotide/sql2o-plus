package io.github.cotide.core.provider;

import io.github.cotide.core.unit.parm.SQLParts;

public interface IPagingHelper {


    /**
     * Sql 分页
     * @param sql
     * @param parts
     * @return
     */
    Boolean splitsql(String sql, SQLParts parts);
}
