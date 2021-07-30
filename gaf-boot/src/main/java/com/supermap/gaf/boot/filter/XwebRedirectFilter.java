package com.supermap.gaf.boot.filter;

import com.supermap.gaf.gateway.commontypes.ExchangeAuthenticationAttribute;
import com.supermap.gaf.gateway.commontypes.properties.GatewaySecurityProperties;
import com.supermap.gaf.gateway.util.AccessValidUtils;
import org.springframework.http.HttpStatus;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static com.supermap.gaf.gateway.commontypes.constant.GatewayConst.EXCHANGE_AUTHENTICATION_ATTRIBUTE_NAME;
import static com.supermap.gaf.gateway.commontypes.constant.GatewayConst.SEPARATOR;

/**
 * 注意： 该代码对应gaf-microservice-gateway中的同名的filter,功能逻辑等应该保持一致
 *
 *  此过滤器提供的重定向逻辑
 * 这些重定向逻辑只是暂时存在于域名直接指向网关的情况下
 *      1.'/'请求，跳转到indexUrl
 *      2.gatewayLoginUrl请求，跳转到centerLoginUrl
 *      3.gatewayLogoutUrl请求，跳转到centerLogoutUrl
 * @author wxl
 * @date 2021/4/17
 */
public class XwebRedirectFilter implements Filter {

    private GatewaySecurityProperties gatewaySecurityProperties;

    public XwebRedirectFilter() {
    }

    public XwebRedirectFilter(GatewaySecurityProperties gatewaySecurityProperties) {
        this.gatewaySecurityProperties = gatewaySecurityProperties;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String uri = request.getRequestURI();
        boolean isIndexUrl = uri.startsWith(gatewaySecurityProperties.getIndexUrl());
        List<String> publicUrls = gatewaySecurityProperties.getPublicUrls();
        boolean isPublicUri = AccessValidUtils.isPublicUrls(uri,publicUrls);
        ExchangeAuthenticationAttribute attribute = new ExchangeAuthenticationAttribute();
        attribute.setIsIndexUrl(isIndexUrl);
        attribute.setIsPublicUrl(isPublicUri);
        attribute.setGatewaySecurityProperties(gatewaySecurityProperties);
        attribute.setUri(uri);
        request.setAttribute(EXCHANGE_AUTHENTICATION_ATTRIBUTE_NAME, attribute);
        if (uri.equals(SEPARATOR)){
            response.setStatus(HttpStatus.FOUND.value());
            response.sendRedirect(gatewaySecurityProperties.getIndexUrl());
        }else if (uri.equals(gatewaySecurityProperties.getGatewayLoginUrl())){
            response.setStatus(HttpStatus.FOUND.value());
            response.sendRedirect(gatewaySecurityProperties.getCenterLoginUrl());
        }else if (uri.equals(gatewaySecurityProperties.getGatewayLogoutUrl())){
            response.setStatus(HttpStatus.FOUND.value());
            response.sendRedirect(gatewaySecurityProperties.getCenterLogoutUrl());
        }else {
            filterChain.doFilter(request,response);
        }
    }

    @Override
    public void destroy() {

    }
}
