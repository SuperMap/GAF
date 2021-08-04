/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.gateway.filters;

import com.supermap.gaf.utils.LogUtil;
import org.slf4j.Logger;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


/**
 * @author : duke
 * @date:2021/3/25 放行逻辑是，判断uri是否是resource，如果是resource列表里面，判断用户是否拥有此资源的权限
 * @since 2020/4/18 1:30 PM
 * GlobalFilter优先级低于security自己加的webFilter
 * 所以进入此filter的除了publicUrl、静态资源，就是已登录过的,或者请求登录的
 */
public class AccessValidFilter implements GlobalFilter, Ordered {

    private Logger log = LogUtil.getLocLogger(com.supermap.gaf.gateway.filters.AccessValidFilter.class);
    private static final String TOKEN_USERID_CLIM = "sub";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        //-90是jwttokenfilter，先于修改修改http请求头
        return -100;
    }
}
