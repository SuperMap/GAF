/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.gateway.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.supermap.gaf.gateway.GatewayFilterDefinition;
import com.supermap.gaf.gateway.GatewayPredicateDefinition;
import com.supermap.gaf.gateway.GatewayRouteDefinition;
import com.supermap.gaf.gateway.commontypes.GatewayRouteEntity;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: gaf-service-manager-modules
 * @description:
 * @author: lidong
 * @date:2021/3/25
 * @create: 2019/07/18
 */
public class RouteConvertUtil {
    /**
     * 将前端入参转换为自定义的租户路由实体模型
     *
     * @param gatewayRouteInfoDefinition
     * @return
     */
    public static GatewayRouteEntity assembleTenantGatewayRouteEntity(GatewayRouteDefinition gatewayRouteInfoDefinition) {
        if (null == gatewayRouteInfoDefinition) {
            return null;
        }
        //这里没解析id和时间属性，在resource层里做
        GatewayRouteEntity gatewayRouteEntity = new GatewayRouteEntity();
        gatewayRouteEntity.setId(gatewayRouteInfoDefinition.getId());
        gatewayRouteEntity.setCreateTime(gatewayRouteInfoDefinition.getCreateTime());
        gatewayRouteEntity.setUpdateTime(gatewayRouteInfoDefinition.getUpdateTime());
        gatewayRouteEntity.setRouteId(gatewayRouteInfoDefinition.getRouteId());
        gatewayRouteEntity.setRouteUri(gatewayRouteInfoDefinition.getUri());
        gatewayRouteEntity.setRouteOrder(gatewayRouteInfoDefinition.getOrder());
        gatewayRouteEntity.setEnable(gatewayRouteInfoDefinition.getEnable());
        gatewayRouteEntity.setPredicates(JSON.toJSONString(gatewayRouteInfoDefinition.getPredicates()));
        gatewayRouteEntity.setFilters(JSON.toJSONString(gatewayRouteInfoDefinition.getFilters()));
        gatewayRouteEntity.setType(gatewayRouteInfoDefinition.getType());
        gatewayRouteEntity.setTenantId(gatewayRouteInfoDefinition.getTenantId());
        return gatewayRouteEntity;
    }

    public static void main(String[] args) {
        List <GatewayPredicateDefinition> predicates = new ArrayList <>();
        predicates.add(new GatewayPredicateDefinition("Path", "/test/**"));
        predicates.add(new GatewayPredicateDefinition("Path2", "/test/**"));
        System.out.println(JSON.toJSONString(predicates));
    }

    private static List <String> parsePredicateStr(List <GatewayPredicateDefinition> predicates) {
        List <String> predicateStr = new ArrayList <>();
        for (GatewayPredicateDefinition predicate : predicates) {
            predicateStr.add(predicate.getName() + "=" + predicate.getArgs());
        }
        return predicateStr;
    }

    private static List <String> parseFilterStr(List <GatewayFilterDefinition> filters) {
        List <String> filterStr = new ArrayList <>();
        for (GatewayFilterDefinition filter : filters) {
            filterStr.add(filter.getName() + "=" + filter.getArgs());
        }
        return filterStr;
    }

    /**
     * 将数据库中信息转换为自定义的网关路由信息，便于展示
     *
     * @param gatewayRouteEntity
     * @return
     */
    public static GatewayRouteDefinition assembleGatewayRouteDefinition(GatewayRouteEntity gatewayRouteEntity) {
        if (null == gatewayRouteEntity) {
            return null;
        }
        GatewayRouteDefinition gatewayRouteDefinition = new GatewayRouteDefinition();
        gatewayRouteDefinition.setId(gatewayRouteEntity.getId());
        gatewayRouteDefinition.setEnable(gatewayRouteEntity.getEnable());
        gatewayRouteDefinition.setType(gatewayRouteEntity.getType());
        gatewayRouteDefinition.setTenantId(gatewayRouteEntity.getTenantId());
        gatewayRouteDefinition.setRouteId(gatewayRouteEntity.getRouteId());
        gatewayRouteDefinition.setCreateTime(gatewayRouteEntity.getCreateTime());
        gatewayRouteDefinition.setUpdateTime(gatewayRouteEntity.getUpdateTime());
        gatewayRouteDefinition.setPredicates(JSONArray.parseArray(gatewayRouteEntity.getPredicates(), GatewayPredicateDefinition.class));
        gatewayRouteDefinition.setFilters(JSONArray.parseArray(gatewayRouteEntity.getFilters(), GatewayFilterDefinition.class));
        gatewayRouteDefinition.setUri(gatewayRouteEntity.getRouteUri());
        gatewayRouteDefinition.setOrder(gatewayRouteEntity.getRouteOrder());
        return gatewayRouteDefinition;
    }

    /**
     * 获取过滤器集合
     *
     * @return
     */
    public static List <FilterDefinition> getFilterDefinition(GatewayRouteEntity gatewayRouteEntity) {
        List <FilterDefinition> filterDefinitions = new ArrayList <>();
        if (!StringUtils.isEmpty(gatewayRouteEntity.getPredicates())) {
            List <GatewayFilterDefinition> gatewayFilters = JSON.parseArray(gatewayRouteEntity.getFilters(), GatewayFilterDefinition.class);
            for (GatewayFilterDefinition gatewayFilter : gatewayFilters) {
                filterDefinitions.add(new FilterDefinition(gatewayFilter.getName() + "=" + gatewayFilter.getArgs()));
            }
        }
        return filterDefinitions;
    }

    /**
     * 获取断言集合
     *
     * @return
     */
    public static List <PredicateDefinition> getPredicateDefinition(GatewayRouteEntity gatewayRouteEntity) {
        List <PredicateDefinition> predicateDefinitions = new ArrayList <>();
        if (!StringUtils.isEmpty(gatewayRouteEntity.getFilters())) {
            List <GatewayPredicateDefinition> gatewayPredicates = JSON.parseArray(gatewayRouteEntity.getPredicates(), GatewayPredicateDefinition.class);
            for (GatewayPredicateDefinition gatewayPredicate : gatewayPredicates) {
                predicateDefinitions.add(new PredicateDefinition(gatewayPredicate.getName() + "=" + gatewayPredicate.getArgs()));
            }
        }
        return predicateDefinitions;
    }

    /**
     * 将数据库中信息 解析为Springcloud Gateway路由对象
     * <p>
     *
     * @param gatewayRouteEntity
     * @return
     */
    public static RouteDefinition assembleRouteDefinition(GatewayRouteEntity gatewayRouteEntity) {
        if (gatewayRouteEntity == null) {
            return null;
        }
        RouteDefinition definition = new RouteDefinition();
        definition.setId(gatewayRouteEntity.getRouteId());
        definition.setOrder(gatewayRouteEntity.getRouteOrder());

        //设置断言
        List <PredicateDefinition> pdList = getPredicateDefinition(gatewayRouteEntity);
        definition.setPredicates(pdList);

        //设置过滤器
        List <FilterDefinition> filters = getFilterDefinition(gatewayRouteEntity);
        definition.setFilters(filters);
        URI uri = null;
        if (gatewayRouteEntity.getRouteUri().startsWith(HttpHost.DEFAULT_SCHEME_NAME)) {
            uri = UriComponentsBuilder.fromHttpUrl(gatewayRouteEntity.getRouteUri()).build().toUri();
        } else {
            uri = URI.create(gatewayRouteEntity.getRouteUri());
        }
        definition.setUri(uri);
        return definition;
    }
}
