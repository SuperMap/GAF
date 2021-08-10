/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.gateway.listener;

import com.supermap.gaf.commontypes.MessageResult;
import com.supermap.gaf.gateway.GatewayRouteDefinition;
import com.supermap.gaf.gateway.GatewayRouteType;
import com.supermap.gaf.gateway.commontypes.RouteSearchParam;
import com.supermap.gaf.gateway.dynamicroute.AbstractDynamicRouteServiceImpl;
import com.supermap.gaf.gateway.service.RouteService;
import com.supermap.gaf.gateway.util.RouteConvertUtil;
import com.supermap.gaf.utils.LogUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.context.ApplicationListener;

import java.util.List;

/**
 * @program: gaf-service-manager-modules
 * @description:
 * @author: lidong
 * @date:2021/3/25
 * @create: 2019/07/15
 */
public class DynamicRouteLoadListener implements ApplicationListener<ApplicationReadyEvent> {
    private static Logger logger = LogUtil.getLocLogger(AbstractDynamicRouteServiceImpl.class);

    @Autowired(required = false)
    private AbstractDynamicRouteServiceImpl dynamicRouteService;

    @Autowired(required = false)
    private RouteService routeService;

    @Value("${tenantId:empty}")
    private String tenantId;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        // 从数据库读取
        RouteSearchParam param = new RouteSearchParam();
        param.setType(GatewayRouteType.TENANT);
        param.setTenantId(tenantId);
        MessageResult<List<GatewayRouteDefinition>> result = routeService.queryRoutes(param);
        if (result.IsSuccessed() && CollectionUtils.isNotEmpty(result.getData())) {
            List<GatewayRouteDefinition> gatewayRouteDefinitions = result.getData();
            for (GatewayRouteDefinition gatewayRouteDefinition : gatewayRouteDefinitions) {
                RouteDefinition routeDefinition = RouteConvertUtil
                        .assembleRouteDefinition(RouteConvertUtil.assembleTenantGatewayRouteEntity(gatewayRouteDefinition));
                dynamicRouteService.add(routeDefinition);
            }
        }
    }
}
