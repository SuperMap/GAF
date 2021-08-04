/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.webgis.service;

import com.supermap.gaf.webgis.entity.WebgisCatalogLayer;
import com.supermap.gaf.webgis.vo.WebgisCatalogLayerSelectVo;
import com.supermap.gaf.webgis.util.Page;
import com.supermap.gaf.webgis.vo.WebgisServiceToLayerVo;

import java.util.*;

/**
 * 图层服务类
 *
 * @author wangxiaolong
 * @date 2020-12-05
 */
public interface WebgisCatalogLayerService {

    /**
     * id查询图层
     *
     * @return
     */
    WebgisCatalogLayer getById(String catalogLayerId);

    /**
     * 分页条件查询
     *
     * @param webgisCatalogLayerSelectVo 查询条件
     * @param pageNum                    当前页数
     * @param pageSize                   页面大小
     * @return 分页对象
     */
    Page<WebgisCatalogLayer> listByPageCondition(WebgisCatalogLayerSelectVo webgisCatalogLayerSelectVo, int pageNum, int pageSize);


    /**
     * 新增图层
     *
     * @return
     */
    WebgisCatalogLayer insertWebgisCatalogLayer(WebgisCatalogLayer webgisCatalogLayer);

    /**
     * 批量插入
     */
    void batchInsert(List<WebgisCatalogLayer> webgisCatalogLayers);

    /**
     * 删除图层
     */
    void deleteWebgisCatalogLayer(String catalogLayerId);

    /**
     * 批量删除
     */
    void batchDelete(List<String> catalogLayerIds);

    /**
     * 更新图层
     *
     * @return
     */
    WebgisCatalogLayer updateWebgisCatalogLayer(WebgisCatalogLayer webgisCatalogLayer);

    /**
     * 按目录集查找所有关联图层
     *
     * @param catalogIds
     * @return
     */
    List<WebgisCatalogLayer> getWebgisCatalogLayerByCatalogIds(List<String> catalogIds);

    /**
     * 通过webgis服务新增对应的图层
     *
     * @param webgisServiceToLayerVos 关联服务的图层vo集合
     */
    void batchInsertByService(List<WebgisServiceToLayerVo> webgisServiceToLayerVos);

    /**
     * 查询某目录下的图层的gisServiceId
     *
     * @param layerCatalogId 图层目录id
     * @return 若未查询到则返回为空
     */
    List<String> listGisIdsByCatalogId(String layerCatalogId);

    /**
     * 查询资源目录下的图层数量
     *
     * @param layerCatalogId 资源目录id
     * @return 若未查询到则返回0
     */
    Integer countByCatalogId(String layerCatalogId);
}
