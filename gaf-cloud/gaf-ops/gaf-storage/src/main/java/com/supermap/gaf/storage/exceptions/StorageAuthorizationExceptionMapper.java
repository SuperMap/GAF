package com.supermap.gaf.storage.exceptions;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class StorageAuthorizationExceptionMapper implements ExceptionMapper<StorageAuthorizationException> {
    @Override
    public Response toResponse(StorageAuthorizationException e) {

        return Response.status(Response.Status.FORBIDDEN).entity(e.getMessage()).build();
    }

}
