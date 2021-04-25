/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.data.mgt.service;

import com.supermap.gaf.commontypes.MessageResult;
import com.supermap.gaf.data.mgt.entity.MapTemplate;

import java.util.Map;

/**
 * @program: gaf-datacenter-modules
 * @description: 地图模板管理
 * @author: lidong
 * @date:2021/3/25
 * @create: 2019/09/05
 */
public interface MapTemplateService {

    /**
     * 获取默认地图模板
     *
     * @return
     */
    Map<String, MapTemplate> getDefaultMapTemplate();

    /**
     * 根据资源地址获取地图模板内容
     *
     * @param mapTemplateUrl
     * @return
     */
    MessageResult<MapTemplate> getMapTemplateContentByUrl(String mapTemplateUrl);


}
