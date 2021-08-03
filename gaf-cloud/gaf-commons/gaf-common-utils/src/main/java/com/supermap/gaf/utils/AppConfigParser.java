/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.supermap.gaf.annotation.ConfigName;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @author heykb
 * @date:2021/3/25
 */
public class AppConfigParser {

    static boolean isPrimitiveOrWrap(Class clazz) {
        try {
            return clazz.isPrimitive() || ((Class) clazz.getField("TYPE").get(null)).isPrimitive();
        } catch (Exception e) {
            return false;
        }

    }

    public static void expandKey(Map source, Object key) {
        Object o = source.get(key);
        if (o != null && o instanceof Map) {
            source.remove(key);
            source.putAll((Map) o);
        }
    }

    public static Object parse(Object o) {
        return parse(o, false);
    }

    public static Object parse(Object o, boolean toJson) {
        if (o == null) {
            return null;
        }
        if (isPrimitiveOrWrap(o.getClass())) {
            return o;
        } else if (o instanceof String) {
            if (toJson) {
                String json = ((String) o).trim().replace('\u00A0', ' ');
                try {
                    if (json.startsWith("{")) {
                        return JSON.parse(json);
                    } else if (json.startsWith("[")) {
                        return JSONArray.parse(json);
                    } else {
                        return json;
                    }
                } catch (Exception e) {
                    return json;
                }
            } else {
                return o;
            }
        } else if (o.getClass().isArray()) {
            int length = Array.getLength(o);
            Object[] arr = new Object[length];
            for (int i = 0; i < length; i++) {
                Object item = Array.get(o, i);
                arr[i] = parse(item);
            }
            return arr;
        } else if (Collection.class.isAssignableFrom(o.getClass())) {
            List<Object> list = new ArrayList<>();
            for (Object item : (Collection) o) {
                list.add(parse(item));
            }
            return list;
        } else if (Map.class.isAssignableFrom(o.getClass())) {
            Map<String, Object> map = new HashMap<>();
            for (Object key : ((Map) o).keySet()) {
                map.put(key.toString(), parse(((Map) o).get(key)));
            }
            return map;
        } else {
            Map<String, Object> map = new HashMap<>();
            Class clazz = o.getClass();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                ConfigName configName = field.getAnnotation(ConfigName.class);
                if (configName != null) {
                    String fieldName = field.getName();
                    Object value = null;
                    try {
                        value = field.get(o);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    boolean expand = configName.expand();
                    String[] names = configName.value();
                    if (ArrayUtils.isEmpty(names)) {
                        map.put(fieldName, parse(value, expand || configName.toJson()));
                    } else {
                        for (String name : names) {
                            name = StringUtils.isEmpty(name) ? fieldName : name;
                            fieldName = name;
                            map.put(name, parse(value, expand || configName.toJson()));
                        }
                    }
                    if (expand) {
                        expandKey(map, fieldName);
                    }
                }
            }
            return map;
        }
    }

//    public static void main(String[] args) {
////        String json = "{\n  \"resourceTag\": true,\n  \"isHierarchicalHousehold\": true\n}";
//        String json = "{\n  \"resourceTag\": true,\n  \"isHierarchicalHousehold\": true\n}";
////        String json = ((String) o).trim();
////        json = json.replace('\u00A0',' ');
//        ObjectMapper mapper = new ObjectMapper();
//
////        mapper.reader(student);
//        try{
//            System.out.println(mapper.readTree(json));
////            System.out.println(JSON.parseObject(json));
//        }catch (Exception e){
//            e.printStackTrace();
////            System.out.println(json);
//        }
//    }
}
