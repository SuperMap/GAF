/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.authority.service.impl;

import com.supermap.gaf.authority.commontype.AuthResourceApi;
import com.supermap.gaf.authority.constant.CommonConstant;
import com.supermap.gaf.authority.constant.DbFieldNameConstant;
import com.supermap.gaf.authority.dao.AuthResourceApiMapper;
import com.supermap.gaf.authority.service.AuthResourceApiService;
import com.supermap.gaf.authority.vo.AuthResourceApiSelectVo;
import com.supermap.gaf.data.access.service.BatchSortAndCodeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 接口(api)服务实现类
 *
 * @author zhm
 * @date:2021/3/25
 */
@Service
public class AuthResourceApiServiceImpl implements AuthResourceApiService {
    private final AuthResourceApiMapper authResourceApiMapper;
    private final BatchSortAndCodeService batchSortAndCodeService;

    public AuthResourceApiServiceImpl(AuthResourceApiMapper authResourceApiMapper, BatchSortAndCodeService batchSortAndCodeService) {
        this.authResourceApiMapper = authResourceApiMapper;
        this.batchSortAndCodeService = batchSortAndCodeService;
    }

    @Override
    public AuthResourceApi getById(String resourceApiId) {
        if (resourceApiId == null) {
            throw new IllegalArgumentException("resourceApiId不能为空");
        }
        return authResourceApiMapper.select(resourceApiId);
    }

    @Override
    public Map<String, Object> pageList(AuthResourceApiSelectVo authResourceApiSelectVo) {
        if (authResourceApiSelectVo.getPageSize() == null || authResourceApiSelectVo.getPageSize() == 0) {
            authResourceApiSelectVo.setPageSize(50);
        }
        List<AuthResourceApi> pageList;
        if (authResourceApiSelectVo.getOffset() == null || authResourceApiSelectVo.getOffset() < CommonConstant.OFFSET_MAX_FOR_SQL_BETTER) {
            pageList = authResourceApiMapper.pageList(authResourceApiSelectVo);
        } else {
            pageList = authResourceApiMapper.bigOffsetPageList(authResourceApiSelectVo);
        }
        int totalCount = authResourceApiMapper.pageListCount();
        Map<String, Object> result = new HashMap<>(2);
        result.put(DbFieldNameConstant.PAGE_LIST, pageList);
        result.put(DbFieldNameConstant.TOTAL_COUNT, totalCount);
        return result;
    }

    @Override
    public Map<String, Object> searchList(AuthResourceApiSelectVo authResourceApiSelectVo) {
        if (authResourceApiSelectVo.getPageSize() == null || authResourceApiSelectVo.getPageSize() == 0) {
            authResourceApiSelectVo.setPageSize(50);
        }
        List<AuthResourceApi> pageList;
        pageList = authResourceApiMapper.searchList(authResourceApiSelectVo);
        Integer totalCount = authResourceApiMapper.countOneField(authResourceApiSelectVo.getSearchFieldName(), authResourceApiSelectVo.getSearchFieldValue());
        Map<String, Object> result = new HashMap<>(2);
        result.put(DbFieldNameConstant.PAGE_LIST, pageList);
        result.put(DbFieldNameConstant.TOTAL_COUNT, totalCount);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AuthResourceApi insertAuthResourceApi(AuthResourceApi authResourceApi) {
        authResourceApi.setResourceApiId(UUID.randomUUID().toString());
        authResourceApiMapper.insert(authResourceApi);
        batchSortAndCodeService.revisionSortSnForInsertOrDelete(AuthResourceApi.class, Collections.singletonList(authResourceApi.getApiCatalogId()));
        return authResourceApi;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchInsert(List<AuthResourceApi> authResourceApis) {
        if (authResourceApis != null && authResourceApis.size() > 0) {
            Set<String> parentIds = new HashSet<>();
            authResourceApis.forEach(authResourceApi -> {
                authResourceApi.setResourceApiId(UUID.randomUUID().toString());
                parentIds.add(authResourceApi.getApiCatalogId());
            });
            authResourceApiMapper.batchInsert(authResourceApis);
            batchSortAndCodeService.revisionSortSnForInsertOrDelete(AuthResourceApi.class, parentIds);
        }

    }

    @Override
    public void deleteAuthResourceApi(String resourceApiId) {
        authResourceApiMapper.delete(resourceApiId);
    }

    @Override
    public void batchDelete(List<String> resourceApiIds) {
        authResourceApiMapper.batchDelete(resourceApiIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AuthResourceApi updateAuthResourceApi(AuthResourceApi authResourceApi) {
        AuthResourceApi oldAuthResourceApi = getById(authResourceApi.getResourceApiId());
        authResourceApiMapper.update(authResourceApi);
        String parentId = authResourceApi.getApiCatalogId() != null ? authResourceApi.getApiCatalogId() : oldAuthResourceApi.getApiCatalogId();
        batchSortAndCodeService.revisionSortSnForUpdate(AuthResourceApi.class, parentId, oldAuthResourceApi.getSortSn(), authResourceApi.getSortSn());
        return authResourceApi;
    }

    @Override
    public List<AuthResourceApi> listByCatalogId(String catalogId) {
        return authResourceApiMapper.listByCatalogId(catalogId);
    }

    @Override
    public Integer countByApiCatalogId(String apiCatalogId) {
        return authResourceApiMapper.countByCombination(AuthResourceApi.builder().apiCatalogId(apiCatalogId).status(true).build());
    }


    @Override
    public List<AuthResourceApi> list(AuthResourceApi query) {
        return authResourceApiMapper.selectByCombination(query);
    }

}
