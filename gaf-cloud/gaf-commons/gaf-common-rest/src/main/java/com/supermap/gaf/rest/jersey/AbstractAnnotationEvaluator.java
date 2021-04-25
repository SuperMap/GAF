package com.supermap.gaf.rest.jersey;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.core.MediaType;

public abstract class AbstractAnnotationEvaluator<T extends Annotation> implements IsAbleEvaluator {
    
    private Class<T> annoClass;

    protected AbstractAnnotationEvaluator(Class<T> annotationClass) {
        annoClass = annotationClass;
    }

    private Class<?> expectClass;

    @Override
    public boolean isAbleTo(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        T anno = get(annotations, annoClass);
        return anno != null && isAbleToByAnnotation(anno, expectClass);
    }
    
    abstract protected boolean isAbleToByAnnotation(T annotation, Class<?> clz);

    public void setExpectClass(Class<?> value) {
        expectClass = value;
    }

    private static <T extends Annotation> T get(Annotation[] arr, Class<T> clz) {
        for(Annotation ann : arr) {
            if(clz.isInstance(ann)) {
                return clz.cast(ann);
            }
        }
        return null;
    }

}
