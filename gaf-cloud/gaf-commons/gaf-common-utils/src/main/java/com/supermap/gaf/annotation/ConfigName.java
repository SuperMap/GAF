package com.supermap.gaf.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author heykb
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfigName {
    String[] value() default {};

    boolean toJson() default false;

    /**
     * if expand is true toJson also true
     *
     * @return
     */
    boolean expand() default false;
}
