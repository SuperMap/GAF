/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.gateway.dao;


import com.supermap.gaf.gateway.commontypes.RouteSearchParam;
import com.supermap.gaf.gateway.commontypes.GatewayRouteEntity;

import java.util.List;

/**
 * @program: gaf-service-manager-modules
 * @description: 租户路由dao
 * @author: lidong
 * @date:2021/3/25
 * @create: 2019/07/16
 */
public interface RouteDao {
    /**
     * 新增路由
     *
     * @param route
     * @return
     */
    boolean addRoute(GatewayRouteEntity route);

    /**
     * 查询路由
     *
     * @param param
     * @return
     */
    List <GatewayRouteEntity> queryRoutes(RouteSearchParam param);

    /**
     * 删除路由信息
     *
     * @param id
     * @return
     */
    boolean deleteRoute(String id);

    /**
     * 更新路由信息
     *
     * @param route
     * @return
     */
    boolean updateRoute(GatewayRouteEntity route);

    /**
     * 根据id查询路由信息
     *
     * @param id
     * @return
     */
    GatewayRouteEntity queryRoute(String id);

    /**
     * 批量删除路由信息
     *
     * @param ids
     * @return
     */
    boolean batchDeleteRoute(List<String> ids);
}
