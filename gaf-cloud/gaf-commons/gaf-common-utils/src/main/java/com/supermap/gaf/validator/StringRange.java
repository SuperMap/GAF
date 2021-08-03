package com.supermap.gaf.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 该注解用于校验字段名是否在指定范围内
 * range和entityClass至少一个不为空
 * 都不为空则范围求并集
 *
 * @author wxl
 * @since 2021/6/18
 */
@Documented
@Constraint(validatedBy = StringRangeValidator.class)
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
public @interface StringRange {
    String message() default "字符串需要在指定范围内";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * 字段名的范围
     *
     * @return
     */
    String[] value() default {};

    /**
     * 用于反射获取字段名，转换为小写下划线,得到范围
     *
     * @return
     */
    Class<?> entityClass() default void.class;


}
