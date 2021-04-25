/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.srv.governance.rest.client;

import com.supermap.gaf.commontypes.MessageResult;
import com.supermap.gaf.gateway.GatewayRouteDefinition;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author : duke
 * @date:2021/3/25
 * @since 2020/12/05 5:43 PM
 */
@FeignClient(name = "GAF-MICROSERVICE-GATEWAY")
@Component
public interface GafGatewayClient {

    /**
     * 查询自定义网关路由
     * @param routeSearchParam
     * @return
     */
    @GetMapping("/routes")
    MessageResult<List<GatewayRouteDefinition>> queryTenantGatewayRoutes(@RequestParam("routeSearchParam") String routeSearchParam);

    /**
     * Id为参数查询自定义网关路由
     * @param id
     * @return
     */
    @GetMapping("/routes/{id}")
    MessageResult<GatewayRouteDefinition> queryTenantGatewayRoute(@PathVariable("id") String id);

    /**
     * 新增自定义网关路由
     * @param route
     * @return
     */
    @PostMapping("/routes")
    MessageResult<String> addTenantRoute(@RequestBody GatewayRouteDefinition route);

    /**
     * 批量删除路由
     * @param idsJsonStr
     * @return
     */
    @DeleteMapping("/routes")
    MessageResult<String> batchDeleteTenantRoute(@RequestBody String idsJsonStr);

    /**
     * ID删除路由
     * @param id
     * @return
     */
    @DeleteMapping("/routes/{id}")
    MessageResult<String> deleteTenantRoute(@PathVariable("id") String id);

    /**
     * 编辑路由
     * @param route
     * @return
     */
    @PutMapping("/routes")
    MessageResult<String> updateTenantRoute(@RequestBody GatewayRouteDefinition route);
}



