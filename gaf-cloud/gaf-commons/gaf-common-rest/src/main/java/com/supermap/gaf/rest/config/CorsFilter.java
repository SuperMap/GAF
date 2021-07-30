package com.supermap.gaf.rest.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import java.io.IOException;

/**
 * @author : duke
 * @since 2021/7/23 2:16 PM
 */
@Component
public class CorsFilter implements ContainerResponseFilter {
    @Value("${cors-filter:false}")
    private boolean corsFilter;

    @Override
    public void filter(ContainerRequestContext request, ContainerResponseContext response) throws IOException {
        if (!corsFilter){
            return;
        }
//        response.getHeaders().add("Access-Control-Allow-Origin", request.getHeaders().get("origin"));
        response.getHeaders().add("Access-Control-Allow-Origin", "*");
        response.getHeaders().add("Access-Control-Allow-Credentials", "true");
        response.getHeaders().add("Access-Control-Allow-Methods", "GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS");
        response.getHeaders().add("Access-Control-Max-Age", "86400");
        response.getHeaders().add("Access-Control-Allow-Headers", "*");
        // 如果是OPTIONS则结束请求
        if (HttpMethod.OPTIONS.toString().equals(request.getMethod())) {
            response.setStatus(HttpStatus.NO_CONTENT.value());
        }
    }
}