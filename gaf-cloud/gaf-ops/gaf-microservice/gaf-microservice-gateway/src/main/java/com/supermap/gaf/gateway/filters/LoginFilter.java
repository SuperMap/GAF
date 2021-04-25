/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.gateway.filters;


import com.supermap.gaf.gateway.commontypes.properties.GatewaySecurityProperties;
import com.supermap.gaf.utils.LogUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * @author dqc
 * security检测是否登录跳转的过滤器
 * 无此过滤器，发起请求，会自动跳转第三方登录，然后访问请求的地址
 * 有此过滤器，登录只能通过loginUrl或/login登录，然后重定向到indexUrl
 * @date:2021/3/25
 * 如果登录或者请求js、html、publicUrl，此过滤放行
 */
public class LoginFilter implements WebFilter {
    private Logger logger = LogUtil.getLocLogger(com.supermap.gaf.gateway.filters.LoginFilter.class);
    private static final String AUTH_LOGIN_URL = "oauth2/authorization/okta";
    private static final String FINAL_LOGIN_URL = "login/oauth2/code/okta";

    private String loginUrl;
    private String indexUrl;
    private String[] publicUrls;

    public LoginFilter(String loginUrl, String indexUrl, String homeUrl, String[] publicUrls) {
        this.loginUrl = StringUtils.isNotEmpty(loginUrl) ? loginUrl : GatewaySecurityProperties.DEFAULT_LOGIN_URL;
        this.indexUrl = StringUtils.isNotEmpty(indexUrl) ? indexUrl : GatewaySecurityProperties.DEFAULT_INDEX_URL;
        this.publicUrls = publicUrls;
    }

    /**
     * 筛选登录请求和/请求的跳转逻辑
     * @param exchange
     * @param chain
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String urlPath = exchange.getRequest().getPath().toString();
        return chain.filter(exchange);
//        //如果是登录请求
//        if (urlPath.equals(loginUrl)) {
//            return exchange.getSession().flatMap(session -> {
//                //如果已登录,跳转到index
//                if (isAuthed(session)) {
//                    return redirectTo(exchange, indexUrl);
//                } else {
//                    //如果没登录，跳转第三方登录界面
//                    return redirectTo(exchange, AUTH_LOGIN_URL);
//                }
//            });
//        }
//        //如果是/根目录请求
//        if (exchange.getRequest().getPath().toString().equals("/")) {
//            return exchange.getSession().flatMap(session -> {
//                if (isAuthed(session)){
//                    //如果登录过跳转到view
//                    return redirectTo(exchange, homeUrl);
//                }else {
//                    //没登录跳转到iportal
//                    return redirectTo(exchange, indexUrl);
//                }
//            });
//        }
//        //如果是其他请求，如果是publicUrl直接通过，如果不是，运行下面的401过滤器方法
//        return ServerWebExchangeMatchers.pathMatchers(publicUrls).matches(exchange)
//                .flatMap(result -> {
//                    if(result.isMatch()) {
//                        return chain.filter(exchange);
//                    }else {
//                        return filterUnAuthorized(urlPath, exchange, chain);
//                    }
//                });
    }
//
//    /**
//     * 从session验证是否登录
//     * @param session
//     * @return
//     */
//    private boolean isAuthed(WebSession session){
//        if (session != null && session.getAttributes().get(SPRING_SECURITY_CONTEXT_KEY) != null
//                && ((SecurityContext) session.getAttributes().get(SPRING_SECURITY_CONTEXT_KEY)).getAuthentication() != null
//                && ((SecurityContext) session.getAttributes().get(SPRING_SECURITY_CONTEXT_KEY)).getAuthentication().isAuthenticated()) {
//            return true;
//        }else {
//            return false;
//        }
//    }

//    /**
//     * 有登录用户信息，直接通过
//     * 非publicUrl的请求里,后缀为json，url为/，登录请求地址，请求头带authorization之外的所有请求，先删除session数据，然后返回401
//     * @param urlPath
//     * @param exchange
//     * @param chain
//     * @return
//     */
//    private Mono<Void> filterUnAuthorized(String urlPath, ServerWebExchange exchange, WebFilterChain chain) {
//        return exchange.getSession().flatMap(session -> {
//            // 非.js,.html等请求，直接返回401
//            if (urlPath.indexOf(".") == -1 && !urlPath.contains(".json")
//                    && !urlPath.contains(AUTH_LOGIN_URL) && !urlPath.contains(FINAL_LOGIN_URL)
//                    && !exchange.getRequest().getHeaders().containsKey(AUTHORIZATION)) {
//                if (session == null
//                        || session.getAttributes().get(SPRING_SECURITY_CONTEXT_KEY) == null
//                        || ((SecurityContext) session.getAttributes().get(SPRING_SECURITY_CONTEXT_KEY)).getAuthentication() == null
//                        || !((SecurityContext) session.getAttributes().get(SPRING_SECURITY_CONTEXT_KEY)).getAuthentication().isAuthenticated()) {
//                    return unAuth(exchange);
//                }
//            }
//            //如果有用户信息，或者带authorization头，直接通过
//            return chain.filter(exchange);
//        });
//    }

}
