/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.sys.mgt.util;

import com.supermap.gaf.sys.mgt.commontype.SysResourceDatasource;
import com.supermap.gaf.sys.mgt.vo.DatasourceConnParam;
import com.supermap.gaf.sys.mgt.vo.JdbcConnectionInfo;
import org.springframework.util.StringUtils;

/**
 * @date:2021/3/25
 * @author C
 */
public class ConvertUtils {
    public static JdbcConnectionInfo convert2JdbcConnectionInfo(SysResourceDatasource sysResourceDatasource) {
        DatasourceConnParam datasourceConnParam = new DatasourceConnParam();
        datasourceConnParam.setDatasourceType(sysResourceDatasource.getTypeCode());
        datasourceConnParam.setDatabaseOrServiceName(sysResourceDatasource.getDbName());
        datasourceConnParam.setHost(sysResourceDatasource.getAddr());
        datasourceConnParam.setPassword(sysResourceDatasource.getPassword());
        datasourceConnParam.setUsername(sysResourceDatasource.getUserName());
        return convert2JdbcConnectionInfo(datasourceConnParam);
    }

    public static JdbcConnectionInfo convert2JdbcConnectionInfo(DatasourceConnParam datasourceConnParam){
        String url;
        switch (datasourceConnParam.getDatasourceType()){
            case "POSTGRESQL":{
                url = "jdbc:postgresql://"+datasourceConnParam.getHost()+"/"+datasourceConnParam.getDatabaseOrServiceName();
                if(!StringUtils.isEmpty(datasourceConnParam.getSchema())){
                    url+="?"+"searchpath="+datasourceConnParam.getSchema()+"&"+"currentSchema="+datasourceConnParam.getSchema();
                }
                break;
            }
            case "MYSQL":{
                url ="jdbc:mysql://"+datasourceConnParam.getHost()+"/"+datasourceConnParam.getDatabaseOrServiceName()+"?serverTimezone=UTC";

                break;
            }
            case "ORACLE":{
               url ="jdbc:oracle:thin:@//"+datasourceConnParam.getHost()+"/"+datasourceConnParam.getDatabaseOrServiceName();
                break;
            }
            case "SQL_SERVER" :{
                url = "jdbc:sqlserver://"+datasourceConnParam.getHost()+";databaseName="+datasourceConnParam.getDatabaseOrServiceName();
                break;
            }
            default:{
                throw new IllegalArgumentException("暂时不支持测试连接的数据源类型:"+datasourceConnParam.getDatasourceType());
            }
        }
        return JdbcConnectionInfo.builder()
                .url(url)
                .username(datasourceConnParam.getUsername())
                .password(datasourceConnParam.getPassword())
                .build();
    }
}
