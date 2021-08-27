/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.data.mgt;


import com.supermap.gaf.rest.remote.ExternalRemoteService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

/**
 * @author:yw
 * @date:2021/3/25
 * @Date 2021-3-12
 **/
@SpringBootApplication
@ComponentScan(basePackages = {"com.supermap.gaf"}, excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = "com.supermap.gaf.common.storage.*"))
@EnableDiscoveryClient
@EnableFeignClients({"com.supermap.gaf"})
@MapperScan(basePackages = {"com.supermap.gaf.**.dao", "com.supermap.gaf.**.mapper"})
@EnableTransactionManagement
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    /**
     * 第三方服务调用
     *
     * @return
     */
    @Bean
    ExternalRemoteService externalRemoteService() {
        ExternalRemoteService externalRemoteService = new ExternalRemoteService();
        externalRemoteService.setRestTemplate(new RestTemplate());
        return externalRemoteService;
    }

}



