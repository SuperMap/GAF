package com.supermap.gaf.common.storage.config;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.supermap.gaf.commontypes.MessageResult;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class AmazonS3ExceptionMapper implements ExceptionMapper<AmazonS3Exception> {
    @Override
    public Response toResponse(AmazonS3Exception e) {
        MessageResult body = MessageResult.failed(Object.class).status(e.getStatusCode())
                .message(e.toString()).build();
        return Response.ok().type(MediaType.APPLICATION_JSON_TYPE).entity(body).build();
    }

}
