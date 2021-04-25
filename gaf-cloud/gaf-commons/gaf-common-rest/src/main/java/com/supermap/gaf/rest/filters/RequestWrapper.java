/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.rest.filters;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.slf4j.cal10n.LocLogger;

import com.supermap.gaf.utils.LogUtil;
import com.supermap.services.rest.util.HttpUtil;
import com.supermap.services.rest.util.UrlpostfixMapping;


/**
 * @author:yj
 * @date:2021/3/25
*/
public class RequestWrapper extends HttpServletRequestWrapper {

    public RequestWrapper(HttpServletRequest request) {
        super(request);
    }
    
    /**
     * <p>
     * 获取指定的请求头的值（字符串形式）。
     * </p>
     *
     * @param name 指定的请求头的名称（大小写不敏感）。
     * @return 请求头的值。如果 HTTP 请求中不包含指定名称的请求头，则返回 null。
     */
    public String getHeader(String name) {
        
        if (name.equalsIgnoreCase("Accept")) {
            return this.getAccept();
        } else if (name.equalsIgnoreCase("Content-Type")) {
            return this.getContentTypeByRequest();
        }
        return super.getHeader(name);
    }

    /**
     * @return
     */
    private String getContentTypeByRequest() {
        String contentType = null;
        final String[] contentTypeHeadNames = new String[] { "X-RequestEntity-ContentType", "Content-Type" };
        String queryString = this.getQueryString();
        Map<String, String> queryStringMap = HttpUtil.getURLParameters(queryString);
        for (String contentTypeHeadName : contentTypeHeadNames) {
            contentType = super.getHeader(contentTypeHeadName);
            if (contentType == null) {
                contentType = queryStringMap.get(contentTypeHeadName);
            } else {
                break;
            }
            if (contentType != null) {
                break;
            }
        }
        if (contentType == null || contentType.contains("application/x-www-form-urlencoded")) {
            return "application/json";
        } else {
            return contentType;
        }

    }

    // 返回 客户端期望的媒体类型。
    private String getAccept() {
        return this.getAcceptMediaType(super.getRequestURI(), "application/json");
    }

    /**
     * 根据 url 的路径，设置accept请求头
     *
     * @param path
     * @param defaultMediaType
     * @return
     */
    private String getAcceptMediaType(String path, String defaultMediaType) {
        String strFormat = null;
        String mediaType = null;
        // 根据url 路径中最后一个目录名来判断表述类型。
        if (path.indexOf('/') != -1) {
            int lastIndext1 = path.lastIndexOf('/');
            path = path.substring(lastIndext1 + 1);
        }
        if (path != null && path.indexOf('.') != -1) {
            int lastIndex = path.lastIndexOf('.');
            strFormat = path.substring(lastIndex + 1);
            strFormat = strFormat.trim();
        }
        // 可能是因为 url 最后部分是 图层名，而专题图层中带有 . ,这个 "." 不是表示url 后缀的意思。
        if (strFormat == null || strFormat.equals("") || strFormat.matches("^\\d.*")) {
            mediaType = defaultMediaType;
            strFormat = "html";
        } else {
            if (UrlpostfixMapping.getMediaType(strFormat) != null) {
                mediaType = UrlpostfixMapping.getMediaType(strFormat);
            } else {
                mediaType = "application/" + strFormat;
            }
        }
        if (mediaType.equals("application/rjson")) {
            mediaType = "text/plain";
        }
        this.setAttribute("urlPostfix", strFormat);
        return mediaType;
    }

    // 将url 中表示媒体类型后缀的部分裁剪掉。
    private String cutPostfixForURL(String baseUrl) {
        String remainUrl = baseUrl;
        int lastIndexOfXie = baseUrl.lastIndexOf('/');
        String file = baseUrl.substring(lastIndexOfXie + 1);
        // 如果url模板是以. 结束，也是可以的。
        if (file.indexOf('.') != -1 && !file.endsWith(".")) {
            int lastIndexOfPoint = file.lastIndexOf('.');
            String postfix = file.substring(lastIndexOfPoint + 1);
            // 这个 "." 可能是 图层名的一部分，而不是url 后缀。
            if (!postfix.matches("^\\d.*")) {
                remainUrl = baseUrl.substring(0, lastIndexOfXie + 1 + lastIndexOfPoint);
            }
        }
        return remainUrl;
    }
}
