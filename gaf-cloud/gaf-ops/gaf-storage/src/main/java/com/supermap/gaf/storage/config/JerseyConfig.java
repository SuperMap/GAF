package com.supermap.gaf.storage.config;

import com.supermap.gaf.storage.resources.root.RootResource;
import com.supermap.gaf.storage.exceptions.DuplicateKeyExceptionMapper;
import com.supermap.gaf.storage.exceptions.SQLiteExceptionMapper;
import com.supermap.gaf.storage.exceptions.StorageAuthorizationExceptionMapper;
import com.supermap.gaf.storage.filter.RequestFilter;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.config.SwaggerContextService;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;
import io.swagger.models.SecurityRequirement;
import io.swagger.models.Swagger;
import io.swagger.models.auth.ApiKeyAuthDefinition;
import io.swagger.models.auth.BasicAuthDefinition;
import io.swagger.models.auth.In;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.web.context.ServletConfigAware;

import javax.annotation.PostConstruct;
import javax.servlet.ServletConfig;
import javax.ws.rs.ext.Provider;
import java.util.Arrays;
import java.util.stream.Collectors;

@Component
public class JerseyConfig extends ResourceConfig implements ServletConfigAware {
    @Value("${spring.jersey.application-path:/}")
    private String apiPath;

    public JerseyConfig() {

        register(RequestFilter.class);
        register(RootResource.class);
        register(StorageAuthorizationExceptionMapper.class);
        register(DuplicateKeyExceptionMapper.class);
        register(SQLiteExceptionMapper.class);
//        packages("com.supermap.gaf.common.storage");
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(Provider.class));
        registerClasses(scanner.findCandidateComponents("com.supermap.gaf.common.storage").stream()
                .map(beanDefinition -> ClassUtils.resolveClassName(beanDefinition.getBeanClassName(), getClassLoader()))
                .collect(Collectors.toSet()));
    }

    private ServletConfig servletConfig;


    /**
     * Configure the Swagger documentation for this API.
     */
    @PostConstruct
    private void configureSwagger() {
        // Creates file at localhost:port/swagger.json
        this.register(ApiListingResource.class);
        this.register(SwaggerSerializers.class);
        Swagger swagger = new Swagger();
        swagger.setBasePath(apiPath);
        swagger.securityDefinition("basicAuth", new BasicAuthDefinition());
        swagger.securityDefinition("token", new ApiKeyAuthDefinition("Authorization", In.HEADER));
        SecurityRequirement securityRequirement = new SecurityRequirement();
        securityRequirement.requirement("token");
        SecurityRequirement securityRequirement2 = new SecurityRequirement();
        securityRequirement.requirement("basicAuth");
        swagger.setSecurity(Arrays.asList(securityRequirement, securityRequirement2));
        new SwaggerContextService().withServletConfig(servletConfig).updateSwagger(swagger);
        BeanConfig config = new BeanConfig();

        // ... this is all unchanged ...
        config.setConfigId("example-jersey-app");
        config.setTitle("Spring Boot + Jersey + Swagger");
        config.setVersion("2");
        config.setContact("Me <me@example.com>");
        config.setSchemes(new String[]{"http", "https"});
        config.setResourcePackage(RootResource.class.getPackage().getName());
        config.setPrettyPrint(true);
//        config.setScan(true);
        config.setBasePath(apiPath);


    }

    @Override
    public void setServletConfig(ServletConfig servletConfig) {
        this.servletConfig = servletConfig;
    }

}