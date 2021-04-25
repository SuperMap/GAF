/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.exception;

import com.alibaba.fastjson.JSON;
import com.supermap.gaf.commontypes.MessageResult;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import java.util.HashMap;
import java.util.Map;

/**
 * @date:2021/3/25
 * @author yd
 */
public class ValidExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        Map<String, String> messageMap = new HashMap<>(16);
        for (ConstraintViolation<?> cv : exception.getConstraintViolations()) {
            String path = cv.getPropertyPath().toString();
            messageMap.put(path.substring(path.lastIndexOf(".") + 1), cv.getMessage());
        }
        MessageResult<Void> result = MessageResult.failed(Void.class).message(JSON.toJSONString(messageMap)).build();
        return Response.ok(result, MediaType.APPLICATION_JSON).build();
    }

}
