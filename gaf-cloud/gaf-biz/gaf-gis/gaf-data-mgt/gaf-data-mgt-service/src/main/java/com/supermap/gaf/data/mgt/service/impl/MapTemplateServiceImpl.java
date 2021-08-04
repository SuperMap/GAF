/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.data.mgt.service.impl;

import com.google.common.collect.Lists;
import com.supermap.gaf.commontypes.MessageResult;
import com.supermap.gaf.data.mgt.entity.MapTemplate;
import com.supermap.gaf.data.mgt.service.MapTemplateService;
import com.supermap.gaf.rest.remote.ExternalRemoteService;
import com.supermap.gaf.utils.LogUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @program: gaf-datacenter-modules
 * @description:
 * @author: lidong
 * @date:2021/3/25
 * @create: 2019/09/05
 */
@Service
public class MapTemplateServiceImpl implements MapTemplateService, InitializingBean {
    private static Logger logger = LogUtil.getLocLogger(MapTemplateService.class);

    @Autowired
    private ExternalRemoteService externalRemoteService;

    /**
     * 默认地图模板
     */
    private Map<String, MapTemplate> defaultMapTemplateMap = new HashMap<>(8);

    @Override
    public void afterPropertiesSet() {
        try {
            ClassPathResource classPathResource = new ClassPathResource("templates/maptemplate/default/Default_Point.xml");
            byte[] bytes = IOUtils.toByteArray(classPathResource.getInputStream());
            MapTemplate mapTemplate = new MapTemplate("", "Default_Point",
                    MapTemplate.Type.VECTOR_SINGLE, "", 1, IOUtils.toString(bytes, "utf-8"));
            defaultMapTemplateMap.put("Default_Point", mapTemplate);

            classPathResource = new ClassPathResource("templates/maptemplate/default/Default_Line.xml");
            bytes = IOUtils.toByteArray(classPathResource.getInputStream());
            mapTemplate = new MapTemplate("", "Default_Line",
                    MapTemplate.Type.VECTOR_SINGLE, "", 1, IOUtils.toString(bytes, "utf-8"));
            defaultMapTemplateMap.put("Default_Line", mapTemplate);

            classPathResource = new ClassPathResource("templates/maptemplate/default/Default_Region.xml");
            bytes = IOUtils.toByteArray(classPathResource.getInputStream());
            mapTemplate = new MapTemplate("", "Default_Region",
                    MapTemplate.Type.VECTOR_SINGLE, "", 1, IOUtils.toString(bytes, "utf-8"));
            defaultMapTemplateMap.put("Default_Region", mapTemplate);
            logger.info("加载默认地图模板完成");
        } catch (IOException e) {
            logger.info("加载默认地图模板异常，{}", e.getMessage());
        }
    }

    @Override
    public Map<String, MapTemplate> getDefaultMapTemplate() {
        return defaultMapTemplateMap;
    }

    /**
     * 根据资源地址获取地图模板内容
     */
    @Override
    public MessageResult<MapTemplate> getMapTemplateContentByUrl(String mapTemplateUrl) {
        MapTemplate mapTemplate = null;
        try {
            String content = externalRemoteService.remoteGet(mapTemplateUrl, new LinkedMultiValueMap<>(), Lists.newArrayList(), String.class);
            if (!StringUtils.isEmpty(content)) {
                mapTemplate = new MapTemplate();
                mapTemplate.content = content;
            }
        } catch (Exception e) {
            return MessageResult.failed(MapTemplate.class).message("根据资源地址获取地图模板内容失败," + e.getMessage()).build();
        }
        return MessageResult.successe(MapTemplate.class).data(mapTemplate).message("查询成功").build();
    }

    /**
     * 从模板内容中获取数据集名称
     *
     * @param content
     * @return
     */
    private String getDatasetNameFromTemplate(String content) {
        String info = "(?<=sml:DatasetName>)(.*?)(?=</sml:DatasetName>)";
        Pattern pattern = Pattern.compile(info);
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

}
