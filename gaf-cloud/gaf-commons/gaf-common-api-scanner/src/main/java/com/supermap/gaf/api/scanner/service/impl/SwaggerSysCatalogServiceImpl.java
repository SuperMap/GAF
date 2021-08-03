/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.api.scanner.service.impl;

import com.supermap.gaf.api.scanner.dao.SwaggerSysCatalogMapper;
import com.supermap.gaf.api.scanner.entity.SysCatalog;
import com.supermap.gaf.api.scanner.entity.SysComponent;
import com.supermap.gaf.api.scanner.enums.CatalogTypeEnum;
import com.supermap.gaf.api.scanner.service.SwaggerSysCatalogService;
import com.supermap.gaf.data.access.service.BatchSortAndCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;


/**
 * @author wangxiaolong
 * @date:2021/3/25 目录服务类
 */
@Service
public class SwaggerSysCatalogServiceImpl implements SwaggerSysCatalogService {

    @Autowired
    private SwaggerSysCatalogMapper swaggerSysCatalogMapper;

    @Autowired
    private BatchSortAndCodeService batchSortAndCodeService;

    private static final String ROOT_PARENT_ID = "0";


    /**
     * 插入组件对应的根目录
     *
     * @param sysComponent 组件
     * @param catalogType  参考 CatalogTypeEnum 只能是CatalogTypeEnum.MODULE_GROUP_TYPE;CatalogTypeEnum.API_GROUP_TYPE
     * @return
     */
    @Override
    public boolean insertSysCatlog(SysComponent sysComponent, CatalogTypeEnum catalogType) {
        SysCatalog build = SysCatalog.builder()
                .parentId(ROOT_PARENT_ID)
                .type(catalogType.getValue())
                .sysComponentId(sysComponent.getSysComponentId())
                .name(sysComponent.getNameCn())
                .status(true)
                .description(sysComponent.getNameCn() + "默认根目录")
                .build();
        this.insertSysCatalog(build);
        batchSortAndCodeService.revisionSortSnForInsertOrDelete(SysCatalog.class, Arrays.asList(build.getParentId()));
        return true;
    }


    @Override
    public SysCatalog insertSysCatalog(SysCatalog sysCatalog) {
        if (sysCatalog.getSysComponentId() != null && sysCatalog.getTenantId() != null) {
            throw new RuntimeException("插入目录异常，目录数据不能同时包含组件id和租户id");
        }
        sysCatalog.setCatalogId(UUID.randomUUID().toString());
        SysCatalog queryCatalog = SysCatalog.builder().type(sysCatalog.getType()).parentId(sysCatalog.getParentId()).status(true).build();
        List<SysCatalog> sameLevels = swaggerSysCatalogMapper.selectByCombination(queryCatalog);
        if (sameLevels.size() <= 0) {
            sysCatalog.setSortSn(1);
        }
        // 新增一级目录 即目录类别
        if (!ROOT_PARENT_ID.equalsIgnoreCase(sysCatalog.getParentId())) {
            SysCatalog parentCatalog = swaggerSysCatalogMapper.selectByIdAndStatus(sysCatalog.getParentId(), true);
            if (parentCatalog == null) {
                throw new RuntimeException("未找到上级目录");
            }
            if (parentCatalog.getTenantId() != null) {
                sysCatalog.setTenantId(parentCatalog.getTenantId());
            }
            if (parentCatalog.getSysComponentId() != null) {
                sysCatalog.setSysComponentId(parentCatalog.getSysComponentId());
            }
            if (!StringUtils.isEmpty(parentCatalog.getBizTypeCode())) {
                sysCatalog.setBizTypeCode(parentCatalog.getBizTypeCode());
            }
            if (sameLevels.size() <= 0) {
                sysCatalog.setSortSn(1);
            }
        }
        if (sysCatalog.getStatus() == null) {
            sysCatalog.setStatus(true);
        }
        swaggerSysCatalogMapper.insert(sysCatalog);
        batchSortAndCodeService.revisionSortSnForInsertOrDelete(SysCatalog.class, Arrays.asList(sysCatalog.getParentId()));
        return sysCatalog;
    }


    @Override
    public List<SysCatalog> getByComponentAndType(String componentId, String type) {
        return swaggerSysCatalogMapper.getByComponentAndType(componentId, type);
    }


}
