package com.sqltest.dto;

import com.sqltest.model.enums.EnumGroup;
import com.sqltest.model.enums.EnumUserStatus;
import com.sqltest.model.enums.EnumVipLevel;
import lombok.Data;

import java.util.Date;

@Data
public class UserInfoSimpleDto {

    private int id;

    // private String name;

    private int login;

    private EnumUserStatus status;

    private EnumGroup group;

    private EnumVipLevel level;

    private Date createTime;
}
