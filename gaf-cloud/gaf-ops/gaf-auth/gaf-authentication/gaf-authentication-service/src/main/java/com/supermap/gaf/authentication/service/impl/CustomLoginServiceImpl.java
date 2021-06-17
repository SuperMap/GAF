/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.authentication.service.impl;

import com.supermap.gaf.authentication.entity.entity.properties.LoginOidcClientProperties;
import com.supermap.gaf.authentication.service.CustomLoginService;
import com.supermap.gaf.authentication.utils.HttpRequestUtils;
import com.supermap.gaf.authentication.utils.ThirdPartyLoginTypePropertiesHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerEndpointsConfiguration;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static com.supermap.gaf.authentication.entity.constant.LoginConstant.CUSTOM_CLIENT;
import static com.supermap.gaf.authentication.entity.constant.LoginConstant.REDIS_LOGIN_SESSION_PREFIX;
import static com.supermap.gaf.authentication.entity.constant.LoginConstant.TOKEN_REFRESH_EXPIRE;

/**
 * @author : duke
 * @date:2021/3/25
 * @since 2020/11/17 3:34 PM
 */
@Service
public class CustomLoginServiceImpl implements CustomLoginService {
    @Autowired
    private AuthorizationServerEndpointsConfiguration configuration;
    @Autowired
    private ThirdPartyLoginTypePropertiesHolder thirdPartyLoginTypePropertiesHolder;
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;



    @Override
    public OAuth2AccessToken createOauth2AccessTokenWithoutPassword(String username,String clientId) {
        String scope = "all";

        HashMap<String, String> authorizationParameters = new HashMap<String, String>(16);
        authorizationParameters.put("scope", scope);
        authorizationParameters.put("username", username);
        authorizationParameters.put("client_id", clientId);
        authorizationParameters.put("grant", "password");

        HashSet<String> resourceIds = new HashSet<String>();
        resourceIds.add("rid");

        Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("ROLE_CUSTOM_ACCESS_USER"));

        Set<String> responseType = new HashSet<String>();
        responseType.add("password");

        Set<String> scopes = new HashSet<String>();
        scopes.add(scope);

        OAuth2Request authorizationRequest = new OAuth2Request(
                authorizationParameters, clientId,
                authorities, true,scopes, resourceIds, "",
                responseType, null);

        User userPrincipal = new User(username, "", true, true, true, true, authorities);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                userPrincipal, null, authorities);

        OAuth2Authentication authenticationRequest = new OAuth2Authentication(authorizationRequest, authenticationToken);
        authenticationRequest.setAuthenticated(true);

        AuthorizationServerTokenServices tokenServices = configuration.getEndpointsConfigurer().getTokenServices();
        return tokenServices.createAccessToken(authenticationRequest);
    }

    @Override
    public OAuth2AccessToken createOauth2AccessTokenWithoutPassword(String username) {
        return createOauth2AccessTokenWithoutPassword(username,CUSTOM_CLIENT);
    }

    @Override
    public OAuth2AccessToken refreshOauth2AccessToken(String refreshToken) {
        String clientId = "custom_client";
        String scope = "all";
        String grantType = "refresh_token";
        String clientSecret = "123";

        HashMap<String, String> authorizationParameters = new HashMap<String, String>(16);
        authorizationParameters.put("scope", scope);
        authorizationParameters.put("client_id", clientId);
        authorizationParameters.put("grant_type", grantType);
        authorizationParameters.put("client_secret", clientSecret);

        Set<String> scopes = new HashSet<String>();
        scopes.add(scope);

        TokenRequest refreshTokenRequest = new TokenRequest(authorizationParameters,clientId,scopes,grantType);

        AuthorizationServerTokenServices tokenServices = configuration.getEndpointsConfigurer().getTokenServices();

        return tokenServices.refreshAccessToken(refreshToken,refreshTokenRequest);
    }

    @Override
    public Map<String,?> checkJwtToken(String token) {
        token = HttpRequestUtils.removeTokenBeareHead(token);
        if (StringUtils.isEmpty(token)){
            return null;
        }
        return configuration.checkTokenEndpoint().checkToken(token);
    }



    @Override
    public void storeLoginSession(String sessionId, String username,OAuth2AccessToken token, Map thirdPartyToken,String enabledThirdParty,String thirdPartyUsername) {
        String key = REDIS_LOGIN_SESSION_PREFIX + sessionId;
        if (null != username){
            redisTemplate.opsForHash().put(key,"username",username);
        }
        if (null != token){
            redisTemplate.opsForHash().put(key,"token",token);
        }
        if (null != thirdPartyToken){
            redisTemplate.opsForHash().put(key,"thirdPartyToken",thirdPartyToken);
        }
        if (null != enabledThirdParty){
            redisTemplate.opsForHash().put(key,"enabledThirdParty", enabledThirdParty);
        }
        if (null != thirdPartyUsername){
            redisTemplate.opsForHash().put(key,"thirdPartyUsername", thirdPartyUsername);
        }

        try {
            redisTemplate.expire(key,TOKEN_REFRESH_EXPIRE, TimeUnit.SECONDS);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void refreshStoreLoginSession(String sessionId, OAuth2AccessToken token) {
        String key = REDIS_LOGIN_SESSION_PREFIX + sessionId;
        if (null != token){
            redisTemplate.opsForHash().put(key,"token",token);
        }
        try {
            redisTemplate.expire(key,TOKEN_REFRESH_EXPIRE, TimeUnit.SECONDS);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public Map<String, Object> getLoginSessionById(String sessionId) {
        Map map = redisTemplate.opsForHash().entries(REDIS_LOGIN_SESSION_PREFIX+sessionId);
        Map<String,Object> result = new HashMap<>(16);
        map.keySet().forEach(key -> result.put(key.toString(),map.get(key)));
        return result;
    }


    @Override
    public Map<String, String> getThirdPartyTokenByCode(String code, String enabledThirdParty) {
        LoginOidcClientProperties.OidcClientInfo oidcClientInfo = (LoginOidcClientProperties.OidcClientInfo)thirdPartyLoginTypePropertiesHolder.getThirdPartyContext().get(enabledThirdParty.toLowerCase());

        LoginOidcClientProperties.Provider provider = oidcClientInfo.getProvider();
        LoginOidcClientProperties.Registration registration = oidcClientInfo.getRegistration();

        MultiValueMap body = new LinkedMultiValueMap();
        body.set("grant_type","authorization_code");
        body.set("client_id", registration.getClientId());
        body.set("client_secret", registration.getClientSecret());
        body.set("redirect_uri", registration.getRedirectUri());
        body.set("code",code);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity req = new HttpEntity<>(body,httpHeaders);
        Map<String,String> tokenRes = restTemplate.postForObject(provider.getTokenUri(),req,Map.class);
        return tokenRes;
    }
}
