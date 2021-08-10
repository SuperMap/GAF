/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.gateway.resources;

import com.alibaba.fastjson.JSON;
import com.supermap.gaf.commontypes.MessageResult;
import com.supermap.gaf.gateway.GatewayRouteDefinition;
import com.supermap.gaf.gateway.commontypes.RouteSearchParam;
import com.supermap.gaf.gateway.service.RouteService;
import com.supermap.gaf.utils.LogUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @program: gaf-service-manager-modules
 * @description:
 * @author: lidong
 * @date:2021/3/25
 * @create: 2019/07/18
 */
@RestController
@RequestMapping("/routes")
public class RouteMgtResource {

    private Logger logger = LogUtil.getLocLogger(com.supermap.gaf.gateway.resources.RouteMgtResource.class);
    @Autowired
    private RouteService routeService;

    @Value("${tenantId:empty}")
    String tenantId;

    @ApiOperation(value = "查询自定义网关路由", notes = "查询自定义网关路由")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "routeSearchParam", value = "路由搜索参数json字符串", paramType = "query", dataType = "string")
    })
    @GetMapping
    public MessageResult<List<GatewayRouteDefinition>> queryTenantGatewayRoutes(@RequestParam(name = "routeSearchParam", required = false) String routeSearchParam) {
        MessageResult<List<GatewayRouteDefinition>> result = new MessageResult<>();
        try {
            RouteSearchParam param = buildRouteSearchParam(routeSearchParam);
            MessageResult<List<GatewayRouteDefinition>> result1 = routeService.queryRoutes(param);
            result.setData(result1.getData());
            result.setSuccessed(result1.IsSuccessed());
            result.setMessage(result1.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "Id为参数查询自定义网关路由", notes = "Id为参数查询自定义网关路由")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "自定义路由id", paramType = "path", dataType = "string")
    })
    @GetMapping(value = "/{id}", produces = "application/json")
    public MessageResult<GatewayRouteDefinition> queryTenantGatewayRoute(@PathVariable("id") String id) {
        MessageResult<GatewayRouteDefinition> result = new MessageResult<>();
        try {
            result = routeService.queryRoute(id);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "新增自定义网关路由", notes = "添加动态网关路由")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "route", value = "动态网关路由对象", paramType = "body", dataTypeClass = GatewayRouteDefinition.class)
    })
    @PostMapping(produces = "application/json;charset=UTF-8")
    public MessageResult<String> addTenantRoute(@RequestBody GatewayRouteDefinition route) {
        MessageResult<String> result = new MessageResult<>();
        try {
            if (CollectionUtils.isEmpty(route.getPredicates())) {
                throw new Exception("路由Predicates不能为空");
            }
            if (CollectionUtils.isEmpty(route.getFilters())) {
                throw new Exception("路由Filters不能为空");
            }
            if (StringUtils.isBlank(route.getUri()) || route.getUri().endsWith("//")) {
                throw new Exception("uri参数填写有误");
            }
            if (StringUtils.isEmpty(route.getTenantId())) {
                route.setTenantId(tenantId);
            }
            result = routeService.addRoute(route);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "批量删除路由", notes = "传入json字符串参数，批量删除路由")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "idsJsonStr", value = "json字符串参数", paramType = "body")
    })
    @DeleteMapping(produces = "application/json")
    public MessageResult<String> batchDeleteTenantRoute(@RequestBody String idsJsonStr) {
        MessageResult<String> result = new MessageResult<>();
        try {
            if (StringUtils.isEmpty(idsJsonStr)) {
                throw new IllegalArgumentException("请传入正确的id集合");
            }
            List<String> ids = JSON.parseArray(idsJsonStr, String.class);
            result = routeService.batchDeleteRoute(ids);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "ID删除路由", notes = "传入ID参数，删除路由")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "路由id", paramType = "path", dataType = "string")
    })
    @DeleteMapping(value = "/{id}", produces = "application/json")
    public MessageResult<String> deleteTenantRoute(@PathVariable("id") String id) {
        MessageResult<String> result = new MessageResult<>();
        try {
            result = routeService.deleteRoute(id);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "编辑路由", notes = "编辑路由信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "route", value = "路由对象", paramType = "body", dataTypeClass = GatewayRouteDefinition.class)
    })
    @PutMapping(produces = "application/json")
    public MessageResult<String> updateTenantRoute(@RequestBody GatewayRouteDefinition route) {
        MessageResult<String> result = new MessageResult<>();
        try {
            result = routeService.updateRoute(route);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    private RouteSearchParam buildRouteSearchParam(String routeSearchParam) throws Exception {
        RouteSearchParam searchParam = new RouteSearchParam();
        if (StringUtils.isNotEmpty(routeSearchParam)) {
            try {
                RouteSearchParam temp = (RouteSearchParam) JSON.parseObject(routeSearchParam, RouteSearchParam.class);
                searchParam = (temp != null) ? temp : searchParam;
            } catch (Exception e) {
                throw new Exception(e.getMessage());
            }
        }
        return searchParam;
    }

}
