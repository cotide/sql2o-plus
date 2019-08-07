package com.sqltest.dto;

import com.sqltest.model.enums.EnumGroup;
import com.sqltest.model.enums.EnumUserStatus;
import com.sqltest.model.enums.EnumVipLevel;
import io.github.cotide.dapper.core.attr.Column;
import io.github.cotide.dapper.core.attr.Ignore;
import lombok.Data;
import java.util.Date;

@Data
public class UserInfoDto {

    @Column("user_id")
    private int id;

    @Column("user_name")
    private String name;

    private int login;

    private EnumUserStatus status;

    @Column("`group`")
    private EnumGroup group;

    private EnumVipLevel level;

    private Date createTime;

    @Ignore
    private String other;
}
