package com.sqltest.model.enums;
import com.google.gson.annotations.SerializedName;
import io.github.cotide.dapper.basic.enums.IntegerEnum;
import lombok.Getter;

@Getter
public enum EnumUserStatus implements IntegerEnum {

    @SerializedName("1")
    NORMAL(1,"正常"),

    @SerializedName("2")
    STOP(2,"禁止");

    private Integer code;

    private String desc;

    EnumUserStatus(Integer code,String desc)
    {

        this.code = code;
        this.desc = desc;
    }
}
