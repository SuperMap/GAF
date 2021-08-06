/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.webgis.service;

import com.supermap.gaf.webgis.domain.WebgisRouteInfo;
import com.supermap.gaf.webgis.domain.WebgisRouteStopInfo;
import com.supermap.gaf.webgis.entity.WebgisRoamRoute;
import com.supermap.gaf.webgis.util.Page;
import com.supermap.gaf.webgis.vo.WebgisRoamRouteSelectVo;

import javax.security.sasl.AuthenticationException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * 漫游路线服务类
 *
 * @author wangxiaolong
 * @date yyyy-mm-dd
 */
public interface WebgisRoamRouteService {

    /**
     * id查询漫游路线
     *
     * @return
     */
    WebgisRoamRoute getById(String gisRoamRouteId);

    /**
     * 根据查询路线id查询路线下的所有站点
     *
     * @param routeId 路线id
     * @return
     */
    List<WebgisRouteStopInfo> listRouteStops(String routeId);

    /**
     * 根据路线名查询漫游路线
     *
     * @return
     */
    List<WebgisRoamRoute> listByName(String name);

    /**
     * 分页条件查询
     *
     * @param webgisRoamRouteSelectVo 查询条件
     * @param pageNum                 当前页数
     * @param pageSize                页面大小
     * @return 分页对象
     */
    Page<WebgisRoamRoute> listByPageCondition(WebgisRoamRouteSelectVo webgisRoamRouteSelectVo, int pageNum, int pageSize);


    /**
     * 新增漫游路线
     *
     * @param webgisRouteInfo 漫游路线信息
     * @return 漫游路线信息
     */
    WebgisRouteInfo createRoute(WebgisRouteInfo webgisRouteInfo) throws IOException;


    /**
     * 批量插入
     */
    void batchInsert(List<WebgisRoamRoute> webgisRoamRoutes);

    /**
     * 批量删除
     */
    void batchDelete(List<String> gisRoamRouteIds);

    /**
     * 更新漫游路线
     *
     * @return
     */
    WebgisRoamRoute updateWebgisRoamRoute(WebgisRoamRoute webgisRoamRoute);


    /**
     * 根据路线id删除当前用户下的漫游路线
     *
     * @param routeId 路线id
     */
    void deleteRoute(String routeId) throws IOException;

    /**
     * 根据路线id清除路线的所有站点
     *
     * @param routeId
     */
    void clearRouteStops(String routeId) throws IOException;

    /**
     * 添加站点
     *
     * @param webgisRouteInfo 路线信息，附带站点信息
     */
    void addRouteStops(WebgisRouteInfo webgisRouteInfo) throws IOException;

    ///**
    // * 根据路线id获取路径信息
    // * 与getById不同，若没有生成对应的fpf文件则会先生成
    // *
    // * @param routeId
    // * @return
    // */
    //WebgisRoamRoute getRoamRoute(String routeId) throws IOException;

    /**
     * 获取用户下的所有
     *
     * @return
     */
    List<WebgisRouteInfo> listRoutes();

    /**
     * 根据路线id获取路径的weburl
     *
     * @param routeId 路径id
     * @return
     */
    String getRouteFileUrl(String routeId) throws URISyntaxException, AuthenticationException;

    ///**
    // * 根据路径id获取绝对路径
    // *
    // * @param routeId 路径id
    // * @return
    // */
    //String getAbsolutePath(String routeId) throws AuthenticationException;
}
