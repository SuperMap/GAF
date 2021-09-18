/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.common.storage.config;


import com.supermap.gaf.common.storage.handler.MinioConfigHandlerI;
import com.supermap.gaf.common.storage.service.S3ClientService;
import com.supermap.gaf.common.storage.service.impl.S3ClientServiceImpl;
import com.supermap.gaf.common.storage.spi.TenantInfoI;
import com.supermap.gaf.common.storage.web.VolumeConfigResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author heykb
 * @date:2021/3/25
 */
@Configuration
public class StorageServerAutoConfiguration {

    @Autowired
    private TenantInfoI tenantInfoI;


    @Bean
    public StorageConfig storageConfig() {
        return new StorageConfig();
    }

    //    @Bean
//    @ConditionalOnMissingBean(MinioConfigHandlerI.class)
//    public MinioConfigHandlerI minioConfigHandlerI(){
//        return new DefaultMinioConfigHandler(storageConfig(),tenantInfoI,tenantConfigMaps(),globalConfigs());
//    }
//    @Bean()
//    @ConfigurationProperties(prefix = "gaf.storage.tenantconfig")
//    @ConditionalOnMissingBean(MinioConfigHandlerI.class)
//    public Map<String, List<MinioConfig>> tenantConfigMaps() {
//        return new HashMap();
//    }
//    @Bean()
//    @ConfigurationProperties(prefix = "gaf.storage.globalconfig")
//    @ConditionalOnMissingBean(MinioConfigHandlerI.class)
//    public List<MinioConfig> globalConfigs() {
//        return new ArrayList<>();
//    }
    @Bean
    public S3ClientService s3ClientService(@Autowired MinioConfigHandlerI minioConfigHandlerI) {
        return new S3ClientServiceImpl(minioConfigHandlerI);
    }

    @Bean
    public VolumeConfigResource volumeConfigResource(@Autowired MinioConfigHandlerI minioConfigHandlerI) {
        return new VolumeConfigResource(minioConfigHandlerI);
    }
}
