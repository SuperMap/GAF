package com.supermap.gaf.data.mgt.resource;

import com.supermap.gaf.commontypes.MessageResult;
import com.supermap.gaf.data.mgt.entity.MmField;
import com.supermap.gaf.data.mgt.service.FieldsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Component
public class DatasetResource {

    @Autowired
    private FieldsService fieldsService;
    @Path("/fields")
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    public MessageResult<Void> addField(@PathParam("datasourceId") String datasourceId, @PathParam("datasetName")String datasetName, MmField mmField){
        fieldsService.addField(datasourceId,datasetName,mmField);
        return MessageResult.successe(Void.class).build();
    }

    @Path("/fields/{fieldName}")
    @PUT
    @Produces({MediaType.APPLICATION_JSON})
    public MessageResult<Void> modifyField(@PathParam("fieldName") String fieldName, @PathParam("datasourceId") String datasourceId, @PathParam("datasetName")String datasetName, MmField mmField){
        fieldsService.modifyField(datasourceId,datasetName,fieldName,mmField);
        return MessageResult.successe(Void.class).build();
    }
    @Path("/fields/{fieldName}")
    @DELETE
    @Produces({MediaType.APPLICATION_JSON})
    public MessageResult<Void> removeField(@PathParam("fieldName") String fieldName, @PathParam("datasourceId") String datasourceId, @PathParam("datasetName")String datasetName){
        fieldsService.removeField(datasourceId,datasetName,fieldName);
        return MessageResult.successe(Void.class).build();
    }
    @Path("/fields")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public MessageResult<List<MmField>> fields( @PathParam("datasourceId") String datasourceId, @PathParam("datasetName")String datasetName){
        return MessageResult.data(fieldsService.fields(datasourceId,datasetName)).build();
    }
}
