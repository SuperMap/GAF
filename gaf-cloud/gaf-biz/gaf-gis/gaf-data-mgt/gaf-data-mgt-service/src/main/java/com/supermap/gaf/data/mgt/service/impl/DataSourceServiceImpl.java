/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.data.mgt.service.impl;

import com.supermap.data.*;
import com.supermap.gaf.data.mgt.entity.DataSourceInfo;
import com.supermap.gaf.data.mgt.entity.GDataset;
import com.supermap.gaf.data.mgt.service.DataSourceService;
import com.supermap.gaf.data.mgt.util.DatasourceParser;
import com.supermap.gaf.exception.GafException;
import com.supermap.gaf.sys.mgt.commontype.SysResourceDatasource;
import com.supermap.gaf.sys.mgt.service.SysResourceDatasourceQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author:yd
 * @date:2021/3/25
 * @Date 2021-3-01
 */
@Service
public class DataSourceServiceImpl implements DataSourceService {

    @Autowired
    private SysResourceDatasourceQueryService sysResourceDatasourceQueryService;

    @Override
    public List<GDataset> listDataset(DataSourceInfo dataSourceInfo) {
        if (null == dataSourceInfo) {
            return Collections.emptyList();
        }
        Datasource datasource = DatasourceParser.parserDatasource(dataSourceInfo);
        return listDatasets(datasource);
    }

    @Override
    public List<GDataset> listDataset(String datasourceId) {
        SysResourceDatasource sysResourceDatasource = sysResourceDatasourceQueryService.getById(datasourceId);
        Boolean isSdx = sysResourceDatasource.getIsSdx();
        if (!isSdx) {
            return Collections.emptyList();
        }
        Datasource datasource = DatasourceParser.openDatasource(convert(sysResourceDatasource));
        List<GDataset> gDatasets = listDatasets(datasource);
        Workspace workspace = datasource.getWorkspace();
        workspace.close();
        workspace.dispose();
        return gDatasets;
    }


    /**
     * 获取数据源下的所有数据集
     * @param datasource 数据源
     * @return 数据集集合
     */
    private List<GDataset> listDatasets(Datasource datasource) {
        if (Objects.isNull(datasource)) {
            return Collections.emptyList();
        }
        Datasets datasets = datasource.getDatasets();
        int datasetCount = datasets.getCount();
        List<GDataset> data = new ArrayList<>(datasetCount);
        for (int i = 0; i < datasetCount; i++) {
            Dataset dataset = datasets.get(i);
            GDataset gDataset = new GDataset();
            gDataset.setDatasetName(dataset.getName());
            gDataset.setCaption(dataset.getTableName());
            gDataset.setDatasetType(dataset.getType().name());
            data.add(gDataset);
        }
        return data;
    }


    /**
     * 转换数据连接信息
     * @param sysResourceDatasource 数据源连接信息
     * @return
     */
    private DatasourceConnectionInfo convert(SysResourceDatasource sysResourceDatasource) {
        DatasourceConnectionInfo datasourceConnectionInfo = new DatasourceConnectionInfo();
        datasourceConnectionInfo.setServer(sysResourceDatasource.getAddr());
        datasourceConnectionInfo.setAlias(sysResourceDatasource.getDbName());
        datasourceConnectionInfo.setUser(sysResourceDatasource.getUserName());
        datasourceConnectionInfo.setPassword(sysResourceDatasource.getPassword());
        datasourceConnectionInfo.setDatabase(sysResourceDatasource.getDbName());
        String typeCode = sysResourceDatasource.getTypeCode().toLowerCase();
        switch (typeCode) {
            case "udb":
                datasourceConnectionInfo.setEngineType(EngineType.UDB);
                break;
            case "udbx":
                datasourceConnectionInfo.setEngineType(EngineType.UDBX);
                break;
            case "postgresql":
                datasourceConnectionInfo.setEngineType(EngineType.POSTGRESQL);
                break;
            case "mysql":
                datasourceConnectionInfo.setEngineType(EngineType.MYSQL);
                break;
            case "oracle":
                datasourceConnectionInfo.setEngineType(EngineType.ORACLEPLUS);
                break;
            default:
                throw  new GafException("不支持此类型！");
        }
        return datasourceConnectionInfo;
    }

}
