/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.desktop.develop.utils;

import com.alibaba.fastjson.JSON;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.security.sasl.AuthenticationException;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * @author heykb
 * @date:2021/3/25
 */
public class HttpUtils {

    public static Object doGet(String url, Class<?> resultType) throws Exception {
        return doGet(url, null, null, resultType);
    }

    public static Object doGet(String url, Map<String, String> urlParams, Class<?> resultType) throws Exception {
        return doGet(url, urlParams, null, resultType);
    }

    public static Object doPostJson(String url, String jsonBody, Class<?> resultType) throws Exception {
        return doPostJson(url, jsonBody, null, null, resultType);
    }

    public static Object doPostJson(String url, String jsonBody, Map<String, String> urlParams, Class<?> resultType) throws Exception {
        return doPostJson(url, jsonBody, urlParams, null, resultType);
    }

    public static Object doPutJson(String url, String jsonBody, Map<String, String> urlParams, Class<?> resultType) throws Exception {
        return doPutJson(url, jsonBody, urlParams, null, resultType);
    }

    public static Object doGet(String url, Map<String, String> urlParams, Map<String, String> headers, Class<?> resultType) throws Exception {

        CloseableHttpClient httpclient = HttpClients.createDefault();
        Object result = null;
        CloseableHttpResponse responseBody = null;
        try {
            URL r = new URL(url);
            URIBuilder builder = new URIBuilder();
            builder.setHost(r.getHost()).setPort(r.getPort()).setScheme(r.getProtocol())
                    .setPath(r.getPath()).setCustomQuery(r.getQuery()).setFragment(r.getRef());
            if (urlParams != null) {
                for (String key : urlParams.keySet()) {
                    builder.addParameter(key, urlParams.get(key));
                }
            }
            URI uri = builder.build();
            HttpGet request = new HttpGet(uri);
            if (headers != null) {
                for (String key : headers.keySet()) {
                    request.addHeader(key, headers.get(key));
                }
            }
            ResponseHandler<String> responseHandler = response -> {
                int status = response.getStatusLine().getStatusCode();
                if (status >= 200 && status < 300) {
                    HttpEntity entity = response.getEntity();
                    return entity != null ? EntityUtils.toString(entity, "UTF-8") : null;
                } else if (status == 401 || status == 302) {
                    throw new AuthenticationException();
                } else {
                    throw new ClientProtocolException("Unexpected response status: " + status);
                }
            };
            request.getParams().setParameter(ClientPNames.HANDLE_REDIRECTS, false);
            String resultString = httpclient.execute(request, responseHandler);
            result = JSON.parseObject(resultString, resultType);
        } finally {
            try {
                if (responseBody != null) {
                    responseBody.close();
                }
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static Object doPostJson(String url, List<NameValuePair> params, Class<?> resultType) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();

        Object result;
        try {
            URL r = new URL(url);
            URIBuilder builder = new URIBuilder();
            builder.setHost(r.getHost()).setPort(r.getPort()).setScheme(r.getProtocol())
                    .setPath(r.getPath()).setCustomQuery(r.getQuery()).setFragment(r.getRef());
            URI uri = builder.build();
            HttpPost request = new HttpPost(uri);
            request.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            request.setHeader("Content-type", "application/x-www-form-urlencoded");
            ResponseHandler<String> responseHandler = response -> {
                int status = response.getStatusLine().getStatusCode();
                if (status >= 200 && status < 300) {
                    HttpEntity responseEntity = response.getEntity();
                    return responseEntity != null ? EntityUtils.toString(responseEntity, "UTF-8") : null;
                } else if (status == 401 || status == 302) {
                    throw new AuthenticationException();
                } else {
                    throw new ClientProtocolException("Unexpected response status: " + status);
                }
            };
            request.getParams().setParameter(ClientPNames.HANDLE_REDIRECTS, false);
            String resultString = httpClient.execute(request, responseHandler);
            result = JSON.parseObject(resultString, resultType);
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static Object doPostJson(String url, String jsonBody, Map<String, String> urlParams, Map<String, String> headers, Class<?> resultType) throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        Object result = null;
        try {
            URL r = new URL(url);
            URIBuilder builder = new URIBuilder();
            builder.setHost(r.getHost()).setPort(r.getPort()).setScheme(r.getProtocol())
                    .setPath(r.getPath()).setCustomQuery(r.getQuery()).setFragment(r.getRef());
            if (urlParams != null) {
                for (String key : urlParams.keySet()) {
                    builder.addParameter(key, urlParams.get(key));
                }
            }
            URI uri = builder.build();
            HttpPost request = new HttpPost(uri);
            if (headers != null) {
                for (String key : headers.keySet()) {
                    request.addHeader(key, headers.get(key));
                }
            }
            if (jsonBody != null) {
                StringEntity entity = new StringEntity(jsonBody, ContentType.APPLICATION_JSON);
                request.setEntity(entity);
            }
            ResponseHandler<String> responseHandler = response -> {
                int status = response.getStatusLine().getStatusCode();
                if (status >= 200 && status < 300) {
                    HttpEntity responseEntity = response.getEntity();
                    return responseEntity != null ? EntityUtils.toString(responseEntity, "UTF-8") : null;
                } else if (status == 401 || status == 302) {
                    throw new AuthenticationException();
                } else {
                    throw new ClientProtocolException("Unexpected response status: " + status);
                }
            };
            String resultString = httpclient.execute(request, responseHandler);
            result = JSON.parseObject(resultString, resultType);
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static Object doPutJson(String url, String jsonBody, Map<String, String> urlParams, Map<String, String> headers, Class<?> resultType) throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        Object result = null;
        CloseableHttpResponse responseBody = null;
        try {
            URL r = new URL(url);
            URIBuilder builder = new URIBuilder();
            builder.setHost(r.getHost()).setPort(r.getPort()).setScheme(r.getProtocol())
                    .setPath(r.getPath()).setCustomQuery(r.getQuery()).setFragment(r.getRef());
            if (urlParams != null) {
                for (String key : urlParams.keySet()) {
                    builder.addParameter(key, urlParams.get(key));
                }
            }
            URI uri = builder.build();
            HttpPut request = new HttpPut(uri);
            if (headers != null) {
                for (String key : headers.keySet()) {
                    request.addHeader(key, headers.get(key));
                }
            }
            if (jsonBody != null) {
                StringEntity entity = new StringEntity(jsonBody, ContentType.APPLICATION_JSON);
                request.setEntity(entity);
            }
            ResponseHandler<String> responseHandler = response -> {
                int status = response.getStatusLine().getStatusCode();
                if (status >= 200 && status < 300) {
                    HttpEntity responseEntity = response.getEntity();
                    return responseEntity != null ? EntityUtils.toString(responseEntity, "UTF-8") : null;
                } else if (status == 401 || status == 302) {
                    throw new AuthenticationException();
                } else {
                    throw new ClientProtocolException("Unexpected response status: " + status);
                }
            };
            String resultString = httpclient.execute(request, responseHandler);
            result = JSON.parseObject(resultString, resultType);
        } finally {
            try {
                if (responseBody != null) {
                    responseBody.close();
                }
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
