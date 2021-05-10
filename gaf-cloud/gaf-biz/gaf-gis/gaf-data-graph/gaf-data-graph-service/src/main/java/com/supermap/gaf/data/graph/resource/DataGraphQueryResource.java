package com.supermap.gaf.data.graph.resource;

import com.supermap.gaf.commontypes.MessageResult;
import com.supermap.gaf.data.graph.entity.vo.NodeQueryParam;
import com.supermap.gaf.data.graph.entity.vo.NodeQueryResponse;
import com.supermap.gaf.data.graph.service.DataGraphQueryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * @author : duke
 * @since 2021/5/10 2:13 PM
 * /data-graph/query
 */
@Component
@Api(value = "数据图谱查询接口")
public class DataGraphQueryResource {
    @Autowired
    private DataGraphQueryService queryService;

    @ApiOperation(value = "数据图谱层级查询接口", notes = "数据图谱层级查询接口")
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/level")
    public MessageResult<NodeQueryResponse> levelQuery(NodeQueryParam nodeQueryParam){
        NodeQueryResponse response = queryService.levelQuery(nodeQueryParam);
        return MessageResult.successe(NodeQueryResponse.class).message("操作成功").data(response).build();
    }

}
