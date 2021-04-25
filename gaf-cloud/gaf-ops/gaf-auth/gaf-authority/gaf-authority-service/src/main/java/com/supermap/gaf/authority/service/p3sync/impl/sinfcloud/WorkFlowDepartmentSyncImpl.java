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
 * @since : 2020-11-27
 */
public class WorkFlowDepartmentSyncImpl extends AbstractSync {
    private static final Logger logger = LogUtil.getLocLogger(WorkFlowDepartmentSyncImpl.class);

    /**
     * 根节点标志
     */
    private static final String ROOT_ID = "0";

    private String sinfCloudHost;
    private String sinfCloudPublicKeyUrl;
    private String sinfCloudLoginUrl;
    private String sinfCloudVerifyTokenUrl;
    private String sinfCloudDepartmentManageUrl;

    private String sinfCloudAdminUserName;
    private String sinfCloudAdminPassword;

    public WorkFlowDepartmentSyncImpl(AuthP3TenantMappingService authP3TenantMappingService, AuthP3DepartmentMappingService authP3DepartmentMappingService, AuthP3UserMappingService authP3UserMappingService, AuthTenantService authTenantService, AuthDepartmentService authDepartmentService, AuthUserService authUserService, RestTemplate restTemplate) {
        super(authP3TenantMappingService, authP3DepartmentMappingService, authP3UserMappingService, authTenantService, authDepartmentService, authUserService, restTemplate);
    }


    @Override
    public List<P3SyncResult> pull(AuthP3MappingRule authP3MappingRule, String search) {
        return null;
    }

    @Override
    public boolean push(AuthP3MappingRule authP3MappingRule, String gafResourceId) {
        logger.info("同步工作流部门", gafResourceId);
        AuthDepartment authDepartment = authDepartmentService.getById(gafResourceId);
        if (null == authDepartment || !authDepartment.getStatus()) {
            throw new GafException("部门无效");
        }
        //0、校验是否已同步
        AuthP3DepartmentMapping authP3DepartmentMapping = authP3DepartmentMappingService.getByDepartmentId(authP3MappingRule.getP3ComponentId(), authDepartment.getDepartmentId());
        if (null != authP3DepartmentMapping && authP3DepartmentMapping.getStatus()) {
            logger.info("工作流部门已经同步，直接返回");
            return true;
        }

        //1、初始化参数
        initParam(authP3MappingRule, authDepartment);

        //2、组织访问sinfcloud api参数
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

        Map<String, Object> paramMap = new HashMap<>(16);

        //2.1、组装参数
        authDepartment = buildP3Param(authP3MappingRule, authDepartment, paramMap);

        //3、获取token
        SinfCloudTokenManager sinfCloudTokenManager = SinfCloudTokenManager.getInstance(sinfCloudPublicKeyUrl, sinfCloudLoginUrl, sinfCloudVerifyTokenUrl, restTemplate);
        String token = sinfCloudTokenManager.getToken(sinfCloudHost, sinfCloudAdminUserName, sinfCloudAdminPassword, authDepartment.getTenantId());

        ObjectMapper mapper = new ObjectMapper();
        String paramStr;
        try {
            paramStr = mapper.writeValueAsString(paramMap);
        } catch (JsonProcessingException e) {
            throw new GafException(e.getMessage());
        }

        HttpEntity<String> request = new HttpEntity<>(paramStr, headers);
        //3.2、配置url
        String url = String.format("%s%s&access_token=%s", sinfCloudHost, sinfCloudDepartmentManageUrl, token);
        //4、发起请求
        SinfCloudRestUtil sinfCloudRestUtil = SinfCloudRestUtil.getInstance(restTemplate);
        Map<String, Object> data = (Map<String, Object>) sinfCloudRestUtil.doPostRequest(url, request);
        String departmentId = data.get("Department_ID").toString();
        AuthP3DepartmentMapping newAuthP3DepartmentMapping = AuthP3DepartmentMapping.builder()
                .departmentId(authDepartment.getDepartmentId())
                .p3DepartmentId(departmentId)
                .p3ComponentId(authP3MappingRule.getP3ComponentId())
                .p3TenantId(authDepartment.getTenantId())
                .status(Boolean.TRUE)
                .build();
        authP3DepartmentMappingService.insertAuthP3DepartmentMapping(newAuthP3DepartmentMapping);
        return true;
    }

    /**
     * 组装第三方资源参数
     *
     */
    private AuthDepartment buildP3Param(AuthP3MappingRule authP3MappingRule, AuthDepartment param, Map<String, Object> paramMap) {
        //3.1、查询第三方上级部门id
        String pid = param.getParentId();
        if (!ROOT_ID.equals(pid) && !StringUtils.isEmpty(pid)) {
            AuthP3DepartmentMapping parentWorkFlowDepartmentMapping = authP3DepartmentMappingService.getByDepartmentId(authP3MappingRule.getP3ComponentId(), pid);
            if (null == parentWorkFlowDepartmentMapping || !parentWorkFlowDepartmentMapping.getStatus()) {
                throw new GafException("上级部门未同步或无效");
            }
            paramMap.put("Department_PID", parentWorkFlowDepartmentMapping.getP3DepartmentId());
        }
        //3.2、查询所属第三方租户
        AuthP3TenantMapping authP3TenantMapping = authP3TenantMappingService.getByTenantId(authP3MappingRule.getP3ComponentId(), param.getTenantId());
        if (null == authP3TenantMapping || !authP3TenantMapping.getStatus()) {
            throw new GafException("该部门所属租户未同步或无效");
        }
        //3.3、组装部门参数
        paramMap.put("Department_Name", param.getDepartmentName());
        paramMap.put("Department_Type", 1);
        paramMap.put("Department_Index", 1);
        paramMap.put("Tenant_ID", authP3TenantMapping.getP3TenantId());
        //3.4、将第三方关系映射暂存在authDepartment中，方便后面获取
        param.setTenantId(authP3TenantMapping.getP3TenantId());
        return param;
    }

    /**
     * 初始化参数
     */
    private void initParam(AuthP3MappingRule authP3MappingRule, AuthDepartment authDepartment) {
        //1、参数非空校验
        if (null == authDepartment) {
            throw new GafException("同步数据不能为空");
        }
        String extraParamJson = authP3MappingRule.getExtraParamJson();
        if (StringUtils.isEmpty(extraParamJson)) {
            throw new GafException("配置参数不能为空");
        }
        //2、参数赋值
        JSONObject jsonObject = JSONObject.parseObject(extraParamJson);
        sinfCloudHost = jsonObject.getString("sinfCloudHost");
        sinfCloudDepartmentManageUrl = jsonObject.getString("sinfCloudDepartmentManageUrl");
        sinfCloudAdminUserName = jsonObject.getString("sinfCloudAdminUserName");
        sinfCloudAdminPassword = jsonObject.getString("sinfCloudAdminPassword");
        sinfCloudPublicKeyUrl = jsonObject.getString("sinfCloudPublicKeyUrl");
        sinfCloudLoginUrl = jsonObject.getString("sinfCloudLoginUrl");
        sinfCloudVerifyTokenUrl = jsonObject.getString("sinfCloudVerifyTokenUrl");
    }
}
