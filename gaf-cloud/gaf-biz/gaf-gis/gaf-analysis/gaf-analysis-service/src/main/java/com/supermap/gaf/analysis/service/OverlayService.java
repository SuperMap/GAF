/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.analysis.service;

import com.supermap.gaf.analysis.entity.RecordSetVO;
import com.supermap.gaf.analysis.entity.OverlayParamVO;
import com.supermap.gaf.commontypes.MessageResult;

/**
 * @author dqc
 * @date:2021/3/25
 * @since : 2020-12-29
 */
public interface OverlayService {

    /**
     * 专题分析
     *
     * @param overlayParamVO
     * @return
     */
    MessageResult<String> overlay(OverlayParamVO overlayParamVO);

    /**
     * 获取专题分析结果
     *
     * @param resultId
     * @param pageIndex
     * @param pageSize
     * @return
     */
    MessageResult<RecordSetVO> getResultRecordSet(String resultId, Integer pageIndex, Integer pageSize);
}
