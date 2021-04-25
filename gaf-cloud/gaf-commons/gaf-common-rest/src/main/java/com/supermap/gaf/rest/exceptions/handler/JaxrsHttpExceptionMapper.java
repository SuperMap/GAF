/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.rest.exceptions.handler;

import com.supermap.gaf.commontypes.MessageResult;
import com.supermap.gaf.exception.JaxrsHttpException;
import com.supermap.gaf.rest.HttpServletRequestAware;
import com.supermap.gaf.rest.utils.HttpUtil;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.ext.ExceptionMapper;


/**
 * @author:yj
 * @date:2021/3/25
*/
public class JaxrsHttpExceptionMapper implements HttpServletRequestAware, ExceptionMapper<JaxrsHttpException> {
    private HttpServletRequest request;

    @Context
    public void setHttpServletRequest(@Context HttpServletRequest request) {
        this.request = request;
    }

    public Response toResponse(JaxrsHttpException ex) {
        String errMessage = ex.getMessage() == null ? "HttpException" : ex.getMessage();
        int status = ex.getErrorStatus() == null ? 500 : ex.getErrorStatus().getStatusCode();
        MessageResult<String> result = MessageResult.failed(String.class)
                .status(status)
                .message(errMessage)
                .data(errMessage).build();
        ResponseBuilder var2 = Response.status(status)
                .header("Content-Type", HttpUtil.getAcceptMediaType(this.request))
                .entity(result);
        return var2.build();
    }
}
