/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.authentication.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

/**
 * @author : duke
 * @date:2021/3/25
 * @since 2020/12/7 9:58 PM
 */
@Configuration
@EnableConfigurationProperties({RedisProperties.class})
public class RedisConnectionConfiguration {
    @Autowired
    private RedisProperties redisProperties;


    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        //单机模式
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(redisProperties.getHost());
        configuration.setPort(redisProperties.getPort());
        configuration.setDatabase(redisProperties.getDatabase());
        LettuceConnectionFactory factory = new LettuceConnectionFactory(configuration);
        // 使用前先校验连接
        factory.setValidateConnection(true);
        factory.setShareNativeConnection(false);
        return factory;
    }

}
