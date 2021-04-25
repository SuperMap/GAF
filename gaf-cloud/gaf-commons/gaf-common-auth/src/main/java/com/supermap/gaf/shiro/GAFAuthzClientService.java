/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.shiro;

import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.representations.idm.authorization.AuthorizationResponse;
import org.pac4j.oauth.profile.OAuth20Profile;
import org.pac4j.oidc.profile.OidcProfile;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.supermap.gaf.utils.LogUtil;


/**
 * @author:yj
 * @date:2021/3/25
*/
public class GAFAuthzClientService {

    @Autowired
    private GAFJWTTokenParser jwtTokenParser;

    @Autowired
    private AuthzClient authzClient;
    
    private static Logger logger = LogUtil.getLocLogger(GAFAuthzClientService.class);

    public OAuth20Profile parseOauthJWTToken(String token) throws Exception{
        return getJwtTokenParser().parseOauthJWTToken(token);
    }
    public OidcProfile parseJWTToken(String token) throws Exception {
        return getJwtTokenParser().parseToken(token);
    }
    public OidcProfile authzWithToken(String token) {
        try {
            AuthorizationResponse authzResponse = getAuthzClient().authorization(token).authorize();
            return getJwtTokenParser().parseToken(authzResponse.getToken());
        } catch (Exception e) {
            logger.debug(e.getMessage(), e);
        }
        return null;
    }
    
    public OidcProfile authzWithUsernamePassword(String username, String password) {
        try {
            AuthorizationResponse authzResponse = getAuthzClient().authorization(username, password).authorize();
            return getJwtTokenParser().parseToken(authzResponse.getToken());
        } catch (Exception e) {
            logger.debug(e.getMessage(), e);
        }
        return null;
    }
    
//    public void recordKeycloakUser(OidcProfile profile){
//        SecurityUtilsExt.recordKeycloakUser(profile);
//    }
    
    public GAFJWTTokenParser getJwtTokenParser() {
        return jwtTokenParser;
    }

    public void setJwtTokenParser(GAFJWTTokenParser jwtTokenParser) {
        this.jwtTokenParser = jwtTokenParser;
    }

    public AuthzClient getAuthzClient() {
        return authzClient;
    }

    public void setAuthzClient(AuthzClient authzClient) {
        this.authzClient = authzClient;
    }
}
