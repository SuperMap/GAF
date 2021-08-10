/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.gateway.service.impl;

import com.supermap.gaf.commontypes.MessageResult;
import com.supermap.gaf.gateway.GatewayRouteDefinition;
import com.supermap.gaf.gateway.commontypes.RouteSearchParam;
import com.supermap.gaf.gateway.commontypes.GatewayRouteEntity;
import com.supermap.gaf.gateway.dao.RouteDao;
import com.supermap.gaf.gateway.event.RoutesChangeEvent;
import com.supermap.gaf.gateway.service.RouteService;
import com.supermap.gaf.gateway.util.RouteConvertUtil;
import com.supermap.gaf.utils.PrimaryKeyUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @program: gaf-service-manager-modules
 * @description:
 * @author: lidong
 * @date:2021/3/25
 * @create: 2019/07/16
 */
@Service
public class RouteServiceImpl implements RouteService {

    @Autowired
    private RouteDao routeDao;

    @Resource
    private ApplicationContext applicationContext;

    /**
     * 新增路由
     *
     * @param route
     * @return
     */
    @Override
    public MessageResult<String> addRoute(GatewayRouteDefinition route) {
        MessageResult result = new MessageResult();
        route.setId(PrimaryKeyUtil.getInstance().GetPrimaryKeyValue());
        if (StringUtils.isEmpty(route.getRouteId())) {
            route.setRouteId(PrimaryKeyUtil.getInstance().GetPrimaryKeyValue());
        }
        route.setId(PrimaryKeyUtil.getInstance().GetPrimaryKeyValue());
        route.setCreateTime(new Date());
        route.setUpdateTime(new Date());
        route.setTenantId(route.getTenantId());
        GatewayRouteEntity routeEntity = RouteConvertUtil.assembleTenantGatewayRouteEntity(route);
        boolean addRst = routeDao.addRoute(routeEntity);
        if (addRst) {
            applicationContext.publishEvent(new RoutesChangeEvent(this, routeEntity, RoutesChangeEvent.OperateType.ADD));
        }
        result.setSuccessed(addRst);
        result.setMessage(addRst ? "添加成功！" : "添加失败");
        return result;
    }

    /**
     * 查询路由
     *
     * @param param
     * @return
     */
    @Override
    public MessageResult<List<GatewayRouteDefinition>> queryRoutes(RouteSearchParam param) {
        MessageResult<List<GatewayRouteDefinition>> result = new MessageResult<>();
        List<GatewayRouteDefinition> gatewayRouteDefinitions = new ArrayList<>();
        List<GatewayRouteEntity> list = routeDao.queryRoutes(param);
        for (GatewayRouteEntity gatewayRouteEntity : list) {
            gatewayRouteDefinitions.add(RouteConvertUtil.assembleGatewayRouteDefinition(gatewayRouteEntity));
        }
        result.setData(gatewayRouteDefinitions);
        result.setSuccessed(CollectionUtils.isNotEmpty(list));
        result.setMessage(CollectionUtils.isNotEmpty(list) ? "查询成功！" : "查无所获");
        return result;
    }

    /**
     * 删除路由信息
     *
     * @param id
     * @return
     */
    @Override
    public MessageResult deleteRoute(String id) throws Exception {
        MessageResult result = new MessageResult();
        GatewayRouteDefinition gatewayRouteDefinition = queryRoute(id).getData();
        if (gatewayRouteDefinition == null) {
            throw new Exception("未能正确获取到路由信息");
        }
        boolean rst = routeDao.deleteRoute(id);
        if (rst) {
            GatewayRouteEntity gatewayRouteEntity = RouteConvertUtil.assembleTenantGatewayRouteEntity(gatewayRouteDefinition);
            applicationContext.publishEvent(new RoutesChangeEvent(this, gatewayRouteEntity, RoutesChangeEvent.OperateType.DELETE));
        }
        result.setSuccessed(rst);
        result.setMessage(rst ? "删除成功！" : "删除失败");
        return result;
    }

    /**
     * 更新路由信息
     *
     * @param route
     * @return
     */
    @Override
    public MessageResult updateRoute(GatewayRouteDefinition route) {
        MessageResult result = new MessageResult();
        GatewayRouteEntity routeEntity = RouteConvertUtil.assembleTenantGatewayRouteEntity(route);
        routeEntity.setUpdateTime(new Date());
        boolean rst = routeDao.updateRoute(routeEntity);
        if (rst) {
            applicationContext.publishEvent(new RoutesChangeEvent(this, routeEntity, RoutesChangeEvent.OperateType.UPDATE));
        }
        result.setSuccessed(rst);
        result.setMessage(rst ? "更新成功！" : "更新失败");
        return result;
    }

    /**
     * 根据id查询路由信息
     *
     * @param id
     * @return
     */
    @Override
    public MessageResult<GatewayRouteDefinition> queryRoute(String id) {
        MessageResult<GatewayRouteDefinition> result = new MessageResult();
        GatewayRouteEntity route = routeDao.queryRoute(id);
        if (route != null) {
            result.setData(RouteConvertUtil.assembleGatewayRouteDefinition(route));
        }
        result.setSuccessed(route != null);
        result.setMessage(route == null ? "未能正确获取到路由信息" : "成功获取到路由信息");
        return result;
    }

    /**
     * 批量删除路由信息
     *
     * @param ids
     * @return
     */
    @Override
    public MessageResult<String> batchDeleteRoute(List<String> ids) throws Exception {
        MessageResult<String> result = new MessageResult<>();
        RouteSearchParam param = new RouteSearchParam();
        param.setIds(ids);
        MessageResult<List<GatewayRouteDefinition>> queryRst = queryRoutes(param);
        if (!queryRst.IsSuccessed() || CollectionUtils.isEmpty(queryRst.getData())) {
            throw new Exception("未能获取到路由信息");
        }
        List<GatewayRouteDefinition> tenantGatewayRouteEntities = queryRst.getData();
        boolean deleteRst = routeDao.batchDeleteRoute(ids);
        if (deleteRst) {
            for (GatewayRouteDefinition routeDefinition : tenantGatewayRouteEntities) {
                applicationContext.publishEvent(
                        new RoutesChangeEvent(this, RouteConvertUtil.assembleTenantGatewayRouteEntity(routeDefinition), RoutesChangeEvent.OperateType.DELETE));
            }
        }
        result.setSuccessed(deleteRst);
        result.setMessage(deleteRst ? "删除成功" : "删除失败");
        return result;
    }
}
