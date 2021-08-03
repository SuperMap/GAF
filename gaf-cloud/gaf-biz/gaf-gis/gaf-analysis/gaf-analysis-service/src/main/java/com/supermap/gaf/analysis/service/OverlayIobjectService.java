/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.analysis.service;

import com.supermap.gaf.analysis.entity.OverlayAnalystParamVO;
import com.supermap.services.components.commontypes.Recordset;

/**
 * @author dqc
 * @date:2021/3/25
 * @since : 2021-03-25
 */
public interface OverlayIobjectService {

    /**
     * 专题分析
     *
     * @param overlayAnalystParamVO
     * @return
     */
    Recordset overlayViaIobject(OverlayAnalystParamVO overlayAnalystParamVO);

}
