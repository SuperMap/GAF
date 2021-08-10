/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.webgis.service.impl;

import com.alibaba.fastjson.JSON;
import com.supermap.gaf.exception.GafException;
import com.supermap.gaf.sys.mgt.commontype.SysCatalog;
import com.supermap.gaf.sys.mgt.service.SysCatalogQueryService;
import com.supermap.gaf.utils.AppConfigParser;
import com.supermap.gaf.webgis.domain.WebgisConfigData;
import com.supermap.gaf.webgis.domain.WebgisToolbarButtonDo;
import com.supermap.gaf.webgis.domain.WebgisToolbarDo;
import com.supermap.gaf.webgis.entity.WebgisCatalogLayer;
import com.supermap.gaf.webgis.entity.WebgisToolbarButton;
import com.supermap.gaf.webgis.enums.PositionTypeEnum;
import com.supermap.gaf.webgis.service.WebgisCatalogLayerService;
import com.supermap.gaf.webgis.service.WebgisConfigService;
import com.supermap.gaf.webgis.service.WebgisToolbarService;
import com.supermap.gaf.webgis.util.BeanUtils;
import com.supermap.gaf.webgis.vo.WebgisToolbarVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * pro
 * 地图应用运行配置相关方法
 *
 * @author zhurongcheng
 * @date 2020 -12-05
 */
@Service
public class WebgisConfigServiceImpl implements WebgisConfigService {
    /**
     * The constant BASIC_TYPE.
     */
    public final static String BASIC_TYPE = "1";
    @Autowired
    private WebgisToolbarService webgisToolbarService;
    @Autowired
    private WebgisCatalogLayerService webgisCatalogLayerService;
    @Autowired
    private SysCatalogQueryService sysCatalogQueryService;

    @Override
    public Object parseConfig(WebgisToolbarVo toolbarVo) {
        WebgisToolbarDo toolbarDo = new WebgisToolbarDo();
        BeanUtils.copyProperties(toolbarVo, toolbarDo);
        List<WebgisToolbarButton> buttons = toolbarVo.getWebgisToolbarButtons();
        if (buttons != null) {
            toolbarDo.setWebgisToolbarButtonDos(buttons.stream().map(button -> {
                WebgisToolbarButtonDo buttonDo = new WebgisToolbarButtonDo();
                BeanUtils.copyProperties(button, buttonDo);
                return buttonDo;
            }).collect(Collectors.toList()));
        }

        return parseConfig(toolbarDo);
    }

    /**
     * 通过根目录id解析出资源目录树配置
     *
     * @return
     */
    @Override
    public WebgisConfigData convert2ResourceTreeConfig(String rootCatalogId) {
        WebgisConfigData re = new WebgisConfigData();
        Map<String, Object> resourceTree = getResourceTree(rootCatalogId);
        re.setResourceTree(resourceTree);
        return re;
    }

    /**
     * 通过根目录id解析出资源目录树
     *
     * @return
     */
    @Override
    public Map<String, Object> getResourceTree(String rootCatalogId) {
        if (!StringUtils.isEmpty(rootCatalogId)) {
            List<SysCatalog> sysCatalogs = sysCatalogQueryService.getCatalogTreeListByRootId(rootCatalogId);
            if (CollectionUtils.isEmpty(sysCatalogs)) {
                return null;
            }
            Map<String, Object> resourceTree = new HashMap<>();
            for (SysCatalog sysCatalog : sysCatalogs) {
                if (sysCatalog.getParentId().equals("0")) {
                    sysCatalog.setParentId("");
                }
            }
            List catalogTree = (List) parseConfig(sysCatalogs);
            List<WebgisCatalogLayer> webgisCatalogLayers = webgisCatalogLayerService.getWebgisCatalogLayerByCatalogIds(
                    sysCatalogs.stream().map(sysCatalog -> sysCatalog.getCatalogId()).collect(Collectors.toList())
            );
            List layers = (List) parseConfig(webgisCatalogLayers);
            catalogTree.addAll(layers);
            resourceTree.put("allDataList", catalogTree);
            resourceTree.put("replaceFields", JSON.parse("{\n" +
                    "                       title: 'resourceName',\n" +
                    "                       key: 'resourceId'\n" +
                    "                     }"));
            return resourceTree;
        } else {
            return Collections.emptyMap();
        }
    }

    @Override
    public WebgisConfigData convert2ToolbarConfig(List<WebgisToolbarDo> toolbarDos) {
        WebgisConfigData re = new WebgisConfigData();
        if (!CollectionUtils.isEmpty(toolbarDos)) {
            List<WebgisToolbarDo> horizontalToolbarDos = new ArrayList<>();
            List<WebgisToolbarDo> verticalToolbarDos = new ArrayList<>();
            for (WebgisToolbarDo toolbarDo : toolbarDos) {
                toolbarDo.setToolbarLocation(PositionTypeEnum.get(toolbarDo.getToolbarLocation()).name());
                if (BASIC_TYPE.equals(toolbarDo.getType())) {
                    verticalToolbarDos.add(toolbarDo);
                } else {
                    horizontalToolbarDos.add(toolbarDo);
                }
            }
            List verticalToolbars = (List) parseConfig(verticalToolbarDos);
            List horizontalToolbars = (List) parseConfig(horizontalToolbarDos);
            handleSubToolBarConfig(verticalToolbars);
            handleSubToolBarConfig(horizontalToolbars);
            re.setHorizontalToolbars(horizontalToolbars);
            re.setVerticalToolbars(verticalToolbars);
        } else {
            re.setHorizontalToolbars(Collections.emptyList());
            re.setVerticalToolbars(Collections.emptyList());
        }
        return re;
    }


    @Override
    public Object parseConfig(Object obj) {
        try {
            return AppConfigParser.parse(obj);
        } catch (Exception e) {
            throw new GafException(e.getMessage());
        }
    }

    private void handleSubToolBarConfig(List toolbars) {
        for (Object toolbar : toolbars) {
            if (toolbar instanceof Map) {
                Map<String, Object> toolbarsMap = (Map) toolbar;
                List buttons = (List) toolbarsMap.get("buttons");
                if (CollectionUtils.isEmpty(buttons)) {
                    return;
                }
                for (Object button : buttons) {
                    if (button instanceof Map) {
                        Map<String, Object> buttonMap = (Map) button;
                        String name = (String) buttonMap.get("name");
                        String subToolbarId = (String) buttonMap.get("children");
                        if (!StringUtils.isEmpty(subToolbarId)) {
                            WebgisToolbarDo subToolbarDo = null;
                            try {
                                subToolbarDo = webgisToolbarService.getDoById(subToolbarId);
                                List children = (List) parseConfig(subToolbarDo.getWebgisToolbarButtonDos());
                                for (Object childrenButton : children) {
                                    if (childrenButton instanceof Map) {
                                        Map<String, Object> childrenButtonMap = (Map) childrenButton;
                                        childrenButtonMap.put("selectedName", name);
                                    }
                                }
                                buttonMap.put("children", children);
                            } catch (Exception e) {
                            }
                        }
                    }
                }
            }
        }
    }
}
