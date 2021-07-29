/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.storage.utils;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.MediaType;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;


/**
 * @author:yj
 * @date:2021/3/25
*/
public class HttpUtil {

    public static Map<String, String> getURLParameters(String queryString) {
        return getURLParameters(queryString, true);
    }

    public static Map<String, String> getURLParameters(String queryString, boolean decode) {
        Map<String, String> urlParams = new HashMap<>();
        String str = queryString;
        String utf = "utf-8";

        if (isValidQueryString(str)) {
            if (decode) {
                String regex = "%(?!(([a-f]|[A-F]|[0-9]){2}))";
                str = str.replaceAll(regex, "%25");

                str = str.replace("%26eq;", "$eq;");
                str = str.replace("%26gt;", "$gt;");
                str = str.replace("%26lt;", "$lt;");
            }

            String[] strEntitys = str.split("&");
            for (String strEntity : strEntitys) {
                if (isValidQueryString(strEntity)) {
                    int index = strEntity.indexOf(61);
                    String[] entity = new String[2];
                    if ((index == -1) || (index == 0))
                        continue;
                    if (index + 1 >= strEntity.length()) {
                        continue;
                    }
                    entity[0] = strEntity.substring(0, index);
                    entity[1] = strEntity.substring(index + 1);

                    entity[0] = entity[0].trim();
                    entity[1] = entity[1].trim();
                    try {
                        String key = entity[0];
                        String value = entity[1];
                        if (decode) {
                            key = URLDecoder.decode(key, utf);
                            value = URLDecoder.decode(value, utf);

                            key = spatialCharConverter(key);
                            value = spatialCharConverter(value);
                        }
                        urlParams.put(key, value);
                    } catch (UnsupportedEncodingException e) {
                    }
                }
            }
        }
        return urlParams;
    }

    private static String spatialCharConverter(String source) {
        String result = source;

        result = result.replace("$gt;", ">");
        result = result.replace("$lt;", "<");
        result = result.replace("$eq;", "=");
        return result;
    }

    private static boolean isValidQueryString(String str) {
        return ((str != null) && (!(str.equals(""))) && (str.indexOf(61) != -1));
    }

    public static Map<String, String> buildUrlParameter(HttpServletRequest request) {
        String paramString = null;
        try {
            String queryString = request.getQueryString();
            paramString = (StringUtils.isEmpty(queryString)) ? null : URLDecoder.decode(queryString, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // throw new HttpException(Status.CLIENT_ERROR_BAD_REQUEST, resource.getMessage(IPortalResource.RESOURCE_PARAM_IS_ILLEGAL, new Object[0]), e);
        }
        return HttpUtil.getURLParameters(paramString);
    }
    
    public static MediaType getAcceptMediaType(HttpServletRequest request) {
        String accept = request == null ? null : request.getHeader("Accept");
        MediaType type = MediaType.APPLICATION_JSON_TYPE;
        if(StringUtils.isNotEmpty(accept) && accept.indexOf(",") > -1) {
            accept = accept.split(",")[0];
        }
        try {
            type = MediaType.valueOf(accept);
        }catch (Exception e) {
        }
        if(type.toString().equals(MediaType.WILDCARD) || type.toString().startsWith("application/signed-exchange")){
            String uri = request.getRequestURI();
            if(uri.endsWith("js")){
                type = MediaType.valueOf("application/javascript");
            }
            if(uri.endsWith("css")){
                type = MediaType.valueOf("text/css");
            }
        }

        return type;
    }
    
    /**
     * 获取请求ip
     * 
     * @param request
     * @return
     */
    public static String getRequestIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = ip.indexOf(",");
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        }
        ip = request.getHeader("X-Real-IP");
        if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            return ip;
        }
        ip = request.getRemoteAddr();
        return ip.equals("0:0:0:0:0:0:0:1") ? "127.0.0.1" : ip;
    }
    
    public static Map<String,String> getRequestHeaders(HttpServletRequest request){
        Map<String,String> headerMap = new HashMap<>();
        Enumeration<String> headers = request.getHeaderNames();
        while (headers.hasMoreElements()) {
            String header = (String) headers.nextElement();
            headerMap.put(header, request.getHeader(header));
        }
        return headerMap;
    }
    
    /**
     * <p>
     *  检查rest请求是否编辑操作（对资源产生影响） 
     * </p>
     * @param method
     * @return
     * @since 1.0.0
     */
    public static boolean isEditType(String method) {
        //只记录这3种谓词的body
        if (StringUtils.equalsAnyIgnoreCase(HttpMethod.POST, method)
                || StringUtils.equalsAnyIgnoreCase(HttpMethod.PUT, method)
                || StringUtils.equalsAnyIgnoreCase(HttpMethod.DELETE, method)
                || StringUtils.equalsAnyIgnoreCase(HttpMethod.PATCH, method)) {
            return true;
        }
        return false;
    } 

}
