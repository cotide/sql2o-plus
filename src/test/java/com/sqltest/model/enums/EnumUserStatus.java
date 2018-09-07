package com.sqltest.model.enums;


import lombok.Getter;

@Getter
public enum  EnumUserStatus {

    NORMAL(0,"正常"),
    STOP(1,"禁止");

    private int code;

    private String desc;

    EnumUserStatus(int code,String desc)
    {

        this.code = code;
        this.desc = desc;
    }
}
