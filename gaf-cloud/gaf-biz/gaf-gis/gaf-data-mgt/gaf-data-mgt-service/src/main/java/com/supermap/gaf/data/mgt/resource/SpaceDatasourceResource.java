package com.supermap.gaf.data.mgt.resource;

import com.supermap.gaf.commontypes.MessageResult;
import com.supermap.gaf.data.mgt.commontype.SysResourceDatasource;
import com.supermap.gaf.data.mgt.conversion.result.DataExportResult;
import com.supermap.gaf.data.mgt.conversion.result.DataImportResult;
import com.supermap.gaf.data.mgt.entity.BuildS3MParameter;
import com.supermap.gaf.data.mgt.entity.DataSourceInfo;
import com.supermap.gaf.data.mgt.entity.GDataset;
import com.supermap.gaf.data.mgt.service.S3MCacheService;
import com.supermap.gaf.data.mgt.service.SpaceDatasourceService;
import com.supermap.gaf.data.mgt.vo.TemplateToTargetVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * 空间数据源接口
 * @author wxl
 * @since 2021/7/27
 */
@Component
@Api(value = "空间数据源接口")
public class SpaceDatasourceResource {

    @Autowired
    private SpaceDatasourceService spaceDatasourceService;

    @Autowired
    private S3MCacheService s3mCacheService;

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
            @ApiImplicitParam(name = "importSettingJsonArray", value = "数据导入设置信息数组", dataTypeClass = String.class, paramType = "body",required = true),
    })
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Path(value = "/import")
    public MessageResult<DataImportResult> importData(String importSettingJsonArray) {
        DataImportResult dataImportResult = spaceDatasourceService.importData(importSettingJsonArray);
        if (dataImportResult.getFailed().size() <= 0) {
            return MessageResult.successe(DataImportResult.class).data(dataImportResult).message("全部导入成功").build();
        } else {
            return MessageResult.failed(DataImportResult.class).data(dataImportResult).message("全部导入失败").build();
        }
    }

    @ApiOperation(value = "数据导出", notes = "批量导出矢量、栅格、海图、三维、电信、COGO")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "exportSettingJsonArray", value = "数据导出设置信息数组", dataTypeClass = String.class, paramType = "body",required = true),
    })
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Path(value = "/export")
    public MessageResult<DataExportResult> exportData(String exportSettingJsonArray) {
        DataExportResult dataExportResult = spaceDatasourceService.exportData(exportSettingJsonArray);
        if (dataExportResult.getFailed().size() <= 0) {
            return MessageResult.successe(DataExportResult.class).data(dataExportResult).message("全部导出成功").build();
        } else {
            return MessageResult.failed(DataExportResult.class).data(dataExportResult).message("全部导出失败").build();
        }
    }


    @ApiOperation(value = "查找数据源下的数据集列表", notes = "根据数据源信息查找数据集列表")
    @ApiImplicitParam(name = "dataSourceInfo",value = "数据源实体类",paramType = "body",dataTypeClass = DataSourceInfo.class)
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/list-dataset")
    public MessageResult<List<GDataset>> listDatasets(DataSourceInfo dataSourceInfo) {
        List<GDataset> result = spaceDatasourceService.listDataset(dataSourceInfo);
        return MessageResult.data(result).message("查询成功").build();
    }

    @ApiOperation(value = "根据数据源id查找数据源下的数据集列表",notes = "数据源应该是空间数据源，若未非空间数据源则返回空集合")
    @ApiImplicitParam(name = "datasourceId",value = "数据源id",paramType = "path",dataTypeClass = String.class)
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/{datasourceId}/datasets")
    public MessageResult<List<GDataset>> listDatasetsById(@PathParam("datasourceId") String datasourceId) {
        List<GDataset> result = spaceDatasourceService.listDataset(datasourceId);
        return MessageResult.data(result).message("查询成功").build();
    }



    @ApiOperation(value = "生成s3m切片", notes = "生成s3m切片")
    @ApiImplicitParam(name = "buildS3MParameter",value = "构建s3m实体类",paramType = "body",dataTypeClass = BuildS3MParameter.class)
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/s3m")
    public MessageResult<String> buildS3m(BuildS3MParameter buildS3mParameter) {
        return s3mCacheService.buildS3M(buildS3mParameter.getDataSourceInfo(), buildS3mParameter.getDatasetNames(), buildS3mParameter.isOverWrite(), buildS3mParameter.getOutputFolder());
    }



}
