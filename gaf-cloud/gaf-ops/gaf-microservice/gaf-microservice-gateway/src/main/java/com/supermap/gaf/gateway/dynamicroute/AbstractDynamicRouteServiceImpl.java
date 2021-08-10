package com.supermap.gaf.gateway.dynamicroute;

import com.supermap.gaf.commontypes.MessageResult;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @program: gaf-service-manager-modules
 * @description: 动态路由服务
 * @author: lidong
 * @create: 2019/07/15
 */
public abstract class AbstractDynamicRouteServiceImpl implements ApplicationEventPublisherAware {

    public ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    /**
     * 获取路由列表
     *
     * @return
     */
    public abstract List<RouteDefinition> getRouteDefinitions();


    /**
     * 增加路由
     *
     * @param definition
     * @return
     */
    public abstract MessageResult<String> add(RouteDefinition definition);

    /**
     * 更新路由:先删除在重新添加
     *
     * @param definition
     * @return
     */
    public abstract MessageResult<String> update(RouteDefinition definition);

    /**
     * 删除路由
     *
     * @param id
     * @return
     */
    public abstract MessageResult<Mono<ResponseEntity<Object>>> delete(String id);

    /**
     * 修改路由后将绑定到刷新事件
     */
    public void notifyChanged() {
        this.applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this));
    }

}
