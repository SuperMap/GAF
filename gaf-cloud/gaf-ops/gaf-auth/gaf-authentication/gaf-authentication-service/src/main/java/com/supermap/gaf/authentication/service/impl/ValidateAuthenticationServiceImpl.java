/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.authentication.service.impl;

import com.supermap.gaf.authentication.entity.entity.AuthenticationParam;
import com.supermap.gaf.authentication.entity.entity.AuthenticationResult;
import com.supermap.gaf.authentication.service.CustomLoginService;
import com.supermap.gaf.authentication.service.ValidateAuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;

import static com.supermap.gaf.authentication.entity.constant.LoginConstant.REDIS_LOGIN_SESSION_PREFIX;


/**
 * @author : duke
 * @date:2021/3/25
 * @since 2020/11/23 10:48 AM
 */
@Slf4j
@Service
public class ValidateAuthenticationServiceImpl implements ValidateAuthenticationService {
    @Autowired
    private CustomLoginService customLoginService;
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Override
    public AuthenticationResult authentication(AuthenticationParam authenticationParam) {
        String customSessionId = authenticationParam.getCustomSessionId();
        String jwtToken = authenticationParam.getAuthorization();

        AuthenticationResult result = new AuthenticationResult();
        if (!StringUtils.isEmpty(jwtToken)){
            try {
                Map<String,?> map = customLoginService.checkJwtToken(jwtToken);
                if (null != map){
                    result.setUsername((String) map.get("user_name"));
                    result.setJwtToken(jwtToken);
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        if (StringUtils.isEmpty(result.getUsername()) && !StringUtils.isEmpty(customSessionId)){
            try {
                Map<String,String> oAuth2AccessToken = (Map<String,String>) redisTemplate.opsForHash().get(REDIS_LOGIN_SESSION_PREFIX + customSessionId, "token");
                if (oAuth2AccessToken != null){
                    jwtToken = oAuth2AccessToken.get("access_token");
                    Map<String,?> map = null;
                    try {
                        map = customLoginService.checkJwtToken(jwtToken);
                        if (null != map){
                            result.setUsername((String) map.get("user_name"));
                            result.setJwtToken(jwtToken);
                        }
                    }catch (Exception e){
                        //检查到过期可以使用refreshtoken 刷新token
                        String refreshToken = oAuth2AccessToken.get("refresh_token");
                        OAuth2AccessToken refreshOauth2AccessToken = customLoginService.refreshOauth2AccessToken(refreshToken);
                        //更新redis存储的认证信息
                        customLoginService.refreshStoreLoginSession(customSessionId,refreshOauth2AccessToken);
                        String username = (String) redisTemplate.opsForHash().get(REDIS_LOGIN_SESSION_PREFIX + customSessionId, "username");
                        result.setUsername(username);
                        result.setJwtToken(refreshOauth2AccessToken.getValue());
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return result;
    }


}
