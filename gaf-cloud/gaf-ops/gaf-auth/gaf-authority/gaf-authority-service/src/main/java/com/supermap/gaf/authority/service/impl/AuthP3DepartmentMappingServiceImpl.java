/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.authority.service.impl;

import com.supermap.gaf.authority.commontype.AuthP3DepartmentMapping;
import com.supermap.gaf.authority.constant.CommonConstant;
import com.supermap.gaf.authority.dao.AuthP3DepartmentMappingMapper;
import com.supermap.gaf.authority.service.AuthP3DepartmentMappingService;
import com.supermap.gaf.authority.vo.AuthP3DepartmentMappingSelectVo;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 第三方部门映射服务实现类
 * @date:2021/3/25
 * @author yangdong
 */
@Service
public class AuthP3DepartmentMappingServiceImpl implements AuthP3DepartmentMappingService {
    private final AuthP3DepartmentMappingMapper authP3DepartmentMappingMapper;

    public AuthP3DepartmentMappingServiceImpl(AuthP3DepartmentMappingMapper authP3DepartmentMappingMapper) {
        this.authP3DepartmentMappingMapper = authP3DepartmentMappingMapper;
    }


    @Override
    public AuthP3DepartmentMapping insertAuthP3DepartmentMapping(AuthP3DepartmentMapping authP3DepartmentMapping) {

        if (authP3DepartmentMappingMapper.select(authP3DepartmentMapping.getDepartmentMappingId()) != null) {
            throw new IllegalArgumentException("resource已经存在：" + authP3DepartmentMapping.getDepartmentMappingId());
        }
        authP3DepartmentMapping.setDepartmentMappingId(UUID.randomUUID().toString());
        authP3DepartmentMappingMapper.insert(authP3DepartmentMapping);
        return authP3DepartmentMapping;
    }

    @Override
    public AuthP3DepartmentMapping selectById(String departmentMappingId) {
        if (departmentMappingId == null) {
            throw new IllegalArgumentException("departmentMappingId不能为空");
        }
        return authP3DepartmentMappingMapper.select(departmentMappingId);
    }

    @Override
    public AuthP3DepartmentMapping getByDepartmentId(String p3ComponentId, String departmentId) {
        if (departmentId == null || p3ComponentId == null) {
            throw new IllegalArgumentException("p3ComponentId、departmentId不能为空");
        }
        return authP3DepartmentMappingMapper.getByDepartmentId(p3ComponentId, departmentId);
    }


    @Override
    public Map<String, Object> pageList(AuthP3DepartmentMappingSelectVo authP3DepartmentMappingSelectVo) {
        if (authP3DepartmentMappingSelectVo.getPageSize() == null || authP3DepartmentMappingSelectVo.getPageSize() == 0) {
            authP3DepartmentMappingSelectVo.setPageSize(50);
        }
        List<AuthP3DepartmentMapping> pageList;
        if (authP3DepartmentMappingSelectVo.getOffset() == null || authP3DepartmentMappingSelectVo.getOffset() < CommonConstant.OFFSET_MAX_FOR_SQL_BETTER) {
            pageList = authP3DepartmentMappingMapper.pageList(authP3DepartmentMappingSelectVo);
        } else {
            pageList = authP3DepartmentMappingMapper.bigOffsetPageList(authP3DepartmentMappingSelectVo);
        }
        int totalCount = authP3DepartmentMappingMapper.pageListCount();
        Map<String, Object> result = new HashMap<>(2);
        result.put("pageList", pageList);
        result.put("totalCount", totalCount);
        return result;
    }

    @Override
    public Map<String, Object> searchList(AuthP3DepartmentMappingSelectVo authP3DepartmentMappingSelectVo) {
        if (authP3DepartmentMappingSelectVo.getPageSize() == null || authP3DepartmentMappingSelectVo.getPageSize() == 0) {
            authP3DepartmentMappingSelectVo.setPageSize(50);
        }
        List<AuthP3DepartmentMapping> pageList;
        pageList = authP3DepartmentMappingMapper.searchList(authP3DepartmentMappingSelectVo);
        Map<String, Object> result = new HashMap<>(2);
        result.put("pageList", pageList);
        result.put("totalCount", pageList.size());
        return result;
    }

    @Override
    public void batchInsert(List<AuthP3DepartmentMapping> authP3DepartmentMappings) {
        authP3DepartmentMappingMapper.batchInsert(authP3DepartmentMappings);
    }

    @Override
    public void batchDelete(List<String> departmentMappingIds) {
        authP3DepartmentMappingMapper.batchDelete(departmentMappingIds);
    }
}
