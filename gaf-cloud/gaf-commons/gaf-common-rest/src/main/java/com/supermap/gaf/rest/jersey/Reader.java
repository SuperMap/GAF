package com.supermap.gaf.rest.jersey;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.ws.rs.ext.MessageBodyReader;

/**
 * 
 * <p>
 * 该注解用于请求实体的读取
 * </p>
 * @author ${Author}
 * @version ${Version}
 * @since 8.1.0
 *
 */
@Target({java.lang.annotation.ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Reader {
    Class<? extends MessageBodyReader<?>> value();
}
