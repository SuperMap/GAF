/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.rest.jersey;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;


/**
 * @author:yj
 * @date:2021/3/25
*/
@Provider
@Produces(MediaType.WILDCARD)
public class AdditionalIsWriteableWriterWrapper<T> implements MessageBodyWriter<T> {

    private MessageBodyWriter<T> writer;
    private IsAbleEvaluator evaluator;

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return evaluator.isAbleTo(type, genericType, annotations, mediaType) && writer.isWriteable(type, genericType, annotations, mediaType);
    }

    @Override
    public long getSize(T t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return writer.getSize(t, type, genericType, annotations, mediaType);
    }

    @Override
    public void writeTo(T t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders,
            OutputStream entityStream) throws IOException, WebApplicationException {
        writer.writeTo(t, type, genericType, annotations, mediaType, httpHeaders, entityStream);
    }
    
    public void setWriter(MessageBodyWriter<T> value) {
        writer = value;
    }
    
    public void setWriteableEvaluator(IsAbleEvaluator value) {
        evaluator = value;
    }

}
