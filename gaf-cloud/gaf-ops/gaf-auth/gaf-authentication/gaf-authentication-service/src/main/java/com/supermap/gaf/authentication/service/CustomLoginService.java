/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.authentication.service;

import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author : duke
 * @date:2021/3/25
 * @since 2020/11/17 3:33 PM
 */
@Service
public interface CustomLoginService {
    /**
     * 使用username手动创建oauth2 token
     *
     * @param username
     * @return
     */
    OAuth2AccessToken createOauth2AccessTokenWithoutPassword(String username, String clientId);

    /**
     * 使用username手动创建oauth2 token
     * 默认clientId为CUSTOM_CLIENT
     *
     * @param username
     * @return
     */
    OAuth2AccessToken createOauth2AccessTokenWithoutPassword(String username);

    /**
     * sessionId为主键，存储用户信息
     *
     * @param sessionId
     * @param username
     * @param token
     * @param thirdPartyToken
     * @param enabledThirdParty
     * @param thirdPartyUsername
     */
    void storeLoginSession(String sessionId, String username, OAuth2AccessToken token, Map thirdPartyToken, String enabledThirdParty, String thirdPartyUsername);

    /**
     * 保存刷新token后的redis存储用户信息
     *
     * @param sessionId
     * @param token
     */
    void refreshStoreLoginSession(String sessionId, OAuth2AccessToken token);

    /**
     * redis获取 login session
     *
     * @param sessionId
     * @return
     */
    Map<String, Object> getLoginSessionById(String sessionId);

    /**
     * 通过code获取第三方token
     *
     * @param code
     * @param enabledThirdParty
     * @return
     */
    Map<String, String> getThirdPartyTokenByCode(String code, String enabledThirdParty);

    /**
     * 刷新token
     *
     * @param refreshToken
     * @return
     */
    OAuth2AccessToken refreshOauth2AccessToken(String refreshToken);

    /**
     * checkToken
     *
     * @param token
     * @return
     */
    Map<String, ?> checkJwtToken(String token);

}
