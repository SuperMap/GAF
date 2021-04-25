/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.storage.config;

import com.supermap.gaf.storage.service.MinioConfigHandlerI;
import com.supermap.gaf.storage.service.impl.MultiTenantMinioConfigHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @date:2021/3/25
 * @author heykb
 */
@Configuration
@Data
@EqualsAndHashCode
public class AppConfiguration {

    @Bean
    @ConditionalOnMissingBean(MinioConfigHandlerI.class)
    public MinioConfigHandlerI minioConfigHandlerI(){
        return new MultiTenantMinioConfigHandler();
    }
}
