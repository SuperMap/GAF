/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.rest.utils;

import com.supermap.gaf.utils.LogUtil;
import okhttp3.*;
import org.slf4j.Logger;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @program: config-client
 * @description:
 * @author: lidong
 * @date:2021/3/25
 * @create: 2019/08/16
 */
public class OKHttpUtil {

    private static Logger log = LogUtil.getLocLogger(OKHttpUtil.class);

    public static void postUseOkhttpAsync(String url, String requestBody) {
        if (StringUtils.isEmpty(requestBody)) {
            requestBody = "{}";
        }
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        Request request = new Request.Builder().url(url).post(RequestBody.create(mediaType, requestBody)).build();
        OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(10000, TimeUnit.MILLISECONDS).readTimeout(10000, TimeUnit.MILLISECONDS).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                log.info(">>>>>>>>>>>>>>>>请求发生错误,错误信息:", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                log.info(response.protocol() + " " + response.code() + " " + response.message());
                Headers headers = response.headers();
                for (int i = 0; i < headers.size(); i++) {
                    log.info(headers.name(i) + ":" + headers.value(i));
                }
                log.info("onResponse: " + response.body().string());
            }
        });
    }

    public static String postUseOkhttp(String url, String requestBody) {
        if (StringUtils.isEmpty(requestBody)) {
            requestBody = "{}";
        }
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        Request request = new Request.Builder().url(url).post(RequestBody.create(mediaType, requestBody)).build();
        OkHttpClient okHttpClient = new OkHttpClient();
        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return org.apache.commons.lang3.StringUtils.EMPTY;
    }

    /**
     * get
     *
     * @param url
     * @return
     */
    public static String getUseOkhttp(String url) {
        List <String> result = new ArrayList <>();
        OkHttpClient okHttpClient = new OkHttpClient();
        //创建Request 对象,返回json
        Request request = new Request.Builder().url(url)//请求接口。如果需要传参拼接到接口后面。
                .addHeader("Content-Type", "application/json").addHeader("Accept", "application/xml,application/json").get().build();
        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return org.apache.commons.lang3.StringUtils.EMPTY;
    }

    /**
     * get
     *
     * @param url
     * @return
     */
    public static String deleteUseOkhttp(String url, String requestBody) {
        List <String> result = new ArrayList <>();
        OkHttpClient okHttpClient = new OkHttpClient();
        //创建Request 对象,返回json
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        Request request = new Request.Builder().url(url)//请求接口。如果需要传参拼接到接口后面。
                .addHeader("Content-Type", "application/json").addHeader("Accept", "application/xml,application/json")
                .delete(RequestBody.create(mediaType, requestBody)).build();
        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return org.apache.commons.lang3.StringUtils.EMPTY;
    }

}
