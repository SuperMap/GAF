/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.sys.mgt.service;

import com.supermap.gaf.sys.mgt.commontype.SysCatalog;

import java.util.List;

/**
 * @author wangxiaolong
 * @date:2021/3/25
 * 目录服务类
 */
public interface SysCatalogQueryService {

    /**
     * 通过类型查询有效目录
     * 注意返回的目录包含组件一一对应的名字相同的根目录
     * 或者返回的目录包含租户一一对应的名字相同的根目录
     * @param tenantId
     * @param type 目录类型 参考CatalogTypeEnum
     * @return 若未查询到则返回空集合
     */
    List<SysCatalog> getByType(String tenantId, String type);

    /**
     * 根据某个结点以它为根结点的所有层次的子结点
     * @param rootCatalogId
     * @return
     */
    List<SysCatalog> getCatalogTreeListByRootId(String rootCatalogId);

}
