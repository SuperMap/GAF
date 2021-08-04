package com.supermap.gaf.rest.jersey;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * 该注解用于获取请求中的用户名
 * </p>
 *
 * @author ${Author}
 * @version ${Version}
 * @since 8.1.0
 */
@Target({java.lang.annotation.ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface UserId {
}
