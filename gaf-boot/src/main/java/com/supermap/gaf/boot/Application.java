package com.supermap.gaf.boot;


import com.supermap.gaf.rest.remote.ExternalRemoteService;
import com.supermap.gaf.utils.MybatisBatchUtil;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.FilterType;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@ComponentScan(basePackages = {"com.supermap.gaf"},excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = "com.supermap.gaf.common.storage.*"))
@MapperScan(basePackages = {"com.supermap.gaf.**.dao", "com.supermap.gaf.data.mgt.mapper"})
@EnableAsync
@EnableCaching
@EnableTransactionManagement
@EnableAspectJAutoProxy(exposeProxy = true)
public class Application {

    @Bean
    MybatisBatchUtil mybatisBatchUtil(SqlSessionFactory sqlSessionFactory) {
        return new MybatisBatchUtil(sqlSessionFactory);
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }


    /**
     * 第三方服务调用
     *
     * @return
     */
    @Bean
    ExternalRemoteService externalRemoteService() {
        ExternalRemoteService externalRemoteService = new ExternalRemoteService();
        externalRemoteService.setRestTemplate(new RestTemplate());
        return externalRemoteService;
    }

}
