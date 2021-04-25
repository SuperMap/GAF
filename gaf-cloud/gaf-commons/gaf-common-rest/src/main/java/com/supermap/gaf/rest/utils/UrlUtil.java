/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.rest.utils;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.supermap.services.rest.util.JsonConverter;


/**
 * @author:yj
 * @date:2021/3/25
*/
public class UrlUtil {

    public static <T> List<T> parseParamValueArray(String parameterName, Map<String, String> urlParameters, Class<T> type, List<T> defaultValue) {
        String str = (String) urlParameters.get(parameterName);
        if (StringUtils.isEmpty(str)) {
            return defaultValue;
        }
        try {
            return JsonConverter.parseJsonToList(str, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defaultValue;
    }
    
    public static <T> T parseParamValue(String parameterName, Map<String, String> urlParameters, Class<T> type, T defaultValue) {
        String str = (String) urlParameters.get(parameterName);
        if (StringUtils.isEmpty(str)) {
            return defaultValue;
        }
        try {
            return JsonConverter.parseJson(str, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defaultValue;
    }
}
