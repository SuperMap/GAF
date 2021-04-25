/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.data.mgt.util;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *@program: app-landuse
 *@description: 字符串处理_工具成果发布用
 *@author: lidong
 * @date:2021/3/25
 *@create: 2018/08/15
 */
public class StrUtils {

    /**
     * 正则表达式匹配两个指定字符串中间的内容
     * @param str
     * @return
     */
    public static List<String> getSubStrList(String str,String rgex){
        List<String> list = new ArrayList<String>();
        // 匹配的模式
        Pattern pattern = Pattern.compile(rgex);
        Matcher m = pattern.matcher(str);
        while (m.find()) {
            int i = 1;
            list.add(m.group(i));
            i++;
        }
        return list;
    }

    /**
     * 返回单个字符串，若匹配到多个的话就返回第一个，方法与getSubUtil一样
     * @param str 字符串
     * @param rgex  正则表达式
     * @return
     */
    public static String getSubStrSimple(String str,String rgex){
        // 匹配的模式
        Pattern pattern = Pattern.compile(rgex);
        Matcher m = pattern.matcher(str);
        if(m.find()){
            return m.group(1);
        }
        return StringUtils.EMPTY;
    }

}
