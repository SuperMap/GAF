/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.gateway.filters;

import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.NettyWriteResponseFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static com.supermap.gaf.gateway.commontypes.constant.GatewayConst.SEPARATOR;

/**
 * 注意： 该代码对应gaf-boot中的同名的filter,功能逻辑等应该保持一致
 * @author : duke
 * @date:2021/3/25
 * @since 2020/11/22 11:28 PM
 */
@Component
public class WrapperRedirectResponseGlobalFilter implements GlobalFilter, Ordered {

    private static final int LOCATION_CHAR_NUMBER =  8;

    @Override
    public int getOrder() {
        return NettyWriteResponseFilter.WRITE_RESPONSE_FILTER_ORDER - 1;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpResponse originalResponse = exchange.getResponse();
        URI uri = exchange.getRequest().getURI();
        ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
            @SuppressWarnings("serial")
            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                switch (getDelegate().getStatusCode().value()) {
                    case 302:
                    case 301:
                        HttpHeaders headers = getDelegate().getHeaders();
                        String location = headers.getFirst(HttpHeaders.LOCATION);
                        int i = -1;
                        if (StringUtils.isEmpty(location) || (i = location.indexOf(SEPARATOR, LOCATION_CHAR_NUMBER)) == -1) {
                            break;
                        }

                        String newLocation = uri.getScheme() + "://" + uri.getHost() + ":" + (uri.getPort() > 0 ? uri.getPort() : 80) + location.substring(i);
                        List<String> newLocationList = new ArrayList<>();
                        newLocationList.add(newLocation);
                        headers.put(HttpHeaders.LOCATION, newLocationList);
                        break;
                    default:
                        break;
                }
                return super.writeWith(body);
            }
        };
        // replace response with decorator
        ServerHttpRequest request = exchange.getRequest();
        request = exchange.getRequest().mutate().header("SourceHost", request.getHeaders().get("Host").get(0)).build();
        return chain.filter(exchange.mutate().request(request).response(decoratedResponse).build());
    }
}
