/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.analysis.utils;

import com.supermap.data.*;
import org.apache.commons.lang3.StringUtils;

import static com.supermap.data.EngineType.MYSQL;
import static com.supermap.data.EngineType.SQLPLUS;

/**
 * @author dqc
 * @date:2021/3/25
 * 工作空间解析器工具类
 */
public class WorkspaceParser {



    /**
     * 获取工作空间连接信息
     */
    public WorkspaceConnectionInfo getWorkspaceConnectionInfo(DatasourceConnectionInfo datasourceConnectionInfo, String wsName) {
        WorkspaceConnectionInfo workspaceConnectionInfo = new WorkspaceConnectionInfo();
        EngineType engineType = datasourceConnectionInfo.getEngineType();
        if (EngineType.POSTGRESQL.equals(engineType)) {
            workspaceConnectionInfo.setType(WorkspaceType.PGSQL);
            workspaceConnectionInfo.setDriver("pgSQL Server");
        } else if (EngineType.ORACLEPLUS.equals(engineType)) {
            workspaceConnectionInfo.setType(WorkspaceType.ORACLE);
            workspaceConnectionInfo.setDriver("ORACLE");
        } else if (MYSQL.equals(engineType)) {
            workspaceConnectionInfo.setType(WorkspaceType.MYSQL);
            workspaceConnectionInfo.setDriver("MYSQL");
        } else if (SQLPLUS.equals(engineType)) {
            workspaceConnectionInfo.setType(WorkspaceType.SQL);
            workspaceConnectionInfo.setDriver("SQL Server");
        } else {
            return null;
        }
        try {
            workspaceConnectionInfo.setDatabase(datasourceConnectionInfo.getDatabase());
            workspaceConnectionInfo.setName(StringUtils.isEmpty(wsName) ? datasourceConnectionInfo.getDatabase() : wsName);
            workspaceConnectionInfo.setUser(datasourceConnectionInfo.getUser());
            workspaceConnectionInfo.setServer(datasourceConnectionInfo.getServer());
            workspaceConnectionInfo.setPassword(datasourceConnectionInfo.getPassword());
        } catch (Exception e) {
            return null;
        }
        return workspaceConnectionInfo;
    }

    /**
     * 根据数据源连接信息获取工作空间
     */
    public static Workspace createWorkspace(DatasourceConnectionInfo connInfo, String wsName) {
        Workspace workspace = new Workspace();
        Datasource datasource = workspace.getDatasources().open(connInfo);
        return datasource.getWorkspace();
//        WorkspaceConnectionInfo workspaceConnectionInfo = getWorkspaceConnectionInfo(connInfo, wsName);
//        if (workspace.create(workspaceConnectionInfo)) {
//             workspace;
//        }
//        return null;
    }

    /**
     * 向workspace添加新的datasource
     *
     * @param workspace
     * @param connInfo
     * @return
     */
    public static Datasource addDataSourceToWorkSpace(Workspace workspace, DatasourceConnectionInfo connInfo) {
        Datasources datasources = workspace.getDatasources();
        Datasource ds = datasources.get(connInfo.getAlias());
        if (null != ds) {
            return ds;
        }
        datasources.open(connInfo);
        workspace.save();
        return datasources.get(connInfo.getAlias());
    }

    /**
     * workspace获取dataset
     *
     * @param connInfo
     * @param dataSetName
     * @return
     */
    public static Dataset getDataset(Workspace workspace, DatasourceConnectionInfo connInfo, String dataSetName) {
        Datasource datasource = workspace.getDatasources().get(connInfo.getAlias());
        if (null == datasource) {
            datasource = workspace.getDatasources().open(connInfo);
        }
        Datasets datasets = datasource.getDatasets();
        return datasets.get(dataSetName);
    }


}
