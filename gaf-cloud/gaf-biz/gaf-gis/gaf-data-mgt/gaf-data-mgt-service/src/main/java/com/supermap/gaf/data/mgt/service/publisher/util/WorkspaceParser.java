/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.data.mgt.service.publisher.util;

import com.supermap.data.*;
import com.supermap.gaf.data.mgt.entity.DBServer;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import static com.supermap.data.EngineType.MYSQL;
import static com.supermap.data.EngineType.SQLPLUS;

/**
 * @author:yw
 * @Date 2021-3-12
 * @date:2021/3/25 工作空间解析器工具类
 */
@Component
public class WorkspaceParser {

    /**
     * Server转换为DatasourceConnectionInfo
     */
    public DatasourceConnectionInfo getDatasourceConnectionInfo(DBServer dbServer) {
        DatasourceConnectionInfo connectionInfo = null;
        switch (dbServer.getType()) {
            case ORACLE:
                connectionInfo = new DatasourceConnectionInfo();
                connectionInfo.setEngineType(EngineType.ORACLEPLUS);
                connectionInfo.setServer(String.format("%s:%s/%s", dbServer.getIp(), dbServer.getPort(), dbServer.getDatabaseName()));
                break;
            case POSTGRESQL:
                connectionInfo = new DatasourceConnectionInfo();
                connectionInfo.setEngineType(EngineType.POSTGRESQL);
                connectionInfo.setDatabase(dbServer.getDatabaseName());
                connectionInfo.setServer(String.format("%s:%s", dbServer.getIp(), dbServer.getPort()));
                break;
            default:
                break;
        }
        if (null == connectionInfo) {
            return null;
        }
        connectionInfo.setUser(dbServer.getUsername());
        connectionInfo.setPassword(dbServer.getPassword());
        connectionInfo.setAlias(dbServer.getName());
        return connectionInfo;
    }

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
    public Workspace createWorkspace(DatasourceConnectionInfo connInfo, String wsName) {
        WorkspaceConnectionInfo workspaceConnectionInfo = getWorkspaceConnectionInfo(connInfo, wsName);
        Workspace workspace = new Workspace();
        workspace.create(workspaceConnectionInfo);
        return workspace;
//        Datasource datasource = workspace.getDatasources().open(connInfo);
//        return datasource.getWorkspace();

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
    public Datasource addDataSourceToWorkSpace(Workspace workspace, DatasourceConnectionInfo connInfo) {
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
    public Dataset getDataset(Workspace workspace, DatasourceConnectionInfo connInfo, String dataSetName) {
        Datasource datasource = workspace.getDatasources().get(connInfo.getAlias());
        if (null == datasource) {
            datasource = workspace.getDatasources().open(connInfo);
        }
        Datasets datasets = datasource.getDatasets();
        return datasets.get(dataSetName);
    }


}
