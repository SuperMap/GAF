/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.gateway.filters;

import com.supermap.gaf.authentication.entity.entity.AuthenticationResult;
import com.supermap.gaf.gateway.commontypes.ExchangeAuthenticationAttribute;
import com.supermap.gaf.gateway.util.GafFluxUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;

import static com.supermap.gaf.gateway.commontypes.constant.GatewayConst.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

/**
 *  注意： 该代码对应gaf-boot中的同名的filter,功能逻辑等应该保持一致
 *
 * 此拦截器主要在获取到认证信息后，将认证信息token添加到请求头中
 * @author : duke
 * @date:2021/3/25
 * @since 2020/11/23 4:29 PM
 */
@Component
public class XgatewayRequestTokenFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ExchangeAuthenticationAttribute attribute = exchange.getAttribute(EXCHANGE_AUTHENTICATION_ATTRIBUTE_NAME);
        AuthenticationResult authenticationResult = attribute.getAuthenticationResult();

        if (null != authenticationResult && !StringUtils.isEmpty(authenticationResult.getJwtToken())){
            String token = authenticationResult.getJwtToken();
            token = GafFluxUtils.removeTokenBeareHead(token);
            ServerHttpRequest newRequest = exchange.getRequest().mutate().headers(addHttpHeadersAuth(token)).build();
            return chain.filter(exchange.mutate().request(newRequest).build());
        }else {
            return chain.filter(exchange);
        }
    }

    private Consumer<HttpHeaders> addHttpHeadersAuth(String token) {
        return httpHeader -> {
            httpHeader.remove(AUTHORIZATION);
            httpHeader.add(AUTHORIZATION, BEARER + " " + token);
        };
    }

    @Override
    public int getOrder() {
        return GATEWAY_REQUEST_TOKEN_FILTER_ORDER;
    }
}
