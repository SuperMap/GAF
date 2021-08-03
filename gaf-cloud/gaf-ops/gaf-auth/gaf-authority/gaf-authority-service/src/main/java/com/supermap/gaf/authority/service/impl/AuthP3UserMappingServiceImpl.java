/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.authority.service.impl;

import com.supermap.gaf.authority.commontype.AuthP3UserMapping;
import com.supermap.gaf.authority.constant.CommonConstant;
import com.supermap.gaf.authority.dao.AuthP3UserMappingMapper;
import com.supermap.gaf.authority.service.AuthP3UserMappingService;
import com.supermap.gaf.authority.vo.AuthP3UserMappingSelectVo;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 第三方用户映射服务实现类
 *
 * @author yangdong
 * @date:2021/3/25
 */
@Service
public class AuthP3UserMappingServiceImpl implements AuthP3UserMappingService {
    private final AuthP3UserMappingMapper authP3UserMappingMapper;

    public AuthP3UserMappingServiceImpl(AuthP3UserMappingMapper authP3UserMappingMapper) {
        this.authP3UserMappingMapper = authP3UserMappingMapper;
    }

    @Override
    public AuthP3UserMapping insertAuthP3UserMapping(AuthP3UserMapping authP3UserMapping) {
        if (authP3UserMappingMapper.select(authP3UserMapping.getUserMappingId()) != null) {
            throw new IllegalArgumentException("resource已经存在：" + authP3UserMapping.getUserMappingId());
        }
        authP3UserMapping.setUserMappingId(UUID.randomUUID().toString());
        authP3UserMappingMapper.insert(authP3UserMapping);
        return authP3UserMapping;
    }


    @Override
    public void deleteAuthP3UserMapping(String userMappingId) {
        selectById(userMappingId);
        authP3UserMappingMapper.delete(userMappingId);
    }


    @Override
    public AuthP3UserMapping updateAuthP3UserMapping(AuthP3UserMapping authP3UserMapping) {
        selectById(authP3UserMapping.getUserMappingId());
        authP3UserMappingMapper.update(authP3UserMapping);
        return authP3UserMapping;
    }

    @Override
    public AuthP3UserMapping selectById(String userMappingId) {
        if (userMappingId == null) {
            throw new IllegalArgumentException("userMappingId不能为空");
        }
        return authP3UserMappingMapper.select(userMappingId);
    }

    @Override
    public AuthP3UserMapping getByUserId(String p3ComponentId, String userId) {
        if (userId == null || p3ComponentId == null) {
            throw new IllegalArgumentException("userId、p3ComponentId不能为空");
        }
        return authP3UserMappingMapper.getByUserId(p3ComponentId, userId);
    }

    @Override
    public Map<String, Object> pageList(AuthP3UserMappingSelectVo authP3UserMappingSelectVo) {
        if (authP3UserMappingSelectVo.getPageSize() == null || authP3UserMappingSelectVo.getPageSize() == 0) {
            authP3UserMappingSelectVo.setPageSize(50);
        }
        List<AuthP3UserMapping> pageList;
        if (authP3UserMappingSelectVo.getOffset() == null || authP3UserMappingSelectVo.getOffset() < CommonConstant.OFFSET_MAX_FOR_SQL_BETTER) {
            pageList = authP3UserMappingMapper.pageList(authP3UserMappingSelectVo);
        } else {
            pageList = authP3UserMappingMapper.bigOffsetPageList(authP3UserMappingSelectVo);
        }
        int totalCount = authP3UserMappingMapper.pageListCount();
        Map<String, Object> result = new HashMap<>(2);
        result.put("pageList", pageList);
        result.put("totalCount", totalCount);
        return result;
    }

    @Override
    public Map<String, Object> searchList(AuthP3UserMappingSelectVo authP3UserMappingSelectVo) {
        if (authP3UserMappingSelectVo.getPageSize() == null || authP3UserMappingSelectVo.getPageSize() == 0) {
            authP3UserMappingSelectVo.setPageSize(50);
        }
        List<AuthP3UserMapping> pageList;
        pageList = authP3UserMappingMapper.searchList(authP3UserMappingSelectVo);
        Map<String, Object> result = new HashMap<>(2);
        result.put("pageList", pageList);
        result.put("totalCount", pageList.size());
        return result;
    }

    @Override
    public void batchInsert(List<AuthP3UserMapping> authP3UserMappings) {
        authP3UserMappingMapper.batchInsert(authP3UserMappings);
    }

    @Override
    public void batchDelete(List<String> userMappingIds) {
        authP3UserMappingMapper.batchDelete(userMappingIds);
    }
}
