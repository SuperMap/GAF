/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.authority.service.impl;

import com.supermap.gaf.authority.commontype.AuthResourceModule;
import com.supermap.gaf.authority.constant.CommonConstant;
import com.supermap.gaf.authority.dao.AuthResourceModuleMapper;
import com.supermap.gaf.authority.enums.NodeTypeEnum;
import com.supermap.gaf.authority.service.AuthResourceModuleService;
import com.supermap.gaf.authority.vo.AuthResourceModuleSelectVo;
import com.supermap.gaf.authority.vo.TreeNode;
import com.supermap.gaf.exception.GafException;
import com.supermap.gaf.sys.mgt.service.SysCatalogService;
import com.supermap.gaf.data.access.service.BatchSortAndCodeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 模块服务实现类
 *
 * @author zhm
 * @date:2021/3/25
 */
@Service
public class AuthResourceModuleServiceImpl implements AuthResourceModuleService {
    private final AuthResourceModuleMapper authResourceModuleMapper;

    private final SysCatalogService sysCatalogService;
    private final BatchSortAndCodeService batchSortAndCodeService;

    public AuthResourceModuleServiceImpl(AuthResourceModuleMapper authResourceModuleMapper, SysCatalogService sysCatalogService, BatchSortAndCodeService batchSortAndCodeService) {
        this.authResourceModuleMapper = authResourceModuleMapper;
        this.sysCatalogService = sysCatalogService;
        this.batchSortAndCodeService = batchSortAndCodeService;
    }

    @Override
    public AuthResourceModule getById(String resourceModuleId) {
        if (resourceModuleId == null) {
            throw new GafException("resourceModuleId不能为空");
        }
        return authResourceModuleMapper.select(resourceModuleId);
    }

    @Override
    public List<TreeNode> getModuleNodes() {
        List<AuthResourceModule> authResourceModules = authResourceModuleMapper.listModules();
        List<TreeNode> collect = authResourceModules.stream().map(module -> {
            TreeNode node = new TreeNode();
            node.setTitle(module.getName());
            node.setSortSn(module.getSortSn());
            node.setParentId(module.getModuleCatalogId());
            node.setKey(module.getResourceModuleId());
            node.setType(NodeTypeEnum.MODULE.getValue());
            return node;
        }).collect(Collectors.toList());
        List<TreeNode> moduleCatalogs = sysCatalogService.getNodesByType("1");
        collect.addAll(moduleCatalogs);
        return collect;
    }

    @Override
    public Map<String, Object> pageList(AuthResourceModuleSelectVo authResourceModuleSelectVo) {
        if (authResourceModuleSelectVo.getPageSize() == null || authResourceModuleSelectVo.getPageSize() == 0) {
            authResourceModuleSelectVo.setPageSize(50);
        }
        List<AuthResourceModule> pageList;
        if (authResourceModuleSelectVo.getOffset() == null || authResourceModuleSelectVo.getOffset() < CommonConstant.OFFSET_MAX_FOR_SQL_BETTER) {
            pageList = authResourceModuleMapper.pageList(authResourceModuleSelectVo);
        } else {
            pageList = authResourceModuleMapper.bigOffsetPageList(authResourceModuleSelectVo);
        }
        int totalCount = authResourceModuleMapper.pageListCount();
        Map<String, Object> result = new HashMap<>(2);
        result.put("pageList", pageList);
        result.put("totalCount", totalCount);
        return result;
    }

    @Override
    public Map<String, Object> searchList(AuthResourceModuleSelectVo authResourceModuleSelectVo) {
        if (authResourceModuleSelectVo.getPageSize() == null || authResourceModuleSelectVo.getPageSize() == 0) {
            authResourceModuleSelectVo.setPageSize(50);
        }
        List<AuthResourceModule> pageList;
        pageList = authResourceModuleMapper.searchList(authResourceModuleSelectVo);
        Integer totalCount = authResourceModuleMapper.countOneField(authResourceModuleSelectVo.getSearchFieldName(), authResourceModuleSelectVo.getSearchFieldValue());
        Map<String, Object> result = new HashMap<>(2);
        result.put("pageList", pageList);
        result.put("totalCount", totalCount);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AuthResourceModule insertAuthResourceModule(AuthResourceModule authResourceModule) {
        authResourceModule.setResourceModuleId(UUID.randomUUID().toString());
        authResourceModuleMapper.insert(authResourceModule);
        batchSortAndCodeService.revisionSortSnForInsertOrDelete(AuthResourceModule.class, Collections.singletonList(authResourceModule.getModuleCatalogId()));
        return authResourceModule;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchInsert(List<AuthResourceModule> authResourceModules) {
        if (authResourceModules != null && authResourceModules.size() > 0) {
            Set<String> parentIds = new HashSet<>();
            authResourceModules.forEach(authResourceModule -> {
                authResourceModule.setResourceModuleId(UUID.randomUUID().toString());
                parentIds.add(authResourceModule.getModuleCatalogId());
            });
            authResourceModuleMapper.batchInsert(authResourceModules);
            batchSortAndCodeService.revisionSortSnForInsertOrDelete(AuthResourceModule.class, parentIds);
        }

    }

    @Override
    public void deleteAuthResourceModule(String resourceModuleId) {
        authResourceModuleMapper.delete(resourceModuleId);
    }

    @Override
    public void batchDelete(List<String> resourceModuleIds) {
        authResourceModuleMapper.batchDelete(resourceModuleIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AuthResourceModule updateAuthResourceModule(AuthResourceModule authResourceModule) {
        AuthResourceModule oldAuthResourceModule = getById(authResourceModule.getResourceModuleId());
        authResourceModuleMapper.update(authResourceModule);
        String parentId = authResourceModule.getResourceModuleId() != null ? authResourceModule.getResourceModuleId() : oldAuthResourceModule.getResourceModuleId();
        batchSortAndCodeService.revisionSortSnForUpdate(AuthResourceModule.class, parentId, oldAuthResourceModule.getSortSn(), authResourceModule.getSortSn());
        return authResourceModule;
    }

    @Override
    public Integer countByModuleCatalogId(String moduleCatalogId) {
        return authResourceModuleMapper.countByCombination(AuthResourceModule.builder().moduleCatalogId(moduleCatalogId).status(true).build());
    }

}
