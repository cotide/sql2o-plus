package com.sqltest.model.enums;
import io.github.cotide.dapper.basic.enums.IntegerEnum;
import lombok.Getter;

@Getter
public enum EnumUserStatus implements IntegerEnum {

    NORMAL(1,"正常"),
    STOP(2,"禁止");

    private Integer code;

    private String desc;

    EnumUserStatus(Integer code,String desc)
    {

        this.code = code;
        this.desc = desc;
    }
}
