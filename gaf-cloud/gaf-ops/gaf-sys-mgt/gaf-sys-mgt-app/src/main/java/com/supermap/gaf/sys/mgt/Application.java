/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.sys.mgt;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

//import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @date:2021/3/25
 * @author wxl
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.supermap.gaf"})
@MapperScan(basePackages = {"com.supermap.gaf.**.dao","com.supermap.gaf.dao","com.supermap.gaf.**.mapper"})
@EnableDiscoveryClient
@EnableTransactionManagement
@EnableAspectJAutoProxy(exposeProxy = true)
@EnableFeignClients(basePackages = "com.supermap.gaf")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
