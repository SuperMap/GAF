/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.webgis.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author : duke
 * @date:2021/3/25
 * @since 2021/2/25 8:49 AM
 */
@EnableAsync
@Configuration
public class AsyncConfiguration {

    /**
     * 线程池配置
     */
    @Bean
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(10);
        executor.setRejectedExecutionHandler(new CustomRejectedExecutionHandler());
        executor.initialize();
        return executor;
    }

    /**
     * 自定义淘汰策略（队列满了阻塞）
     */
    class CustomRejectedExecutionHandler implements RejectedExecutionHandler {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            try {
                System.out.println("task executor block...");
                executor.getQueue().put(r);
                System.out.println("task executor block end");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
}
