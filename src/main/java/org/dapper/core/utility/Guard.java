package org.dapper.core.utility;

public class Guard {


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
            String showTitle = "数据验证错误：";
            String showMsg = " is not null ";
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


    //#region Helper
    private static boolean isBlankOrNull(String str) {
        return (str == null || "".equals(str.trim()));
    }
    //#endregion
}
