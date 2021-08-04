package com.supermap.gaf.common.storage.config;

import com.supermap.gaf.commontypes.MessageResult;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class StorageNotFoundExceptionMapper implements ExceptionMapper<StorageNotFoundException> {
    @Override
    public Response toResponse(StorageNotFoundException e) {
        MessageResult body = MessageResult.failed(Object.class).status(Response.Status.NOT_FOUND.getStatusCode())
                .message(e.getMessage()).build();
        return Response.ok().type(MediaType.APPLICATION_JSON_TYPE).entity(body).build();
    }

}
