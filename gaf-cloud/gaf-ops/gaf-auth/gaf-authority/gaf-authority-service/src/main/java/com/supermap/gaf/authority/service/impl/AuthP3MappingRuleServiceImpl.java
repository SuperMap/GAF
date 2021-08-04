/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.authority.service.impl;

import com.supermap.gaf.authority.commontype.AuthP3MappingRule;
import com.supermap.gaf.authority.commontype.SysComponent;
import com.supermap.gaf.authority.constant.CommonConstant;
import com.supermap.gaf.authority.constant.DbFieldNameConstant;
import com.supermap.gaf.authority.dao.AuthP3MappingRuleMapper;
import com.supermap.gaf.authority.enums.AuthP3MappingRuleTypeEnum;
import com.supermap.gaf.authority.service.*;
import com.supermap.gaf.authority.vo.AuthP3MappingRuleSelectVo;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 第三方映射规则服务实现类
 *
 * @author yangdong
 * @date:2021/3/25
 */
@Service
public class AuthP3MappingRuleServiceImpl implements AuthP3MappingRuleService {
    private final AuthP3MappingRuleMapper authP3MappingRuleMapper;
    private final SysComponentService sysComponentService;
    private final AuthP3TenantMappingService authP3TenantMappingService;
    private final AuthP3DepartmentMappingService authP3DepartmentMappingService;
    private final AuthP3UserMappingService authP3UserMappingService;

    public AuthP3MappingRuleServiceImpl(AuthP3MappingRuleMapper authP3MappingRuleMapper, SysComponentService sysComponentService, AuthP3TenantMappingService authP3TenantMappingService, AuthP3DepartmentMappingService authP3DepartmentMappingService, AuthP3UserMappingService authP3UserMappingService) {
        this.authP3MappingRuleMapper = authP3MappingRuleMapper;
        this.sysComponentService = sysComponentService;
        this.authP3TenantMappingService = authP3TenantMappingService;
        this.authP3DepartmentMappingService = authP3DepartmentMappingService;
        this.authP3UserMappingService = authP3UserMappingService;
    }

    @Override
    public AuthP3MappingRule insertAuthP3MappingRule(AuthP3MappingRule authP3MappingRule) {

        if (authP3MappingRuleMapper.select(authP3MappingRule.getMappingRuleId()) != null) {
            throw new IllegalArgumentException("resource已经存在：" + authP3MappingRule.getMappingRuleId());
        }
        authP3MappingRule.setMappingRuleId(UUID.randomUUID().toString());
        authP3MappingRuleMapper.insert(authP3MappingRule);
        return authP3MappingRule;
    }


    @Override
    public void deleteAuthP3MappingRule(String mappingRuleId) {
        selectById(mappingRuleId);
        authP3MappingRuleMapper.delete(mappingRuleId);
    }


    @Override
    public AuthP3MappingRule updateAuthP3MappingRule(AuthP3MappingRule authP3MappingRule) {
        selectById(authP3MappingRule.getMappingRuleId());
        authP3MappingRuleMapper.update(authP3MappingRule);
        return authP3MappingRule;
    }


    @Override
    public AuthP3MappingRule selectById(String mappingRuleId) {
        if (mappingRuleId == null) {
            throw new IllegalArgumentException("mappingRuleId不能为空");
        }
        return authP3MappingRuleMapper.select(mappingRuleId);
    }

    @Override
    public Map<String, Object> pageList(AuthP3MappingRuleSelectVo authP3MappingRuleSelectVo) {
        if (authP3MappingRuleSelectVo.getPageSize() == null || authP3MappingRuleSelectVo.getPageSize() == 0) {
            authP3MappingRuleSelectVo.setPageSize(50);
        }
        List<AuthP3MappingRule> pageList;
        if (authP3MappingRuleSelectVo.getOffset() == null || authP3MappingRuleSelectVo.getOffset() < CommonConstant.OFFSET_MAX_FOR_SQL_BETTER) {
            pageList = authP3MappingRuleMapper.pageList(authP3MappingRuleSelectVo);
        } else {
            pageList = authP3MappingRuleMapper.bigOffsetPageList(authP3MappingRuleSelectVo);
        }

        if (null != pageList) {
            pageList.forEach(authP3MappingRule -> {
                SysComponent sysComponent = sysComponentService.getById(authP3MappingRule.getP3ComponentId());
                if (null != sysComponent) {
                    authP3MappingRule.setP3ComponentName(sysComponent.getNameCn());
                }
            });
        }
        int totalCount = authP3MappingRuleMapper.pageListCount();
        Map<String, Object> result = new HashMap<>(2);
        result.put("pageList", pageList);
        result.put("totalCount", totalCount);
        return result;
    }

    @Override
    public Map<String, Object> searchList(AuthP3MappingRuleSelectVo authP3MappingRuleSelectVo) {
        if (authP3MappingRuleSelectVo.getPageSize() == null || authP3MappingRuleSelectVo.getPageSize() == 0) {
            authP3MappingRuleSelectVo.setPageSize(50);
        }
        List<AuthP3MappingRule> pageList = authP3MappingRuleMapper.searchList(authP3MappingRuleSelectVo);
        if (null != pageList) {
            pageList.forEach(authP3MappingRule -> {
                SysComponent sysComponent = sysComponentService.getById(authP3MappingRule.getP3ComponentId());
                if (null != sysComponent) {
                    authP3MappingRule.setP3ComponentName(sysComponent.getNameCn());
                }
            });
        }
        Map<String, Object> result = new HashMap<>(2);
        result.put("pageList", pageList);
        if (pageList != null) {
            result.put("totalCount", pageList.size());
        }
        return result;
    }

    @Override
    public List<AuthP3MappingRule> listByMappingType(String mappingType, String id) {
        List<AuthP3MappingRule> authP3MappingRules = authP3MappingRuleMapper.listByOneField(DbFieldNameConstant.P3_MAPPING_TYPE, mappingType);
        if (!CollectionUtils.isEmpty(authP3MappingRules)) {
            for (AuthP3MappingRule authP3MappingRule : authP3MappingRules) {
                SysComponent sysComponent = sysComponentService.getById(authP3MappingRule.getP3ComponentId());
                if (null != sysComponent) {
                    authP3MappingRule.setP3ComponentName(sysComponent.getNameCn());
                }
                Object saved = null;
                if (AuthP3MappingRuleTypeEnum.TENANT.getValue().equals(mappingType)) {
                    saved = authP3TenantMappingService.getByTenantId(authP3MappingRule.getP3ComponentId(), id);
                } else if (AuthP3MappingRuleTypeEnum.DEPARTMENT.getValue().equals(mappingType)) {
                    saved = authP3DepartmentMappingService.getByDepartmentId(authP3MappingRule.getP3ComponentId(), id);
                } else if (AuthP3MappingRuleTypeEnum.USER.getValue().equals(mappingType)) {
                    saved = authP3UserMappingService.getByUserId(authP3MappingRule.getP3ComponentId(), id);
                }
                if (null != saved) {
                    authP3MappingRule.setMapped(Boolean.TRUE);
                }
            }
        }
        return authP3MappingRules;
    }

    /**
     * 批量插入
     *
     * @author yangdong
     **/
    @Override
    public void batchInsert(List<AuthP3MappingRule> authP3MappingRules) {
        authP3MappingRuleMapper.batchInsert(authP3MappingRules);
    }

    /**
     * 批量删除
     *
     * @author yangdong
     **/
    @Override
    public void batchDelete(List<String> mappingRuleIds) {
        authP3MappingRuleMapper.batchDelete(mappingRuleIds);
    }
}
