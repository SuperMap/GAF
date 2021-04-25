/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.rest.exceptions.handler;

import com.supermap.gaf.commontypes.MessageResult;
import com.supermap.gaf.rest.HttpServletRequestAware;
import com.supermap.gaf.rest.utils.HttpUtil;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;


/**
 * @author:yj
 * @date:2021/3/25
*/
public class WebApplicationExceptionMapper implements HttpServletRequestAware, ExceptionMapper<WebApplicationException> {
    
    private HttpServletRequest request;
    
    @Override
    public void setHttpServletRequest(HttpServletRequest request) {
        this.request = request;
    }
    
    @Override
    public Response toResponse(WebApplicationException ex) {
        Response response = ex.getResponse();
        String errMessage = ex.getMessage() == null ? "webApplicaitonExceptioni" : ex.getMessage();
        MessageResult<String> result = MessageResult.failed(String.class)
                .status(response.getStatus())
                .message(errMessage)
                .data(errMessage).build();
        return Response.status(response.getStatus())
                .header("Content-Type", HttpUtil.getAcceptMediaType(this.request))
                .entity(result)
                .build();
    }
}
