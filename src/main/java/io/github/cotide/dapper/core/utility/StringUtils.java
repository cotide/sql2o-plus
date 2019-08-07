package io.github.cotide.dapper.core.utility;

public class StringUtils {

    public static boolean isNullOrEmpty(String str) {
        return (isNull(str) || isEmpty(str));
    }

    public static boolean isNull(Object obj)
    {
        return (obj == null);
    }

    public static boolean isEmpty(String str){

        return "".equals(str.trim());
    }

    public static boolean isNotNullOrEmpty(String str)
    {
        return !isNullOrEmpty(str);
    }
}
