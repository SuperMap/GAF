/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.data.mgt.service;

import com.supermap.gaf.commontypes.MessageResult;
import com.supermap.gaf.data.mgt.entity.DataSourceInfo;

import java.util.List;

/**
 * @author:yd
 * @date:2021/3/25
 * @Date 2021-3-01
 */
public interface S3MCacheService {

    /**
     * 生成三维切片缓存（S3M）
     *
     * @param dataSourceInfo 数据源信息
     * @param datasetNames   待切片的数据集列表，数据集类型必须为模型数据集
     * @param isOverWrite    是否覆盖，true：删除所有已存在的缓存
     * @param outputFolder   切片结果存放路径，如果为空，则按数据源名称管理目录
     * @return
     */
    MessageResult<String> buildS3M(DataSourceInfo dataSourceInfo, List<String> datasetNames, boolean isOverWrite, String outputFolder);

}
