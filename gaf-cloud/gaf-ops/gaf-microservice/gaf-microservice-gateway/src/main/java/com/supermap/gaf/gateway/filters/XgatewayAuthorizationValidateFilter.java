/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.gateway.filters;

import com.supermap.gaf.authentication.client.ValidateClient;
import com.supermap.gaf.authentication.entity.entity.AuthenticationResult;
import com.supermap.gaf.authentication.entity.entity.AuthorizationParam;
import com.supermap.gaf.authority.client.AuthUserClient;
import com.supermap.gaf.authority.enums.ResourceApiMethodEnum;
import com.supermap.gaf.gateway.commontypes.ExchangeAuthenticationAttribute;
import com.supermap.gaf.gateway.util.GafFluxUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;

import static com.supermap.gaf.gateway.commontypes.constant.GatewayConst.EXCHANGE_AUTHENTICATION_ATTRIBUTE_NAME;
import static com.supermap.gaf.gateway.commontypes.constant.GatewayConst.GATEWAY_AUTHORIZATION_VALIDATE_FILTER_ORDER;

/**
 * 注意： 该代码对应gaf-boot中的同名的filter,功能逻辑等应该保持一致
 * <p>
 * 此过滤器提供用户API鉴权的逻辑
 * 验证认证信息
 * 1.如果是indexurl或publicurl直接通过
 * 2.如果开启网关api验证，则请求接口验证是否有权限通过网关
 *
 * @author : duke
 * @date:2021/3/25
 * @since 2020/11/23 3:44 PM
 */
@Component
public class XgatewayAuthorizationValidateFilter implements GlobalFilter, Ordered {
    private static final String STORAGE_FILTER_URL_REGIX = "^/storage/api/tenant[^/]*/.*";
    private static final String STORAGE_PERMISSION_HEADER = "PERMISSION";
    private static final String STORAGE_TENANTID_HEADER = "TENANT_ID";

    @Autowired
    private ValidateClient validateClient;
    @Autowired
    private AuthUserClient authUserClient;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ExchangeAuthenticationAttribute attribute = exchange.getAttribute(EXCHANGE_AUTHENTICATION_ATTRIBUTE_NAME);
        if (attribute.getIsPublicUrl() || attribute.getIsIndexUrl() || attribute.getIsProfileUrl()){
            return chain.filter(exchange);
        }
        AuthenticationResult authenticationResult = attribute.getAuthenticationResult();
        AuthorizationParam authorizationParam = new AuthorizationParam();
        authorizationParam.setUsername(authenticationResult.getUsername());
        authorizationParam.setUri(exchange.getRequest().getURI().getPath());
        authorizationParam.setMethod(ResourceApiMethodEnum.valueOf(exchange.getRequest().getMethod().name()).getValue());
        ServerHttpRequest newRequest = null;
        // 文件权限
        if (authorizationParam.getUri().startsWith("/storage/")) {
            Map<String,Object> infos = authUserClient.someInfo(authenticationResult.getUsername()).getData();
            String roleIds = (String) infos.get(AuthUserClient.SOME_INFO_ROLE_IDS_KEY);
            String tenantId = (String) infos.get(AuthUserClient.SOME_INFO_TENANT_ID_KEY);
            ServerHttpRequest.Builder builder = exchange.getRequest().mutate()
                    .header(STORAGE_TENANTID_HEADER,tenantId);
            if(authorizationParam.getUri().matches(STORAGE_FILTER_URL_REGIX)){
                if(!StringUtils.isEmpty(roleIds)){
                    builder.header(STORAGE_PERMISSION_HEADER,roleIds);
                }
                return chain.filter(exchange.mutate().request(builder.build()).build());
            }
            newRequest = builder.build();
        }
        boolean apiAuthzEnabled = attribute.getGatewaySecurityProperties().isApiAuthzEnable();
        if (!apiAuthzEnabled) {
            return newRequest==null?chain.filter(exchange):chain.filter(exchange.mutate().request(newRequest).build());
        }

        Boolean result = validateClient.authorization(authorizationParam);
        if (!BooleanUtils.isTrue(result)) {
            return GafFluxUtils.unAuth(exchange, "API资源访问权限不足");
        }
        return newRequest==null?chain.filter(exchange):chain.filter(exchange.mutate().request(newRequest).build());

    }


    @Override
    public int getOrder() {
        return GATEWAY_AUTHORIZATION_VALIDATE_FILTER_ORDER;
    }
}
