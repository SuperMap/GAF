/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.webgis.service;

import com.supermap.gaf.webgis.domain.WebgisConfigData;
import com.supermap.gaf.webgis.domain.WebgisToolbarDo;
import com.supermap.gaf.webgis.vo.WebgisToolbarVo;

import java.util.List;
import java.util.Map;

/**
 * pro
 * 地图应用服务类
 *
 * @author zhurongcheng
 * @date 2020 -12-05
 */
public interface WebgisConfigService {
    Object parseConfig(WebgisToolbarVo toolbarVo);
    Object parseConfig(Object obj);
    WebgisConfigData convert2ToolbarConfig(List<WebgisToolbarDo> toolbarDos);
    WebgisConfigData convert2ResourceTreeConfig(String rootCatalogId);
    /**
     * 通过根目录id解析出资源目录树
     * @return
     */
    Map<String,Object> getResourceTree(String rootCatalogId);
}
