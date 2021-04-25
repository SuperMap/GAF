/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.analysis.spi;

import com.supermap.gaf.analysis.entity.OverlayParamVO;
import com.supermap.gaf.analysis.entity.RecordSetVO;
import com.supermap.gaf.analysis.entity.SpatialAnalysisResult;
import com.supermap.gaf.commontypes.MessageResult;

/**
 * @author : yd
 * @date:2021/3/25
 * @since : 2020-12-29
 */
public interface IspatialOverlay {

    /**
     * 支持的空间分析服务类型
     *
     * @return
     */
    String type();

    /**
     * 校验参数合法性
     *
     * @param overlayParamVO
     * @return
     */
    MessageResult<String> verifyParam(OverlayParamVO overlayParamVO);

    /**
     * 叠加分析实现
     *
     * @param resultId
     * @param overlayParamVO
     * @return
     */
    MessageResult<SpatialAnalysisResult> overlay(String resultId, OverlayParamVO overlayParamVO);

    /**
     * 获取分析结果
     *
     * @param spatialAnalysisResult
     * @param pageIndex
     * @param pageSize
     * @return
     */
    MessageResult<RecordSetVO> getResultRecordSet(SpatialAnalysisResult spatialAnalysisResult, Integer pageIndex, Integer pageSize);

}
