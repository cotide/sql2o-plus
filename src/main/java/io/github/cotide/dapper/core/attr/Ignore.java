package io.github.cotide.dapper.core.attr;

import java.lang.annotation.*;

/**
 * 忽略字段
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Ignore {

    String type() default "ignore";
}
