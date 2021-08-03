/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.gateway.service;

import com.supermap.gaf.commontypes.MessageResult;
import com.supermap.gaf.gateway.GatewayRouteDefinition;
import com.supermap.gaf.gateway.commontypes.RouteSearchParam;

import java.util.List;

/**
 * @program: gaf-service-manager-modules
 * @description:
 * @author: lidong
 * @date:2021/3/25
 * @create: 2019/07/16
 */
public interface RouteService {
    /**
     * 新增路由
     *
     * @param route
     * @return
     */
    MessageResult<String> addRoute(GatewayRouteDefinition route);

    /**
     * 查询路由
     *
     * @param param
     * @return
     */
    MessageResult<List<GatewayRouteDefinition>> queryRoutes(RouteSearchParam param);

    /**
     * 删除路由信息
     *
     * @param id
     * @return
     * @throws Exception
     */
    MessageResult deleteRoute(String id) throws Exception;

    /**
     * 更新路由信息
     *
     * @param route
     * @return
     */
    MessageResult updateRoute(GatewayRouteDefinition route);

    /**
     * 根据id查询路由信息
     *
     * @param id
     * @return
     */
    MessageResult<GatewayRouteDefinition> queryRoute(String id);

    /**
     * 批量删除路由信息
     *
     * @param ids
     * @return
     * @throws Exception
     */
    MessageResult<String> batchDeleteRoute(List<String> ids) throws Exception;
}
