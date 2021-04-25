/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.gateway.dynamicroute;

import com.supermap.gaf.commontypes.MessageResult;
import com.supermap.gaf.utils.LogUtil;
import org.slf4j.Logger;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @program: gaf-service-manager-modules
 * @description: 动态路由服务
 * @author: lidong
 * @date:2021/3/25
 * @create: 2019/07/15
 */
public class DefaultAbstractDynamicRouteServiceImpl extends AbstractDynamicRouteServiceImpl {
    private static Logger logger = LogUtil.getLocLogger(DefaultAbstractDynamicRouteServiceImpl.class);

    private RouteDefinitionRepository routeDefinitionWriter;

    public DefaultAbstractDynamicRouteServiceImpl(RouteDefinitionRepository routeDefinitionWriter) {
        this.routeDefinitionWriter = routeDefinitionWriter;
    }

    @Override
    public List <RouteDefinition> getRouteDefinitions() {
        return routeDefinitionWriter.getRouteDefinitions().collectList().block();
    }


    /**
     * 增加路由
     *
     * @param definition
     * @return
     */
    @Override
    public MessageResult<String> add(RouteDefinition definition) {
        MessageResult<String> result = new MessageResult<>();
        try {
            routeDefinitionWriter.save(Mono.just(definition)).subscribe();
            this.notifyChanged();
            result.setSuccessed(true);
            result.setMessage("路由添加成功");
        } catch (Exception e) {
            logger.error("添加失败" + e.getMessage(), e);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    /**
     * 更新路由:先删除在重新添加
     *
     * @param definition
     * @return
     */
    @Override
    public MessageResult<String> update(RouteDefinition definition) {
        MessageResult<String> result = new MessageResult<>();
        try {
            MessageResult deleteRst = delete(definition.getId());
            if (!deleteRst.IsSuccessed()) {
                throw new Exception(deleteRst.getMessage());
            }
        } catch (Exception e) {
            result.setMessage("更新失败，找不到路由信息routeId: " + definition.getId());
        }
        try {
            routeDefinitionWriter.save(Mono.just(definition)).subscribe();
            this.notifyChanged();
            result.setSuccessed(true);
            result.setMessage("路由更新成功");
        } catch (Exception e) {
            result.setMessage("路由更新失败");
        }
        return result;
    }

    /**
     * 删除路由
     *
     * @param id
     * @return
     */
    @Override
    public MessageResult<Mono <ResponseEntity <Object>>> delete(String id) {
        MessageResult<Mono <ResponseEntity <Object>>> result = new MessageResult<>();
        try {
            Mono <ResponseEntity <Object>> mono = this.routeDefinitionWriter.delete(Mono.just(id)).then(Mono.defer(() -> {
                result.setMessage("删除成功");
                notifyChanged();
                return Mono.just(ResponseEntity.ok().build());
            })).onErrorResume((t) -> {
                result.setMessage("删除失败");
                return t instanceof NotFoundException;
            }, (t) -> {
                return Mono.just(ResponseEntity.notFound().build());
            });
            result.setData(mono);
        } catch (Exception e) {
            logger.error("删除异常：" + e.getMessage(), e);
            result.setMessage("删除异常：" + e.getMessage());
        }
        return result;
    }

}
