package com.supermap.gaf.data.graph.resource;

import com.supermap.gaf.commontypes.MessageResult;
import com.supermap.gaf.data.graph.service.DataGraphImportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * @author : duke
 * @since 2021/5/6 4:45 PM
 * /data-graph/import
 */
@Component
@Api(value = "数据图谱导入接口")
public class DataGraphImportResource {
    @Autowired
    private DataGraphImportService dataGraphImportService;

    @ApiOperation(value = "行政区划字典数据导入图谱", notes = "行政区划字典数据导入图谱")
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/xzqh")
    public MessageResult<String> importXzqh(){
        dataGraphImportService.importXzqh();
        return MessageResult.successe(String.class).message("操作成功").build();
    }

    @ApiOperation(value = "自然资源数据体系字典数据导入图谱", notes = "自然资源数据体系字典数据导入图谱")
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/zrzysjtx")
    public MessageResult<String> importZrzysjtx(){
        dataGraphImportService.importZrzysjtx();
        return MessageResult.successe(String.class).message("操作成功").build();
    }

    @ApiOperation(value = "年数据导入图谱", notes = "年数据导入图谱")
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/year_chain")
    public MessageResult<String> importyearChain(){
        dataGraphImportService.importYearChain();
        return MessageResult.successe(String.class).message("操作成功").build();
    }

    @ApiOperation(value = "分类要素数据导入图谱", notes = "分类要素数据导入图谱")
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/feature")
    public MessageResult<String> importFeature(){
        dataGraphImportService.importFeature();
        return MessageResult.successe(String.class).message("操作成功").build();
    }
}
