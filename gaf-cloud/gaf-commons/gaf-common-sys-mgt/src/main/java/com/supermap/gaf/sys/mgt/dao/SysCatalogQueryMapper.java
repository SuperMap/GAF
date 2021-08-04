/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.sys.mgt.dao;

import com.supermap.gaf.sys.mgt.commontype.SysCatalog;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author wangxiaolong
 * @date:2021/3/25
 */
@Component
public interface SysCatalogQueryMapper {

    /**
     * 组合条件等值查询
     * 注意： queryCatalog 至少有一个属性不为null
     *
     * @param queryCatalog 查询参数
     * @return 查询结果
     */
    List<SysCatalog> selectByCombination(SysCatalog queryCatalog);

    /**
     * 根据某个结点以它为根结点的所有层次的子结点
     *
     * @param rootId
     * @return
     */
    List<SysCatalog> getCatalogTreeListByRootId(@Param("rootId") String rootId);

}
