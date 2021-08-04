/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.rest.jersey;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Set;

import javax.ws.rs.core.MediaType;

import com.google.common.collect.Sets;


/**
 * @author:yj
 * @date:2021/3/25
 */
public class TypeEvaluator implements IsAbleEvaluator {

    private Set<Class<?>> types = Collections.emptySet();

    public void setTypes(Set<Class<?>> value) {
        types = Sets.newHashSet(value);
    }

    @Override
    public boolean isAbleTo(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return types.contains(type);
    }

}
