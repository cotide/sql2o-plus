package io.github.cotide.dapper.core.utility;

/**
 * @author cotide
 */
public class Guard {

    /**
     * 判断是否为null
     */
    public static void isNotNull(Object parameter,String parameterName){
        isNotNull(parameter,parameterName,"","");
    }


    /**
     * 判断是否为null
     */
    public static  void  isNotNull(Object parameter,
                                   String parameterName,
                                   String title,
                                   String msg){
        if (isNull(parameter))
        {
            String showTitle = "数据验证错误";
            String showMsg = "is not null";
            if(!isNull(parameter))
            {
                showTitle = title;
            }
            if(!isBlankOrNull(msg))
            {
                showMsg = msg;
            }
            String errorMsg = String.format("%s:%s %s",showTitle,parameterName,showMsg);
            throw  new NullPointerException(errorMsg);
        }

    }


    /**
     * 判断是否为null or empty
     * @param parameter
     * @param parameterName
     */
    public static void isNotNullOrEmpty(
            String parameter,
            String parameterName){
        isNotNullOrEmpty(parameter,parameterName,"","");
    }


    /**
     * 判断是否为null or empty
     * @param parameter
     * @param parameterName
     * @param title
     */
    public static void isNotNullOrEmpty(
            String parameter,
            String parameterName,
            String title){
        isNotNullOrEmpty(parameter,parameterName,title,"");
    }


    /**
     * 判断是否为null or empty
     * @param parameter
     * @param parameterName
     * @param title
     * @param msg
     */
    public static void isNotNullOrEmpty(
            String parameter,
            String parameterName,
            String title,
            String msg)
    {
        if (isBlankOrNull(parameter))
        {
            String showTitle = "数据验证错误";
            String showMsg = "is not null";
            if(!isBlankOrNull(title))
            {
                showTitle = title;
            }
            if(!isBlankOrNull(msg))
            {
                showMsg = msg;
            }
            String errorMsg = String.format("%s:%s %s",showTitle,parameterName,showMsg);
            throw  new NullPointerException(errorMsg);
        }
    }


    public static void  isNotZeroOrNegative(
            Integer parameter,
            String parameterName)
    {

        if(parameter == null || parameter<=0)
        {
            String showTitle = "数据验证错误";
            String showMsg = "is not int value";
            String errorMsg = String.format("%s:%s %s",showTitle,parameterName,showMsg);
            throw  new IllegalArgumentException(errorMsg);
        }
    }


    public static void  isNotZeroOrNegative(
            Long parameter,
            String parameterName)
    {
        if(parameter == null || parameter<=0)
        {
            String showTitle = "数据验证错误";
            String showMsg = "is not int value";
            String errorMsg = String.format("%s:%s %s",showTitle,parameterName,showMsg);
            throw  new IllegalArgumentException(errorMsg);
        }
    }

    //#region Helper

    private static boolean isBlankOrNull(String str) {
        return (str == null || "".equals(str.trim()));
    }

    private static boolean isNull(Object obj)
    {
        return (obj == null);
    }

    //#endregion
}
