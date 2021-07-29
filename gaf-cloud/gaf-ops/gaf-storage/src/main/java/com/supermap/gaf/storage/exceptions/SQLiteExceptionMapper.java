package com.supermap.gaf.storage.exceptions;

import com.supermap.gaf.commontypes.MessageResult;
import org.springframework.jdbc.UncategorizedSQLException;
import org.sqlite.SQLiteException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import static org.sqlite.SQLiteErrorCode.SQLITE_CONSTRAINT_UNIQUE;

public class SQLiteExceptionMapper implements ExceptionMapper<UncategorizedSQLException>{
    @Override
    public Response toResponse(UncategorizedSQLException e){
        if(e.getSQLException() instanceof SQLiteException){
            SQLiteException sqLiteException = (SQLiteException) e.getSQLException();
            if(sqLiteException.getResultCode() == SQLITE_CONSTRAINT_UNIQUE){
                MessageResult body = MessageResult.failed(Object.class).status(Response.Status.CONFLICT.getStatusCode())
                        .message("键重复冲突(name)").build();
                return Response.ok().type(MediaType.APPLICATION_JSON_TYPE).entity(body).build();
            }
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN).entity(e.getMessage()).build();
    }

}
