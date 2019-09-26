package com.sqltest.helper;

import io.github.cotide.dapper.core.utility.Guard;
import jdk.nashorn.internal.runtime.regexp.RegExp;
import org.junit.Test;

import java.math.BigDecimal;

public class GuardTest {


    @Test
    public  void isNotZeroOrNegative(){
        BigDecimal value = new BigDecimal(1);
        Guard.isNotZeroOrNegative(value,"value");
    }


    @Test
    public  void replace(){
       String result =   "getTargetId".replace("get", "");
       System.out.println(result);


        String result2 =   "getTargetId".replaceFirst("get", "");
        System.out.println(result2);
    }
}
