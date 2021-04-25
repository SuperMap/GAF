/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.rest.filters;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.google.common.collect.Maps;


/**
 * @author:yj
 * @date:2021/3/25
*/
public class GAFRequestWrapperFilter implements Filter {

    private Map<String, String> contentTypeMap;
    {
        Map<String, String> map = Maps.newHashMap();
        map.put("html", "text/html");
        map.put("htm", "text/html");
        map.put("js", "text/javascript");
        map.put("css", "text/css");
        map.put("png", "image/png");
        map.put("gif", "image/gif");
        map.put("bmp", "image/bmp");
        map.put("jpb", "image/jpeg");
        map.put("json", "application/json");
        contentTypeMap = Collections.unmodifiableMap(map);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //TODO 
        //待补充初始化支持的MediaType解析等资源
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        RequestWrapper wrappeedrRequest = new RequestWrapper((HttpServletRequest) request);
        chain.doFilter(wrappeedrRequest, response);
    }

    @Override
    public void destroy() {
       this.contentTypeMap.clear();
    }

}
