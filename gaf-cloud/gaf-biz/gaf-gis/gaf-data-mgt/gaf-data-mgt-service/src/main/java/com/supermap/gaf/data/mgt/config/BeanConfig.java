package com.supermap.gaf.data.mgt.config;

import com.supermap.gaf.common.storage.client.StorageClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration("com.supermap.gaf.data.mgt.config.BeanConfig")
public class BeanConfig {

    @Value("${GAF_DATAMGT_STORAGE_CONFIG_NAME:default}")
    private String configName;

    @Value("${GAF_DATAMGT_STORAGE_PRE_URL:http://gaf-storage/storage/api/tenant-created-first/}")
    private String storagePreUrl;

    @Autowired
    @Qualifier("storageRestTemplate")
    private RestTemplate restTemplate;

    @Bean("DatamgtStorageClient")
    public StorageClient storageClient() {
        return new StorageClient(storagePreUrl, configName, restTemplate);
    }
}
