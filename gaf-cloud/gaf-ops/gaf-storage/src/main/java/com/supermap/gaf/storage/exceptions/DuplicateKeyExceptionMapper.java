package com.supermap.gaf.storage.exceptions;

import com.supermap.gaf.commontypes.MessageResult;
import org.springframework.dao.DuplicateKeyException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class DuplicateKeyExceptionMapper implements ExceptionMapper<DuplicateKeyException>{
    @Override
    public Response toResponse(DuplicateKeyException e){
        MessageResult body = MessageResult.failed(Object.class).status(Response.Status.CONFLICT.getStatusCode())
                .message("键重复冲突(name)").build();
        return Response.ok().type(MediaType.APPLICATION_JSON_TYPE).entity(body).build();
    }

}
