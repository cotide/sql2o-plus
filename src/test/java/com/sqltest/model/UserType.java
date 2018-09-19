package com.sqltest.model;

import io.github.cotide.dapper.basic.domain.Entity;
import io.github.cotide.dapper.core.attr.Column;
import io.github.cotide.dapper.core.attr.PrimaryKey;
import io.github.cotide.dapper.core.attr.Table;
import lombok.Data;

import java.util.Date;

/**
 * 用户类型
 */
@Data
@Table("user_type")
public class UserType extends Entity {

    /**
     * 主键Id
     */
    @PrimaryKey()
    private int id;

    /**
     * 类型名称
     */
    @Column("name")
    private String name;

    /**
     * 创建时间
     */
    @Column("create_time")
    private Date createTime;
}
