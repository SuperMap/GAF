/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.gateway.filters;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * 注意： 该代码对应gaf-boot中的同名的filter,功能逻辑等应该保持一致
 * @date:2021/3/25
 * @author dqc
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SecurityHeaderFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange swe, WebFilterChain wfc) {
        ServerHttpRequest request = swe.getRequest();
        ServerHttpResponse response = swe.getResponse();
        HttpHeaders headers = response.getHeaders();
        headers.add("X-Content-Type-Options", "nosniff");
        if (request.getMethod() == HttpMethod.OPTIONS) {
            response.setStatusCode(HttpStatus.OK);
            return Mono.empty();
        }
        return wfc.filter(swe);
    }
}
