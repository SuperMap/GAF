/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.data.mgt.support;

import com.google.protobuf.ServiceException;
import com.supermap.data.Datasource;
import com.supermap.data.DatasourceConnectionInfo;
import com.supermap.data.Workspace;
import com.supermap.gaf.commontypes.MessageResult;
import com.supermap.gaf.data.mgt.commontype.SysResourceDatasource;
import com.supermap.gaf.data.mgt.enums.DatasourceTypeEnum;
import com.supermap.gaf.exception.GafException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * 使用iobject方式校验数据库连接
 *
 * @author wxl
 * @since 2021/7/22
 */
@Component
public class CheckHelper {
    /**
     * 数据源类型为HBASE
     */
    public static final String HBASE = "HBASE";

    @Autowired
    private ConvertHelper convertHelper;

    public MessageResult<Void> checkConnection(SysResourceDatasource sysResourceDatasource) {
        if (HBASE.equalsIgnoreCase(sysResourceDatasource.getTypeCode())) {
            return checkHbaseConnection(sysResourceDatasource);
        }
        if (sysResourceDatasource.getIsSdx()) {
            return checkSdxConnect(sysResourceDatasource);
        } else {
            return checkOrdinaryConnect(sysResourceDatasource);
        }
    }

    public MessageResult<Void> checkHbaseConnection(SysResourceDatasource sysResourceDatasource) {
        if (!HBASE.equalsIgnoreCase(sysResourceDatasource.getTypeCode())) {
            throw new IllegalArgumentException("不支持连接校验的数据库类型:"+ sysResourceDatasource.getTypeCode());
        }
        Configuration configuration = HBaseConfiguration.create();
        configuration.set("hbase.zookeeper.quorum", sysResourceDatasource.getAddr());
        try {
            HBaseAdmin.checkHBaseAvailable(configuration);
            return MessageResult.successe(Void.class).message("校验连接成功").build();
        } catch (ServiceException | IOException e) {
            return MessageResult.failed(Void.class).message("校验连接失败,原因:" + e.getMessage()).build();
        }
    }

    public MessageResult<Void> checkSdxConnect(SysResourceDatasource sysResourceDatasource) {
        DatasourceConnectionInfo datasourceConnectionInfo = convertHelper.conver2DatasourceConnectionInfo(sysResourceDatasource);
        Workspace workspace = null;
        Datasource datasource = null;
        try {
            workspace = new Workspace();
            datasource = workspace.getDatasources().open(datasourceConnectionInfo);
            return MessageResult.successe(Void.class).message("校验连接成功").build();
        } catch (Exception e) {
            return MessageResult.failed(Void.class).message("校验连接失败,原因:" + e.getMessage()).build();
        } finally {
            if (null != datasource) {
                datasource.close();
            }
            if (null != workspace) {
                workspace.close();
                workspace.dispose();
            }
        }
    }

    public MessageResult<Void> checkOrdinaryConnect(SysResourceDatasource sysResourceDatasource) {
        if (sysResourceDatasource.getIsSdx()) {
            throw new GafException("不支持校验空间数据源连接");
        }
        JdbcConnectionInfo jdbcConn = DatasourceTypeEnum.valueOf(sysResourceDatasource.getTypeCode()).convert2JdbcConnectionInfo(sysResourceDatasource);
        try {
            Class.forName(jdbcConn.getDriverClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e);
        }
        try(Connection connection = DriverManager.getConnection(jdbcConn.getUrl(), jdbcConn.getUsername(), jdbcConn.getPassword())) {
            return MessageResult.successe(Void.class).message("连接成功").build();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            return MessageResult.failed(Void.class).message("连接失败,原因:" + sqlException.getMessage()).build();
        }
    }
}
