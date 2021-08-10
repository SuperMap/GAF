/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.rest.filters;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.collections4.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.nimbusds.jose.util.StandardCharset;
import com.supermap.gaf.commontypes.MessageResult;
import com.supermap.gaf.utils.LicenseManager;

/**
 * <p>
 * 提供基于web的许可模块校验，管理等。目前暂只支持许可校验
 * </p>
 *
 * @author ${Author}
 * @version ${Version}
 * @date:2021/3/25
 * @since 1.0.0
 */
public class GAFLicenseFilter implements Filter {

    private Set<Integer> licenseModules = new HashSet<>();

    private LicenseManager licenseManager = LicenseManager.getInstance();

    public GAFLicenseFilter() {
    }

    public GAFLicenseFilter(Integer licenseModule) {
        this.licenseModules.add(licenseModule);
    }

    public GAFLicenseFilter(Set<Integer> licenseModules) {
        this.licenseModules.addAll(licenseModules);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (CollectionUtils.isNotEmpty(this.licenseModules)) {
            for (Integer moduleType : licenseModules) {
                if (!licenseManager.isVerified(moduleType)) {
                    //TODO 暂时返回许可未授权错误，后期补充许可配置引导页面
                    MessageResult<String> result = MessageResult.failed(String.class)
                            .status(Status.INTERNAL_SERVER_ERROR.getStatusCode()).message("许可配置错误").build();
                    response.setContentType(MediaType.APPLICATION_JSON);
                    response.setCharacterEncoding(StandardCharset.UTF_8.toString());
                    ((HttpServletResponse) response).setStatus(Status.INTERNAL_SERVER_ERROR.getStatusCode());
                    response.getWriter().write(JSON.toJSONString(result));
                    response.flushBuffer();
                }
            }
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // TODO Auto-generated method stub

    }

    public Set<Integer> getLicenseModules() {
        return licenseModules;
    }

    public void setLicenseModules(Set<Integer> licenseModules) {
        this.licenseModules = licenseModules;
    }

}
