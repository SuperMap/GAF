/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.rest.filters;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Maps;
import com.supermap.gaf.utils.ClassLoaderUtil;


/**
 * @author:yj
 * @date:2021/3/25
 */
public class StaticContentFilter {

    private static final String STATIC_FLAG = "/static/";
    private static final int STATIC_FLAG_LEN = STATIC_FLAG.length();
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

    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        // TODO cache
        String uri = request.getRequestURI();
        int index = StringUtils.indexOf(uri, STATIC_FLAG);
        if (index == -1) {
            chain.doFilter(request, response);
            return;
        }
        String resource = "staticFiles/" + StringUtils.substring(uri, index + STATIC_FLAG_LEN);
        if (StringUtils.isBlank(resource)) {
            chain.doFilter(request, response);
            return;
        }

        int dotIndex = StringUtils.lastIndexOf(uri, '.');
        if (dotIndex != -1) {
            String suffix = StringUtils.substring(uri, dotIndex + 1);
            String contentType = contentTypeMap.get(suffix);
            if (StringUtils.isNotBlank(contentType)) {
                response.setContentType(contentType);
            }
        }
        try (InputStream is = ClassLoaderUtil.contextLoader().getResourceAsStream(resource);
             ServletOutputStream os = response.getOutputStream();) {
            if (is == null) {
                chain.doFilter(request, response);
                return;
            }
            IOUtils.copy(is, os);
        } catch (Exception ex) {
            //解析异常
        }
        chain.doFilter(request, response);
    }

}
