/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.gateway.filters;

import com.supermap.gaf.utils.LogUtil;
import org.slf4j.Logger;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.*;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

/**
 * @author dqc
 * @date:2021/3/25 此过滤器主要是将token从网关传递到微服务，微服务解析这个token获得登录认证信息
 */
public class JwtTokenFilter implements GlobalFilter, Ordered {

    public static String JWT_PREFIX = "Bearer";
    private static Logger logger = LogUtil.getLocLogger(JwtTokenFilter.class);


    /**
     * 过滤器
     *
     * @param exchange
     * @param chain
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return chain.filter(exchange);
    }


    /**
     * 请求头带oauth token的请求
     *
     * @param chain
     * @param exchange
     * @param token
     * @return
     */
    private Mono<Void> appendTokenHeader(GatewayFilterChain chain, ServerWebExchange exchange, String token) {
        //判断是否存在auth请求头，如果存在，不添加
        List<String> authHeader = exchange.getRequest().getHeaders().get(AUTHORIZATION);
        if (authHeader != null && !authHeader.isEmpty()) {
            return chain.filter(exchange);
        } else {
            ServerHttpRequest newRequest = exchange.getRequest().mutate().header(AUTHORIZATION, JWT_PREFIX + " " + token).build();
            return chain.filter(exchange.mutate().request(newRequest).build());
        }
    }


//    /**
//     * 通过网关的security上下文中取得token
//     * @param session
//     * @param exchange
//     * @return
//     */
//    private Mono<String> resolverToken(WebSession session, ServerWebExchange exchange) {
//        SecurityContext securityContext = (SecurityContext) session.getAttributes().get(SPRING_SECURITY_CONTEXT_KEY);
//        if (securityContext != null && securityContext.getAuthentication().isAuthenticated()) {
//            if (securityContext.getAuthentication() instanceof OAuth2AuthenticationToken) {
//                OAuth2AuthenticationToken authenticationToken = (OAuth2AuthenticationToken) securityContext.getAuthentication();
//                Mono<OAuth2AuthorizedClient> client = authorizedClientService.loadAuthorizedClient(authenticationToken.getAuthorizedClientRegistrationId(),
//                        authenticationToken, exchange);
//                return client.map(oClient -> {
//                    Instant now = Instant.now();
//                    if (now.isBefore(oClient.getAccessToken().getExpiresAt())) {
//                        return oClient.getAccessToken().getTokenValue();
//                    } else {
//                        return refreshToken(oClient, securityContext.getAuthentication(), exchange);
//                    }
//                });
//            }
//        }
//        if (exchange.getRequest().getHeaders().containsKey(AUTHORIZATION)) {
//            String jwtToken = exchange.getRequest().getHeaders().get(AUTHORIZATION).get(0);
//            if (jwtToken.startsWith(JWT_PREFIX)) {
//                return Mono.just(jwtToken.substring(JWT_PREFIX.length()).trim());
//            }
//        }
//        return Mono.just("");
//    }
//
//    private String refreshToken(OAuth2AuthorizedClient authorizedClient, Authentication authentication, ServerWebExchange exchange) {
////        Authentication authentication = r.getAuthentication();
//        ClientRegistration clientRegistration = authorizedClient.getClientRegistration();
//        String tokenUri = clientRegistration.getProviderDetails().getTokenUri();
//        RestTemplate restTemplate = new RestTemplate();
//        String clientAndSecret = clientRegistration.getClientId() + ":" + clientRegistration.getClientSecret();
//        // 这里需要注意为 Basic 而非 Bearer
//        clientAndSecret = "Basic " + Base64.getEncoder().encodeToString(clientAndSecret.getBytes());
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.set(AUTHORIZATION, clientAndSecret);
//
//        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
//        map.put("grant_type", Collections.singletonList(AuthorizationGrantType.REFRESH_TOKEN.getValue()));
//        map.put("refresh_token", Collections.singletonList(authorizedClient.getRefreshToken().getTokenValue()));
//        // HttpEntity
//        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<MultiValueMap<String, String>>(map, httpHeaders);
//        Map<String, Object> jsonResult = restTemplate
//                .exchange(URI.create(tokenUri), HttpMethod.POST, httpEntity, new ParameterizedTypeReference<Map<String, Object>>() {
//                }).getBody();
//        OAuth2AccessTokenResponse accessTokenResponse = oauth2AccessTokenResponse(parse(jsonResult));
//        if(accessTokenResponse != null) {
//         // 获取 Token
//            authorizedClientService.saveAuthorizedClient(new OAuth2AuthorizedClient(authorizedClient.getClientRegistration(), authorizedClient.getPrincipalName(),
//                    accessTokenResponse.getAccessToken(), accessTokenResponse.getRefreshToken()), authentication, exchange);
//            return accessTokenResponse.getAccessToken().getTokenValue();
//        }
//        return "";
//    }
//
//    private AccessTokenResponse parse(Map<String, Object> json) {
//        try {
//            TokenResponse response = TokenResponse.parse(new JSONObject(json));
//            if (response.indicatesSuccess()) {
//                return (AccessTokenResponse) response;
//            }
//        } catch (ParseException pe) {
//        }
//        return null;
//    }

//    private static OAuth2AccessTokenResponse oauth2AccessTokenResponse(AccessTokenResponse accessTokenResponse) {
//        if (accessTokenResponse != null) {
//            AccessToken accessToken = accessTokenResponse.getTokens().getAccessToken();
//            OAuth2AccessToken.TokenType accessTokenType = null;
//            if (OAuth2AccessToken.TokenType.BEARER.getValue().equalsIgnoreCase(accessToken.getType().getValue())) {
//                accessTokenType = OAuth2AccessToken.TokenType.BEARER;
//            }
//            long expiresIn = accessToken.getLifetime();
//
//            Set<String> scopes = accessToken.getScope() == null ? Collections.emptySet() : new LinkedHashSet<>(accessToken.getScope().toStringList());
//
//            String refreshToken = null;
//            if (accessTokenResponse.getTokens().getRefreshToken() != null) {
//                refreshToken = accessTokenResponse.getTokens().getRefreshToken().getValue();
//            }
//
//            Map<String, Object> additionalParameters = new LinkedHashMap<>(accessTokenResponse.getCustomParameters());
//
//            return OAuth2AccessTokenResponse.withToken(accessToken.getValue()).tokenType(accessTokenType).expiresIn(expiresIn).scopes(scopes)
//                    .refreshToken(refreshToken).additionalParameters(additionalParameters).build();
//        }
//        return null;
//    }

    @Override
    public int getOrder() {
        // TODO Auto-generated method stub
        return -90;
    }

    static class TokenHolder {
        public String token;
    }
}
