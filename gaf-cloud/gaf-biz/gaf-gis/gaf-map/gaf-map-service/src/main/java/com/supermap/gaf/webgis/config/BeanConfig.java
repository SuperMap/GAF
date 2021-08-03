package com.supermap.gaf.webgis.config;

import com.supermap.gaf.common.storage.client.StorageClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class BeanConfig {

    @Value("${GAF_WEBGIS_STORAGE_CONFIG_NAME:default}")
    private String configName;

    @Value("${GAF_WEBGIS_STORAGE_PRE_URL:http://gaf-storage/storage/api/tenant-created-first/}")
    private String storagePreUrl;

    @Bean("BalancedRestTemplate")
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean("WebgisStorageClient")
    public StorageClient storageClient() {
        return new StorageClient(storagePreUrl, configName, restTemplate());
    }

}
