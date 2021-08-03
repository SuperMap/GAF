/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.gateway.event;

import com.supermap.gaf.gateway.commontypes.GatewayRouteEntity;
import com.supermap.gaf.utils.LogUtil;
import org.slf4j.Logger;
import org.springframework.context.ApplicationEvent;

/**
 * @program: gaf-service-manager-modules
 * @description:
 * @author: lidong
 * @date:2021/3/25
 * @create: 2019/07/19
 */
public class RoutesChangeEvent extends ApplicationEvent {
    private static Logger logger = LogUtil.getLocLogger(com.supermap.gaf.gateway.event.RoutesChangeEvent.class);
    private GatewayRouteEntity route;
    private OperateType operateType;

    public GatewayRouteEntity getRoute() {
        return route;
    }

    public void setRoute(GatewayRouteEntity route) {
        this.route = route;
    }

    public OperateType getOperateType() {
        return operateType;
    }

    public void setOperateType(OperateType operateType) {
        this.operateType = operateType;
    }

    public RoutesChangeEvent(Object source, GatewayRouteEntity route, OperateType type) {
        super(source);
        logger.info("路由信息更改事件");
        this.setRoute(route);
        this.setOperateType(type);
    }

    public static enum OperateType {
        /**
         * 添加
         */
        ADD,
        /**
         * 编辑
         */
        UPDATE,
        /**
         * 删除
         */
        DELETE
    }
}
