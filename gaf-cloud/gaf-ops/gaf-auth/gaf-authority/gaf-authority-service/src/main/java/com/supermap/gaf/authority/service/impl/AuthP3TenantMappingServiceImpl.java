/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.authority.service.impl;

import com.supermap.gaf.authority.commontype.AuthP3TenantMapping;
import com.supermap.gaf.authority.constant.CommonConstant;
import com.supermap.gaf.authority.dao.AuthP3TenantMappingMapper;
import com.supermap.gaf.authority.service.AuthP3TenantMappingService;
import com.supermap.gaf.authority.vo.AuthP3TenantMappingSelectVo;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 第三方租户映射服务实现类
 * @author yangdong
 * @date:2021/3/25
 *
 */
@Service
public class AuthP3TenantMappingServiceImpl implements AuthP3TenantMappingService {
    private final AuthP3TenantMappingMapper authP3TenantMappingMapper;

    public AuthP3TenantMappingServiceImpl(AuthP3TenantMappingMapper authP3TenantMappingMapper) {
        this.authP3TenantMappingMapper = authP3TenantMappingMapper;
    }

    @Override
    public AuthP3TenantMapping insertAuthP3TenantMapping(AuthP3TenantMapping authP3TenantMapping) {

        if (authP3TenantMappingMapper.select(authP3TenantMapping.getTenantMappingId()) != null) {
            throw new IllegalArgumentException("resource已经存在：" + authP3TenantMapping.getTenantMappingId());
        }
        authP3TenantMapping.setTenantMappingId(UUID.randomUUID().toString());
        authP3TenantMappingMapper.insert(authP3TenantMapping);
        return authP3TenantMapping;
    }

    @Override
    public void deleteAuthP3TenantMapping(String tenantMappingId) {
        selectById(tenantMappingId);
        authP3TenantMappingMapper.delete(tenantMappingId);
    }


    @Override
    public AuthP3TenantMapping updateAuthP3TenantMapping(AuthP3TenantMapping authP3TenantMapping) {
        selectById(authP3TenantMapping.getTenantMappingId());
        authP3TenantMappingMapper.update(authP3TenantMapping);
        return authP3TenantMapping;
    }


    @Override
    public AuthP3TenantMapping selectById(String tenantMappingId) {
        if (tenantMappingId == null) {
            throw new IllegalArgumentException("tenantMappingId不能为空");
        }
        return authP3TenantMappingMapper.select(tenantMappingId);
    }

    @Override
    public AuthP3TenantMapping getByTenantId(String p3ComponentId, String tenantId) {
        if (tenantId == null || p3ComponentId == null) {
            throw new IllegalArgumentException("tenantId、p3ComponentId不能为空");
        }
        return authP3TenantMappingMapper.getByTenantId(p3ComponentId, tenantId);
    }

    @Override
    public Map<String, Object> pageList(AuthP3TenantMappingSelectVo authP3TenantMappingSelectVo) {
        if (authP3TenantMappingSelectVo.getPageSize() == null || authP3TenantMappingSelectVo.getPageSize() == 0) {
            authP3TenantMappingSelectVo.setPageSize(50);
        }
        List<AuthP3TenantMapping> pageList;
        if (authP3TenantMappingSelectVo.getOffset() == null || authP3TenantMappingSelectVo.getOffset() < CommonConstant.OFFSET_MAX_FOR_SQL_BETTER) {
            pageList = authP3TenantMappingMapper.pageList(authP3TenantMappingSelectVo);
        } else {
            pageList = authP3TenantMappingMapper.bigOffsetPageList(authP3TenantMappingSelectVo);
        }
        int totalCount = authP3TenantMappingMapper.pageListCount();
        Map<String, Object> result = new HashMap<>(2);
        result.put("pageList", pageList);
        result.put("totalCount", totalCount);
        return result;
    }

    @Override
    public Map<String, Object> searchList(AuthP3TenantMappingSelectVo authP3TenantMappingSelectVo) {
        if (authP3TenantMappingSelectVo.getPageSize() == null || authP3TenantMappingSelectVo.getPageSize() == 0) {
            authP3TenantMappingSelectVo.setPageSize(50);
        }
        List<AuthP3TenantMapping> pageList;
        pageList = authP3TenantMappingMapper.searchList(authP3TenantMappingSelectVo);
        Map<String, Object> result = new HashMap<>(2);
        result.put("pageList", pageList);
        result.put("totalCount", pageList.size());
        return result;
    }

    @Override
    public void batchInsert(List<AuthP3TenantMapping> authP3TenantMappings) {
        authP3TenantMappingMapper.batchInsert(authP3TenantMappings);
    }


    @Override
    public void batchDelete(List<String> tenantMappingIds) {
        authP3TenantMappingMapper.batchDelete(tenantMappingIds);
    }
}
