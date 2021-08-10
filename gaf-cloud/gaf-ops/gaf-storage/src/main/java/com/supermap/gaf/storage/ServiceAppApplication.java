package com.supermap.gaf.storage;

import com.supermap.gaf.common.storage.annotations.EnableStorageServer;
import com.supermap.gaf.storage.dao.StoragePermissionMapper;
import com.supermap.gaf.storage.enums.SelectMode;
import com.supermap.gaf.storage.filter.StoragePermissionFilter;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@MapperScan("com.supermap.gaf.mapper")
@EnableDiscoveryClient
@EnableStorageServer
public class ServiceAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceAppApplication.class, args);
    }

    @Bean
    @ConditionalOnProperty(value = "GAF_STORAGE_PERMISSION_ENABLE", havingValue = "true")
    public FilterRegistrationBean<StoragePermissionFilter> securityHeaderFilterRegistrationBean(@Autowired StoragePermissionMapper storagePermissionMapper) {
        FilterRegistrationBean<StoragePermissionFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new StoragePermissionFilter(storagePermissionMapper));
        String urlPatternTemplate = "/storage/api/%s/*";
        bean.addUrlPatterns(String.format(urlPatternTemplate, SelectMode.TENANT_ALLOCATED.getPathName()));
        bean.addUrlPatterns(String.format(urlPatternTemplate, SelectMode.TENANT_CREATED.getPathName()));
        bean.addUrlPatterns(String.format(urlPatternTemplate, SelectMode.TENANT_CREATED_FIRST.getPathName()));
        bean.setOrder(Integer.MIN_VALUE);
        bean.setName("StoragePermissionFilter");
        return bean;
    }
}



