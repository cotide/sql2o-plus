package io.github.cotide.core.attr;

import java.lang.annotation.*;

/**
 * 忽略字段
 */
//@Target({ElementType.METHOD, ElementType.FIELD})
////@Retention(RetentionPolicy.RUNTIME)


@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Ignore {

    String type() default "ignore";
}
