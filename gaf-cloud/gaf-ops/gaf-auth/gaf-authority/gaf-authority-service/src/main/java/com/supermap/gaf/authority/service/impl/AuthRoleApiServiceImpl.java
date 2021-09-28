/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.authority.service.impl;

import com.supermap.gaf.authority.commontype.AuthRoleApi;
import com.supermap.gaf.authority.constant.CacheGroupConstant;
import com.supermap.gaf.authority.constant.CommonConstant;
import com.supermap.gaf.authority.constant.DbFieldNameConstant;
import com.supermap.gaf.authority.dao.AuthRoleApiMapper;
import com.supermap.gaf.authority.service.AuthRoleApiService;
import com.supermap.gaf.authority.service.AuthRoleService;
import com.supermap.gaf.authority.vo.AuthRoleApiSelectVo;
import com.supermap.gaf.authority.vo.RoleApiVo;
import com.supermap.gaf.exception.GafException;
import com.supermap.gaf.utils.LogUtil;
import org.slf4j.Logger;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 角色接口服务实现类
 *
 * @author yangdong
 * @date:2021/3/25
 */
@Service
public class AuthRoleApiServiceImpl implements AuthRoleApiService {
    private final AuthRoleApiMapper authRoleApiMapper;

    private final AuthRoleService authRoleService;

    private static final Logger logger = LogUtil.getLocLogger(AuthRoleApiServiceImpl.class);

    public AuthRoleApiServiceImpl(AuthRoleApiMapper authRoleApiMapper, AuthRoleService authRoleService) {
        this.authRoleApiMapper = authRoleApiMapper;
        this.authRoleService = authRoleService;
    }

    @Override
    public AuthRoleApi getById(String roleApiId) {
        if (StringUtils.isEmpty(roleApiId)) {
            throw new GafException("roleApiId不能为空");
        }
        return authRoleApiMapper.select(roleApiId);
    }

    @Cacheable(value = CacheGroupConstant.ROLE_API, key = "#roleId", unless = "#result == null")
    @Override
    public List<AuthRoleApi> listByRole(String roleId) {
        if (StringUtils.isEmpty(roleId)) {
            throw new GafException("roleId不能为空");
        }
        return authRoleApiMapper.listByRole(roleId);
    }

    @Override
    public List<AuthRoleApi> getByRoleId(String roleId, Boolean status) {
        if (StringUtils.isEmpty(roleId)) {
            return new ArrayList<>();
        }
        return authRoleApiMapper.getByRoleId(roleId, status);
    }

    @CacheEvict(value = CacheGroupConstant.ROLE_API, key = "#roleApiVo.roleId")
    @Override
    public void handleRoleApi(RoleApiVo roleApiVo) {
        String roleId = roleApiVo.getRoleId();
        if (roleId == null || StringUtils.isEmpty(roleId)) {
            throw new GafException("角色id为空");
        }
        logger.info(roleId);

        if (authRoleService.getById(roleId) == null) {
            throw new GafException("角色不存在：" + roleId);
        }
        List<String> newList = roleApiVo.getApiList();
        List<String> oldList = getByRoleId(roleId, true).stream().map(AuthRoleApi::getResourceApiId).collect(Collectors.toList());
        List<String> addList = new ArrayList<>();
        List<String> removeList = new ArrayList<>();
        newList.forEach(item -> {
            if (!oldList.contains(item)) {
                addList.add(item);
            }
        });
        logger.info("toadd:");
        oldList.forEach(item -> {
            if (!newList.contains(item)) {
                removeList.add(item);
            }
        });
        logger.info("toremove:");
        //新增或修改
        if (!CollectionUtils.isEmpty(addList)) {
            List<AuthRoleApi> addRoleApiList = new ArrayList<>();
            List<String> updateList = new ArrayList<>();
            addList.forEach(item -> {
                AuthRoleApi oldRoleApi = getByRoleAndApi(roleId, item, false);
                if (oldRoleApi != null) {
                    updateList.add(oldRoleApi.getRoleApiId());
                } else {
                    AuthRoleApi authRoleApi = AuthRoleApi.builder()
                            .resourceApiId(item)
                            .roleId(roleId)
                            .status(true)
                            .sortSn(1)
                            .build();
                    addRoleApiList.add(authRoleApi);
                }
            });
            //批量修改
            if (!CollectionUtils.isEmpty(updateList)) {
                batchUpdate(updateList);
            }
            //批量新增
            if (!CollectionUtils.isEmpty(addRoleApiList)) {
                batchInsertRoleApi(addRoleApiList);
            }
        }

        //禁用
        if (!CollectionUtils.isEmpty(removeList)) {
            //根据角色id和接口id获取角色接口关联id
            List<String> roleApiIds = new ArrayList<>();
            removeList.forEach(item -> {
                AuthRoleApi authRoleApi = getByRoleAndApi(roleId, item, true);
                if (authRoleApi != null) {
                    roleApiIds.add(authRoleApi.getRoleApiId());
                }
            });
            batchDelete(roleApiIds);
        }
    }

    public AuthRoleApi getByRoleAndApi(String roleId, String apiId, Boolean status) {
        if (StringUtils.isEmpty(roleId) || StringUtils.isEmpty(apiId)) {
            return null;
        }
        return authRoleApiMapper.getByRoleAndApi(roleId, apiId, status);
    }

    @Override
    public Map<String, Object> pageList(AuthRoleApiSelectVo authRoleApiSelectVo) {
        if (authRoleApiSelectVo.getPageSize() == null || authRoleApiSelectVo.getPageSize() == 0) {
            authRoleApiSelectVo.setPageSize(50);
        }
        List<AuthRoleApi> pageList;
        if (authRoleApiSelectVo.getOffset() == null || authRoleApiSelectVo.getOffset() < CommonConstant.OFFSET_MAX_FOR_SQL_BETTER) {
            pageList = authRoleApiMapper.pageList(authRoleApiSelectVo);
        } else {
            pageList = authRoleApiMapper.bigOffsetPageList(authRoleApiSelectVo);
        }
        int totalCount = authRoleApiMapper.pageListCount();
        Map<String, Object> result = new HashMap<>(2);
        result.put(DbFieldNameConstant.PAGE_LIST, pageList);
        result.put(DbFieldNameConstant.TOTAL_COUNT, totalCount);
        return result;
    }

    @Override
    public Map<String, Object> searchList(AuthRoleApiSelectVo authRoleApiSelectVo) {
        if (authRoleApiSelectVo.getPageSize() == null || authRoleApiSelectVo.getPageSize() == 0) {
            authRoleApiSelectVo.setPageSize(50);
        }
        List<AuthRoleApi> pageList = authRoleApiMapper.searchList(authRoleApiSelectVo);
        Integer totalCount = authRoleApiMapper.countOneField(authRoleApiSelectVo.getSearchFieldName(), authRoleApiSelectVo.getSearchFieldValue());
        Map<String, Object> result = new HashMap<>(2);
        result.put(DbFieldNameConstant.PAGE_LIST, pageList);
        result.put(DbFieldNameConstant.TOTAL_COUNT, totalCount);
        return result;
    }

    @Override
    public void batchInsertRoleApi(List<AuthRoleApi> authRoleApis) {
        if (authRoleApis != null && !CollectionUtils.isEmpty(authRoleApis)) {
            authRoleApis.forEach(authRoleApi -> authRoleApi.setRoleApiId(UUID.randomUUID().toString()));
            authRoleApiMapper.batchInsert(authRoleApis);
        }

    }

    @Override
    public void batchDelete(List<String> roleApiIds) {
        authRoleApiMapper.batchDelete(roleApiIds);
    }


    @Override
    public int deleteByApiIds(List<String> apiIds) {
        return authRoleApiMapper.deleteByApiIds(apiIds);
    }

    /**
     * 批量修改
     *
     * @author zhm
     **/
    public void batchUpdate(List<String> authRoleApiIds) {
        authRoleApiMapper.batchUpdate(authRoleApiIds);
    }


}
