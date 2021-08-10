/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.gateway.filters;

import com.supermap.gaf.authentication.client.ValidateClient;
import com.supermap.gaf.authentication.entity.entity.AuthenticationParam;
import com.supermap.gaf.authentication.entity.entity.AuthenticationResult;
import com.supermap.gaf.commontypes.MessageResult;
import com.supermap.gaf.gateway.commontypes.ExchangeAuthenticationAttribute;
import com.supermap.gaf.gateway.util.GafFluxUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static com.supermap.gaf.gateway.commontypes.constant.GatewayConst.EXCHANGE_AUTHENTICATION_ATTRIBUTE_NAME;
import static com.supermap.gaf.gateway.commontypes.constant.GatewayConst.GATEWAY_AUTHENTICATION_QUERY_FILTER_ORDER;

/**
 * 注意： 该代码对应gaf-boot中的同名的filter,功能逻辑等应该保持一致
 * <p>
 * 此过滤器提供用户获取认证信息的逻辑
 * 1.获取认证信息
 *     静态资源和公共资源不用获取
 *
 * @author : duke
 * @date:2021/3/25
 * @since 2020/11/23 3:44 PM
 */
@Slf4j
@Component
public class XgatewayAuthenticationQueryFilter implements GlobalFilter, Ordered {

    @Autowired
    private ValidateClient validateClient;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ExchangeAuthenticationAttribute attribute = exchange.getAttribute(EXCHANGE_AUTHENTICATION_ATTRIBUTE_NAME);
        AuthenticationParam authenticationParam = null;
        if (attribute.getIsPublicUrl()) {
            return chain.filter(exchange);
        }
        authenticationParam = GafFluxUtils.getAuthenticationParamByServerHttpRequest(exchange.getRequest());
        if (authenticationParam != null) {
            MessageResult<AuthenticationResult> authenticationResultMessageResult = validateClient.authentication(authenticationParam);
            if (authenticationResultMessageResult != null) {
                attribute.setAuthenticationResult(authenticationResultMessageResult.getData());
            }
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return GATEWAY_AUTHENTICATION_QUERY_FILTER_ORDER;
    }
}
