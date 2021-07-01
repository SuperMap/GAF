/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.gateway.filters;

import com.supermap.gaf.authentication.client.ValidateClient;
import com.supermap.gaf.authentication.entity.entity.AuthenticationResult;
import com.supermap.gaf.authentication.entity.entity.AuthorizationParam;
import com.supermap.gaf.authority.enums.ResourceApiMethodEnum;
import com.supermap.gaf.gateway.commontypes.ExchangeAuthenticationAttribute;
import com.supermap.gaf.gateway.util.GafFluxUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static com.supermap.gaf.gateway.commontypes.constant.GatewayConst.EXCHANGE_AUTHENTICATION_ATTRIBUTE_NAME;
import static com.supermap.gaf.gateway.commontypes.constant.GatewayConst.GATEWAY_AUTHORIZATION_VALIDATE_FILTER_ORDER;

/**
 * 注意： 该代码对应gaf-boot中的同名的filter,功能逻辑等应该保持一致
 *
 * 此过滤器提供用户API鉴权的逻辑
 * 验证认证信息
 *      1.如果是indexurl或publicurl直接通过
 *      2.如果开启网关api验证，则请求接口验证是否有权限通过网关
 * @author : duke
 * @date:2021/3/25
 * @since 2020/11/23 3:44 PM
 */
@Component
public class XgatewayAuthorizationValidateFilter implements GlobalFilter, Ordered {
    @Autowired
    private ValidateClient validateClient;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ExchangeAuthenticationAttribute attribute = exchange.getAttribute(EXCHANGE_AUTHENTICATION_ATTRIBUTE_NAME);
        boolean apiAuthzEnabled = attribute.getGatewaySecurityProperties().isApiAuthzEnable();
        if (attribute.getIsPublicUrl() || attribute.getIsIndexUrl() || !apiAuthzEnabled){
            return chain.filter(exchange);
        }

        AuthenticationResult authenticationResult = attribute.getAuthenticationResult();
        AuthorizationParam authorizationParam = new AuthorizationParam();
        authorizationParam.setUsername(authenticationResult.getUsername());
        authorizationParam.setUri(exchange.getRequest().getURI().getPath());
        authorizationParam.setMethod(ResourceApiMethodEnum.valueOf(exchange.getRequest().getMethod().name()).getValue());

        Boolean result = validateClient.authorization(authorizationParam);
        if (!BooleanUtils.isTrue(result)){
            return GafFluxUtils.unAuth(exchange,"API资源访问权限不足");
        }
        return chain.filter(exchange);

    }


    @Override
    public int getOrder() {
        return GATEWAY_AUTHORIZATION_VALIDATE_FILTER_ORDER;
    }
}
