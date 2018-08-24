package com.sqltest.model;

import org.dapper.basic.domain.base.BaseEntityByType;
import org.dapper.core.attr.Column;
import org.dapper.core.attr.Ignore;
import org.dapper.core.attr.PrimaryKey;
import org.dapper.core.attr.Table;

import java.util.Date;

/**
 * 用户信息
 */

@lombok.Getter
@lombok.Setter
@Table("user_info")
public class UserInfo extends BaseEntityByType {

    @PrimaryKey("user_id")
    private int id;

    @Column("user_Name")
    private String name;

    @Column("password")
    private String pwd;

    private int login;

    @Column("creat_time")
    private Date creatTime;

    @Ignore
    private String other;
}
