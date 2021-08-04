/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.analysis.spi.overlay;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.supermap.analyst.spatialanalyst.OverlayAnalyst;
import com.supermap.analyst.spatialanalyst.OverlayAnalystParameter;
import com.supermap.data.*;
import com.supermap.gaf.analysis.entity.OverlayParamVO;
import com.supermap.gaf.analysis.entity.RecordSetVO;
import com.supermap.gaf.analysis.entity.SpatialAnalysisDataConnInfo;
import com.supermap.gaf.analysis.entity.SpatialAnalysisResult;
import com.supermap.gaf.analysis.spi.IspatialOverlay;
import com.supermap.gaf.analysis.utils.CommontypeConversionUtilsExt;
import com.supermap.gaf.analysis.utils.GeometryUtil;
import com.supermap.gaf.analysis.utils.WorkspaceParser;
import com.supermap.gaf.commontypes.MessageResult;
import com.supermap.gaf.utils.LogUtil;
import com.supermap.services.components.commontypes.Geometry;
import com.supermap.services.providers.util.CommontypesConversion;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static com.supermap.gaf.analysis.entity.SpatialConstant.UDBX;
import static com.supermap.gaf.analysis.entity.SpatialConstant.XOR;


/**
 * @author dqc
 * @date:2021/3/25
 * @since : 2020-12-29
 */
public class SpatialOverlayIobjectProvider implements IspatialOverlay {

    private static Logger logger = LogUtil.getLocLogger(SpatialOverlayIserverProvider.class);

    private static final String DEFAULT_RESULT_DATASET_NAME = "resultDataSet";
    private static final String FIELD_INFO_NAME = "fields";
    private static final String FIELD_INFO_CAPTION = "captions";

    private static final String DATASOURCE_ALIAS = "datasourceAlias";
    private static final String INPUT_DATASET_NAME = "input_dataset";
    private static final String TEMP_UDBX_PATH = "tempUDBXPath";

    private static final String XOR_FOR_OPERATOR = "xOR";

    @Override
    public String type() {
        return "OVERLAY-IOBJECT";
    }

    @Override
    public MessageResult<String> verifyParam(OverlayParamVO overlayParamVO) {
        //1. sourceGeometries、sourceDataConnInfo必存其一
        List<Geometry> sourceGeometries = overlayParamVO.getSourceGeometries();
        SpatialAnalysisDataConnInfo sourceDataConnInfo = overlayParamVO.getSourceDataConnInfo();
        String sourceDataSetName = overlayParamVO.getSourceDataSetName();
        SpatialAnalysisDataConnInfo targetDataConnInfo = overlayParamVO.getTargetDataConnInfo();
        String targetDataSetName = overlayParamVO.getTargetDataSetName();
        if (CollectionUtils.isEmpty(sourceGeometries) && null == sourceDataConnInfo) {
            return MessageResult.<String>failed(String.class).message("源数据信息不能为空").build();
        }
        if (CollectionUtils.isEmpty(sourceGeometries) && StringUtils.isEmpty(sourceDataSetName)) {
            return MessageResult.<String>failed(String.class).message("源数据集名称不能为空").build();
        }
        //2.targetDataConnInfo、targetDataSetName不能为空
        if (null == targetDataConnInfo || StringUtils.isEmpty(targetDataSetName)) {
            return MessageResult.<String>failed(String.class).message("目标数据信息不能为空").build();
        }
        return MessageResult.successe(String.class).build();
    }

    @Override
    public MessageResult<SpatialAnalysisResult> overlay(String resultId, OverlayParamVO overlayParamVO) {
        SpatialAnalysisResult spatialAnalysisResult = null;
        List<Geometry> sourceGeometries = overlayParamVO.getSourceGeometries();
        SpatialAnalysisDataConnInfo sourceDataConnInfo = overlayParamVO.getSourceDataConnInfo();
        String sourceDataSetName = overlayParamVO.getSourceDataSetName();
        String sourceDatasetFilter = overlayParamVO.getSourceDatasetFilter();
        String targetDatasetFilter = overlayParamVO.getTargetDatasetFilter();
        SpatialAnalysisDataConnInfo targetDataConnInfo = overlayParamVO.getTargetDataConnInfo();
        SpatialAnalysisDataConnInfo resultDataConnInfo = overlayParamVO.getResultDataConnInfo();
        String targetDataSetName = overlayParamVO.getTargetDataSetName();
        String resultDataSetName = overlayParamVO.getResultDataSetName();
        String operator = overlayParamVO.getOperator().name();
        List<String> retainedFields = overlayParamVO.getRetainedFields();
        List<com.supermap.data.Geometry> ugoGeometries = new ArrayList<>();

        DatasourceConnectionInfo targetConnectionInfo = CommontypeConversionUtilsExt.getUgcDataSourceConnectionInfo(targetDataConnInfo);
        //新建工作空间
        Workspace workspace = WorkspaceParser.createWorkspace(targetConnectionInfo, null);
        //目标数据源
        Datasource targetDatasource = workspace.getDatasources().get(targetConnectionInfo.getAlias());
        //获取target的vector
        DatasetVector targetDataSetVector = (DatasetVector) targetDatasource.getDatasets().get(targetDataSetName);

        //geometry转换
        if (sourceGeometries != null) {
            for (Geometry geo : sourceGeometries) {
                com.supermap.data.Geometry geometry = CommontypesConversion.getUGOGeometry(geo);
                ugoGeometries.add(geometry);
            }
            //坐标转换
            int targetCode = targetDataSetVector.getPrjCoordSys().getEPSGCode();
            if (targetCode != 0) {
                int sourceCode = 4490;
                ugoGeometries = GeometryUtil.transUgoGeometries(ugoGeometries, sourceCode, targetCode);
            }
            // geometries生成本地临时udbx
            String udbxName = "ANALYSIS_INPUT_" + resultId;
            Map<String, String> resultMap = geos2Udbx(udbxName, ugoGeometries);
            sourceDataConnInfo = new SpatialAnalysisDataConnInfo();
            sourceDataConnInfo.setEngineType("udbx");
            sourceDataConnInfo.setAlias(DATASOURCE_ALIAS);
            sourceDataConnInfo.setUrl(resultMap.get(TEMP_UDBX_PATH));

            sourceDataSetName = resultMap.get(INPUT_DATASET_NAME);
        }
        //结果数据连接信息
        if (null == resultDataConnInfo) {
            resultDataConnInfo = targetDataConnInfo;
            resultDataConnInfo.setAlias(targetConnectionInfo.getAlias());
        }
        DatasourceConnectionInfo sourceConnectionInfo = CommontypeConversionUtilsExt.getUgcDataSourceConnectionInfo(sourceDataConnInfo);
        DatasourceConnectionInfo resultConnectionInfo = CommontypeConversionUtilsExt.getUgcDataSourceConnectionInfo(resultDataConnInfo);
        if (null == resultConnectionInfo) {
            return MessageResult.failed(SpatialAnalysisResult.class).message("转换数据源连接信息异常").build();
        }

        //源数据数据源添加到工作空间
        Datasource sourceDatasource = WorkspaceParser.addDataSourceToWorkSpace(workspace, sourceConnectionInfo);
        //结果数据源添加到工作空间
        Datasource resultDatasource = WorkspaceParser.addDataSourceToWorkSpace(workspace, resultConnectionInfo);
        if (StringUtils.isEmpty(resultDataSetName)) {
            resultDataSetName = DEFAULT_RESULT_DATASET_NAME;
        }
        //获取result的vector
        Dataset resultDataSet = resultDatasource.getDatasets().get(resultDataSetName);
        DatasetVector resultDataSetVector = getResultVector(resultDataSet, resultDatasource, resultDataSetName);
        //获取source的vector
        Dataset sourceDataSet = sourceDatasource.getDatasets().get(sourceDataSetName);
        DatasetVector sourceDataSetVector = (DatasetVector) sourceDataSet;
        //叠加分析
        try {
            //过滤数据
            filterRecordset(sourceDatasetFilter, targetDatasetFilter, sourceDataSetVector, targetDataSetVector);
            //执行iobject分析
            spatialAnalysisResult = executeOverlayByIobject(resultId, retainedFields, operator, resultDataSetName, sourceDataSetVector, targetDataSetVector, resultDataSetVector, resultDataConnInfo);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {//释放资源
            release(sourceGeometries, sourceDataConnInfo, workspace, targetDataSetVector, sourceDataSet, resultDataSetVector);
        }
        return MessageResult.<SpatialAnalysisResult>successe(SpatialAnalysisResult.class).data(spatialAnalysisResult).build();
    }

    /**
     * IObject组件执行分析
     */
    private SpatialAnalysisResult executeOverlayByIobject(String resultId,
                                                          List<String> retainedFields,
                                                          String operator,
                                                          String resultDataSetName,
                                                          DatasetVector sourceDataSetVector,
                                                          DatasetVector targetDataSetVector,
                                                          DatasetVector resultDataSetVector,
                                                          SpatialAnalysisDataConnInfo resultDataConnInfo) throws Exception {
        //设置条件
        OverlayAnalystParameter overlayAnalystParameter = buildOverlayAnalystParameter(retainedFields);

        Class<OverlayAnalyst> overlayAnalystClass = OverlayAnalyst.class;
        Method method = overlayAnalystClass.getMethod(getOperatorMethodName(operator), DatasetVector.class, DatasetVector.class, DatasetVector.class, OverlayAnalystParameter.class);
        Object o = method.invoke(null, targetDataSetVector, sourceDataSetVector, resultDataSetVector, overlayAnalystParameter);
        boolean flag = (boolean) o;
        if (flag) {
            ObjectMapper objectMapper = new ObjectMapper();
            //结果数据集投影转换
            resultDataSetVector.setPrjCoordSys(targetDataSetVector.getPrjCoordSys());
            return SpatialAnalysisResult.builder()
                    .resultId(resultId)
                    .resultDataSetName(resultDataSetName)
                    .resultConnInfo(objectMapper.writeValueAsString(resultDataConnInfo))
                    .build();
        } else {
            throw new RuntimeException("IOBJECTS分析异常");
        }
    }

    /**
     * 构建分析参数
     */
    private OverlayAnalystParameter buildOverlayAnalystParameter(List<String> retainedFields) {
        OverlayAnalystParameter overlayAnalystParameter = new OverlayAnalystParameter();
        if (CollectionUtils.isNotEmpty(retainedFields)) {
            overlayAnalystParameter.setOperationRetainedFields(retainedFields.toArray(new String[retainedFields.size()]));
        }
        overlayAnalystParameter.setTolerance(0.00000001);
        return overlayAnalystParameter;
    }

    /**
     * 获取结果数据集的DatasetVector
     *
     * @param resultDataSet
     * @param resultDatasource
     * @param resultDataSetName
     * @return
     */
    private DatasetVector getResultVector(Dataset resultDataSet, Datasource resultDatasource, String resultDataSetName) {
        DatasetVector resultDataSetVector = null;
        if (null == resultDataSet) {
            resultDataSetName = resultDatasource.getDatasets().getAvailableDatasetName(resultDataSetName);
            DatasetVectorInfo datasetVectorInfo = new DatasetVectorInfo(resultDataSetName, DatasetType.REGION);
            datasetVectorInfo.setEncodeType(EncodeType.NONE);
            resultDataSetVector = resultDatasource.getDatasets().create(datasetVectorInfo);
        } else {
            resultDataSetVector = (DatasetVector) resultDataSet;
        }
        return resultDataSetVector;
    }

    @Override
    public MessageResult<RecordSetVO> getResultRecordSet(SpatialAnalysisResult spatialAnalysisResult, Integer pageIndex, Integer pageSize) {
        String resultConnInfo = spatialAnalysisResult.getResultConnInfo();
        String resultDataSetName = spatialAnalysisResult.getResultDataSetName();
        Workspace workspace = null;
        Datasources datasources = null;
        DatasetVector dataset = null;
        Recordset recordSet = null;
        try {
            workspace = new Workspace();
            datasources = workspace.getDatasources();
            SpatialAnalysisDataConnInfo spatialAnalysisDataConnInfo = JSON.parseObject(resultConnInfo, SpatialAnalysisDataConnInfo.class);
            DatasourceConnectionInfo datasourceConnectionInfo = CommontypeConversionUtilsExt.getUgcDataSourceConnectionInfo(spatialAnalysisDataConnInfo);
            Datasource datasource = datasources.open(datasourceConnectionInfo);

            Datasets datasets = datasource.getDatasets();
            dataset = (DatasetVector) datasets.get(resultDataSetName);

            recordSet = dataset.getRecordset(false, CursorType.STATIC);

            int count = recordSet.getRecordCount();
            Map<String, String[]> fieldInfoMap = getFieldInfos(recordSet);
            String[] fields = fieldInfoMap.get(FIELD_INFO_NAME);
            String[] captions = fieldInfoMap.get(FIELD_INFO_CAPTION);
            List<com.supermap.services.components.commontypes.Geometry> geometryList = new LinkedList<>();
            String[][] values = getDataAndGeometries(recordSet, count, fields.length, geometryList);
            RecordSetVO recordSetVO = getResultVo(fields, captions, values, geometryList, count, pageIndex, pageSize);
            return MessageResult.successe(RecordSetVO.class).data(recordSetVO).build();
        } catch (Exception e) {
            logger.error("获取IOBJECTS分析结果异常");
            throw new RuntimeException("获取IOBJECTS分析结果异常");
        } finally {
            if (null != recordSet) {
                recordSet.dispose();
            }
            if (null != dataset) {
                dataset.close();
            }
            if (null != datasources) {
                datasources.closeAll();
            }
            if (null != workspace) {
                workspace.dispose();
            }
        }
    }

    /**
     * 查询符合条件的记录集
     *
     * @param datasetVector
     * @param filter
     * @return
     */
    private Recordset queryRecordset(DatasetVector datasetVector, String filter) {
        QueryParameter queryParameter = new QueryParameter();
        queryParameter.setAttributeFilter(filter);
        queryParameter.setCursorType(CursorType.STATIC);
        queryParameter.setSpatialQueryMode(SpatialQueryMode.NONE);
        return datasetVector.query(queryParameter);
    }

    /**
     * 图形对象转储sdx空间数据库udbx
     */
    private Map<String, String> geos2Udbx(String udbxName, List<com.supermap.data.Geometry> geometryList) {
        Map<String, String> result = new HashMap<>(16);
        String path = String.format("/tmp/%s.udbx", udbxName);
        //创建udbx数据源
        Workspace workspace = new Workspace();
        Datasources datasources = workspace.getDatasources();
        DatasourceConnectionInfo connectionInfo = new DatasourceConnectionInfo();
        connectionInfo.setServer(path);
        connectionInfo.setAlias(DATASOURCE_ALIAS);
        connectionInfo.setEngineType(EngineType.UDBX);
        Datasource datasource = datasources.create(connectionInfo);
        //创建数据集
        Datasets datasets = datasource.getDatasets();
        DatasetVectorInfo datasetVectorInfo = new DatasetVectorInfo();
        datasetVectorInfo.setType(DatasetType.REGION);
        datasetVectorInfo.setEncodeType(EncodeType.NONE);
        String name = datasets.getAvailableDatasetName(INPUT_DATASET_NAME);
        datasetVectorInfo.setName(name);
        DatasetVector datasetVector = datasets.create(datasetVectorInfo);
        //写入数据集数据
        Recordset recordset = datasetVector.getRecordset(false, CursorType.DYNAMIC);
        recordset.moveFirst();
        recordset.edit();
        recordset.delete();
        recordset.update();
        for (int i = 0; i < geometryList.size(); i++) {
            recordset.addNew(geometryList.get(i));
            recordset.moveNext();
        }
        recordset.update();
        //释放资源
        datasetVector.close();
        datasources.closeAll();
        workspace.dispose();
        result.put(TEMP_UDBX_PATH, path);
        result.put(INPUT_DATASET_NAME, name);
        return result;
    }

    private String getOperatorMethodName(String operator) {
        operator = operator.toLowerCase();
        if (operator.equals(XOR)) {
            operator = XOR_FOR_OPERATOR;
        }
        return operator;
    }

    /**
     * 获取所有字段名及别名
     *
     * @param recordset 组件结果集
     * @return
     */
    private Map<String, String[]> getFieldInfos(Recordset recordset) {
        Map<String, String[]> map = new HashMap<>(16);
        FieldInfos fieldInfos = recordset.getDataset().getFieldInfos();
        int size = fieldInfos.getCount();
        String[] fields = new String[size];
        String[] captions = new String[size];
        for (int i = 0; i < size; i++) {
            FieldInfo fieldInfo = fieldInfos.get(i);
            fields[i] = fieldInfo.getName().toLowerCase();
            captions[i] = fieldInfo.getCaption();
        }
        map.put(FIELD_INFO_NAME, fields);
        map.put(FIELD_INFO_CAPTION, captions);
        return map;
    }

    /**
     * 获取所有数据
     *
     * @param recordSet   组件结果集
     * @param total       数据总数
     * @param fieldLength 字段总数
     * @return
     */
    private String[][] getDataAndGeometries(Recordset recordSet, int total, int fieldLength, List<com.supermap.services.components.commontypes.Geometry> geometryList) {
        String[][] data = new String[total][fieldLength];
        for (int i = 0; i < total; i++) {
            geometryList.add(CommontypesConversion.getGeometry(recordSet.getGeometry()));
            for (int j = 0; j < fieldLength; j++) {
                data[i][j] = recordSet.getString(j);
            }
            recordSet.moveNext();
        }
        return data;
    }

    private RecordSetVO getResultVo(String[] fields, String[] captions, String[][] values, List<com.supermap.services.components.commontypes.Geometry> geometryList, int count, Integer pageIndex, Integer pageSize) {
        //resultData的value进行截取分页
        int start = pageIndex * pageSize;
        int end = (pageIndex + 1) * pageSize - 1;
        int total = count;
        //判断越界
        if (end >= start && total != 0) {
            start = start < 0 ? 0 : start;
            end = end <= total - 1 ? end : total - 1;
        } else {
            return null;
        }
        int offset = end - start + 1;
        //组装结果集
        List<com.supermap.services.components.commontypes.Geometry> paginationGeometries = new LinkedList<>();
        String[][] results = new String[offset][fields.length];
        for (int i = start; i < end + 1; i++) {
            paginationGeometries.add(geometryList.get(i - start));
            for (int j = 0; j < fields.length; j++) {
                results[i - start][j] = values[i][j];
            }
        }
        //组装dataVo
        RecordSetVO dataVo = new RecordSetVO();
        dataVo.setValues(results);
        dataVo.setFields(fields);
        dataVo.setCaptions(captions);
        dataVo.setTotal(total);
        return dataVo;
    }

    /**
     * 释放资源
     */
    private void release(List<Geometry> sourceGeometries, SpatialAnalysisDataConnInfo sourceDataConnInfo, Workspace workspace, DatasetVector targetDataSetVector, Dataset sourceDataSet, DatasetVector resultDataSetVector) {
        sourceDataSet.close();
        targetDataSetVector.close();
        resultDataSetVector.close();
        workspace.getDatasources().closeAll();
        workspace.dispose();
        //如果根据geometry创建了临时udbx文件，则使用完后删除
        if (sourceGeometries != null && sourceDataConnInfo.getEngineType().equalsIgnoreCase(UDBX)) {
            String udbxPath = sourceDataConnInfo.getUrl();
            Path path = Paths.get(udbxPath);
            try {
                Files.delete(path);
            } catch (IOException e) {
                logger.warn("移除临时udbx异常", e);
            }
        }
    }

    /**
     * 过滤符合条件的数据记录
     */
    private void filterRecordset(String sourceDatasetFilter, String targetDatasetFilter, DatasetVector sourceDataSet, DatasetVector targetDataSet) {
        //源数据集存在过滤条件，只有满足条件的对象才进行叠加分析
        if (!StringUtils.isEmpty(sourceDatasetFilter)) {
            Recordset sourceRecordset = queryRecordset(sourceDataSet, sourceDatasetFilter);
            sourceDataSet.truncate();
            sourceDataSet.append(sourceRecordset);
        }
        //目标数据集存在过滤条件，只有满足条件的对象才进行叠加分析
        if (!StringUtils.isEmpty(targetDatasetFilter)) {
            Recordset targetRecordset = queryRecordset(targetDataSet, targetDatasetFilter);
            targetDataSet.truncate();
            targetDataSet.append(targetRecordset);
        }
    }
}
