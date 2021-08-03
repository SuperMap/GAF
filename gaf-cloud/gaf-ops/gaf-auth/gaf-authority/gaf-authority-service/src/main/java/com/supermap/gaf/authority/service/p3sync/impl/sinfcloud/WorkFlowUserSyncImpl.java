/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.authority.service.p3sync.impl.sinfcloud;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.supermap.gaf.authority.commontype.*;
import com.supermap.gaf.authority.service.*;
import com.supermap.gaf.authority.service.p3sync.AbstractSync;
import com.supermap.gaf.authority.service.p3sync.impl.sinfcloud.util.SinfCloudRestUtil;
import com.supermap.gaf.exception.GafException;
import com.supermap.gaf.utils.LogUtil;
import org.slf4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : yd
 * @date:2021/3/25
 * @since : 2020-11-26
 */
public class WorkFlowUserSyncImpl extends AbstractSync {
    private static final Logger logger = LogUtil.getLocLogger(WorkFlowUserSyncImpl.class);

    private String sinfCloudHost;
    private String sinfCloudUserManageUrl;
    private String sinfCloudPublicKeyUrl;
    private String sinfCloudLoginUrl;
    private String sinfCloudVerifyTokenUrl;

    private String sinfCloudAdminUserName;
    private String sinfCloudAdminPassword;
    private String userDefaultPassword;

    public WorkFlowUserSyncImpl(AuthP3TenantMappingService authP3TenantMappingService, AuthP3DepartmentMappingService authP3DepartmentMappingService, AuthP3UserMappingService authP3UserMappingService, AuthTenantService authTenantService, AuthDepartmentService authDepartmentService, AuthUserService authUserService, RestTemplate restTemplate) {
        super(authP3TenantMappingService, authP3DepartmentMappingService, authP3UserMappingService, authTenantService, authDepartmentService, authUserService, restTemplate);
    }


    @Override
    public List<P3SyncResult> pull(AuthP3MappingRule authP3MappingRule, String search) {
        return null;
    }

    @Override
    public boolean push(AuthP3MappingRule authP3MappingRule, String gafResourceId) {
        logger.info("同步工作流用户", gafResourceId);
        AuthUser authUser = authUserService.getById(gafResourceId);
        if (null == authUser || !authUser.getStatus()) {
            throw new GafException("用户无效");
        }
        //0、校验是否已同步
        AuthP3UserMapping authP3UserMapping = authP3UserMappingService.getByUserId(authP3MappingRule.getP3ComponentId(), authUser.getUserId());
        if (null != authP3UserMapping && authP3UserMapping.getStatus()) {
            logger.info("工作流用户已经同步，直接返回");
            return true;
        }

        //1、初始化参数
        initParam(authP3MappingRule, authUser);

        //2、组织访问sinfcloud api参数
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

        Map<String, Object> paramMap = new HashMap<>(16);

        //2.1、组装参数
        authUser = buildP3Param(authP3MappingRule, authUser, paramMap);

        //2、获取token
        SinfCloudTokenManager sinfCloudTokenManager = SinfCloudTokenManager.getInstance(sinfCloudPublicKeyUrl, sinfCloudLoginUrl, sinfCloudVerifyTokenUrl, restTemplate);
        String token = sinfCloudTokenManager.getToken(sinfCloudHost, sinfCloudAdminUserName, sinfCloudAdminPassword, authUser.getTenantId());

        ObjectMapper mapper = new ObjectMapper();
        String paramStr;
        try {
            paramStr = mapper.writeValueAsString(paramMap);
        } catch (JsonProcessingException e) {
            throw new GafException(e.getMessage());
        }

        HttpEntity<String> request = new HttpEntity<>(paramStr, headers);
        //3.2、配置url
        String url = String.format("%s%s&access_token=%s", sinfCloudHost, sinfCloudUserManageUrl, token);
        //4、发起请求
        SinfCloudRestUtil sinfCloudRestUtil = SinfCloudRestUtil.getInstance(restTemplate);
        Map<String, Object> data = (Map<String, Object>) sinfCloudRestUtil.doPostRequest(url, request);
        String staffId = data.get("Staff_ID").toString();
        AuthP3UserMapping newAuthP3UserMapping = AuthP3UserMapping.builder()
                .userId(authUser.getUserId())
                .p3UserId(staffId)
                .p3ComponentId(authP3MappingRule.getP3ComponentId())
                .p3TenantId(authUser.getTenantId())
                .p3DepartmentId(authUser.getDepartmentId())
                .status(Boolean.TRUE)
                .build();
        authP3UserMappingService.insertAuthP3UserMapping(newAuthP3UserMapping);
        return true;
    }

    /**
     * 组装第三方资源参数
     */
    private AuthUser buildP3Param(AuthP3MappingRule authP3MappingRule, AuthUser param, Map<String, Object> paramMap) {
        //3.1、查询第三方租户id
        AuthP3TenantMapping authP3TenantMapping = authP3TenantMappingService.getByTenantId(authP3MappingRule.getP3ComponentId(), param.getTenantId());
        if (null == authP3TenantMapping || !authP3TenantMapping.getStatus()) {
            throw new GafException("用户对应租户无效");
        }
        //3.2、查询第三方部门id
        AuthP3DepartmentMapping workFlowDepartmentMapping = authP3DepartmentMappingService.getByDepartmentId(authP3MappingRule.getP3ComponentId(), param.getDepartmentId());
        if (null == workFlowDepartmentMapping || !workFlowDepartmentMapping.getStatus()) {
            throw new GafException("用户对应部门无效");
        }
        //3.2、组装用户参数
        paramMap.put("Staff_Name", param.getRealName());
        paramMap.put("Staff_Email", param.getEmail());
        paramMap.put("Staff_User", param.getName());
        paramMap.put("Department_ID", workFlowDepartmentMapping.getP3DepartmentId());
        paramMap.put("Staff_Password", userDefaultPassword);
        paramMap.put("Staff_Status", 1);
        paramMap.put("Staff_Index", 1);

        //3.3、将第三方关系映射暂存在authUser中，方便后面获取
        param.setTenantId(authP3TenantMapping.getP3TenantId());
        param.setDepartmentId(workFlowDepartmentMapping.getP3DepartmentId());
        return param;
    }

    /**
     * 初始化参数
     */
    private void initParam(AuthP3MappingRule authP3MappingRule, AuthUser authUser) {
        //1、参数非空校验
        if (null == authUser) {
            throw new GafException("同步数据不能为空");
        }
        String extraParamJson = authP3MappingRule.getExtraParamJson();
        if (StringUtils.isEmpty(extraParamJson)) {
            throw new GafException("配置参数不能为空");
        }
        //2、参数赋值
        JSONObject jsonObject = JSONObject.parseObject(extraParamJson);
        sinfCloudHost = jsonObject.getString("sinfCloudHost");
        sinfCloudUserManageUrl = jsonObject.getString("sinfCloudUserManageUrl");
        sinfCloudAdminUserName = jsonObject.getString("sinfCloudAdminUserName");
        sinfCloudAdminPassword = jsonObject.getString("sinfCloudAdminPassword");
        userDefaultPassword = jsonObject.getString("userDefaultPassword");
        sinfCloudPublicKeyUrl = jsonObject.getString("sinfCloudPublicKeyUrl");
        sinfCloudLoginUrl = jsonObject.getString("sinfCloudLoginUrl");
        sinfCloudVerifyTokenUrl = jsonObject.getString("sinfCloudVerifyTokenUrl");
    }

}
