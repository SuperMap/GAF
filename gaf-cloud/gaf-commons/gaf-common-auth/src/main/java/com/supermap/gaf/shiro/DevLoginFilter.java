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

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.web.servlet.AbstractFilter;
import org.apache.shiro.web.util.WebUtils;


/**
 * @author:yj
 * @date:2021/3/25
 */
public class DevLoginFilter extends AbstractFilter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (request instanceof HttpServletRequest
                && ((HttpServletRequest) request).getRequestURI().indexOf("loginc") != -1
        ) {
            if (!SecurityUtils.getSubject().isAuthenticated()) {
                String userName = request.getParameter("username");
                String password = request.getParameter("password");
                SecurityUtils.getSubject().login(new UsernamePasswordToken(userName, password));
            } else {
                WebUtils.issueRedirect(request, response, "/");
            }
        }
        chain.doFilter(request, response);
    }


}
