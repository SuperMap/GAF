/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.authentication.config.authc.oauth2;

import com.supermap.gaf.authentication.config.authc.jwtorsession.JwtOrSessionAuthentication;
import com.supermap.gaf.authentication.entity.constant.LoginConstant;
import com.supermap.gaf.authentication.entity.entity.AuthenticationParam;
import com.supermap.gaf.authentication.utils.HttpRequestUtils;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

import static com.supermap.gaf.authentication.entity.constant.LoginConstant.OAUTH2_AUTHORIZE;

/**
 * 拦截/oauth/authorize
 * 对GET和POST请求进行拦截处理
 * @see org.springframework.security.oauth2.provider.endpoint.AuthorizationEndpoint
 * @author : duke
 * @date:2021/6/4
 */
public class Oauth2AuthorizeFilter extends OncePerRequestFilter {

    private RequestMatcher requestMatcher =  new AntPathRequestMatcher(OAUTH2_AUTHORIZE);
    private AuthenticationManager authenticationManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!requestMatcher.matches(request)){
            filterChain.doFilter(request, response);
            return;
        }
        AuthenticationParam authenticationParam = HttpRequestUtils.getJwtOrSession(request);
        JwtOrSessionAuthentication authentication = new JwtOrSessionAuthentication(Collections.emptyList(),authenticationParam);
        //判断是否登陆
        boolean authFlag = false;
        Authentication authResult = null;
        Exception failed = null;
        try {
            authResult = this.getAuthenticationManager().authenticate(authentication);
            authFlag = true;
            SecurityContextHolder.getContext().setAuthentication(authResult);
        }catch (Exception e){
            failed = e;
        }
        //对GET请求和POST请求进行不同处理，(GET请求跳转登陆页带上参数，POST请求发生在用户Approval后，跳转到client的redirecturl)
        if (request.getMethod().equalsIgnoreCase(HttpMethod.GET.name())){
            if (authFlag){
                filterChain.doFilter(request, response);
                return;
            }else {
                String queryString = request.getQueryString();
                response.sendRedirect(LoginConstant.LOGIN_URL + "?" + queryString);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    public AuthenticationManager getAuthenticationManager() {
        return authenticationManager;
    }

    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

}
