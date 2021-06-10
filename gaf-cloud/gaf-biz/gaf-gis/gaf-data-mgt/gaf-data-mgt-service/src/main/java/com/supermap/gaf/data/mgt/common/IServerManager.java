/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.data.mgt.common;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.supermap.gaf.commontypes.MessageResult;
import com.supermap.gaf.data.mgt.entity.HostServerConfig;
import com.supermap.gaf.data.mgt.entity.HostServerSetting;
import com.supermap.gaf.data.mgt.entity.IServerWorkspace;
import com.supermap.gaf.data.mgt.service.publisher.config.UrlConfig;
import com.supermap.gaf.exception.GafException;
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

import java.util.*;

/**
 * @author:yw
 * @Date 2021-3-12
 * @date:2021/3/25 iServer 管理类
 */
@Component
public class IServerManager implements InitializingBean {

    private static Logger logger = LogUtil.getLocLogger(IServerManager.class);

    private static final ObjectMapper mapper = new ObjectMapper();

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

    /**
     * 删除工作空间
     */
    public MessageResult<Boolean> removeWorkspace(String workspaceName) {
        // 1、获取iserver token
        MessageResult<String> tokenResult = applyToken();
        if (!tokenResult.isSuccessed()) {
            logger.error("iServer token 申请失败");
            return MessageResult.<Boolean>failed(Boolean.class).message("iServer token 申请失败").build();
        }
        HostServerSetting hostServerSetting = getAvailableIServerSetting();
        if (null == hostServerSetting) {
            logger.error("iServer服务配置相关参数不能为空");
            return MessageResult.<Boolean>failed(Boolean.class).message("iServer服务配置相关参数不能为空").build();
        }
        String token = tokenResult.getData();
        String instanceManagerUrl = UrlConfig.getWorkspaceManagerUrl(hostServerSetting.getHostServerUrl(), token);
        try (CloseableHttpClient httpCilent = HttpClients.createDefault()) {
            HashMap<String, String> headers = new HashMap<>();
            headers.put("content-type", " application/x-www-form-urlencoded;charset=UTF-8");
            headers.put("access_token", "access_token");
            headers.put("content", "utf-8");
            MessageResult<String> getAddressResult = getWorkspaceAddress(workspaceName);
            if (!getAddressResult.isSuccessed()) {
                throw new GafException(getAddressResult.getMessage());
            }
            Map<String, String> paramMap = new HashMap<>(8);
            paramMap.put("workspaceConnectionInfo", getAddressResult.getData());
            String paramStr = mapper.writeValueAsString(paramMap);
            HttpResponse httpResponse = httpCilent.execute(HttpClientUtil.getInstance().getHttpPut(paramStr, instanceManagerUrl, headers));
            int state = httpResponse.getStatusLine().getStatusCode();
            if (state == HttpStatus.SC_OK) {
                return MessageResult.<Boolean>successe(Boolean.class).build();
            } else {
                throw new Exception("删除工作空间异常，状态码：" + state);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return MessageResult.failed(Boolean.class).message("删除工作空间异常").build();
        }
    }

    /**
     * 获取iserver中工作空间服务地址
     */
    private MessageResult<String> getWorkspaceAddress(String workspaceName) {
        // 1、获取iserver token
        MessageResult<String> tokenResult = applyToken();
        if (!tokenResult.isSuccessed()) {
            logger.error("iServer token 申请失败");
            return MessageResult.<String>failed(String.class).build();
        }
        HostServerSetting hostServerSetting = getAvailableIServerSetting();
        if (null == hostServerSetting) {
            logger.error("iServer服务配置相关参数不能为空");
            return MessageResult.<String>failed(String.class).build();
        }
        String token = tokenResult.getData();
        String workspaceManagerUrl = UrlConfig.getWorkspaceManagerUrl(hostServerSetting.getHostServerUrl(), token);
        // 获取服务地址
        try (CloseableHttpClient httpCilent = HttpClients.createDefault()) {
            HashMap<String, String> headers = new HashMap<>();
            headers.put("content-type", " application/x-www-form-urlencoded;charset=UTF-8");
            headers.put("access_token", "access_token");
            headers.put("content", "utf-8");
            HttpResponse httpResponse = httpCilent.execute(HttpClientUtil.getInstance().getHttpGet(workspaceManagerUrl, headers));
            int state = httpResponse.getStatusLine().getStatusCode();
            if (state == HttpStatus.SC_OK) {
                String respStr = EntityUtils.toString(httpResponse.getEntity());
                JSONArray responseArray = JsonUtils.JSONStrToJSONArray(respStr);
                for (int i = 0; i < responseArray.size(); i++) {
                    Object responseOne = responseArray.get(i);
                    Object nameObj = JsonUtils.getValueFromJSONStr(responseOne.toString(), "name");
                    String name = nameObj.toString();
                    if (name.equalsIgnoreCase(workspaceName)) {
                        Object addressObj = JsonUtils.getValueFromJSONStr(responseOne.toString(), "address");
                        return MessageResult.<String>successe(String.class).data(addressObj.toString()).build();
                    }
                }
            } else {
                throw new Exception("获取工作空间服务地址异常，状态码：" + state);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return MessageResult.failed(String.class).message("获取工作空间服务地址异常").build();
        }
        return MessageResult.<String>failed(String.class).build();
    }

    /**
     * 根据服务类型查询工作空间列表
     */
    public List<IServerWorkspace> queryWorkspacesByType(String serviceType) {
        List<IServerWorkspace> workspaceList = new ArrayList<>(16);
        MessageResult<String> applyTokenResult = applyToken();
        if (!applyTokenResult.isSuccessed()) {
            throw new GafException(String.format("申请iserver token失败，%s", applyTokenResult.getMessage()));
        }
        String token = applyTokenResult.getData();
        HostServerSetting hostServerSetting = getAvailableIServerSetting();
        String iServerDomainUrl = hostServerSetting.getDomainUrl();
        String workspaceUrl = UrlConfig.getWorkspaceManagerUrl(hostServerSetting.getHostServerUrl(), token);
        try (CloseableHttpClient httpCilent = HttpClients.createDefault()) {
            HashMap<String, String> headers = new HashMap<String, String>(16);
            headers.put("content-type", " application/x-www-form-urlencoded;charset=UTF-8");
            headers.put("access_token", "access_token");
            headers.put("content", "utf-8");
            HttpResponse httpResponse = httpCilent.execute(HttpClientUtil.getInstance().getHttpGet(workspaceUrl, headers));
            int state = httpResponse.getStatusLine().getStatusCode();
            String respStr = EntityUtils.toString(httpResponse.getEntity());
            if (state == HttpStatus.SC_OK) {
                JSONArray responseArray = JsonUtils.JSONStrToJSONArray(respStr);
                for (Object response : responseArray) {
                    Object resultServiceType = JsonUtils.getValueFromJSONStr(response.toString(), "serviceType");
                    if (!StringUtils.isEmpty(serviceType) && !serviceType.equalsIgnoreCase(resultServiceType.toString())) {
                        continue;
                    }
                    Object address = JsonUtils.getValueFromJSONStr(response.toString(), "address");
                    Object name = JsonUtils.getValueFromJSONStr(response.toString(), "name");
                    Object available = JsonUtils.getValueFromJSONStr(response.toString(), "available");
                    Object serviceName = JsonUtils.getValueFromJSONStr(response.toString(), "serviceName");
                    Object message = JsonUtils.getValueFromJSONStr(response.toString(), "message");
                    Object enabled = JsonUtils.getValueFromJSONStr(response.toString(), "enabled");

                    IServerWorkspace iServerWorkspace = new IServerWorkspace();
                    iServerWorkspace.setServiceType(resultServiceType.toString());
                    iServerWorkspace.setAddress(address.toString());
                    iServerWorkspace.setName(name.toString());
                    iServerWorkspace.setAvailable((Boolean) available);
                    iServerWorkspace.setServiceName(String.format("%s/services/%s", iServerDomainUrl, serviceName.toString()));
                    iServerWorkspace.setMessage(null == message ? null : message.toString());
                    iServerWorkspace.setEnabled((Boolean) enabled);
                    workspaceList.add(iServerWorkspace);
                }
            } else {
                throw new GafException("获取isever工作空间异常,状态码：" + state);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return workspaceList;
    }
}
