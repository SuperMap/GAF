/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.common.storage.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * @author heykb
 * @date:2021/3/25
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class MinioConfig implements Serializable {
    private static final long serialVersionUID = 1L;
    private String serviceEndpoint;
    private String accessKey;
    private String secretKey;
    private String bucketName;
    private String name;
    private long totalSize = -1;
    private String dir = "";
    private String target;


    @Override
    public MinioConfig clone() {
        return MinioConfig.builder()
                .accessKey(accessKey).secretKey(secretKey)
                .bucketName(bucketName).serviceEndpoint(serviceEndpoint)
                .name(name).dir(dir).build();
    }



    /*
    @Value("${minio.link-expired-minutes:15}")
    public  void setLinkExpiredMinutes(int linkExpiredMinutes) {
        MinioConfig.linkExpiredMinutes = linkExpiredMinutes;
    }

    @Value("${minio.servive-endpoint:}")
    public  void setServiveEndpoint(String serviveEndpoint) {
        MinioConfig.serviveEndpoint = serviveEndpoint;
    }
    @Value("${minio.access-key:}")
    public  void setAccessKey(String accessKey) {
        MinioConfig.accessKey = accessKey;
    }
    @Value("${minio.secret-key:}")
    public  void setSecretKey(String secretKey) {
        MinioConfig.secretKey = secretKey;
    }
    @Value("${minio.bucket-name:}")
    public  void setBucketName(String bucketName) {
        MinioConfig.bucketName = bucketName;
    }

    @Bean
    public AmazonS3 amazonS3(){
        return AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(MinioConfig.serviveEndpoint, Regions.DEFAULT_REGION.getName()))
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(MinioConfig.accessKey, MinioConfig.secretKey)))
                .build();
    }*/
}
