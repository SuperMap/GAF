/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.commontypes;

import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * @author:yj
 * @date:2021/3/25
 */
public class AccessRecord {

    private String userName;

    private String remoteAddress;

    private Date accessTime;

    private String httpMethod;

    private String requestUrl;

    private String requestParams;

    private String requestHeaders;

    private String requestContent;

    private long requestDuration;

    private int responseCode;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRemoteAddress() {
        return remoteAddress;
    }

    public void setRemoteAddress(String remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getRequestHeaders() {
        return requestHeaders;
    }

    public void setRequestHeaders(String requestHeaders) {
        this.requestHeaders = requestHeaders;
    }

    public String getRequestContent() {
        return requestContent;
    }

    public void setRequestContent(String requestContent) {
        this.requestContent = requestContent;
    }

    public Date getAccessTime() {
        return accessTime;
    }

    public void setAccessTime(Date accessTime) {
        this.accessTime = accessTime;
    }

    public String getRequestParams() {
        return requestParams;
    }

    public void setRequestParams(String requestParams) {
        this.requestParams = requestParams;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public long getRequestDuration() {
        return requestDuration;
    }

    public void setRequestDuration(long requestduration) {
        this.requestDuration = requestduration;
    }

    public String toLogString() {
        return MessageFormat.format("{0} - {1} - {2} - {3} - {4} - {5} - {6} - {7} - {8}",
                this.requestDuration,
                this.httpMethod,
                this.responseCode,
                this.remoteAddress,
                this.userName,
                this.requestUrl,
                this.requestParams,
                this.requestHeaders,
                this.requestContent);
    }

    public AccessRecord() {
        super();
    }

    public static Map<String, Object> str2Map(String str) {
        String[] arr = str.split(" - ");
        Map<String, Object> map = new HashMap<>();
        if (arr.length == 9) {
            map.put("requestDuration", arr[0]);
            map.put("httpMethod", arr[1]);
            map.put("responseCode", arr[2]);
            map.put("remoteAddress", arr[3]);
            map.put("userName", arr[4]);
            map.put("requestUrl", arr[5]);
            map.put("requestParams", arr[6]);
            map.put("requestHeaders", arr[7]);
            map.put("requestContent", arr[8]);
        } else {
            map.put("message", "日志解析失败！");
        }
        return map;
    }
}
