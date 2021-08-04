package com.supermap.gaf.webgis.config;

import com.supermap.gaf.common.storage.client.StorageClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration("com.supermap.gaf.webgis.config.BeanConfig")
public class BeanConfig {

    @Value("${GAF_WEBGIS_STORAGE_CONFIG_NAME:default}")
    private String configName;

    @Value("${GAF_WEBGIS_STORAGE_PRE_URL:http://gaf-storage/storage/api/tenant-created-first/}")
    private String storagePreUrl;

    @Autowired
    @Qualifier("storageRestTemplate")
    private RestTemplate restTemplate;

    @Bean("WebgisStorageClient")
    public StorageClient storageClient() {
        return new StorageClient(storagePreUrl, configName, restTemplate);
    }

}
