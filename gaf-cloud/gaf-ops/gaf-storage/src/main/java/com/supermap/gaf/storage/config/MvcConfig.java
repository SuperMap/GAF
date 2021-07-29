package com.supermap.gaf.storage.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableWebMvc
public class MvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/META-INF/storage-ui/")
                .setCacheControl(CacheControl.maxAge(Duration.ofSeconds(3600).getSeconds(), TimeUnit.SECONDS));
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/")
                .setCacheControl(CacheControl.maxAge(Duration.ofSeconds(3600).getSeconds(), TimeUnit.SECONDS));
        registry.addResourceHandler("/swagger/**")
                .addResourceLocations("classpath:/storage/swagger/")
                .setCacheControl(CacheControl.maxAge(Duration.ofSeconds(3600).getSeconds(), TimeUnit.SECONDS));

    }
}
