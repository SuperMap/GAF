package com.supermap.gaf.storage.filter;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import java.io.IOException;

public class RequestFilter implements ContainerRequestFilter, ContainerResponseFilter {
    public static final ThreadLocal<ContainerRequestContext> requestContextHolder = new ThreadLocal<>();

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        requestContextHolder.set(requestContext);
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        // clean up after request
        requestContextHolder.remove();
    }
}
