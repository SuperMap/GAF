/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.analysis.dao;


import com.supermap.gaf.analysis.entity.SpatialAnalysisResult;
import com.supermap.gaf.analysis.entity.SpatialAnalysisResultSearchParameter;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 空间分析结果数据访问类
 *
 * @author
 * @date yyyy-mm-dd
 */
@Component
public interface SpatialAnalysisResultDao {

    /**
     * 主键查询
     * @param resultId
     * @return
     */
    SpatialAnalysisResult getById(String resultId);

    /**
     * 分页查询分析结果
     * @param searchParameter
     * @return
     */
    List<SpatialAnalysisResult> querySpatialAnalysisResults(SpatialAnalysisResultSearchParameter searchParameter);

    /**
     * 数据总条数
     * @return
     */
    int pageListCount();

    /**
     * 新增
     * @param spatialAnalysisResult
     * @return
     */
    boolean addSpatialAnalysisResult(SpatialAnalysisResult spatialAnalysisResult);

}
