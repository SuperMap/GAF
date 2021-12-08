///*
// * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
// * This program are made available under the terms of the Apache License, Version 2.0
// * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
// */
//package com.supermap.gaf.authority.service.impl;
//
//import com.supermap.gaf.authority.commontype.AuthP3TenantMapping;
//import com.supermap.gaf.authority.dao.AuthP3TenantMappingQueryMapper;
//import com.supermap.gaf.authority.service.AuthP3TenantMappingQueryService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
///**
// * @author yangdong
// * @date:2021/3/25 第三方租户映射表服务类
// */
//@Service
//public class AuthP3TenantMappingQueryServiceImpl implements AuthP3TenantMappingQueryService {
//    @Autowired
//    private AuthP3TenantMappingQueryMapper authP3TenantMappingQueryMapper;
//
//    /**
//     * tenantId查询第三方租户映射表
//     *
//     * @return
//     */
//    @Override
//    public AuthP3TenantMapping getByTenantId(String p3ComponentId, String tenantId) {
//        if (tenantId == null || p3ComponentId == null) {
//            throw new IllegalArgumentException("tenantId、p3ComponentId不能为空");
//        }
//        return authP3TenantMappingQueryMapper.getByTenantId(p3ComponentId, tenantId);
//    }
//
//}
