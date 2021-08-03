/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.webgis.service;

import com.supermap.gaf.authority.vo.TreeNode;
import com.supermap.gaf.webgis.domain.BatchRegistryServiceResult;
import com.supermap.gaf.webgis.entity.WebgisService;
import com.supermap.gaf.webgis.vo.WebgisServiceConditonVo;
import com.supermap.gaf.webgis.vo.WebgisServiceSelectVo;
import com.supermap.gaf.webgis.util.Page;

import java.util.*;

/**
 * GIS服务服务类
 *
 * @author wangxiaolong
 * @date 2020-12-05
 */
public interface WebgisServiceService {

    /**
     * id查询GIS服务
     *
     * @return
     */
    WebgisService getById(String gisServiceId);


    BatchRegistryServiceResult getRegistryServiceResult(String code);

    /**
     * 分页条件查询
     *
     * @param webgisServiceSelectVo 查询条件
     * @param pageNum               当前页数
     * @param pageSize              页面大小
     * @return 分页对象
     */
    Page<WebgisService> listByPageCondition(WebgisServiceSelectVo webgisServiceSelectVo, int pageNum, int pageSize);


    /**
     * 新增GIS服务
     *
     * @return
     */
    void insertWebgisService(WebgisService webgisService, String type);


    void registryWebgis(WebgisService webgisService);

    /**
     * 批量插入
     */
    void batchInsert(List<WebgisService> webgisServices);

    /**
     * 删除GIS服务
     */
    void deleteWebgisService(String gisServiceId);

    /**
     * 批量删除
     */
    void batchDelete(List<String> gisServiceIds);

    /**
     * 更新GIS服务
     *
     * @return
     */
    WebgisService updateWebgisService(WebgisService webgisService);

    /**
     * 查询所有服务类别
     *
     * @return
     */
    List<TreeNode> getServiceTypes();

    /**
     * 根据id集合批量查询webgis服务
     *
     * @param ids id集合
     * @return 若未查询到则返回空对象
     */
    List<WebgisService> listByIds(List<String> ids);

    /**
     * 分页条件查询 未挂载在资源目录下的服务
     *
     * @param webgisServiceConditonVo 查询条件
     * @param pageNum                 当前页数
     * @param pageSize                页面大小
     * @param layerCatalogId          资源目录id
     * @return 分页对象
     */
    Page<WebgisService> listByPageCondition(WebgisServiceConditonVo webgisServiceConditonVo, Integer pageNum, Integer pageSize, String layerCatalogId);

    List selectAssociationDataServices(String gisServiceId);

    /**
     * 根据多种类型分页查询服务
     *
     * @param typeCodes
     * @param pageNum
     * @param pageSize
     * @return
     */
    Page<WebgisService> listByTypeCodes(String typeCodes, int pageNum, int pageSize);
}
