/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.gateway;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.supermap.gaf.utils.JsonDateSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @program: gaf-service-manager-modules
 * @description: 网关路由信息，提供rest层使用
 * @author: lidong
 * @date:2021/3/25
 * @create: 2019/07/16
 */
@ApiModel("自定义路由实体类")
public class GatewayRouteDefinition {

    /****************基础属性******************/
    @ApiModelProperty("唯一标识")
    private String id;
    @ApiModelProperty("是否可用：true:1  false:0")
    private Boolean enable = true;
    @ApiModelProperty("类型：系统路由、租户路由")
    private GatewayRouteType type;
    @ApiModelProperty("租户id")
    private String tenantId;
    @ApiModelProperty("创建时间")
    private Date createTime;
    @ApiModelProperty("更新时间")
    private Date updateTime;
    /****************路由属性******************/
    @ApiModelProperty("路由的Id")
    private String routeId;
    @ApiModelProperty("路由断言集合配置")
    private List<GatewayPredicateDefinition> predicates = new ArrayList<>();
    @ApiModelProperty("路由过滤器集合配置")
    private List<GatewayFilterDefinition> filters = new ArrayList<>();
    @ApiModelProperty("路由规则转发的目标uri")
    private String uri;
    @ApiModelProperty("路由执行的顺序")
    private int order = 0;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @JsonSerialize(using = JsonDateSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @JsonSerialize(using = JsonDateSerializer.class)
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public GatewayRouteType getType() {
        return type;
    }

    public void setType(GatewayRouteType type) {
        this.type = type;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public List<GatewayPredicateDefinition> getPredicates() {
        return predicates;
    }

    public void setPredicates(List<GatewayPredicateDefinition> predicates) {
        this.predicates = predicates;
    }

    public List<GatewayFilterDefinition> getFilters() {
        return filters;
    }

    public void setFilters(List<GatewayFilterDefinition> filters) {
        this.filters = filters;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

}
