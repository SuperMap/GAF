package com.supermap.gaf.common.storage.config;

import com.amazonaws.SdkClientException;
import com.supermap.gaf.commontypes.MessageResult;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class SdkClientExceptionMapper implements ExceptionMapper<SdkClientException>{
    @Override
    public Response toResponse(SdkClientException e){
        MessageResult body = MessageResult.failed(Object.class).status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())
                .message(e.getMessage()).build();
        return Response.ok().type(MediaType.APPLICATION_JSON_TYPE).entity(body).build();
    }

}
