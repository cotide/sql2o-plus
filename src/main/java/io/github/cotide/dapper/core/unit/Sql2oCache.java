package io.github.cotide.dapper.core.unit;

import java.util.HashSet;
import java.util.Set;

public final class Sql2oCache {

    /**
     * 值类型数组
     */
    static final Set<Class<?>> WRAPPER_TYPES = getWrapperTypes();


    /**
     * 是否为值类型
     * @param clazz
     * @return
     */
    public static Boolean isValueType(Class<?> clazz)
    {
        return WRAPPER_TYPES.contains(clazz);
    }


    // region Helper
    private static Set<Class<?>> getWrapperTypes()
    {
        Set<Class<?>> ret = new HashSet<Class<?>>();
        ret.add(Boolean.class);
        ret.add(Character.class);
        ret.add(Byte.class);
        ret.add(Short.class);
        ret.add(Integer.class);
        ret.add(Long.class);
        ret.add(Float.class);
        ret.add(Double.class);
        ret.add(Void.class);
        return ret;
    }
    // endregion
}
