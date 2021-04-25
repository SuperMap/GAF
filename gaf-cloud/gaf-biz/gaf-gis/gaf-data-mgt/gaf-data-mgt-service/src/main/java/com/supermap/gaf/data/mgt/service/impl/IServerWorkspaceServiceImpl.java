/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.data.mgt.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.supermap.gaf.commontypes.MessageResult;
import com.supermap.gaf.data.mgt.common.IServerManager;
import com.supermap.gaf.data.mgt.entity.HostServerSetting;
import com.supermap.gaf.data.mgt.entity.IServerWorkspace;
import com.supermap.gaf.data.mgt.service.IServerWorkspaceService;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * iserver工作空间服务实现类
 *
 * @author : yd
 * @date : 2021-3-19
 */
@Service
public class IServerWorkspaceServiceImpl implements IServerWorkspaceService {
    private static final Logger logger = LogUtil.getLocLogger(IServerWorkspaceServiceImpl.class);

    @Autowired
    private IServerManager iServerManager;

    @Override
    public List<IServerWorkspace> queryWorkspaces(String serviceType) {
        List<IServerWorkspace> workspaceList = new ArrayList<>(16);
        MessageResult<String> applyTokenResult = iServerManager.applyToken();
        if (!applyTokenResult.isSuccessed()) {
            throw new GafException(String.format("申请iserver token失败，%s", applyTokenResult.getMessage()));
        }
        String token = applyTokenResult.getData();
        HostServerSetting hostServerSetting = iServerManager.getAvailableIServerSetting();
        String iServerHostUrl = hostServerSetting.getHostServerUrl();
        String iServerDomainUrl = hostServerSetting.getDomainUrl();
        String workspaceUrl = String.format("%s/manager/workspaces.json?token=%s", iServerHostUrl, token);
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
