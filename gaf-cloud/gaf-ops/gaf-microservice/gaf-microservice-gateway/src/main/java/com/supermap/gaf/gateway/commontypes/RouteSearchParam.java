/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.gateway.commontypes;

import com.supermap.gaf.gateway.GatewayRouteType;

import java.util.List;

/**
 * @program: gaf-service-manager-modules
 * @description:
 * @author: lidong
 * @date:2021/3/25
 * @create: 2019/07/17
 */
public class RouteSearchParam {
    private String tenantId;
    private GatewayRouteType type;
    private boolean enable = true;
    private List <String> ids;

    public List <String> getIds() {
        return ids;
    }

    public void setIds(List <String> ids) {
        this.ids = ids;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public GatewayRouteType getType() {
        return type;
    }

    public void setType(GatewayRouteType type) {
        this.type = type;
    }

    public Boolean isEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }
}
