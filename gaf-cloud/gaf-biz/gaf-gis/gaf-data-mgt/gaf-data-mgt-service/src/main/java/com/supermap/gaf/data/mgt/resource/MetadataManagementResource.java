package com.supermap.gaf.data.mgt.resource;


import com.supermap.gaf.commontypes.MessageResult;
import com.supermap.gaf.data.mgt.entity.MmField;
import com.supermap.gaf.data.mgt.entity.vo.MetadataVo;
import com.supermap.gaf.data.mgt.service.FieldsService;
import com.supermap.gaf.data.mgt.service.MetadataManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Component
public class MetadataManagementResource {
    @Autowired
    private MetadataManagementService metadataManagementService;
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public MessageResult<List<MetadataVo>> selectAll(){
        List<MetadataVo> result = metadataManagementService.selectAll();
        return MessageResult.data(result).build();
    }



}
