/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.api.scanner.service;

import com.supermap.gaf.api.scanner.entity.SysCatalog;
import com.supermap.gaf.api.scanner.entity.SysComponent;
import com.supermap.gaf.api.scanner.enums.CatalogTypeEnum;

import java.util.List;

/**
 * @author wangxiaolong
 * @date:2021/3/25
 * 目录服务类
 */
public interface SwaggerSysCatalogService {
	

    /**
     * 插入组件对应的根目录
     * @param sysComponent 组件
     * @param catalogType 参考 CatalogTypeEnum 只能是CatalogTypeEnum.MODULE_GROUP_TYPE;CatalogTypeEnum.API_GROUP_TYPE
     * @return
     */
    boolean insertSysCatlog(SysComponent sysComponent, CatalogTypeEnum catalogType);

    /**
     * 新增目录
     * @param sysCatalog 目录
     * @return
     */
    SysCatalog insertSysCatalog(SysCatalog sysCatalog);

    /**
     * 通过component和type获取目录
     * @param componentId
     * @param type
     * @return
     */
    List<SysCatalog> getByComponentAndType(String componentId, String type);

}
