package com.supermap.gaf.data.access.service;

import com.supermap.data.Datasource;
import com.supermap.data.DatasourceConnectionInfo;
import com.supermap.data.Workspace;

public final class DataSourceManager {

    private DataSourceManager() {

    }

    /**
     * 获取数据源
     *
     * @param datasourceConnectionInfo
     * @return
     */
    public static Datasource getDatasource(DatasourceConnectionInfo datasourceConnectionInfo) {
        final Workspace workspace = new Workspace();
        return workspace.getDatasources().open(datasourceConnectionInfo);
    }

}
