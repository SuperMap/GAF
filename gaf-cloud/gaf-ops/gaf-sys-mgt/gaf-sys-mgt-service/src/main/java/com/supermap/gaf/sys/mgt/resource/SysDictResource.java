/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.sys.mgt.resource;

import com.supermap.gaf.authority.vo.TreeNode;
import com.supermap.gaf.commontypes.MessageResult;
import com.supermap.gaf.commontypes.Page;
import com.supermap.gaf.sys.mgt.client.SysDictClient;
import com.supermap.gaf.sys.mgt.commontype.SysDict;
import com.supermap.gaf.sys.mgt.model.DictData;
import com.supermap.gaf.sys.mgt.model.DictDataNode;
import com.supermap.gaf.sys.mgt.model.DictType;
import com.supermap.gaf.sys.mgt.service.SysDictService;
import com.supermap.gaf.sys.mgt.vo.SysDictSelectVo;
import io.swagger.annotations.*;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 字典接口
 *
 * @author wangxiaolong
 * @date 2021-01-08
 */
@Component
@Api(value = "字典接口")
public class SysDictResource implements SysDictClient {


    private final SysDictService sysDictService;

    public SysDictResource(SysDictService sysDictService) {
        this.sysDictService = sysDictService;
    }

    /**
     * 根据字典类别编码获取字典树
     *
     * @param dictTypeCode 字典类别编码
     * @return 若未查询到则返回null
     */
    @ApiOperation(value = "查询树形结构的字典数据", notes = "根据字典类别编码查询树形结构的字典数据,若未查到则返回的字典数据为null")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dictTypeCode", value = "字典类别编码", example = "ServiceType", paramType = "path", dataType = "string", required = true),
            @ApiImplicitParam(name = "value", value = "字典值, 表示从字典树的哪个节点获取子字典树，默认为null,表示直接获取字典类别下的子字典树", example = "RESTDATA", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "level", value = "控制获取字典值对应字典数据的子字典树最大层级,默认值0表示获取不限制层级，获取字典值下的所有子字典树", defaultValue = "0", paramType = "query", dataType = "integer"),
            @ApiImplicitParam(name = "onlyVisible", value = "表示是否只可见，控制获取属性visible为true的字典值", defaultValue = "false", paramType = "query", dataType = "boolean")
    })
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/{dictTypeCode}/tree")
    @Override
    public MessageResult<List<DictDataNode>> getDictDataTree(@PathParam("dictTypeCode") String dictTypeCode,
                                                             @QueryParam("value") String value,
                                                             @QueryParam("level") @DefaultValue("0") int level,
                                                             @QueryParam("onlyVisible") @DefaultValue("false") boolean onlyVisible) {
        MessageResult<List<DictDataNode>> result = new MessageResult<>();
        List<DictDataNode> dictDataNodeList = sysDictService.getDictDataTree(dictTypeCode, value, level, onlyVisible);
        result.setData(dictDataNodeList);
        result.setStatus(200);
        result.setSuccessed(true);
        return result;
    }


    /**
     * 根据字典类别编码查询字典数据
     *
     * @param dictTypeCode 字典类别编码
     * @return 若为查询到则返回null
     */
    @ApiOperation(value = "查询字典数据", notes = "根据字典类别编码查询字典数据。若未查询到则返回的字典数据为null")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dictTypeCode", value = "字典类别编码", example = "ServiceType", paramType = "path", dataType = "string", required = true),
            @ApiImplicitParam(name = "onlyVisible", value = "是否只可见。若为true,则查询属性visible为true的字典数据，若为false,则查询时不限制属性visible。默认false", defaultValue = "false", paramType = "query", dataType = "boolean")
    })
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/{dictTypeCode}/dict-data")
    @Override
    public MessageResult<List<DictData>> getDictData(@NotEmpty @PathParam("dictTypeCode") String dictTypeCode,
                                                     @DefaultValue("false") @QueryParam("onlyVisible") Boolean onlyVisible) {
        MessageResult<List<DictData>> result = new MessageResult<>();
        List<DictData> dictData = sysDictService.getDictData(dictTypeCode);
        if (dictData == null) {
            result.setData(null);
        } else if (onlyVisible) {
            dictData = dictData.stream().filter(dictData1 -> onlyVisible.equals(dictData1.getVisibility())).collect(Collectors.toList());
            if (dictData.size() == 0) {
                result.setData(null);
            } else {
                result.setData(dictData);
            }
        } else {
            result.setData(dictData);
        }
        result.setStatus(200);
        result.setSuccessed(true);
        return result;
    }


    @ApiOperation(value = "查找路径", notes = "根据字典类别编码和字典值查找路径,若未查到则返回的路径为null")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dictTypeCode", value = "字典类别编码", example = "ServiceType", paramType = "path", dataType = "string", required = true),
            @ApiImplicitParam(name = "value", value = "字典值", example = "RESTDATA", paramType = "path", dataType = "string", required = true)
    })
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/{dictTypeCode}/{value}/path")
    @Override
    public MessageResult<List<DictData>> getPath(@NotEmpty @PathParam("dictTypeCode") String dictTypeCode,
                                                 @NotEmpty @PathParam("value") String value) {
        MessageResult<List<DictData>> result = new MessageResult<>();
        List<DictDataNode> dictDataNodeList = sysDictService.getPath(dictTypeCode, value);
        if (dictDataNodeList != null) {
            List<DictData> collect = dictDataNodeList.stream().map(dictDataNode -> (DictData) dictDataNode).collect(Collectors.toList());
            result.setData(collect);
        } else {
            result.setData(null);
        }
        result.setStatus(200);
        result.setSuccessed(true);
        return result;
    }

    @ApiOperation(value = "查询字典类型", notes = "根据字典类型编码查询字典类型")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dictTypeCode", value = "字典类别编码", example = "ServiceType", paramType = "path", dataType = "string", required = true),
    })
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/{dictTypeCode}/detail")
    @Override
    public MessageResult<DictType> getByTypeCode(@NotEmpty @PathParam("dictTypeCode") String dictTypeCode) {
        DictType dictType = sysDictService.getDictType(dictTypeCode);
        return MessageResult.successe(DictType.class).data(dictType).build();
    }

    @ApiOperation(value = "查询字典",  notes = "根据id查询字典")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dataDictId", value = "字典id", paramType = "path", dataType = "string", required = true)
    })
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/{dataDictId}")
    public MessageResult<SysDict> getById(@PathParam("dataDictId") String dataDictId) {
        SysDict sysDict = sysDictService.getById(dataDictId);
        return MessageResult.successe(SysDict.class).data(sysDict).status(200).message("查询成功").build();
    }

    @ApiOperation(value = "查询所有字典节点和目录节点", notes = "这些节点可由调用者组织为树")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/allnodes")
    public MessageResult<List<TreeNode>> listAllNodes() {
        List<TreeNode> allNodes = sysDictService.listAllNodes();
        MessageResult<List<TreeNode>> result = new MessageResult<>();
        result.setSuccessed(true);
        result.setData(allNodes);
        result.setMessage("查询成功");
        return result;
    }

    @ApiOperation(value = "分页条件查询字典")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页码", example = "1",defaultValue = "1", allowableValues = "range[1,infinity]",paramType = "query", dataType = "integer"),
            @ApiImplicitParam(name = "pageSize", value = "每页条数", example = "10", defaultValue = "10",allowableValues = "range(0,infinity]", paramType = "query", dataType = "integer")
    })
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public MessageResult<Page<DictDataNode>> pageList(@ApiParam @Valid @BeanParam SysDictSelectVo sysDictSelectVo,
                                        @DefaultValue("1") @QueryParam("pageNum") Integer pageNum,
                                        @DefaultValue("10") @QueryParam("pageSize") Integer pageSize) {
        return MessageResult.data(sysDictService.listByPageCondition(sysDictSelectVo, pageNum, pageSize)).message("查询成功").build();
    }

    @ApiOperation(value = "新增字典")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sysDict", value = "字典", dataTypeClass = SysDict.class, paramType = "body",required = true)
    })
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public MessageResult<SysDict> insertSysDict(SysDict sysDict) {
        SysDict result = sysDictService.insertSysDict(sysDict);
        return MessageResult.successe(SysDict.class).data(result).status(200).message("新增操作成功").build();
    }

    @ApiOperation(value = "批量新增字典")
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/batch")
    public MessageResult<Void> batchInsert(List<SysDict> sysDicts) {
        sysDictService.batchInsert(sysDicts);
        return MessageResult.successe(Void.class).status(200).message("批量新增操作成功").build();
    }

    @ApiOperation(value = "删除字典", notes = "根据id删除字典")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dataDictId", value = "字典id", paramType = "path", dataType = "string", required = true)
    })
    @DELETE
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/{dataDictId}")
    public MessageResult<SysDict> deleteSysDict(@PathParam("dataDictId") String dataDictId) {
        SysDict oldSysDict = sysDictService.deleteSysDict(dataDictId);
        return MessageResult.successe(SysDict.class).data(oldSysDict).status(200).message("删除操作成功").build();
    }

    @ApiOperation(value = "批量删除字典", notes = "根据字典id集合批量删除字典")
    @DELETE
    @Produces({MediaType.APPLICATION_JSON})
    public MessageResult<Void> batchDelete(List<String> dataDictIds) {
        sysDictService.batchDelete(dataDictIds);
        return MessageResult.successe(Void.class).status(200).message("批量删除操作成功").build();
    }

    @ApiOperation(value = "更新字典", notes = "根据id更新字典")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sysDict", value = "字典", paramType = "body", dataTypeClass = SysDict.class, required = true),
            @ApiImplicitParam(name = "dataDictId", value = "字典id", paramType = "path", dataType = "string", required = true)
    })
    @PUT
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/{dataDictId}")
    public MessageResult<SysDict> updateSysDict(SysDict sysDict, @PathParam("dataDictId") String dataDictId) {
        sysDict.setDataDictId(dataDictId);
        SysDict result = sysDictService.updateSysDict(sysDict);
        return MessageResult.successe(SysDict.class).data(result).status(200).message("更新操作成功").build();
    }


}
