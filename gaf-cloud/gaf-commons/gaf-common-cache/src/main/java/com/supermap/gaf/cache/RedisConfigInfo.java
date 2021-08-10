/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.cache;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


/**
 * @author:yj
 * @date:2021/3/25
 */
@Data
@Component
@ConfigurationProperties(prefix = "gaf.redis")
public class RedisConfigInfo {

    private boolean enable;

    private String host;
    private Integer port;
    private String password;
    private Integer database;

    private Integer maxActive;
    private Integer maxIdle;
    private Long maxWait;
    private Integer minIdle;


}
