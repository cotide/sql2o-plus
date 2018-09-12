package io.github.cotide.dapper.basic.enums;

public interface IEnum<T>  {

    /**
     * 获取code值
     * @return
     */
     T getCode();

    /**
     * 获取描述
     * @return
     */
     String getDesc();
}
