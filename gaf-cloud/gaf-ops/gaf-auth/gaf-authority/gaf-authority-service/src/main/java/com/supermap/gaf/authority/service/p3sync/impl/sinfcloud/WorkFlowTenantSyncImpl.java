/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.authority.service.p3sync.impl.sinfcloud;

import com.alibaba.fastjson.JSONObject;
import com.supermap.gaf.authority.commontype.AuthP3MappingRule;
import com.supermap.gaf.authority.commontype.P3SyncResult;
import com.supermap.gaf.authority.service.*;
import com.supermap.gaf.authority.service.p3sync.AbstractSync;
import com.supermap.gaf.authority.service.p3sync.impl.sinfcloud.util.SinfCloudRestUtil;
import com.supermap.gaf.exception.GafException;
import com.supermap.gaf.utils.LogUtil;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author : yd
 * @date:2021/3/25
 * @since : 2020-11-27
 */
public class WorkFlowTenantSyncImpl extends AbstractSync {
    private static final Logger logger = LogUtil.getLocLogger(WorkFlowTenantSyncImpl.class);

    private String sinfCloudSystemAdminHost;
    private String sinfCloudPublicKeyUrl;
    private String sinfCloudLoginUrl;
    private String sinfCloudVerifyTokenUrl;
    private String sinfCloudAdminTenantTreeUrl;

    private String sinfCloudSystemAdminUserName;
    private String sinfCloudSystemAdminPassword;

    public WorkFlowTenantSyncImpl(AuthP3TenantMappingService authP3TenantMappingService, AuthP3DepartmentMappingService authP3DepartmentMappingService, AuthP3UserMappingService authP3UserMappingService, AuthTenantService authTenantService, AuthDepartmentService authDepartmentService, AuthUserService authUserService, RestTemplate restTemplate) {
        super(authP3TenantMappingService, authP3DepartmentMappingService, authP3UserMappingService, authTenantService, authDepartmentService, authUserService, restTemplate);
    }

    @Override
    public List<P3SyncResult> pull(AuthP3MappingRule authP3MappingRule, String search) {
        logger.info("拉取工作流有效租户", authP3MappingRule.toString());

        List<P3SyncResult> p3SyncResults = new ArrayList<>();
        //1、初始化参数
        initParam(authP3MappingRule);
        //2、获取token
        SinfCloudTokenManager sinfCloudTokenManager = SinfCloudTokenManager.getInstance(sinfCloudPublicKeyUrl, sinfCloudLoginUrl, sinfCloudVerifyTokenUrl, restTemplate);
        String token = sinfCloudTokenManager.getToken(sinfCloudSystemAdminHost, sinfCloudSystemAdminUserName, sinfCloudSystemAdminPassword, null);

        //3、组织访问sinfcloud api参数
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

        HttpEntity<String> request = new HttpEntity<>(null, headers);
        //组装url
        String url = String.format("%s%s&access_token=%s", sinfCloudSystemAdminHost, sinfCloudAdminTenantTreeUrl, token);

        //4、发起请求
        SinfCloudRestUtil sinfCloudRestUtil = SinfCloudRestUtil.getInstance(restTemplate);
        List<Object> data = (List<Object>) sinfCloudRestUtil.doPostRequest(url, request);
        getTenant(data, p3SyncResults);
        return p3SyncResults;
    }

    @Override
    public boolean push(AuthP3MappingRule authP3MappingRule, String gafResourceId) {
        return false;
    }

    /**
     * 初始化参数
     */
    private void initParam(AuthP3MappingRule authP3MappingRule) {
        String extraParamJson = authP3MappingRule.getExtraParamJson();
        if (StringUtils.isEmpty(extraParamJson)) {
            throw new GafException("配置参数不能为空");
        }
        //2、参数赋值
        JSONObject jsonObject = JSONObject.parseObject(extraParamJson);
        sinfCloudSystemAdminHost = jsonObject.getString("sinfCloudSystemAdminHost");
        sinfCloudAdminTenantTreeUrl = jsonObject.getString("sinfCloudAdminTenantTreeUrl");
        sinfCloudSystemAdminUserName = jsonObject.getString("sinfCloudSystemAdminUserName");
        sinfCloudSystemAdminPassword = jsonObject.getString("sinfCloudSystemAdminPassword");
        sinfCloudPublicKeyUrl = jsonObject.getString("sinfCloudPublicKeyUrl");
        sinfCloudLoginUrl = jsonObject.getString("sinfCloudLoginUrl");
        sinfCloudVerifyTokenUrl = jsonObject.getString("sinfCloudVerifyTokenUrl");
    }

    /**
     * 递归获取工作流租户列表
     */
    private void getTenant(List<Object> tenantGroups, List<P3SyncResult> p3SyncResults) {
        for (Object tenantGroup : tenantGroups) {
            Map<String, Object> map = (Map<String, Object>) tenantGroup;
            if (MapUtils.isNotEmpty(map)) {
                Object children = map.get("children");
                if (null == children) {
                    P3SyncResult p3SyncResult = P3SyncResult.builder()
                            .id(map.get("Id").toString())
                            .name(map.get("Name").toString())
                            .build();
                    p3SyncResults.add(p3SyncResult);
                } else {
                    getTenant((List<Object>) children, p3SyncResults);
                }
            }
        }
    }
}
