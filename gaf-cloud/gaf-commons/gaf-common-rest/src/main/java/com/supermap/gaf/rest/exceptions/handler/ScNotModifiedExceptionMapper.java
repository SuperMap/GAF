/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.rest.exceptions.handler;

import com.supermap.gaf.exception.ScNotModifiedException;
import com.supermap.gaf.rest.HttpServletRequestAware;
import com.supermap.gaf.rest.utils.HttpUtil;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.ext.ExceptionMapper;


/**
 * @author:yj
 * @date:2021/3/25
 */
public class ScNotModifiedExceptionMapper implements HttpServletRequestAware, ExceptionMapper<ScNotModifiedException> {
    private HttpServletRequest request;

    @Override
    public void setHttpServletRequest(HttpServletRequest request) {
        this.request = request;
    }

    public Response toResponse(ScNotModifiedException e) {
        ResponseBuilder var2 = Response.ok();
        var2.header("Content-Type", HttpUtil.getAcceptMediaType(this.request));
        var2.status(e.getHttpStatus());
        return var2.build();
    }
}
