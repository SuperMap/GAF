/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * @program: app-landuse
 * @description:
 * @author: lidong
 * @date:2021/3/25
 * @create: 2018/12/07
 */
public class JsonUtils {

    /**
     * json字符串转数组再根据key获取对应的值
     *
     * @param jsonStr
     * @param paramerName
     * @return
     */
    public static Object getValueFromJSONArrayStr(String jsonStr, String paramerName) {
        Object value = "";
        JSONArray jsonArray = JSONStrToJSONArray(jsonStr);
        if (jsonArray != null) {
            // 遍历
            for (Object obj : jsonArray) {
                JSONObject jsonObject = (JSONObject) obj;
                value = jsonObject.get(paramerName);
            }
        }
        return value;
    }

    /**
     * 获取值
     *
     * @param jsonObject
     * @param paramerName
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getValue(JSONObject jsonObject, String paramerName, Class <T> clazz) {
        return (T) jsonObject.get(paramerName);
    }

    /**
     * json字符串转数组再根据key获取对应的值
     *
     * @param jsonStr
     * @return
     */
    public static JSONArray JSONStrToJSONArray(String jsonStr) {
        if (jsonStr != null) {
            JSONArray jsonArray = JSONArray.parseArray(jsonStr);
            return jsonArray;
        }
        return null;
    }

    /**
     * json字符串转数组再根据key获取对应的值
     *
     * @param jsonStr
     * @param paramerName
     * @return
     */
    public static Object getValueFromJSONStr(String jsonStr, String paramerName) {
        Object value = "";
        if (jsonStr != null) {
            JSONObject object = JSONObject.parseObject(jsonStr);
            value = object.get(paramerName);
        }
        return value;
    }
}
