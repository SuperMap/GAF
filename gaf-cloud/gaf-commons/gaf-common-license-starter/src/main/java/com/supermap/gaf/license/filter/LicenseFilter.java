/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.license.filter;

import com.supermap.gaf.license.core.LicenseManager;
import com.supermap.gaf.license.util.JsonUtil;
import com.supermap.gaf.license.util.MessageResult;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


/**
 * @author:yj
 * @date:2021/3/25
*/
public class LicenseFilter implements Filter {

    private LicenseManager licenseManager;

    public LicenseFilter(LicenseManager licenseManager) {
        this.licenseManager = licenseManager;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String requestURI = httpServletRequest.getRequestURI();
        MessageResult<Void> messageResult = licenseManager.licenseVerify(requestURI);
        if(messageResult.isSuccessed()) {
            filterChain.doFilter(servletRequest,servletResponse);
        } else {
            String resultJson = JsonUtil.obj2String(messageResult);
            servletResponse.setContentType("application/json; charset=UTF-8");
            servletResponse.getWriter().write(resultJson);
            servletResponse.getWriter().flush();
        }
    }

    @Override
    public void destroy() {

    }


}
