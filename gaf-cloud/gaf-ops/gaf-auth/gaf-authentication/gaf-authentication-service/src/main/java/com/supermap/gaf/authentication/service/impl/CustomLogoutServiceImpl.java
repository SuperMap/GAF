/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.authentication.service.impl;

import com.supermap.gaf.authentication.service.CustomLogoutService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Set;

import static com.supermap.gaf.authentication.entity.constant.LoginConstant.REDIS_LOGIN_SESSION_PREFIX;

/**
 * @author : duke
 * @date:2021/3/25
 * @since 2020/11/24 9:19 AM
 */
@Slf4j
@Service
public class CustomLogoutServiceImpl implements CustomLogoutService {
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Override
    public boolean logout(String sessionId) {
        try {
            if (!StringUtils.isEmpty(sessionId)){
                Set<Object> keys = redisTemplate.opsForHash().keys(REDIS_LOGIN_SESSION_PREFIX + sessionId);
                if (null != keys){
                    for (Object key : keys){
                        redisTemplate.opsForHash().delete(REDIS_LOGIN_SESSION_PREFIX + sessionId,key);
                    }
                    return true;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            log.error("删除登录信息失败");
        }
        return false;
    }
}
