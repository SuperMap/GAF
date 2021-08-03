/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.analysis.util;

import com.supermap.analyst.spatialanalyst.OverlayAnalystParameter;
import com.supermap.data.*;
import com.supermap.gaf.analysis.entity.OverlayAnalystParamVO;
import com.supermap.services.components.commontypes.DatasourceConnectionInfo;
import com.supermap.services.providers.util.CommontypesConversion;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;

/**
 * @author : duke
 * @date:2021/3/25
 * @since 2021/3/25 4:58 PM
 */
@Slf4j
public class AnalystUtils {

    private static final String TEMP_DATASET_NAME = "temp_dataset_name";
    private static final String TEMP_DATASOURCE_ALIAS = "temp_datasource_alias";
    private static final String TEMP_FILE_PREFIX = "supermap";
    private static final String TEMP_FILE_SUFFIX = "udbx";

    /**
     * 通过数据源链接信息和数据集名称获取到数据集的DatasetVector
     *
     * @return
     */
    public static DatasetVector getDatasetVector(Workspace workspace,
                                                 DatasourceConnectionInfo datasourceConnectionInfo,
                                                 String datasetName) {
        Datasource datasource = workspace.getDatasources().open(getUgcDatasourceConnectionInfo(datasourceConnectionInfo));
        return (DatasetVector) datasource.getDatasets().get(datasetName);
    }

    /**
     * iserver数据源链接信息实体转为ugc数据源链接信息实体
     *
     * @param datasourceConnectionInfo
     * @return
     */
    public static com.supermap.data.DatasourceConnectionInfo getUgcDatasourceConnectionInfo(DatasourceConnectionInfo datasourceConnectionInfo) {
        com.supermap.data.DatasourceConnectionInfo connectionInfo = new com.supermap.data.DatasourceConnectionInfo();
        connectionInfo.setAlias(datasourceConnectionInfo.alias);
        connectionInfo.setEngineType((com.supermap.data.EngineType) com.supermap.data.Enum.parse(com.supermap.data.EngineType.class, datasourceConnectionInfo.engineType.toString()));
        connectionInfo.setServer(datasourceConnectionInfo.server);
        connectionInfo.setDatabase(datasourceConnectionInfo.dataBase);
        connectionInfo.setUser(datasourceConnectionInfo.user);
        connectionInfo.setPassword(datasourceConnectionInfo.password);
        connectionInfo.setDriver(datasourceConnectionInfo.driver);
        connectionInfo.setReadOnly(datasourceConnectionInfo.readOnly);
        connectionInfo.setOpenLinkTable(datasourceConnectionInfo.openLinkTable);
        connectionInfo.setAutoConnect(datasourceConnectionInfo.connect);
        return connectionInfo;
    }

    /**
     * 创建存放结果的临时数据集
     */
    public static com.supermap.data.DatasetVector createTempDatasetVector(Workspace workspace) {
        String path = null;
        try {
            path = File.createTempFile(TEMP_FILE_PREFIX, TEMP_FILE_SUFFIX).getPath();
        } catch (IOException e) {
            log.error("创建临时文件时错误");
        }
        //创建udbx数据源
        Datasources datasources = workspace.getDatasources();
        com.supermap.data.DatasourceConnectionInfo connectionInfo = new com.supermap.data.DatasourceConnectionInfo();
        connectionInfo.setServer(path);
        connectionInfo.setAlias(TEMP_DATASOURCE_ALIAS);
        connectionInfo.setEngineType(EngineType.UDBX);
        Datasource datasource = datasources.create(connectionInfo);
        //创建数据集
        Datasets datasets = datasource.getDatasets();
        DatasetVectorInfo datasetVectorInfo = new DatasetVectorInfo();
        datasetVectorInfo.setType(DatasetType.REGION);
        datasetVectorInfo.setEncodeType(EncodeType.NONE);
        String name = datasets.getAvailableDatasetName(TEMP_DATASET_NAME);
        datasetVectorInfo.setName(name);
        return datasets.create(datasetVectorInfo);
    }

    /**
     * 在工作空间打开数据源
     *
     * @param workspace
     */
    public static void openDatasource(Workspace workspace, com.supermap.data.DatasourceConnectionInfo... datasourceConnectionInfos) {
        for (com.supermap.data.DatasourceConnectionInfo connectionInfo : datasourceConnectionInfos) {
            if (null != connectionInfo) {
                workspace.getDatasources().open(connectionInfo);
            }
        }
    }


    /**
     * 构建执行叠加分析参数
     *
     * @param paramVO
     * @return
     */
    public static OverlayAnalystParameter buildOverlayAnalystParameter(OverlayAnalystParamVO paramVO) {
        OverlayAnalystParameter overlayAnalystParameter = new OverlayAnalystParameter();
        overlayAnalystParameter.setTolerance(paramVO.getTolerance());
        overlayAnalystParameter.setOperationRetainedFields(paramVO.getOperationRetainedFields());
        overlayAnalystParameter.setSourceRetainedFields(paramVO.getSourceRetainedFields());
        return overlayAnalystParameter;
    }


    /**
     * 转换ugc 几何图形对象
     *
     * @param geometries
     * @return
     */
    public static Geometry[] getUgoGeometries(com.supermap.services.components.commontypes.Geometry[] geometries) {
        Geometry[] resultArr = new Geometry[geometries.length];
        for (int i = 0; i < geometries.length; i++) {
            resultArr[i] = CommontypesConversion.getUGOGeometry(geometries[i]);
        }
        return resultArr;
    }

    /**
     * TODO 先完善ISERVER分析接口
     * 读取结果数据集
     *
     * @param resultDataset
     * @return
     */
    public static com.supermap.services.components.commontypes.Recordset readRecordset(DatasetVector resultDataset, Integer count) {
        com.supermap.services.components.commontypes.Recordset resultRecordset = new com.supermap.services.components.commontypes.Recordset();
        Recordset recordset = resultDataset.getRecordset(false, CursorType.STATIC);
        return null;
    }

}
