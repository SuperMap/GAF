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
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @program: gaf-commons-modules
 * @description:
 * @author: lidong
 * @date:2021/3/25
 * @create: 2019/08/01
 */
public class RemoteCommonService {

    private static Logger logger = LoggerFactory.getLogger(RemoteCommonService.class);

    private RestTemplate restTemplate;

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
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
            K result = restTemplate.exchange(url, HttpMethod.POST, requestEntity, new ParameterizedTypeReference<K>() {
            }).getBody();
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
            ResponseEntity<T> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, clazz);
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

    @HystrixCommand(fallbackMethod = "removeError")
    public MessageResult<String> remoteRemove(String url, MultiValueMap<String, String> params, List<String> cookieList) {
        try {
            HttpHeaders headers = new HttpHeaders();
            if (CollectionUtils.isNotEmpty(cookieList)) {
                headers.put("Cookie", cookieList);
            }
            //  提交方式，表单提交
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            //  封装参数，千万不要替换为Map与HashMap，否则参数无法传递
            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);
            //  执行HTTP请求
            return restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, new ParameterizedTypeReference<MessageResult<String>>() {
            }).getBody();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return MessageResult.failed(String.class).message("删除失败").build();
    }

    public MessageResult<String> removeError(String url, MultiValueMap<String, String> params, List<String> cookieList) {
        return MessageResult.failed(String.class).message("删除失败").build();
    }

    public <T, K> MessageResult<K> remotePut(String url, T t, List<String> cookieList, Class<K> clazz) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            if (CollectionUtils.isNotEmpty(cookieList)) {
                headers.put("Cookie", cookieList);
            }
            HttpEntity<T> requestEntity = new HttpEntity<>(t, headers);
            //  执行HTTP请求
            return restTemplate.exchange(url, HttpMethod.PUT, requestEntity, new ParameterizedTypeReference<MessageResult<K>>() {
            }).getBody();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return MessageResult.failed(clazz).message("更新失败").build();
    }
}
