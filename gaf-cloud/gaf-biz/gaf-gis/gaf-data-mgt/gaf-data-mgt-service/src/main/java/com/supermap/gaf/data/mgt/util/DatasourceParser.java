package com.supermap.gaf.data.mgt.util;

import com.supermap.data.Datasource;
import com.supermap.data.DatasourceConnectionInfo;
import com.supermap.data.EngineType;
import com.supermap.data.Workspace;
import com.supermap.gaf.data.mgt.entity.DataSourceInfo;
import com.supermap.gaf.exception.GafException;
import com.supermap.gaf.utils.LogUtil;
import org.slf4j.Logger;

/**
* @author:yd
* @Date 2021-3-01
 * 数据源解组件
**/
public final class DatasourceParser {
    private DatasourceParser() {
    }

    private static final Logger logger = LogUtil.getLocLogger(DatasourceParser.class);

    /**
     * 解析supermap数据源ds
     *
     * @param dataSourceInfo
     * @return
     */
    public static Datasource parserDatasource(DataSourceInfo dataSourceInfo) {
        //TODO 增加多连接锁控制
        DatasourceConnectionInfo datasourceConnectionInfo = parserDatasourceConnectionInfo(dataSourceInfo);
        if (datasourceConnectionInfo == null) {
            logger.error("解析supermap数据源连接信息失败");
            throw new GafException("解析supermap数据源连接信息失败");
        }
        // 定义工作空间
        Workspace workspace = new Workspace();
        Datasource datasource = null;
        try {
            datasource = workspace.getDatasources().open(datasourceConnectionInfo);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new GafException(ex);
        }
        return datasource;
    }


    public static DatasourceConnectionInfo parserDatasourceConnectionInfo(DataSourceInfo dataSourceInfo) {
        if (null == dataSourceInfo) {
            return null;
        }
        DatasourceConnectionInfo datasourceConnection = new DatasourceConnectionInfo();
        datasourceConnection.setServer(dataSourceInfo.getAddr());
        datasourceConnection.setAlias(dataSourceInfo.getDsName());
        String typeCode = dataSourceInfo.getTypeCode().toLowerCase();
        switch (typeCode) {
            case "udb":
                datasourceConnection.setEngineType(EngineType.UDB);
                break;
            case "udbx":
                datasourceConnection.setEngineType(EngineType.UDBX);
                break;
            case "postgresql":
                datasourceConnection.setServer(dataSourceInfo.getAddr() + ":" + dataSourceInfo.getPort());
                datasourceConnection.setUser(dataSourceInfo.getUserName());
                datasourceConnection.setPassword(dataSourceInfo.getPassword());
                datasourceConnection.setEngineType(EngineType.POSTGRESQL);
                datasourceConnection.setDatabase(dataSourceInfo.getDbName());
                break;
            case "mysql":
                datasourceConnection.setEngineType(EngineType.MYSQL);
                datasourceConnection.setUser(dataSourceInfo.getUserName());
                datasourceConnection.setPassword(dataSourceInfo.getPassword());
                // 数据库服务信息 Server不需要 "/databaseName
                datasourceConnection.setServer(dataSourceInfo.getAddr() + ":" + dataSourceInfo.getPort());
                datasourceConnection.setDatabase(dataSourceInfo.getDbName());
                break;
            case "oracle":
                datasourceConnection.setServer(dataSourceInfo.getAddr() + ":" + dataSourceInfo.getPort() + "/" + dataSourceInfo.getDbName());
                datasourceConnection.setUser(dataSourceInfo.getUserName());
                datasourceConnection.setPassword(dataSourceInfo.getPassword());
                datasourceConnection.setEngineType(EngineType.ORACLEPLUS);
                break;
            default:
                logger.error("不支持此类型！");
                break;
        }
        return datasourceConnection;
    }
}
