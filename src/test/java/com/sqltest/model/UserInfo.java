package com.sqltest.model;

import com.sqltest.model.enums.EnumGroup;
import com.sqltest.model.enums.EnumUserStatus;
import com.sqltest.model.enums.EnumVipLevel;
import io.github.cotide.dapper.basic.domain.Entity;
import io.github.cotide.dapper.core.attr.Column;
import io.github.cotide.dapper.core.attr.Ignore;
import io.github.cotide.dapper.core.attr.PrimaryKey;
import io.github.cotide.dapper.core.attr.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 用户信息
 */

@Data
@Table(value = "user_info")
public class UserInfo extends Entity {

    /**
     * 用户Id
     */
    @PrimaryKey("user_id")
    private int id;

    /**
     * 值
     */
    @Column("`value`")
    private String value;

    /**
     * 用户名
     */
    @Column("user_name")
    private String name;

    /**
     * 用户类型Id
     */
    @Column("user_type_id")
    private Integer userTypeId;

    /**
     * 密码
     */
    @Column("password")
    private String pwd;

    /**
     * 状态 1-正常 2-无效
     */
    @Column("status")
    private EnumUserStatus status;

    /**
     * 等级 VIP1,VIP2,VIP3
     */
    @Column("level")
    private EnumVipLevel level;

    /**
     * 小组 GROUP1,GROUP2,GROUP3
     */
    @Column("`group`")
    private EnumGroup group;

    /**
     * 客户号
     */
    private int login;

    /**
     * 创建时间
     */
    @Column("create_time")
    private Date createTime;

    @Ignore
    private String other;
}
