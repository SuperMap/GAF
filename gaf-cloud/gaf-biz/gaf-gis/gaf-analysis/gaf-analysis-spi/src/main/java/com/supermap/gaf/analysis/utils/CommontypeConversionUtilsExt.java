/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.analysis.utils;

import com.supermap.gaf.analysis.entity.SpatialAnalysisDataConnInfo;
import com.supermap.services.components.commontypes.DatasourceConnectionInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 类型转换
 *
 * @date:2021/3/25
 * @author wangyan
 */
public class CommontypeConversionUtilsExt {
    private static final Logger logger = LoggerFactory.getLogger(CommontypeConversionUtilsExt.class);



    public static com.supermap.data.DatasourceConnectionInfo getUgcDataSourceConnectionInfo(SpatialAnalysisDataConnInfo targetDataConnInfo) {
        if (null == targetDataConnInfo) {
            return null;
        }
        com.supermap.data.DatasourceConnectionInfo connectionInfo = new com.supermap.data.DatasourceConnectionInfo();
        //数据源别名不存在，则临时创建
        String alias = targetDataConnInfo.getAlias();
        if (StringUtils.isEmpty(alias)) {
            alias = "tmpDatasourceAlias";
        }
        // 根据不同引擎类型的数据源进行相应连接信息的读取
        String engineType = targetDataConnInfo.getEngineType();
        if (engineType.equalsIgnoreCase(com.supermap.data.EngineType.ORACLEPLUS.name())) {
            connectionInfo.setServer(targetDataConnInfo.getUrl() + "/" + targetDataConnInfo.getDatabase());
            connectionInfo.setUser(targetDataConnInfo.getUsername());
            connectionInfo.setPassword(targetDataConnInfo.getPassword());
            connectionInfo.setEngineType(com.supermap.data.EngineType.ORACLEPLUS);
            connectionInfo.setAlias(targetDataConnInfo.getDatabase());
        } else if (engineType.equalsIgnoreCase(com.supermap.data.EngineType.POSTGRESQL.name())) {
            connectionInfo.setEngineType(com.supermap.data.EngineType.POSTGRESQL);
            connectionInfo.setServer(targetDataConnInfo.getUrl());
            connectionInfo.setDatabase(targetDataConnInfo.getDatabase());
            connectionInfo.setUser(targetDataConnInfo.getUsername());
            connectionInfo.setPassword(targetDataConnInfo.getPassword());
            connectionInfo.setAlias(alias);
        } else if (engineType.equalsIgnoreCase(com.supermap.data.EngineType.MYSQL.name())) {
            connectionInfo.setEngineType(com.supermap.data.EngineType.MYSQL);
            connectionInfo.setUser(targetDataConnInfo.getUsername());
            connectionInfo.setPassword(targetDataConnInfo.getPassword());
            // 数据库服务信息 Server不需要 "/databaseName
            connectionInfo.setServer(targetDataConnInfo.getUrl());
            connectionInfo.setDatabase(targetDataConnInfo.getDatabase());
            connectionInfo.setAlias(alias);
        } else if (engineType.equalsIgnoreCase(com.supermap.data.EngineType.UDB.name())) {
            connectionInfo.setEngineType(com.supermap.data.EngineType.UDB);
            connectionInfo.setServer(targetDataConnInfo.getUrl());
            connectionInfo.setAlias(alias);
        } else if (engineType.equalsIgnoreCase(com.supermap.data.EngineType.UDBX.name())) {
            connectionInfo.setEngineType(com.supermap.data.EngineType.UDBX);
            connectionInfo.setServer(targetDataConnInfo.getUrl());
            connectionInfo.setAlias(alias);
        } else {
            return null;
        }
        return connectionInfo;
    }


    public static com.supermap.data.DatasourceConnectionInfo getUgcDatasourceConnectionInfo(DatasourceConnectionInfo datasourceConnectionInfo) {
        com.supermap.data.DatasourceConnectionInfo connectionInfo = new com.supermap.data.DatasourceConnectionInfo();
        connectionInfo.setAlias(datasourceConnectionInfo.alias);
        connectionInfo.setEngineType((com.supermap.data.EngineType) com.supermap.data.Enum.parse(com.supermap.data.EngineType.class,
                datasourceConnectionInfo.engineType.toString()));
        connectionInfo.setServer(datasourceConnectionInfo.server);
        connectionInfo.setDatabase(datasourceConnectionInfo.dataBase);
        connectionInfo.setUser(datasourceConnectionInfo.user);
        connectionInfo.setPassword(datasourceConnectionInfo.password);
        if (StringUtils.isNotEmpty(datasourceConnectionInfo.driver)) {
            connectionInfo.setDriver(datasourceConnectionInfo.driver);
        }
        connectionInfo.setReadOnly(datasourceConnectionInfo.readOnly);
        /**
         * 默认为true,若将该项设置为false,则会影响数据源相关操作
         * 及时删除代码：【connectionInfo.setAutoConnect(datasourceConnectionInfo.connect);】
         */
        connectionInfo.setOpenLinkTable(datasourceConnectionInfo.openLinkTable);
        return connectionInfo;
    }

}
