package com.supermap.gaf.analysis.utils;

import com.alibaba.fastjson.JSONObject;
import com.supermap.gaf.utils.LogUtil;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author dqc
 * http工具类
 */
public final class HttpClientUtil {

    private static Logger logger = LogUtil.getLocLogger(HttpClientUtil.class);

    private HttpClientUtil() {
    }

    private static String encode = "UTF-8";
    private static String mimeType = "application/json";

    /**
     * 执行get请求
     *
     * @param url
     * @return
     */
    public static String get(String url) {
        //创建Httpclient对象
        CloseableHttpClient httpclient = HttpClients.createDefault();
        //创建http GET请求
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = null;
        try {
            //执行请求
            response = httpclient.execute(httpGet);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                //请求体内容
                return EntityUtils.toString(response.getEntity(), "UTF-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //释放资源
            try {
                if (response != null) {
                    response.close();
                }
                httpclient.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 执行请求POST
     */
    public static Object execute(String url, String param) {
        try (CloseableHttpClient httpCilent = HttpClients.createDefault()) {
            HashMap<String, String> headers = new HashMap<>(16);
            headers.put("content-type", " application/x-www-form-urlencoded;charset=UTF-8");
            headers.put("access_token", "access_token");
            headers.put("content", "utf-8");
            HttpResponse httpResponse = httpCilent.execute(getHttpPost(param, url, headers));
            int state = httpResponse.getStatusLine().getStatusCode();
            String respStr = EntityUtils.toString(httpResponse.getEntity());
            logger.info("服务发布状态码：" + state);
            logger.info("服务发布反馈内容：" + respStr);
            if (state == HttpStatus.SC_CREATED) {
                return respStr;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static HttpPost getHttpPost(String paramStr, String url, Map<String, String> headers) throws UnsupportedEncodingException {
        HttpPost httpPost = null;
        try {
            RequestConfig requestConfig = getRequestConfig();
            httpPost = new HttpPost(url);
            setStringParam(paramStr, httpPost);
            httpPost.setConfig(requestConfig);
            //设置header
            setHeaders(httpPost, headers);
        } catch (UnsupportedEncodingException e) {
            throw e;
        }
        return httpPost;
    }

    /**
     * 获取请求配置信息
     */
    private static RequestConfig getRequestConfig() {
        /**
         * 设置连接超时时间
         * 设置请求超时时间
         * 默认允许自动重定向
         */
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(100000)
                .setConnectionRequestTimeout(100000)
                .setSocketTimeout(100000).setRedirectsEnabled(true)
                .build();
        return requestConfig;
    }

    /**
     * 设置请求参数
     */
    protected static void setJsonObjectParam(JSONObject json, HttpEntityEnclosingRequestBase httpRequest) throws UnsupportedEncodingException {
        if (json != null) {
            HttpEntity entity = new StringEntity(json.toString(), ContentType.create(mimeType,StandardCharsets.UTF_8));
            httpRequest.setEntity(entity);
        }
    }

    protected static void setStringParam(String str, HttpEntityEnclosingRequestBase httpRequest) throws UnsupportedEncodingException {
        if (StringUtils.isNotEmpty(str)) {
            HttpEntity entity = new StringEntity(str, ContentType.create(mimeType,StandardCharsets.UTF_8));
            httpRequest.setEntity(entity);
        }

    }

    /**
     * 設置请求头
     */
    protected static void setHeaders(HttpRequestBase httpRequest, Map<String, String> headers) {
        if (!MapUtils.isEmpty(headers)) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpRequest.setHeader(entry.getKey(), entry.getValue());
            }
        } else {
            httpRequest.setHeader("Content-Type", mimeType);
        }
    }

}
