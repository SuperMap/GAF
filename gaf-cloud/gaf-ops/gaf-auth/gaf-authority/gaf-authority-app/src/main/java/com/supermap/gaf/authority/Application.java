/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.authority;

import com.supermap.gaf.utils.MybatisBatchUtil;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author wxl
 * @date:2021/3/25
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.supermap.gaf"})
@MapperScan(basePackages = {"com.supermap.gaf.**.dao", "com.supermap.gaf.dao"})
@EnableFeignClients(basePackages = {"com.supermap.gaf"})
@EnableDiscoveryClient
@EnableAsync
@EnableTransactionManagement
@EnableAspectJAutoProxy(exposeProxy = true)
public class Application {
    @Bean
    MybatisBatchUtil mybatisBatchUtil(SqlSessionFactory sqlSessionFactory) {
        return new MybatisBatchUtil(sqlSessionFactory);
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
