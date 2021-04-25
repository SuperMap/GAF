/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.webgis;



import com.supermap.gaf.utils.MybatisBatchUtil;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;


/**
 * @author:yj
 * @date:2021/3/25
*/



@SpringBootApplication
@ComponentScan(basePackages = {
        "com.supermap.gaf"
})
@EnableDiscoveryClient
@EnableHystrix
@ServletComponentScan
@MapperScan(basePackages = {"com.supermap.gaf.**.dao","com.supermap.gaf.**.mapper"})
@EnableFeignClients(basePackages = {"com.supermap.gaf.**.client"})
@EnableTransactionManagement
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args );
    }


    @Bean
    @LoadBalanced
    RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
//        restTemplate.setInterceptors(Collections.singletonList(new JWTTokenClientHttpRequestInterceptor()));
        return restTemplate;
    }

//    @Bean
//    RemoteCommonService remoteCommonService(@Autowired RestTemplate restTemplate) {
//        RemoteCommonService remoteCommonService = new RemoteCommonService();
//        remoteCommonService.setRestTemplate(restTemplate);
//        return remoteCommonService;
//    }
    @Bean
    MybatisBatchUtil mybatisBatchUtil(SqlSessionFactory sqlSessionFactory){
        return  new MybatisBatchUtil(sqlSessionFactory);
    }

    
//    @Bean
//    DBStructureInitListener dbStructureInitListener(@Autowired DatasourceConnInfo connInfo) {
//        DBStructureInitListener dbInitListener = new DBStructureInitListener();
//        dbInitListener.setConnInfo(connInfo);
//        return dbInitListener;
//    }
}



