/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.gateway.filters;

import brave.Tracer;
import com.supermap.gaf.authentication.entity.entity.AuthenticationResult;
import com.supermap.gaf.gateway.commontypes.ExchangeAuthenticationAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static com.supermap.gaf.gateway.commontypes.constant.GatewayConst.EXCHANGE_AUTHENTICATION_ATTRIBUTE_NAME;
import static com.supermap.gaf.gateway.commontypes.constant.GatewayConst.GATEWAY_TRACE_FILTER_ORDER;

/**
 * 此拦截器主要是链路追踪追加信息的过滤器
 *
 * @author : duke
 * @date:2021/3/25
 * @since 2020/12/3 10:29 AM
 */
@Component
public class XgatewayTraceFilter implements GlobalFilter, Ordered {
    @Autowired
    private Tracer tracer;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ExchangeAuthenticationAttribute attribute = exchange.getAttribute(EXCHANGE_AUTHENTICATION_ATTRIBUTE_NAME);
        AuthenticationResult authenticationResult = attribute.getAuthenticationResult();

        if (null != authenticationResult && !StringUtils.isEmpty(authenticationResult.getUsername())) {
            tracer.currentSpan().tag("username", authenticationResult.getUsername());
        } else {
            tracer.currentSpan().tag("username", "Anonymous");
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return GATEWAY_TRACE_FILTER_ORDER;
    }
}
