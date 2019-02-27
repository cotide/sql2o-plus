package com.sqltest.helper;

import io.github.cotide.dapper.core.utility.Guard;
import org.junit.Test;

import java.math.BigDecimal;

public class GuardTest {


    @Test
    public  void isNotZeroOrNegative(){
        BigDecimal value = new BigDecimal(1);
        Guard.isNotZeroOrNegative(value,"value");
    }
}
