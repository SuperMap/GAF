/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.data.graph.configuration;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

import static com.supermap.gaf.commontypes.GafCommonConstant.LIQUIBASE_ENTRY_FORMAT;
import static com.supermap.gaf.commontypes.GafCommonConstant.SPRING_APPLICATION_NAME;
import static com.supermap.gaf.commontypes.GafCommonConstant.SPRING_LIQUIBASE_ENABLED;

/**
 * @author : duke
 * @date:2021/3/25
 * @since 2021/3/31 10:00 AM
 */
@Configuration
@ConditionalOnProperty(name = SPRING_LIQUIBASE_ENABLED, havingValue = "true", matchIfMissing = true)
public class LiquibaseConfig {
    @Autowired
    private DataSource dataSource;
    @Value(SPRING_APPLICATION_NAME)
    private String appName;

    @Bean
    public SpringLiquibase liquibase() {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setChangeLog(String.format(LIQUIBASE_ENTRY_FORMAT,appName));
        liquibase.setDataSource(dataSource);
        return liquibase;
    }
}
