package io.github.cotide.dapper.core.dialect;

import io.github.cotide.dapper.core.utility.StringUtils;
import io.github.cotide.dapper.query.parm.SQLParams;

public interface Dialect {

    default String select(SQLParams sqlParams) {
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT");
        if (!StringUtils.isNullOrEmpty(sqlParams.getSelectColumns())) {
            sql.append(' ').append(sqlParams.getSelectColumns()).append(' ');
        } else {
            sql.append(" * ");
        }
        sql.append("\nFROM ").append(sqlParams.getTableName());

        // Join条件
        if(sqlParams.getJoinSQL().length()>0)
        {
            sql.append(" \n").append(sqlParams.getJoinSQL());
        }

        // 查询条件
        if (sqlParams.getConditionSQL().length() > 0) {
            sql.append(" \nWHERE ").append(sqlParams.getConditionSQL().substring(5));
        }

        // 排序
        if (!StringUtils.isNullOrEmpty(sqlParams.getOrderBy())) {
            sql.append(" \nORDER BY").append(sqlParams.getOrderBy());
        }
        return sql.toString();
    }


}
