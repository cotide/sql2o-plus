package io.github.cotide.dapper.core.utility;


import lombok.experimental.UtilityClass;

@UtilityClass
public class Functions {

    public static <T extends Runnable> void ifThen(boolean flag, T t) {
        if (flag) {
            t.run();
        }
    }


    public static <T extends Runnable> void ifThen(boolean flag, T t1, T t2) {
        if (flag) {
            t1.run();
        } else {
            t2.run();
        }
    }


}
