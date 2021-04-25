/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.data.mgt.common;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.supermap.gaf.commontypes.MessageResult;
import com.supermap.gaf.data.mgt.entity.HostServerConfig;
import com.supermap.gaf.data.mgt.entity.HostServerSetting;
import com.supermap.gaf.data.mgt.service.publisher.config.UrlConfig;
import com.supermap.gaf.utils.HttpClientUtil;
import com.supermap.gaf.utils.JsonUtils;
import com.supermap.gaf.utils.LogUtil;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
* @author:yw
* @Date 2021-3-12
 * @date:2021/3/25
 * iServer 管理类
 */
@Component
public class IServerManager implements InitializingBean {

    private static Logger logger = LogUtil.getLocLogger(IServerManager.class);

    @Autowired
    private HostServerConfig hostServerConfig;

    @Autowired
    private TaskScheduler scheduler;

    /**
     * iServer token过期时间，单位：分钟
     */
    private static final Integer ISERVER_TOKEN_EXPIRATION = 10;
    private String iserverToken = "";

    @Override
    public void afterPropertiesSet() throws Exception {
        logger.info("获取iServer服务配置参数");
        scheduler.scheduleAtFixedRate(() -> {
            logger.info("使iServer token过期");
            iserverToken = "";
        }, ISERVER_TOKEN_EXPIRATION * 60 * 1000L);
    }

    /**
     * 获取配置文件中iServer的HostServerSetting信息
     */
    public HostServerSetting getAvailableIServerSetting() {
        if (hostServerConfig != null) {
            String defaultServerId = hostServerConfig.getDefaultServerId();
            List<HostServerSetting> hostServerSetting = hostServerConfig.getHostServerSetting();
            if (hostServerSetting == null) {
                return null;
            }
            if (StringUtils.isEmpty(defaultServerId)) {
                return hostServerSetting.get(0);
            } else {
                for (HostServerSetting serverSetting : hostServerSetting) {
                    if (defaultServerId.equalsIgnoreCase(serverSetting.getHostId())) {
                        return serverSetting;
                    }
                }
            }
        }
        return null;
    }

    /**
     * 申请iServer token
     */
    public MessageResult<String> applyToken() {
        HostServerSetting hostServerSetting = getAvailableIServerSetting();
        if (null == hostServerSetting) {
            logger.error("iServer服务配置相关参数不能为空");
            return MessageResult.<String>failed(String.class).build();
        }
        MessageResult<String> result = new MessageResult<>();
        // 上次申请的token还存在，则直接返回
        if (!StringUtils.isEmpty(iserverToken)) {
            result.setSuccessed(true);
            result.setData(iserverToken);
            logger.info("token:{}", iserverToken);
            return result;
        }
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            String createTokenUrl = UrlConfig.getServerTokenPostUrl(hostServerSetting.getHostServerUrl());
            JSONObject responseJson = new JSONObject();
            responseJson.put("clientType", "NONE");
            // 申请令牌的有效期，从发布令牌的时间开始计算，单位为分钟
            responseJson.put("expiration", ISERVER_TOKEN_EXPIRATION);
            responseJson.put("userName", hostServerSetting.getUsername());
            responseJson.put("password", hostServerSetting.getPassword());
            HttpResponse httpResponse = httpClient.execute(HttpClientUtil.getInstance().getHttpPost(responseJson, createTokenUrl, null));
            int state = httpResponse.getStatusLine().getStatusCode();
            if (state == HttpStatus.SC_OK) {
                iserverToken = EntityUtils.toString(httpResponse.getEntity());
                logger.info("token: {}", iserverToken);
                result.setSuccessed(true);
                result.setData(iserverToken);
            } else if (state == HttpStatus.SC_UNAUTHORIZED) {
                logger.error("申请iServer token,用户名或密码错误：{}", state);
                result.setMessage("申请iServer token,用户名或密码错误：" + state);
                return result;
            } else if (state == HttpStatus.SC_NOT_FOUND) {
                logger.error("申请iServer token,资源地址错误：{}", state);
                result.setMessage("申请iServer token,资源地址错误：" + state);
                return result;
            } else if (state == HttpStatus.SC_FORBIDDEN) {
                logger.error("申请iServer token时异常,请检查服务是否正常启动：{}", state);
                result.setMessage("申请iServer token时异常,请检查服务是否正常启动：" + state);
                return result;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setMessage("token申请异常：" + e.getMessage());
            return result;
        }
        return result;
    }

    /**
     * 删除服务实例
     */
    public MessageResult<String> removeInstance(String instanceName) {
        // 1、获取iserver token
        MessageResult<String> tokenResult = applyToken();
        if (!tokenResult.isSuccessed()) {
            logger.error("iServer token 申请失败");
            return MessageResult.<String>failed(String.class).message("iServer token 申请失败").data(instanceName).build();
        }
        HostServerSetting hostServerSetting = getAvailableIServerSetting();
        if (null == hostServerSetting) {
            logger.error("iServer服务配置相关参数不能为空");
            return MessageResult.<String>failed(String.class).message("iServer服务配置相关参数不能为空").data(instanceName).build();
        }
        String token = tokenResult.getData();
        String instanceManagerUrl = UrlConfig.getInstanceManagerDelUrl(hostServerSetting.getHostServerUrl(), instanceName, token);
        try (CloseableHttpClient httpCilent = HttpClients.createDefault()) {
            HashMap<String, String> headers = new HashMap<String, String>(16);
            headers.put("content-type", " application/x-www-form-urlencoded;charset=UTF-8");
            headers.put("access_token", "access_token");
            headers.put("content", "utf-8");
            HttpResponse httpResponse = httpCilent.execute(HttpClientUtil.getInstance().getHttpDelete(instanceManagerUrl, headers));
            int state = httpResponse.getStatusLine().getStatusCode();
            if (state == HttpStatus.SC_OK) {
                return MessageResult.<String>successe(String.class).data(instanceName).build();
            } else {
                throw new Exception("删除服务实例异常，状态码：" + state);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return MessageResult.failed(String.class).message("删除服务异常").data(instanceName).build();
        }
    }

    /**
     * 获取当前iserver服务实例名称集合
     */
    public Set<String> getCurrentInstanceName() {
        Set<String> instanceNameCache = new HashSet<>();
        logger.info("获取iServer上服务实例列表");
        // 1、获取iserver token
        MessageResult<String> tokenResult = applyToken();
        if (!tokenResult.isSuccessed()) {
            logger.error("iServer token 申请失败");
            return null;
        }
        HostServerSetting hostServerSetting = getAvailableIServerSetting();
        if (null == hostServerSetting) {
            logger.error("iServer服务配置相关参数不能为空");
            return null;
        }
        String token = tokenResult.getData();
        // 2、获取服务实例名称
        try (CloseableHttpClient httpCilent = HttpClients.createDefault()) {
            HashMap<String, String> headers = new HashMap<String, String>(16);
            headers.put("content-type", " application/x-www-form-urlencoded;charset=UTF-8");
            headers.put("access_token", "access_token");
            headers.put("content", "utf-8");
            String iServerUrl = UrlConfig.getInstanceNameUrl(hostServerSetting.getHostServerUrl(), token);
            HttpResponse httpResponse = httpCilent.execute(HttpClientUtil.getInstance().getHttpGet(iServerUrl, headers));
            int state = httpResponse.getStatusLine().getStatusCode();
            String respStr = EntityUtils.toString(httpResponse.getEntity());
            if (state == HttpStatus.SC_OK) {
                JSONArray responseArray = JsonUtils.JSONStrToJSONArray(respStr);
                for (int i = 0; i < responseArray.size(); i++) {
                    Object responseOne = responseArray.get(i);
                    // 使用组件名称作为服务名称
                    Object componentName = JsonUtils.getValueFromJSONStr(responseOne.toString(), "componentName");
                    if (null != componentName) {
                        instanceNameCache.add(componentName.toString());
                    }
                }
            } else {
                throw new Exception("获取服务实例列表名称异常,状态码：" + state);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return instanceNameCache;
    }
}
