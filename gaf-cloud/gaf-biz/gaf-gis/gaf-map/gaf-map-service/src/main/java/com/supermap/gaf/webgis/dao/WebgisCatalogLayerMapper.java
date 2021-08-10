/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.webgis.dao;

import com.supermap.gaf.webgis.entity.WebgisCatalogLayer;
import com.supermap.gaf.webgis.vo.WebgisCatalogLayerSelectVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 图层数据访问类
 *
 * @author wangxiaolong
 * @date 2020-12-05
 */
@Mapper
@Component
public interface WebgisCatalogLayerMapper {
    /**
     * 根据主键 catalogLayerId 查询
     */
    WebgisCatalogLayer select(@Param("catalogLayerId") String catalogLayerId);

    /**
     * 单字段模糊,再加上根据图层目录条件查询
     * 条件是可选的，若为null 则没有该条件
     *
     * @param webgisCatalogLayerSelectVo 查询条件
     * @return 若未查询到则返回空集合
     */
    List<WebgisCatalogLayer> selectByCondition(WebgisCatalogLayerSelectVo webgisCatalogLayerSelectVo);

    /**
     * 新增
     */
    int insert(WebgisCatalogLayer webgisCatalogLayer);

    /**
     * 批量插入
     */
    int batchInsert(List<WebgisCatalogLayer> webgisCatalogLayers);

    /**
     * 批量删除
     */
    int batchDelete(List<String> catalogLayerIds);

    /**
     * 刪除
     */
    int delete(@Param("catalogLayerId") String catalogLayerId);

    /**
     * 更新
     **/
    int update(WebgisCatalogLayer webgisCatalogLayer);


    /**
     * 按目录集查找所有关联图层
     *
     * @param catalogIds
     * @return
     */
    List<WebgisCatalogLayer> getWebgisCatalogLayerByCatalogIds(List<String> catalogIds);

    /**
     * 查询某目录下的图层的gisServiceId
     *
     * @param layerCatalogId 图层目录id
     * @return 若未查询到则返回为空
     */
    List<String> selectServiceIdsByCatalogId(@Param("layerCatalogId") String layerCatalogId);

    /**
     * 根据图层id集合获取图层集合
     *
     * @param catalogLayerIds 图层id集合
     * @return 图层集合
     */
    List<WebgisCatalogLayer> selectByIds(List<String> catalogLayerIds);
}
