/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.exception;

import com.supermap.gaf.commontypes.MessageResult;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * @author:yw
 * @Date 2021-3-12
 * @date:2021/3/25 全屏异常拦截器
 **/
public class GafExceptionMapper implements ExceptionMapper<Exception> {
    @Override
    public Response toResponse(Exception e) {
        if (e instanceof GafException) {
            GafException gafException = (GafException) e;
            MessageResult result = MessageResult.failed(String.class).message(gafException.getMessage()).build();
            return Response.ok(result, MediaType.APPLICATION_JSON).build();
        } else {
            e.printStackTrace();
        }
        MessageResult result = MessageResult.failed(String.class).message(e.getMessage()).build();
        return Response.ok(result, MediaType.APPLICATION_JSON).build();
    }
}
