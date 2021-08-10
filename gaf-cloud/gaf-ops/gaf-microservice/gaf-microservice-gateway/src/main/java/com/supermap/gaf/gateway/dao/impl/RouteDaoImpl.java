/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.gateway.dao.impl;

import com.supermap.gaf.gateway.commontypes.RouteSearchParam;
import com.supermap.gaf.gateway.commontypes.GatewayRouteEntity;
import com.supermap.gaf.gateway.dao.RouteDao;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: gaf-service-manager-modules
 * @description:
 * @author: lidong
 * @date:2021/3/25
 * @create: 2019/07/16
 */
@Service
public class RouteDaoImpl implements RouteDao {
    @Autowired
    private SqlSession sqlSession;

    /**
     * 新增路由
     *
     * @param route
     * @return
     */
    @Override
    public boolean addRoute(GatewayRouteEntity route) {
        return sqlSession.insert("addRoute", route) > 0;
    }

    /**
     * 查询路由
     *
     * @param param
     * @return
     */
    @Override
    public List<GatewayRouteEntity> queryRoutes(RouteSearchParam param) {
        return sqlSession.selectList("queryRoutes", param);
    }

    /**
     * 删除路由信息
     *
     * @param id
     * @return
     */
    @Override
    public boolean deleteRoute(String id) {
        return sqlSession.delete("deleteRoute", id) > 0;
    }

    /**
     * 更新路由信息
     *
     * @param route
     * @return
     */
    @Override
    public boolean updateRoute(GatewayRouteEntity route) {
        return sqlSession.update("updateRoute", route) > 0;
    }

    /**
     * 根据id查询路由信息
     *
     * @param id
     * @return
     */
    @Override
    public GatewayRouteEntity queryRoute(String id) {
        return sqlSession.selectOne("queryRoute", id);
    }

    /**
     * 批量删除路由信息
     *
     * @param ids
     * @return
     */
    @Override
    public boolean batchDeleteRoute(List<String> ids) {
        return sqlSession.delete("batchDeleteRoute", ids) > 0;
    }
}
