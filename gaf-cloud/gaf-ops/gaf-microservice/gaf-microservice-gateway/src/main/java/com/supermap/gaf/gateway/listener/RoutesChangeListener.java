/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.gateway.listener;

import com.supermap.gaf.commontypes.MessageResult;
import com.supermap.gaf.gateway.commontypes.GatewayRouteEntity;
import com.supermap.gaf.gateway.dynamicroute.AbstractDynamicRouteServiceImpl;
import com.supermap.gaf.gateway.event.RoutesChangeEvent;
import com.supermap.gaf.gateway.util.RouteConvertUtil;
import com.supermap.gaf.utils.LogUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @program: gaf-service-manager-modules
 * @description: 路由信息改变监听
 * @author: lidong
 * @date:2021/3/25
 * @create: 2019/07/19
 */
@Component
public class RoutesChangeListener implements ApplicationListener<RoutesChangeEvent> {
    private static Logger logger = LogUtil.getLocLogger(com.supermap.gaf.gateway.listener.RoutesChangeListener.class);
    @Autowired(required = false)
    private AbstractDynamicRouteServiceImpl dynamicRouteService;

    /**
     * 异步监听
     * @param routesChangeEvent
     */
    @Async
    @Override
    public void onApplicationEvent(RoutesChangeEvent routesChangeEvent) {
        GatewayRouteEntity gatewayRouteEntity = routesChangeEvent.getRoute();
        RoutesChangeEvent.OperateType operateType = routesChangeEvent.getOperateType();
        RouteDefinition routeDefinition = RouteConvertUtil.assembleRouteDefinition(gatewayRouteEntity);
        MessageResult result = new MessageResult();
        try {
            switch (operateType) {
            case ADD:
                result = dynamicRouteService.add(routeDefinition);
                break;
            case DELETE:
                result = dynamicRouteService.delete(routeDefinition.getId());
                break;
            case UPDATE:
                result = dynamicRouteService.update(routeDefinition);
                break;
            default:
                result = result;
            }
            logger.info(result.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}
