package com.supermap.gaf.data.mgt.resource;

import com.supermap.data.conversion.ImportResult;
import com.supermap.gaf.commontypes.MessageResult;
import com.supermap.gaf.data.mgt.commontype.SysResourceDatasource;
import com.supermap.gaf.data.mgt.service.SpaceDatasourceService;
import com.supermap.gaf.data.mgt.vo.TemplateToTargetVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * 空间数据源功能接口
 * @author wxl
 * @since 2021/7/27
 */
@Component
@Api(value = "空间数据源功能接口")
public class SpaceDatasourceResource {

    @Autowired
    private SpaceDatasourceService spaceDatasourceService;

    @ApiOperation(value = "新建空的空间数据源", notes = "新建空的空间数据源")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sysResourceDatasource", value = "数据源", dataTypeClass = SysResourceDatasource.class, paramType = "body",required = true)
    })
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Path(value = "/empty")
    public MessageResult<SysResourceDatasource> createEmptySpaceDatasource(SysResourceDatasource sysResourceDatasource) {
        if (spaceDatasourceService.checkNameRepeat(sysResourceDatasource.getDsName())) {
            throw new IllegalArgumentException("数据源别名重复");
        }
        SysResourceDatasource datasource = spaceDatasourceService.createEmptySpaceDatasource(sysResourceDatasource);
        return MessageResult.data(datasource).build();

    }

    @ApiOperation(value = "根据空间数据源模板创建空间数据源", notes = "根据空间数据源模板创建空间数据源，空间数据源也是空间数据源模板")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "templateToTargetVO", value = "数据源模板或数据源id 和目标数据源id", dataTypeClass = TemplateToTargetVO.class, paramType = "body",required = true),
    })
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Path(value = "/template/target")
    public MessageResult<Void> createSpaceDatasourceByTemplate(TemplateToTargetVO templateToTargetVO) {
        String templateId = templateToTargetVO.getTemplateId();
        String targetId = templateToTargetVO.getTargetId();
        spaceDatasourceService.createSpaceDatasourceByTemplate(templateId,targetId);
        return MessageResult.successe(Void.class).build();

    }


    @ApiOperation(value = "数据导入", notes = "导入矢量、栅格、海图、三维、电信、COGO")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "importSettingJsonStr", value = "数据导入设置信息", dataTypeClass = String.class, paramType = "body",required = true),
    })
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Path(value = "/import")
    public MessageResult<ImportResult> importData(String importSettingJsonStr) {
        ImportResult importResult = spaceDatasourceService.importData(importSettingJsonStr);
        if (importResult.getFailedSettings().length <= 0) {
            return MessageResult.successe(ImportResult.class).data(importResult).build();
        } else {
            return MessageResult.failed(ImportResult.class).data(importResult).build();
        }
    }


}
