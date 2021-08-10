/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.authority.service.p3sync.impl;

import com.supermap.gaf.authority.commontype.AuthP3MappingRule;
import com.supermap.gaf.authority.enums.AuthP3MappingRuleMethodEnum;
import com.supermap.gaf.authority.service.*;
import com.supermap.gaf.authority.service.p3sync.AbstractSync;
import com.supermap.gaf.authority.service.p3sync.P3SyncService;
import com.supermap.gaf.exception.GafException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Constructor;

/**
 * @author yd
 * @date:2021/3/25
 * @since : 2020-11-26
 */
@Service
public class P3SyncServiceImpl implements P3SyncService {

    private final AuthP3MappingRuleService authP3MappingRuleService;
    private final AuthP3TenantMappingService authP3TenantMappingService;
    private final AuthP3DepartmentMappingService authP3DepartmentMappingService;
    private final AuthP3UserMappingService authP3UserMappingService;
    private final AuthDepartmentService authDepartmentService;
    private final AuthUserService authUserService;
    private final AuthTenantService authTenantService;
    private final RestTemplate restTemplate;

    public P3SyncServiceImpl(AuthP3MappingRuleService authP3MappingRuleService, AuthP3TenantMappingService authP3TenantMappingService, AuthP3DepartmentMappingService authP3DepartmentMappingService, AuthP3UserMappingService authP3UserMappingService, AuthDepartmentService authDepartmentService, AuthUserService authUserService, AuthTenantService authTenantService, RestTemplate restTemplate) {
        this.authP3MappingRuleService = authP3MappingRuleService;
        this.authP3TenantMappingService = authP3TenantMappingService;
        this.authP3DepartmentMappingService = authP3DepartmentMappingService;
        this.authP3UserMappingService = authP3UserMappingService;
        this.authDepartmentService = authDepartmentService;
        this.authUserService = authUserService;
        this.authTenantService = authTenantService;
        this.restTemplate = restTemplate;
    }

    @Override
    public Object dispatch(String ruleId, String search, String gafResourceId) {
        try {
            Object result;
            //1、查询规则
            AuthP3MappingRule authP3MappingRule = authP3MappingRuleService.selectById(ruleId);
            if (null == authP3MappingRule || !authP3MappingRule.getStatus()) {
                throw new GafException("规则无效");
            }
            //2、反射获取实现类
            Class mappingImpl = Class.forName(authP3MappingRule.getMappingImpl());
            //2.1、构造函数传参
            Class[] paramTypes = {AuthP3TenantMappingService.class, AuthP3DepartmentMappingService.class, AuthP3UserMappingService.class, AuthTenantService.class, AuthDepartmentService.class, AuthUserService.class, RestTemplate.class};
            Object[] params = {authP3TenantMappingService, authP3DepartmentMappingService, authP3UserMappingService, authTenantService, authDepartmentService, authUserService, restTemplate};
            Constructor con = mappingImpl.getConstructor(paramTypes);
            AbstractSync abstractSync = (AbstractSync) con.newInstance(params);
            //2.2、映射方式
            String mappingMethod = authP3MappingRule.getMappingMethod();
            if (AuthP3MappingRuleMethodEnum.PUSH.getValue().equals(mappingMethod)) {
                result = abstractSync.push(authP3MappingRule, gafResourceId);
            } else if (AuthP3MappingRuleMethodEnum.PULL.getValue().equals(mappingMethod)) {
                result = abstractSync.pull(authP3MappingRule, search);
            } else {
                throw new GafException("不支持的同步方式");
            }
            return result;
        } catch (Exception e) {
            throw new GafException("同步异常，" + e.getMessage());
        }
    }
}
