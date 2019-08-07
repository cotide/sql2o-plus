package io.github.cotide.dapper.query.parm;

import io.github.cotide.dapper.basic.domain.Entity;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * SQL参数对象
 */
@Data
@Builder
public class SQLParams {

    /**
     * 查询表对象
     */
    private Map<String,Class<? extends Entity>> modelClass;

    /**
     * 查询列
     */
    private String                 selectColumns;

    /**
     * 表名
     */
    private String                 tableName;

    /**
     * 主键名
     */
    private String                 pkName;

    /**
     * Join SQL
     */
    private StringBuilder          joinSQL;

    /**
     * 查询条件
     */
    private StringBuilder          conditionSQL;

    /**
     * 条件值
     */
    private List<Object>           columnValues;

    /**
     * 排序
     */
    private String                 orderBy;

}




