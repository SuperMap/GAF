/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.data.mgt.service;

import com.supermap.gaf.data.mgt.entity.DataSourceInfo;
import com.supermap.gaf.data.mgt.entity.GDataset;

import java.util.List;

/**
 * @author:yd
 * @date:2021/3/25
 * @Date 2021-3-01
 */
public interface DataSourceService {

    /**
     * 获取数据源下所有数据集列表
     *
     * @param dataSourceInfo
     * @return
     */
    List<GDataset> listDataset(DataSourceInfo dataSourceInfo);

    /**
     * 根据数据源id获取数据源连接信息，然后获取数据源下的数据集
     *
     * @param datasourceId 数据源id
     * @return 数据集集合
     */
    List<GDataset> listDataset(String datasourceId);
}
