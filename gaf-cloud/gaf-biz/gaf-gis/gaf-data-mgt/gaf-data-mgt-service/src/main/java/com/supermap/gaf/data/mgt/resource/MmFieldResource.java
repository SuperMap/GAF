/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.data.mgt.resource;

import com.supermap.gaf.commontypes.MessageResult;
import com.supermap.gaf.data.mgt.entity.MmField;
import com.supermap.gaf.data.mgt.model.FieldTypeInfo;
import com.supermap.gaf.data.mgt.service.MmFieldService;
import com.supermap.gaf.data.mgt.util.Page;
import com.supermap.gaf.data.mgt.vo.MmFieldSelectVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * 字段接口
 * @author wxl 
 * @since  yyyy-mm-dd
 */
@Component
@Api(value = "字段接口")
public class MmFieldResource{
    @Autowired
    private MmFieldService mmFieldService;

    @ApiOperation(value = "根据模型类型查询对应的字段的数据类型集合", notes = "根据模型类型查询对应的字段的数据类型集合")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "modelType", value = "模型类型", paramType = "query", dataType = "string", required = true)
    })
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/type-infos")
    public MessageResult<List<FieldTypeInfo>> listTypeInfos(@NotEmpty @QueryParam("modelType")String modelType){
        List<FieldTypeInfo> typeInfos = mmFieldService.listTypeInfos(modelType);
        MessageResult<List<FieldTypeInfo>> mr = new MessageResult<>();
        mr.setSuccessed(true);
        mr.setData(typeInfos);
        mr.setMessage("查询成功");
        return mr;
    }

    @ApiOperation(value = "根据id查询字段", notes = "根据id查询字段")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "fieldId", value = "字段id", paramType = "path", dataType = "string", required = true)
    })
	@GET
    @Produces({MediaType.APPLICATION_JSON})
	@Path("/{fieldId}")
    public MessageResult<MmField> getById(@PathParam("fieldId")String fieldId){
        MmField mmField = mmFieldService.getById(fieldId);
		return MessageResult.successe(MmField.class).data(mmField).status(200).message("查询成功").build();
    }

    @ApiOperation(value = "分页条件查询字段", notes = "分页条件查询字段")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页码", example = "1",defaultValue = "1", allowableValues = "range[1,infinity]",paramType = "query", dataType = "integer"),
            @ApiImplicitParam(name = "pageSize", value = "每页条数", example = "10", defaultValue = "10",allowableValues = "range(0,infinity]", paramType = "query", dataType = "integer")
    })
	@GET
    @Produces({MediaType.APPLICATION_JSON})
    public MessageResult<Page> pageList(@Valid @BeanParam MmFieldSelectVo mmFieldSelectVo,
										@DefaultValue("1")@QueryParam("pageNum")Integer pageNum,
										@DefaultValue("10")@QueryParam("pageSize")Integer pageSize){
        Page<MmField> page = mmFieldService.listByPageCondition(mmFieldSelectVo, pageNum, pageSize);
		return MessageResult.successe(Page.class).data(page).status(200).message("查询成功").build();
    }

    @ApiOperation(value = "新增字段", notes = "新增字段")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mmField", value = "字段", dataTypeClass = MmField.class, paramType = "body",required = true)
    })
	@POST
    @Produces({MediaType.APPLICATION_JSON})
    public MessageResult<Void> insertMmField(MmField mmField){
        mmFieldService.insertMmField(mmField);
		return MessageResult.successe(Void.class).status(200).message("新增操作成功").build();
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/batch")
    @ApiOperation(value = "批量新增字段", notes = "批量新增字段")
    public MessageResult<Void> batchInsert(List<MmField> MmFields){
        mmFieldService.batchInsert(MmFields);
		return MessageResult.successe(Void.class).status(200).message("批量新增操作成功").build();
    }

    @ApiOperation(value = "根据id删除字段", notes = "根据id删除字段")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "fieldId", value = "字段id", paramType = "path", dataType = "string", required = true)
    })
    @DELETE
    @Produces({MediaType.APPLICATION_JSON})
	@Path("/{fieldId}")
    public MessageResult<Void> deleteMmField(@PathParam("fieldId")String fieldId){
        mmFieldService.deleteMmField(fieldId);
		return MessageResult.successe(Void.class).status(200).message("删除操作成功").build();
    }

	@DELETE
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "批量删除字段", notes = "批量删除字段")
    public MessageResult<Void> batchDelete(List<String> fieldIds){
        mmFieldService.batchDelete(fieldIds);
		return MessageResult.successe(Void.class).status(200).message("批量删除操作成功").build();
    }

    @ApiOperation(value = "根据id更新字段", notes = "根据id更新字段")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mmField", value = "字段", dataTypeClass = MmField.class, paramType = "body",required = true),
            @ApiImplicitParam(name = "fieldId", value = "字段id", paramType = "path", dataType = "string", required = true)
    })
    @PUT
    @Produces({MediaType.APPLICATION_JSON})
	@Path("/{fieldId}")
    public MessageResult<Void> updateMmField(MmField mmField,@PathParam("fieldId")String fieldId){
        mmField.setFieldId(fieldId);
        mmFieldService.updateMmField(mmField);
		return MessageResult.successe(Void.class).status(200).message("更新操作成功").build();
    }

	
	


}
