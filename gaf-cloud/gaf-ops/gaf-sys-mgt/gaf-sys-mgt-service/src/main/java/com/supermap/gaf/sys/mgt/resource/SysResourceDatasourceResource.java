/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.sys.mgt.resource;

import com.supermap.gaf.commontypes.MessageResult;
import com.supermap.gaf.commontypes.Page;
import com.supermap.gaf.commontypes.tree.DefaultTreeNode;
import com.supermap.gaf.data.mgt.client.SysResourceDatasourceClient;
import com.supermap.gaf.data.mgt.commontype.SysResourceDatasource;
import com.supermap.gaf.data.mgt.vo.SysResourceDatasourceSelectVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * 数据源接口
 *
 * @author wangxiaolong
 * @date:2021/3/25
 */
@Component
@Api(value = "数据源接口")
public class SysResourceDatasourceResource {

    @Autowired
    private SysResourceDatasourceClient sysResourceDatasourceClient;

    @ApiOperation(value = "查询数据源", notes = "id查询数据源")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "datasourceId", value = "数据源id", paramType = "path", dataType = "string", required = true)
    })
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/{datasourceId}")
    public MessageResult<com.supermap.gaf.data.mgt.commontype.SysResourceDatasource> getById(@PathParam("datasourceId")String datasourceId)  {
        return sysResourceDatasourceClient.getById(datasourceId);
    }

    @ApiOperation(value = "查询空间数据源树")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/tree")
    public MessageResult<List<DefaultTreeNode>> getTree() {
        return sysResourceDatasourceClient.getTree();
    }


    @ApiOperation(value = "查询所数据源可选项", notes = "按数据源类型查询所有数据源可选项, 参数typeCodes是数据源类型字典值，可从字典中查询空间数据源类型和普通数据源类型")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "typeCodes", value = "空间数据源类型字典值集合", paramType = "query", allowMultiple = true, dataType = "string", required = true),
            @ApiImplicitParam(name = "isTemplate", value = "是否为空间数据源模板",  paramType = "query", dataType = "Boolean",defaultValue = "false")
    })
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/options")
    public MessageResult<List<com.supermap.gaf.data.mgt.vo.DatasourceOptionVo>> getOptions(@QueryParam("typeCodes") List<String> typeCodes,
                                                                                           @QueryParam("isTemplate") @DefaultValue("false") Boolean isTemplate) {
        return sysResourceDatasourceClient.getOptions(typeCodes,isTemplate);
    }

    @ApiOperation(value = "分页条件查询数据源")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页码", example = "1",defaultValue = "1", allowableValues = "range[1,infinity]",paramType = "query", dataType = "integer"),
            @ApiImplicitParam(name = "pageSize", value = "每页条数", example = "10", defaultValue = "10",allowableValues = "range(0,infinity]", paramType = "query", dataType = "integer")
    })
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public MessageResult<Page<com.supermap.gaf.data.mgt.commontype.SysResourceDatasource>> pageList(@Valid @BeanParam SysResourceDatasourceSelectVo sysResourceDatasourceSelectVo,
                                                                                                    @DefaultValue("1")@QueryParam("pageNum")Integer pageNum,
                                                                                                    @DefaultValue("10")@QueryParam("pageSize")Integer pageSize){
        return sysResourceDatasourceClient.pageList(sysResourceDatasourceSelectVo,pageNum,pageSize);
    }

    @ApiOperation(value = "测试数据库连接", notes = "应该传入是否是空间数据库的标识isSdx,默认false")
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Path(value = "/connection-param/check")
    public MessageResult<Void> checkConnection(@NotNull com.supermap.gaf.data.mgt.commontype.SysResourceDatasource sysResourceDatasource){
        return sysResourceDatasourceClient.checkConnection(sysResourceDatasource);
    }

    @ApiOperation(value = "新增数据源")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sysResourceDatasource", value = "数据源", dataTypeClass = com.supermap.gaf.data.mgt.commontype.SysResourceDatasource.class, paramType = "body",required = true)
    })
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    public MessageResult<com.supermap.gaf.data.mgt.commontype.SysResourceDatasource> insertSysResourceDatasource(com.supermap.gaf.data.mgt.commontype.SysResourceDatasource sysResourceDatasource) {
        return sysResourceDatasourceClient.insertSysResourceDatasource(sysResourceDatasource);
    }


    @ApiOperation(value = "批量新增数据源")
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/batch")
    public MessageResult<Void> batchInsert(List<com.supermap.gaf.data.mgt.commontype.SysResourceDatasource> sysResourceDatasources){
        return sysResourceDatasourceClient.batchInsert(sysResourceDatasources);
    }

    @ApiOperation(value = "删除数据源", notes = "根据数据源id删除数据源")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "datasourceId", value = "数据源id", paramType = "path", dataType = "string", required = true)
    })
    @DELETE
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/{datasourceId}")
    public MessageResult<Void> deleteSysResourceDatasource(@PathParam("datasourceId")String datasourceId){
        return sysResourceDatasourceClient.deleteSysResourceDatasource(datasourceId);
    }

    @ApiOperation(value = "批量删除数据源", notes = "根据id集合批量删除数据源")
    @DELETE
    @Produces({MediaType.APPLICATION_JSON})
    public MessageResult<Void> batchDelete(List<String> datasourceIds){
        return sysResourceDatasourceClient.batchDelete(datasourceIds);
    }

    @ApiOperation(value = "更新数据源")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sysResourceDatasource", value = "数据源", dataTypeClass = com.supermap.gaf.data.mgt.commontype.SysResourceDatasource.class, paramType = "body",required = true),
            @ApiImplicitParam(name = "datasourceId", value = "数据源id", paramType = "path", dataType = "string", required = true)
    })
    @PUT
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/{datasourceId}")
    public MessageResult<Void> updateSysResourceDatasource(com.supermap.gaf.data.mgt.commontype.SysResourceDatasource sysResourceDatasource, @PathParam("datasourceId")String datasourceId) {
        return sysResourceDatasourceClient.updateSysResourceDatasource(sysResourceDatasource,datasourceId);
    }


    @ApiOperation(value = "校验数据源别名是否重复")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dsName", value = "数据源别名",paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "isSdx", value = "是否为空间数据源",  paramType = "query", dataType = "Boolean"),
            @ApiImplicitParam(name = "isTemplate", value = "是否为空间数据源模板",  paramType = "query", dataType = "Boolean",defaultValue = "false")
    })
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/getDsName")
    public MessageResult<List<SysResourceDatasource>> checkRepeat(
            @QueryParam("dsName")String dsName,
            @QueryParam("isSdx")@DefaultValue("true")Boolean isSdx,
            @QueryParam("isTemplate") @DefaultValue("false") Boolean isTemplate){
        return sysResourceDatasourceClient.checkRepeat(dsName,isSdx,isTemplate);
    }

}
