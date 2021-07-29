package com.supermap.gaf.storage.resources;

import com.supermap.gaf.commontypes.MessageResult;
import com.supermap.gaf.storage.entity.Space;
import com.supermap.gaf.storage.service.AllocateSpaceService;
import com.supermap.gaf.storage.utils.Page;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

public class AllocateSpaceResource {

    @Autowired
    private AllocateSpaceService allocateSpaceService;

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/{spaceId}")
    @ApiOperation(value = "分页条件查询", notes = "分页条件查询")
    public MessageResult<Page> pageList(@PathParam("spaceId") String spaceId,
                                        @DefaultValue("1")@QueryParam("pageNum")Integer pageNum,
                                        @DefaultValue("10")@QueryParam("pageSize")Integer pageSize){
        Page<Space> page = allocateSpaceService.select(spaceId,pageNum,pageSize);
        return MessageResult.successe(Page.class).data(page).status(200).message("查询成功").build();
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Path("")
    @ApiOperation(value = "分配存储", notes = "分配存储")
    public MessageResult<Void> allocate(Space space){
        allocateSpaceService.allocate(space);
        return MessageResult.successe(Void.class).status(200).message("查询成功").build();
    }
}
