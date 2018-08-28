package com.sqltest.model;

import org.dapper.basic.domain.base.Entity;
import org.dapper.core.attr.*;

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

    private int login;

    @Column("create_time")
    private Date createTime;

    @Ignore
    private String other;
}
