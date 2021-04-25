package com.supermap.gaf.rest.jersey;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * <p>
 * 组件类注解
 * </p>
 * @author ${Author}
 * @version ${Version}
 * @since 8.1.0
 *
 */
@Target({ElementType.PARAMETER, java.lang.annotation.ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Component {
}
