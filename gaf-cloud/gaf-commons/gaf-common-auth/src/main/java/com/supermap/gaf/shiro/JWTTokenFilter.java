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
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.servlet.AbstractFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.supermap.gaf.shiro.commontypes.JWTToken;


/**
 * @author:yj
 * @date:2021/3/25
*/
@Deprecated
public class JWTTokenFilter extends AbstractFilter {

    private static final Logger log = LoggerFactory.getLogger(AccessControlFilter.class);
    private static String JWT_HEADER = "Authorization";
    private static String JWT_PREFIX = "Bearer";
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if(!SecurityUtils.getSubject().isAuthenticated()) {
            AuthenticationToken token = createToken(request, response);
            if (token != null) {// 如果是jwt header鉴权的请求
                try {
                    SecurityUtils.getSubject().login(token);// 认证
                } catch (AuthenticationException e) {// 认证失败，发送401状态并附带异常信息
                    log.error(e.getMessage(), e);
                    WebUtils.toHttp(response).sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
                }
            }
        }
        chain.doFilter(request, response);
        //token不记录登录状态，登录
        SecurityUtils.getSubject().logout();
    }
    
//    /**
//     * 是否放行
//     */
//    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
//        if (null != getSubject(request, response) && getSubject(request, response).isAuthenticated()) {
//            return true;// 已经认证过直接放行
//        }
//        return false;// 转到拒绝访问处理逻辑
//    }

//    /**
//     * 拒绝处理
//     */
//    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
//        AuthenticationToken token = createToken(request, response);
//        if (token != null) {// 如果是jwt header鉴权的请求
//            try {
//                Subject subject = getSubject(request, response);
//                subject.login(token);// 认证
//                return true;// 认证成功，过滤器链继续
//            } catch (AuthenticationException e) {// 认证失败，发送401状态并附带异常信息
//                log.error(e.getMessage(), e);
//                WebUtils.toHttp(response).sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
//            }
//        }
//        return false;
//    }

    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
        if(request instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest)request;
            String authHeader = httpRequest.getHeader(JWT_HEADER);
            if(StringUtils.isNoneEmpty(authHeader)) {
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
