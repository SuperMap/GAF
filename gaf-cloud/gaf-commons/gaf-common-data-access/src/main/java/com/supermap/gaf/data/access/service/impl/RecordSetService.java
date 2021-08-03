/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.data.access.service.impl;

import com.supermap.data.*;
import com.supermap.gaf.data.access.service.DataSourceManager;
import com.supermap.gaf.data.access.service.RecordSetManager;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


/**
 * @author:yj
 * @date:2021/3/25
 */
@Service
public class RecordSetService implements RecordSetManager {

    @Override
    public Recordset query(DatasourceConnectionInfo datasourceConnectionInfo, String datasetName, String attributeFilter) {
        DatasetVector datasetVector = null;
        Dataset dataset = null;
        Datasource datasource = null;
        try {
            datasource = DataSourceManager.getDatasource(datasourceConnectionInfo);
            dataset = datasource.getDatasets().get(datasetName);
            datasetVector = (DatasetVector) dataset;

            // 设置查询条件，eg：SmID<100
            QueryParameter parameter = new QueryParameter();
            if (!StringUtils.isEmpty(attributeFilter)) {
                parameter.setAttributeFilter(attributeFilter);
                parameter.setCursorType(CursorType.STATIC);
                parameter.setSpatialQueryMode(SpatialQueryMode.NONE);
            }
            return datasetVector.query(parameter);
        } finally {
            release(datasetVector, dataset, datasource);
        }
    }

    @Override
    public boolean add(DatasourceConnectionInfo datasourceConnectionInfo, String datasetName, Recordset recordset) {
        DatasetVector datasetVector = null;
        Dataset dataset = null;
        Datasource datasource = null;
        try {
            datasource = DataSourceManager.getDatasource(datasourceConnectionInfo);
            dataset = datasource.getDatasets().get(datasetName);
            datasetVector = (DatasetVector) dataset;
            return datasetVector.append(recordset);
        } finally {
            release(datasetVector, dataset, datasource);
        }
    }

    @Override
    public boolean update(DatasourceConnectionInfo datasourceConnectionInfo, String datasetName, String fieldName, Object value, String attributeFilter) {
        DatasetVector datasetVector = null;
        Dataset dataset = null;
        Datasource datasource = null;
        Recordset recordset = null;
        try {
            datasource = DataSourceManager.getDatasource(datasourceConnectionInfo);
            dataset = datasource.getDatasets().get(datasetName);
            datasetVector = (DatasetVector) dataset;
            // 设置查询条件，eg：SmID<100
            QueryParameter parameter = new QueryParameter();
            parameter.setCursorType(CursorType.DYNAMIC);
            parameter.setSpatialQueryMode(SpatialQueryMode.NONE);
            if (!StringUtils.isEmpty(attributeFilter)) {
                parameter.setAttributeFilter(attributeFilter);
            }
            recordset = datasetVector.query(parameter);
            recordset.edit();
            recordset.setFieldValue(fieldName, value);
            return recordset.update();
        } finally {
            if (null != recordset) {
                recordset.close();
                recordset.dispose();
            }
            release(datasetVector, dataset, datasource);
        }
    }

    @Override
    public boolean remove(DatasourceConnectionInfo datasourceConnectionInfo, String datasetName, int[] smIds) {
        DatasetVector datasetVector = null;
        Dataset dataset = null;
        Datasource datasource = null;
        try {
            datasource = DataSourceManager.getDatasource(datasourceConnectionInfo);
            dataset = datasource.getDatasets().get(datasetName);
            datasetVector = (DatasetVector) dataset;
            return datasetVector.deleteRecords(smIds);
        } finally {
            release(datasetVector, dataset, datasource);
        }
    }

    // 释放资源
    private void release(DatasetVector datasetVector, Dataset dataset, Datasource datasource) {
        if (null != datasetVector) {
            datasetVector.close();
        }
        if (null != dataset) {
            dataset.close();
        }
        if (null != datasource) {
            datasource.close();
        }
    }
}
