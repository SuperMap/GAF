/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.shiro;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.apache.shiro.web.filter.mgt.FilterChainResolver;
import org.apache.shiro.web.mgt.WebSecurityManager;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.apache.shiro.web.subject.WebSubject;
import org.slf4j.Logger;
import org.springframework.core.annotation.Order;

import com.supermap.gaf.shiro.commontypes.JWTToken;
import com.supermap.gaf.utils.LogUtil;


/**
 * @author:yj
 * @date:2021/3/25
*/
@Order(Integer.MAX_VALUE - 5)
public class CustomShiroFilter extends AbstractShiroFilter {

    private static final Logger log = LogUtil.getLocLogger(CustomShiroFilter.class);
    private static String JWT_HEADER = "Authorization";
    private static String JWT_PREFIX = "Bearer";
    
    public static String STATE_LESS_AUTH_TAG = "stateLessAuthTag";
    
    protected CustomShiroFilter(WebSecurityManager webSecurityManager, FilterChainResolver resolver) {
        super();
        if (webSecurityManager == null) {
            throw new IllegalArgumentException("WebSecurityManager property cannot be null.");
        }
        setSecurityManager(webSecurityManager);
        if (resolver != null) {
            setFilterChainResolver(resolver);
        }
    }
    
    @Override
    protected void doFilterInternal(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws ServletException, IOException {
        try {
            super.doFilterInternal(servletRequest, servletResponse, chain);    
        }finally {
            //移除token登录的会话状态
            try {
                Subject subject = ThreadContext.getSubject();
                HttpServletRequest httpRequest = (HttpServletRequest)servletRequest;
                if(subject != null && subject.isAuthenticated() 
                        && (boolean)httpRequest.getAttribute(STATE_LESS_AUTH_TAG)) {
                    subject.logout();
                }
            }catch (Exception e) {
                // TODO: handle exception
            }
            
        }
        
    }
    
    @Override
    protected WebSubject createSubject(ServletRequest request, ServletResponse response) {
        AuthenticationToken token = createToken(request, response);
        WebSubject subject = super.createSubject(request, response);
        // todo 测试置为null
//        token = null;
        if(token != null) {
            try {
                ThreadContext.bind(subject);
                subject.login(token);// 认证
            } catch (AuthenticationException e) {// 认证失败，发送401状态并附带异常信息
                log.error(e.getMessage(), e);
            }
        }
        return subject;
    }
    
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
        if(request instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest)request;
            String authHeader = httpRequest.getHeader(JWT_HEADER);
            if(StringUtils.isNoneEmpty(authHeader)) {
                httpRequest.setAttribute(STATE_LESS_AUTH_TAG,  true);
                String jwtToken = authHeader.trim();
                if(StringUtils.isNotEmpty(JWT_PREFIX)) {
                    String[] split = authHeader.trim().split("\\s+");
                    if (split !=null && split.length == 2 && split[0].equalsIgnoreCase(JWT_PREFIX)) {
                        jwtToken = split[1];
                    };
                }
                return new JWTToken(jwtToken);
            }
        }
        return null;
    }
}
