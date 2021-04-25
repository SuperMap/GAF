package com.supermap.gaf.rest.jersey;

import java.lang.annotation.Annotation;

/**
 * 
 * <p>
 * 注解辅助工具类
 * </p>
 * @author ${Author}
 * @version ${Version}
 * @since 8.1.0
 *
 */
public final class AnnotationArrayUtil {
    public static <T extends Annotation> T get(Annotation[] arr, Class<T> clz) {
        for(Annotation ann : arr) {
            if(clz.isInstance(ann)) {
                return clz.cast(ann);
            }
        }
        return null;
    }
}
