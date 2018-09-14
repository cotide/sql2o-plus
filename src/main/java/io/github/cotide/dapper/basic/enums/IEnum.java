package io.github.cotide.dapper.basic.enums;

import com.alibaba.fastjson.serializer.JSONSerializable;
import com.alibaba.fastjson.serializer.JSONSerializer;
import java.lang.reflect.Type;

public interface IEnum<T>  extends JSONSerializable {

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

    /**
     * JSON 格式化规则
     * @param jsonSerializer
     * @param o
     * @param t
     * @param i
     */
    @Override
    default void write(JSONSerializer jsonSerializer, Object o, Type t, int i) {
        jsonSerializer.write(getCode());
    }
}
