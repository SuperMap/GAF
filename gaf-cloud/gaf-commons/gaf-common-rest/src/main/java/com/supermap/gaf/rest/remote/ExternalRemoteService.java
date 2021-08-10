/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.rest.remote;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.supermap.gaf.commontypes.MessageResult;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @author:yj
 * @date:2021/3/25 调用第三方rest服务工具
 */
public class ExternalRemoteService {

    private static Logger logger = LoggerFactory.getLogger(ExternalRemoteService.class);

    private RestTemplate restTemplate;

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public RestTemplate getRestTemplate() {
        return this.restTemplate;
    }

    public <T, K> MessageResult<K> remoteCreate(String url, T t, List<String> cookieList, Class<K> clazz) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            if (CollectionUtils.isNotEmpty(cookieList)) {
                headers.put("Cookie", cookieList);
            }
            HttpEntity<T> requestEntity = new HttpEntity<>(t, headers);
            //  执行HTTP请求
            K result = restTemplate.postForObject(url, requestEntity, clazz);
            return MessageResult.successe(clazz).data(result).build();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return MessageResult.failed(clazz).message("创建失败").build();
    }

    public <T, K> MessageResult<K> remoteCreateWithToken(String url, T t, String token, Class<K> clazz) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("Authorization", "Bearer " + token);
            HttpEntity<T> requestEntity = new HttpEntity<>(t, headers);
            //  执行HTTP请求
            K result = restTemplate.postForObject(url, requestEntity, clazz);
            return MessageResult.successe(clazz).data(result).build();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return MessageResult.failed(clazz).message("创建失败").build();
    }

    public <T, K> MessageResult<K> createError(String url, T t, List<String> cookieList, Class<K> clazz) {
        return MessageResult.failed(clazz).message("创建失败").build();
    }

    @HystrixCommand(fallbackMethod = "getError")
    public <T> T remoteGet(String url, MultiValueMap<String, String> params, List<String> cookieList, Class<T> clazz) {
        T serviceSetting = null;
        try {
            HttpHeaders headers = new HttpHeaders();
            //  提交方式，表单提交
            headers.setContentType(MediaType.APPLICATION_JSON);
            if (CollectionUtils.isNotEmpty(cookieList)) {
                headers.put("Cookie", cookieList);
            }
            //  封装参数，千万不要替换为Map与HashMap，否则参数无法传递
            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);
            //  执行HTTP请求
            ResponseEntity<T> responseEntity = restTemplate.getForEntity(url, clazz, requestEntity);
            serviceSetting = responseEntity.getBody();
        } catch (Exception e) {
            logger.error("remoteGet exception", e);
        }
        return serviceSetting;
    }

    public <T> T getError(String url, MultiValueMap<String, String> params, List<String> cookieList, Class<T> clazz) {
        MessageResult.failed(clazz).message("服务信息获取失败").build();
        return null;
    }
}
