/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.utils;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.supermap.gaf.services.resource.CommonResource;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @program: gaf-datacenter
 * @description: HttpClient工具类：
 * @author: lidong
 * @date:2021/3/25
 * @create: 2019/05/28
 */
public class HttpClientUtil {
    private static ResourceManager rm = new ResourceManager(CommonResource.class);
    private static Logger logger = LogUtil.getLocLogger(HttpClientUtil.class, rm);

    private String encode = "UTF-8";
    private String mimeType = "application/json";

    public void setEncode(String encode) {
        this.encode = encode;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    private static HttpClientUtil instance = new HttpClientUtil();

    private HttpClientUtil() {
    }

    public static HttpClientUtil getInstance() {
        return instance;
    }

    public JSONObject GetHttpEntity(HttpResponse response) {
        String line = null;
        JSONObject resultJsonObject = null;
        StringBuilder entityStringBuilder = new StringBuilder();
        try {
            BufferedReader b = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), encode), 8 * 1024);
            while ((line = b.readLine()) != null) {
                entityStringBuilder.append(line + "/n");
            }
            //利用从HttpEntity中得到的String生成JsonObject
            resultJsonObject = (JSONObject) JSONObject.parse(entityStringBuilder.toString());
        } catch (JSONException e) {
            logger.error(e.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return resultJsonObject;

    }

    /**
     * 获取httpPost 提交formData表单
     *
     * @param formData
     * @param url
     * @param headers
     * @return
     * @throws UnsupportedEncodingException
     */
    public HttpPost getHttpPost(Map <String, String> formData, String url, Map <String, String> headers) throws UnsupportedEncodingException {
        HttpPost httpPost = null;
        try {
            RequestConfig requestConfig = getRequestConfig();
            httpPost = new HttpPost(url);
            setFormData(httpPost, formData);
            httpPost.setConfig(requestConfig);
            //设置header
            setHeaders(httpPost, headers);
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage());
            throw e;
        } finally {
            rest();
        }
        return httpPost;
    }

    /**
     * 获取httpPost
     *
     * @param json
     * @param url
     * @param headers
     * @return
     * @throws UnsupportedEncodingException
     */
    public HttpPost getHttpPost(JSONObject json, String url, Map <String, String> headers) throws UnsupportedEncodingException {
        HttpPost httpPost = null;
        try {
            RequestConfig requestConfig = getRequestConfig();
            httpPost = new HttpPost(url);
            setJsonObjectParam(json, httpPost);
            httpPost.setConfig(requestConfig);
            //设置header
            setHeaders(httpPost, headers);
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage());
            throw e;
        } finally {
            rest();
        }
        return httpPost;
    }

    /**
     * 获取httpPost
     *
     * @param paramStr
     * @param url
     * @param headers
     * @return
     * @throws UnsupportedEncodingException
     */
    public HttpPost getHttpPost(String paramStr, String url, Map <String, String> headers) throws UnsupportedEncodingException {
        HttpPost httpPost = null;
        try {
            RequestConfig requestConfig = getRequestConfig();
            httpPost = new HttpPost(url);
            setStringParam(paramStr, httpPost);
            httpPost.setConfig(requestConfig);
            //设置header
            setHeaders(httpPost, headers);
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage());
            throw e;
        } finally {
            rest();
        }
        return httpPost;
    }

    /**
     * 获取httpGet
     *
     * @param url
     * @return
     * @throws Exception
     */
    public HttpGet getHttpGet(String url, Map <String, String> headers) throws Exception {
        HttpGet httpGet = null;
        try {
            RequestConfig requestConfig = getRequestConfig();
            httpGet = new HttpGet(url);
            httpGet.setConfig(requestConfig);
            //设置header
            setHeaders(httpGet, headers);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw ex;
        } finally {
            rest();
        }
        return httpGet;
    }

    /**
     * 获取HttpDelete
     *
     * @param url
     * @return
     * @throws Exception
     */
    public HttpDelete getHttpDelete(String url, Map <String, String> headers) throws Exception {
        HttpDelete httpDelete = null;
        try {
            RequestConfig requestConfig = getRequestConfig();
            httpDelete = new HttpDelete(url);
            httpDelete.setConfig(requestConfig);
            //设置header
            setHeaders(httpDelete, headers);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw ex;
        } finally {
            rest();
        }
        return httpDelete;
    }

    /**
     * 获取HttpPut
     *
     * @param url
     * @return
     * @throws Exception
     */
    public HttpPut getHttpPut(JSONObject json, String url, Map <String, String> headers) throws Exception {
        HttpPut httpPut = null;
        try {
            RequestConfig requestConfig = getRequestConfig();
            httpPut = new HttpPut(url);
            setJsonObjectParam(json, httpPut);
            httpPut.setConfig(requestConfig);
            //设置header
            setHeaders(httpPut, headers);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw ex;
        } finally {
            rest();
        }
        return httpPut;
    }

    /**
     * 获取HttpPut,提交FormData
     *
     * @param url
     * @return
     * @throws Exception
     */
    public HttpPut getHttpPut(Map <String, String> formData, String url, Map <String, String> headers) throws Exception {
        HttpPut httpPut = null;
        try {
            RequestConfig requestConfig = getRequestConfig();
            httpPut = new HttpPut(url);
            setFormData(httpPut, formData);
            httpPut.setConfig(requestConfig);
            //设置header
            setHeaders(httpPut, headers);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw ex;
        } finally {
            rest();
        }
        return httpPut;
    }

    /**
     * 获取HttpPut
     *
     * @param url
     * @return
     * @throws Exception
     */
    public HttpPut getHttpPut(String paramStr, String url, Map <String, String> headers) throws Exception {
        HttpPut httpPut = null;
        try {
            RequestConfig requestConfig = getRequestConfig();
            httpPut = new HttpPut(url);
            httpPut.setConfig(requestConfig);
            setStringParam(paramStr, httpPut);
            //设置header
            setHeaders(httpPut, headers);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw ex;
        } finally {
            rest();
        }
        return httpPut;
    }

    /**
     * 获取请求配置信息
     *
     * @return
     */
    private RequestConfig getRequestConfig() {
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(100000) // 设置连接超时时间
                .setConnectionRequestTimeout(100000) // 设置请求超时时间
                .setSocketTimeout(100000).setRedirectsEnabled(true)// 默认允许自动重定向
                .build();
        return requestConfig;
    }

    /**
     * 設置请求头
     *
     * @param httpRequest
     * @param headers
     */
    protected void setHeaders(HttpRequestBase httpRequest, Map <String, String> headers) {
        if (!MapUtils.isEmpty(headers)) {
            for (Map.Entry <String, String> entry : headers.entrySet()) {
                httpRequest.setHeader(entry.getKey(), entry.getValue());
            }
        } else {
            httpRequest.setHeader("Content-Type", mimeType);
        }
    }

    /**
     * 設置FormData
     *
     * @param httpRequest
     * @param formData
     */
    protected void setFormData(HttpEntityEnclosingRequestBase httpRequest, Map <String, String> formData) throws UnsupportedEncodingException {
        if (!MapUtils.isEmpty(formData)) {
            List <NameValuePair> paramList = new ArrayList <>();
            List <String> list = new ArrayList <>();
            Set <String> keySet = formData.keySet();
            for (String key : keySet) {
                paramList.add(new BasicNameValuePair(key, formData.get(key)));
                list.add(formData.get(key));
            }
            httpRequest.setEntity(new UrlEncodedFormEntity(paramList, encode));
        }
    }

    /**
     * 设置请求参数
     *
     * @param json
     * @param httpRequest
     * @throws UnsupportedEncodingException
     */
    protected void setJsonObjectParam(JSONObject json, HttpEntityEnclosingRequestBase httpRequest) throws UnsupportedEncodingException {
        if (json != null) {
            HttpEntity entity = new StringEntity(json.toString(), mimeType, encode);
            httpRequest.setEntity(entity);
        }
    }

    /**
     * 设置请求参数
     *
     * @param str
     * @param httpRequest
     * @throws UnsupportedEncodingException
     */
    protected void setStringParam(String str, HttpEntityEnclosingRequestBase httpRequest) throws UnsupportedEncodingException {
        if (StringUtils.isNotEmpty(str)) {
            HttpEntity entity = new StringEntity(str, mimeType, encode);
            httpRequest.setEntity(entity);
        }
    }

    protected void rest() {
        this.encode = "UTF-8";
        this.mimeType = "application/json";
    }

}
