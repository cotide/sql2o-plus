package com.sqltest.model.enums;

import io.github.cotide.dapper.basic.enums.StringEnum;
import lombok.Getter;

@Getter
public enum  EnumGroup  implements StringEnum  {


    GROUP1("GROUP1","分组1"),
    GROUP2("GROUP2","分组2");

    private String code;

    private String desc;

    EnumGroup(String code,String desc)
    {

        this.code = code;
        this.desc = desc;
    }
}
