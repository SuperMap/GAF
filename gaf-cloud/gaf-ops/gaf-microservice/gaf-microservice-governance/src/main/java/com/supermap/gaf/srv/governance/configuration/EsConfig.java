/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.srv.governance.configuration;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @author : duke
 * @date:2021/3/25
 * @since 2020/11/30 3:43 PM
 */
@Configuration
public class EsConfig {
    @Value("${es.host}")
    private String esHost;

    @Value("${es.port}")
    private Integer esPort;

    @Bean
    public RestHighLevelClient customClient() {
        RestClientBuilder builder = RestClient.builder(new HttpHost(esHost, esPort, "http"))
                .setRequestConfigCallback(requestConfigBuilder -> requestConfigBuilder.setConnectTimeout(10000).setSocketTimeout(30000));

        return new RestHighLevelClient(builder);
    }

    /**
     * health-check需要使用lowlevelclient
     * @return
     */
    @Bean(destroyMethod = "close")
    public RestClient restClient() {
        return customClient().getLowLevelClient();
    }
}
