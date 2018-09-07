package com.sqltest.model;

import com.sqltest.model.enums.EnumUserStatus;
import com.sqltest.model.enums.EnumVipLevel;
import io.github.cotide.dapper.basic.domain.Entity;
import io.github.cotide.dapper.basic.enums.EnumMapping;
import io.github.cotide.dapper.core.attr.Column;
import io.github.cotide.dapper.core.attr.Ignore;
import io.github.cotide.dapper.core.attr.PrimaryKey;
import io.github.cotide.dapper.core.attr.Table;

import java.util.Date;

/**
 * 用户信息
 */
@lombok.Getter
@lombok.Setter
@Table("user_info")
public class UserInfo extends Entity {

    @PrimaryKey("user_id")
    private int id;

    @Column("user_Name")
    private String name;

    @Column("password")
    private String pwd;

    @EnumMapping(EnumMapping.ORDINAL)
    private EnumUserStatus status;

    @Column("level")
    private EnumVipLevel level;

    private int login;

    @Column("create_time")
    private Date createTime;

    @Ignore
    private String other;
}
