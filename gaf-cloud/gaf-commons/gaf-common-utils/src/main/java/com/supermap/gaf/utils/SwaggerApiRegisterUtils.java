/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.utils;

import com.supermap.gaf.commontypes.Resource;
import io.swagger.models.Operation;
import io.swagger.models.Path;
import io.swagger.models.Swagger;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * swagger对象转keycloak资源对象工具类
 *
 * @author : duke
 * @date:2021/3/25
 * @since 2020/4/27 4:13 PM
 */
public class SwaggerApiRegisterUtils {
    private static final String GROUP_TYPE_PREFIX = "g:";
    private static final String REST = "rest";


    /**
     * swagger对象转为resourece对象列表
     *
     * @return
     */
    public static List<Resource> swaggerToResources(Swagger swagger, List<String> rootApiList) {
        //获取paths
        Map<String, Path> paths = swagger.getPaths();
        List<Resource> resources = new LinkedList<>();
        //遍历paths这个map
        for (String key : paths.keySet()) {
            //path要进行筛选，必须有一组根目录，否则Class<>这种jersey写法会有不从根节点 直接扫描到的path
            if (!startsWithInList(key, rootApiList)) {
                continue;
            }
            //获取path对象
            Path path = paths.get(key);
            //将Path对象转为Resource对象
            resources.add(pathToResources(key, path));
        }
        return resources;
    }

    /**
     * 筛选根节点
     *
     * @param value
     * @param list
     * @return
     */
    private static boolean startsWithInList(String value, List<String> list) {
        for (String patch : list) {
            if (value.startsWith(patch)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 将Path对象转为Resource对象
     *
     * @param path
     * @return
     */
    private static Resource pathToResources(String uri, Path path) {
        Resource resource = new Resource();
        String resourceName = String.format("AUTO:%s:", uri);
        resource.setName(resourceName);
        resource.setCaption(resourceName);
        resource.setUrl(uri);
        String groupType = null;
        //按顺序遍历，对于一个path。找到第一个groupType就使用
        for (Operation operation : path.getOperations()) {
            groupType = getGroupTypeTag(operation);
            if (groupType != null) {
                break;
            }
        }
        resource.setGroupType(groupType);
        resource.setType(REST);
        return resource;
    }

    /**
     * 获取groupType
     *
     * @param operation
     */
    private static String getGroupTypeTag(Operation operation) {
        List<String> tags = operation.getTags();
        //groupType是我们的自定约束，规定前缀。
        if (tags.isEmpty()) {
            return null;
        }
        //从后往前检测groupType的tag
        for (int i = tags.size() - 1; i >= 0; i--) {
            String tag = tags.get(i);
            if (tag.startsWith(GROUP_TYPE_PREFIX)) {
                return tag.substring(GROUP_TYPE_PREFIX.length());
            }
        }
        return null;
    }

}
