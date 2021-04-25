/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.rest.jersey;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.core.MediaType;


/**
 * @author:yj
 * @date:2021/3/25
*/
public class NotEvaluator implements IsAbleEvaluator {

    private IsAbleEvaluator wrapped;

    @Override
    public boolean isAbleTo(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return !wrapped.isAbleTo(type, genericType, annotations, mediaType);
    }
    
    public void setWrapped(IsAbleEvaluator value) {
        wrapped = value;
    }

}
