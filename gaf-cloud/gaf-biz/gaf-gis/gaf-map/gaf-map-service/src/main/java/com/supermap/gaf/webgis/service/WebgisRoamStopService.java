/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.webgis.service;

import com.supermap.gaf.webgis.entity.WebgisRoamStop;
import com.supermap.gaf.webgis.util.Page;
import com.supermap.gaf.webgis.vo.WebgisRoamStopSelectVo;

import java.util.List;

/**
 * 漫游站点服务类
 *
 * @author wangxiaolong
 * @date yyyy-mm-dd
 */
public interface WebgisRoamStopService {

    /**
     * id查询漫游站点
     *
     * @return
     */
    WebgisRoamStop getById(String gisRoamStopId);

    /**
     * 根据路线id查询站点
     *
     * @param routeId 路线id
     * @return 路线下的站点集合
     */
    List<WebgisRoamStop> listStops(String routeId);


    /**
     * 条件查询
     *
     * @param webgisRoamStopSelectVo 条件对象
     * @return
     */
    List<WebgisRoamStop> list(WebgisRoamStopSelectVo webgisRoamStopSelectVo);


    /**
     * 分页条件查询
     *
     * @param webgisRoamStopSelectVo 查询条件
     * @param pageNum                当前页数
     * @param pageSize               页面大小
     * @return 分页对象
     */
    Page<WebgisRoamStop> listByPageCondition(WebgisRoamStopSelectVo webgisRoamStopSelectVo, int pageNum, int pageSize);


    /**
     * 新增漫游站点
     *
     * @return
     */
    WebgisRoamStop insertWebgisRoamStop(WebgisRoamStop webgisRoamStop);

    /**
     * 批量插入
     */
    void batchInsert(List<WebgisRoamStop> webgisRoamStops);

    /**
     * 删除漫游站点
     */
    void deleteWebgisRoamStop(String gisRoamStopId);


    /**
     * 批量删除
     */
    void batchDelete(List<String> gisRoamStopIds);

    /**
     * 更新漫游站点
     *
     * @return
     */
    WebgisRoamStop updateWebgisRoamStop(WebgisRoamStop webgisRoamStop);


    /**
     * 根据路线id删除站点
     *
     * @param routeId 路线id
     */
    void removeByRouteId(String routeId);

}
