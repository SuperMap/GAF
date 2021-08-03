package com.supermap.gaf.rest.jersey;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.ws.rs.ext.MessageBodyWriter;

/**
 * <p>
 * 写入响应实体的注解
 * </p>
 *
 * @author ${Author}
 * @version ${Version}
 * @since 8.1.0
 */
@Target({java.lang.annotation.ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Writer {
    Class<? extends MessageBodyWriter<?>> value();
}
