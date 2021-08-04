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
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static com.supermap.gaf.gateway.commontypes.constant.GatewayConst.*;

/**
 * 注意： 该代码对应gaf-boot中的同名的filter,功能逻辑等应该保持一致
 *
 * 此过滤器提供用户验证认证信息的逻辑
 * 验证认证信息
 *      1.1.静态资源和公共资源不用验证(index页面必须验证)
 *      1.2.其他都需要验证
 *           1.2.1验证失败需要清除cookie
 *           1.2.2验证失败,返回401
 * @author : duke
 * @date:2021/3/25
 * @since 2020/11/23 3:44 PM
 */
@Component
public class XgatewayAuthenticationValidateFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ExchangeAuthenticationAttribute attribute = exchange.getAttribute(EXCHANGE_AUTHENTICATION_ATTRIBUTE_NAME);
        AuthenticationResult authenticationResult = attribute.getAuthenticationResult();
        if (attribute.getIsPublicUrl()){
            return chain.filter(exchange);
        }else if (authenticationResult == null
                || StringUtils.isEmpty(authenticationResult.getUsername())
                || StringUtils.isEmpty(authenticationResult.getJwtToken())){
            removeCookie(exchange);
            return GafFluxUtils.unAuth(exchange,"未获取到资源访问的认证身份");
        }else {
            return chain.filter(exchange);
        }
    }

    /**
     * 清楚cookie
     * @param exchange
     */
    private void removeCookie(ServerWebExchange exchange){
        exchange.getResponse().addCookie(ResponseCookie.from(CUSTOM_LOGIN_SESSION_NAME,null).path("/").maxAge(0).build());
    }

    @Override
    public int getOrder() {
        return GATEWAY_AUTHENTICATION_VALIDATE_FILTER_ORDER;
    }
}