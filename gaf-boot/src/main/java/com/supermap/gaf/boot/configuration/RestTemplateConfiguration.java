package com.supermap.gaf.boot.configuration;

import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;

@Configuration
public class RestTemplateConfiguration {

    // 连接池大小配置，时间单位毫秒
    private static final int POOL_MAX_TOTAL = 50;
    private static final int POOL_DEFAULT_MAX_PER_ROUTE = 20;
    private static final int POOL_VALIDATE_AFTER_INACTIVITY = 30000;

    // 请求超时时间配置，单位毫秒
    private static final int CONNECT_TIME_OUT = 6000;
    private static final int READ_TIME_OUT = 6000;
    private static final int CONNECTION_REQUEST_TIME_OUT = 5000;

    /**
     * 让spring管理RestTemplate,参数相关配置
     *
     * @param builder
     * @return
     */
    @Bean
    @Primary
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        RestTemplate restTemplate = builder.build();// 生成一个RestTemplate实例
        restTemplate.setRequestFactory(clientHttpRequestFactory());
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));// 设置utf-8编码格式
        return restTemplate;
    }

    /**
     * 客户端请求链接策略
     */
    @Bean
    public ClientHttpRequestFactory clientHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        clientHttpRequestFactory.setHttpClient(httpClientBuilder().build());
        clientHttpRequestFactory.setConnectTimeout(CONNECT_TIME_OUT); // 连接超时时间/毫秒
        clientHttpRequestFactory.setReadTimeout(READ_TIME_OUT); // 读写超时时间/毫秒
        clientHttpRequestFactory.setConnectionRequestTimeout(CONNECTION_REQUEST_TIME_OUT);// 请求超时时间/毫秒
        return clientHttpRequestFactory;
    }

    /**
     * 设置HTTP连接管理器,连接池相关配置管理
     *
     * @return 客户端链接管理器
     */
    @Bean
    public HttpClientBuilder httpClientBuilder() {
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        httpClientBuilder.setConnectionManager(poolingConnectionManager());
        return httpClientBuilder;
    }

    /**
     * 链接线程池管理,可以keep-alive不断开链接请求,这样速度会更快
     * MaxTotal 连接池最大连接数
     * DefaultMaxPerRoute 每个主机的并发数，不能超过MaxTotal
     * ValidateAfterInactivity 可用空闲连接过期时间,重用空闲连接时会先检查是否空闲时间超过这个时间，如果超过，释放socket重新建立
     *
     * @return
     */
    @Bean
    public HttpClientConnectionManager poolingConnectionManager() {
        PoolingHttpClientConnectionManager poolingConnectionManager = new PoolingHttpClientConnectionManager();
        poolingConnectionManager.setMaxTotal(POOL_MAX_TOTAL);
        poolingConnectionManager.setDefaultMaxPerRoute(POOL_DEFAULT_MAX_PER_ROUTE);
        poolingConnectionManager.setValidateAfterInactivity(POOL_VALIDATE_AFTER_INACTIVITY);
        return poolingConnectionManager;
    }
}
