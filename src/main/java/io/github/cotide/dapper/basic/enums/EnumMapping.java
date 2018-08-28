package io.github.cotide.dapper.basic.enums;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EnumMapping {

    String TO_STRING = "toString";
    String ORDINAL   = "ordinal";

    String value() default TO_STRING;
}