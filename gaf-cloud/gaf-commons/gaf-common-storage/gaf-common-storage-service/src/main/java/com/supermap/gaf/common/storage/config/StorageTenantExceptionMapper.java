package com.supermap.gaf.common.storage.config;

import com.supermap.gaf.common.storage.spi.exceptions.StorageTenantException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class StorageTenantExceptionMapper implements ExceptionMapper<StorageTenantException> {
    @Override
    public Response toResponse(StorageTenantException e) {
        return Response.status(Response.Status.UNAUTHORIZED).entity("获取租户id失败").build();

    }

}
