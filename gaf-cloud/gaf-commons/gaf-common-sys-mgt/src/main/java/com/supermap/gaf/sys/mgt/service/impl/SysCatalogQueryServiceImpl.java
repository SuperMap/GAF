/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.sys.mgt.service.impl;

import com.supermap.gaf.sys.mgt.commontype.SysCatalog;
import com.supermap.gaf.sys.mgt.dao.SysCatalogQueryMapper;
import com.supermap.gaf.sys.mgt.enums.CatalogTypeEnum;
import com.supermap.gaf.sys.mgt.service.SysCatalogQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wangxiaolong
 * @date:2021/3/25
 * 目录服务类
 */
@Service
public class SysCatalogQueryServiceImpl implements SysCatalogQueryService {

    @Autowired
    private SysCatalogQueryMapper sysCatalogQueryMapper;

    @Override
    public List<SysCatalog> getByType(String tenantId, String type) {
        SysCatalog querySysCatalog;
        if (CatalogTypeEnum.ROLE_GROUP_TYPE.getValue().equals(type)) {
            // 若没查到或者判断为平台管理员 则认为是平台级 若有租户则查询租户的所有分类目录
            querySysCatalog = SysCatalog.builder().type(type).tenantId(tenantId).status(true).build();
        } else {
            querySysCatalog = SysCatalog.builder().type(type).status(true).build();
        }
        return sysCatalogQueryMapper.selectByCombination(querySysCatalog);
    }

    @Override
    public List<SysCatalog> getCatalogTreeListByRootId(String rootCatalogId){
        return sysCatalogQueryMapper.getCatalogTreeListByRootId(rootCatalogId);
    }

}
