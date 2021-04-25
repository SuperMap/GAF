/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.cache;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.util.StringUtils;

import java.time.Duration;

/**
 * @author:yj
 * @date:2021/3/25
 * redis缓存配置
 */
@ConditionalOnProperty(name = "gaf.redis.enable", havingValue = "true")
@EnableConfigurationProperties({RedisConfigInfo.class})
@Configuration
public class RedisCacheConfig {

    @Autowired
    private RedisConfigInfo redisConfigInfo;

    @Bean
    @Primary
    public CacheManager cacheManager(LettuceConnectionFactory lettuceConnectionFactory) {
        RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10));
        return RedisCacheManager.builder(RedisCacheWriter.lockingRedisCacheWriter(lettuceConnectionFactory))
                .cacheDefaults(defaultCacheConfig)
                .build();
    }

    @Bean
    public LettuceConnectionFactory lettuceConnectionFactory(RedisStandaloneConfiguration redisConfig, GenericObjectPoolConfig redisPoolConfig) {
        LettuceClientConfiguration clientConfig = LettucePoolingClientConfiguration.builder()
                .commandTimeout(Duration.ofMillis(200))
                .poolConfig(redisPoolConfig)
                .build();
        return new LettuceConnectionFactory(redisConfig, clientConfig);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(StringRedisTemplate stringRedisTemplate, LettuceConnectionFactory lettuceConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(lettuceConnectionFactory);

        //使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);

        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);

        redisTemplate.setKeySerializer(stringRedisTemplate.getKeySerializer());
        redisTemplate.setHashKeySerializer(stringRedisTemplate.getHashKeySerializer());
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);

        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean
    public GenericObjectPoolConfig redisPoolConfig() {
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        if (!StringUtils.isEmpty(redisConfigInfo.getMaxActive())) {
            config.setMaxTotal(redisConfigInfo.getMaxActive());
        }
        if (!StringUtils.isEmpty(redisConfigInfo.getMaxIdle())) {
            config.setMaxIdle(redisConfigInfo.getMaxIdle());
        }
        if (!StringUtils.isEmpty(redisConfigInfo.getMinIdle())) {
            config.setMinIdle(redisConfigInfo.getMinIdle());
        }
        if (!StringUtils.isEmpty(redisConfigInfo.getMaxWait())) {
            config.setMaxWaitMillis(redisConfigInfo.getMaxWait());
        }
        return config;
    }

    @Bean
    public RedisStandaloneConfiguration redisConfig() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(redisConfigInfo.getHost());
        config.setPassword(StringUtils.isEmpty(redisConfigInfo.getPassword()) ? RedisPassword.none() : RedisPassword.of(redisConfigInfo.getPassword()));
        config.setPort(redisConfigInfo.getPort());
        config.setDatabase(redisConfigInfo.getDatabase());
        return config;
    }

}
