/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.data.mgt.enums;

import com.supermap.gaf.data.mgt.commontype.SysResourceDatasource;
import com.supermap.gaf.data.mgt.support.ConnectionInfoConverter;
import com.supermap.gaf.data.mgt.support.JdbcConnectionInfo;


/**
 * 普通关系型数据库类型
 * 与字典中的非普通数据源保持一致
 * @author wxl
 * @since 2021/7/22
 */
public enum DatasourceTypeEnum implements ConnectionInfoConverter {

    POSTGRESQL("1") {

        @Override
        public JdbcConnectionInfo convert2JdbcConnectionInfo(SysResourceDatasource datasource) {
            String url = "jdbc:postgresql://" + datasource.getAddr() + "/" + datasource.getDbName();
            String driverClassName = "org.postgresql.Driver";
            return new JdbcConnectionInfo(driverClassName,url,datasource.getUserName(),datasource.getPassword());
        }
    },
    ORACLE("2") {

        @Override
        public JdbcConnectionInfo convert2JdbcConnectionInfo(SysResourceDatasource datasource) {
            String url = "jdbc:oracle:thin:@//" + datasource.getAddr() + ":" + datasource.getDbName();
            String driverClassName = "oracle.jdbc.driver.OracleDriver";
            return new JdbcConnectionInfo(driverClassName,url,datasource.getUserName(),datasource.getPassword());
        }
    },
    MYSQL("3") {

        @Override
        public JdbcConnectionInfo convert2JdbcConnectionInfo(SysResourceDatasource datasource) {
            String url = "jdbc:mysql://" + datasource.getAddr() + "/" + datasource.getDbName() + "?serverTimezone=UTC";
            String driverClassName = "com.mysql.cj.jdbc.Driver";
            return new JdbcConnectionInfo(driverClassName,url,datasource.getUserName(),datasource.getPassword());
        }
    },
    SQL_SERVER("4") {

        @Override
        public JdbcConnectionInfo convert2JdbcConnectionInfo(SysResourceDatasource datasource) {
            String url = "jdbc:sqlserver://" + datasource.getAddr() + ";databaseName=" + datasource.getDbName();
            String driverClassName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
            return new JdbcConnectionInfo(driverClassName,url,datasource.getUserName(),datasource.getPassword());
        }
    };

    private String code;

    DatasourceTypeEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
