/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.gateway.filters;

import com.supermap.gaf.gateway.commontypes.ExchangeAuthenticationAttribute;
import com.supermap.gaf.gateway.commontypes.properties.GatewaySecurityProperties;
import com.supermap.gaf.gateway.util.AccessValidUtils;
import com.supermap.gaf.gateway.util.GafFluxUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.supermap.gaf.gateway.commontypes.constant.GatewayConst.EXCHANGE_AUTHENTICATION_ATTRIBUTE_NAME;
import static com.supermap.gaf.gateway.commontypes.constant.GatewayConst.SEPARATOR;

/**
 * 注意： 该代码对应gaf-boot中的同名的filter,功能逻辑等应该保持一致
 * 此过滤器提供的重定向逻辑
 * 这些重定向逻辑只是暂时存在于域名直接指向网关的情况下
 * 1.'/'请求，跳转到indexUrl
 * 2.gatewayLoginUrl请求，跳转到centerLoginUrl
 * 3.gatewayLogoutUrl请求，跳转到centerLogoutUrl
 *
 * @author : duke
 * @date:2021/3/25
 * @since 2020/11/23 3:29 PM
 */
@Component
@EnableConfigurationProperties({GatewaySecurityProperties.class})
public class XwebRedirectFilter implements WebFilter {
    @Autowired
    private GatewaySecurityProperties gatewaySecurityProperties;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String uri = exchange.getRequest().getPath().toString();

        //入口filter填充uri基础属性
        boolean isIndexUrl = uri.startsWith(gatewaySecurityProperties.getIndexUrl());
        List<String> publicUrls = gatewaySecurityProperties.getPublicUrls();
        boolean isPublicUri = AccessValidUtils.isPublicUrls(uri, publicUrls);
        ExchangeAuthenticationAttribute attribute = new ExchangeAuthenticationAttribute();
        attribute.setIsIndexUrl(isIndexUrl);
        attribute.setIsPublicUrl(isPublicUri);
        attribute.setGatewaySecurityProperties(gatewaySecurityProperties);
        attribute.setUri(uri);
        exchange.getAttributes().put(EXCHANGE_AUTHENTICATION_ATTRIBUTE_NAME, attribute);

        if (uri.equals(SEPARATOR)) {
            return GafFluxUtils.redirectTo(exchange, gatewaySecurityProperties.getIndexUrl());
        } else if (uri.equals(gatewaySecurityProperties.getGatewayLoginUrl())) {
            return GafFluxUtils.redirectTo(exchange, gatewaySecurityProperties.getCenterLoginUrl());
        } else if (uri.equals(gatewaySecurityProperties.getGatewayLogoutUrl())) {
            return GafFluxUtils.redirectTo(exchange, gatewaySecurityProperties.getCenterLogoutUrl());
        } else {
            return chain.filter(exchange);
        }
    }
}
