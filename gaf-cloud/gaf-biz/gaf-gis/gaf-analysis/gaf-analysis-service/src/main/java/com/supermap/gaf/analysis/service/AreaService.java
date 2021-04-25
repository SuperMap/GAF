/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.analysis.service;

import com.supermap.data.DatasourceConnectionInfo;
import com.supermap.services.components.commontypes.Geometry;

import java.util.List;

/**
 * @author : duke
 * @date:2021/3/25
 * @since 2020/9/2 8:38 AM
 */
public interface AreaService {

    /**
     * 获取指定数据集指定id的地理对象
     *
     * @param connectionInfo
     * @param dataSetName
     * @param id
     * @return
     */
    Geometry getGeometryById(DatasourceConnectionInfo connectionInfo,
                             String dataSetName,
                             String id);

    /**
     * 获取指定数据集总面积
     *
     * @param connectionInfo
     * @param dataSetName
     * @return
     */
    String calculateDataSetArea(DatasourceConnectionInfo connectionInfo, String dataSetName);

    /**
     * 获取geo的面积
     *
     * @param ugoGeometries
     * @return
     */
    String getGeometryArea(List<com.supermap.data.Geometry> ugoGeometries);
}
