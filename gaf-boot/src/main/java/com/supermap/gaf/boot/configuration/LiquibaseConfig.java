package com.supermap.gaf.boot.configuration;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

import static com.supermap.gaf.commontypes.GafCommonConstant.*;

/**
 * @author : duke
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
