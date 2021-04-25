/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.gateway.util;

import com.alibaba.fastjson.JSON;
import com.supermap.gaf.authentication.entity.entity.AuthenticationParam;
import com.supermap.gaf.commontypes.MessageResult;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.nio.charset.StandardCharsets;

import static com.supermap.gaf.gateway.commontypes.constant.GatewayConst.BEARER;
import static com.supermap.gaf.gateway.commontypes.constant.GatewayConst.CUSTOM_LOGIN_SESSION_NAME;
import static javax.ws.rs.core.HttpHeaders.AUTHORIZATION;


/**
 * @author : duke
 * @date:2021/3/25
 * @since 2020/4/18 2:17 PM
 */
public class GafFluxUtils {

    /**
     * 得到无访问权限响应体
     * @param exchange
     * @return
     */
    public static Mono<Void> unAuth(ServerWebExchange exchange){
        MessageResult<String> result = MessageResult.failed(String.class).status(HttpStatus.UNAUTHORIZED.value()).message("访问权限不足").build();
        ServerHttpResponse response = exchange.getResponse();
        byte[] bits = JSON.toJSONString(result).getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(bits);
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        // 指定编码，否则在浏览器中会中文乱码
        response.getHeaders().add("Content-Type", "text/plain;charset=UTF-8");
        return response.writeWith(Mono.just(buffer));
    }

    /**
     * 重定向
     * @param exchange
     * @param url
     * @return
     */
    public static Mono<Void> redirectTo(ServerWebExchange exchange, String url) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.FOUND);
        response.getHeaders().setLocation(URI.create(url));
        return response.writeAndFlushWith(Mono.empty());
    }



    /**
     * request中获取认证参数
     * @param request
     * @return
     */
    public static AuthenticationParam getAuthenticationParamByServerHttpRequest(ServerHttpRequest request){
        AuthenticationParam param = null;

        String authorization = request.getHeaders().getFirst(AUTHORIZATION);
        HttpCookie cookie = request.getCookies().getFirst(CUSTOM_LOGIN_SESSION_NAME);

        String cookieVal = cookie == null ? null:cookie.getValue();

        if (!StringUtils.isEmpty(cookieVal) || !StringUtils.isEmpty(authorization)){
            param = new AuthenticationParam();
            param.setCustomSessionId(cookieVal);
            param.setAuthorization(authorization);
        }
        return param;
    }

    public static String removeTokenBeareHead(String token){
        if (token != null && token.startsWith(BEARER)){
            token = token.substring(BEARER.length()).trim();
        }
        return token;
    }


//    /**
//     * WebSession获取SecurityContext
//     * @param session
//     * @return
//     */
//    public static SecurityContext getSecurityContext(WebSession session){
//        return (SecurityContext) session.getAttributes().get(SPRING_SECURITY_CONTEXT_KEY);
//    }
}
