/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.data.mgt.support;

import com.supermap.data.DatasourceConnectionInfo;
import com.supermap.data.EngineType;
import com.supermap.gaf.data.mgt.commontype.SysResourceDatasource;
import com.supermap.gaf.exception.GafException;
import com.supermap.gaf.storage.service.MinioConfigHandlerI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.security.sasl.AuthenticationException;
import java.nio.file.Paths;
import java.util.UUID;

;

/**
 * 数据源连接转换
 * @author wxl
 * @since 2021/7/22
 */
@Component
public class ConvertHelper {

    @Autowired
    private MinioConfigHandlerI minioConfigHandlerI;

    // 不支持非空间数据源
    public DatasourceConnectionInfo conver2DatasourceConnectionInfo(SysResourceDatasource sysResourceDatasource) {
        if (!sysResourceDatasource.getIsSdx()) {
            throw new GafException("不支持非空间数据源连接转换为空间数据源连接");
        }
        final EngineType engineType = (EngineType) EngineType.parse(EngineType.class, sysResourceDatasource.getTypeCode());
        if (EngineType.UDB.equals(engineType) || EngineType.UDBX.equals(engineType)) {
            try {
                String filePath = Paths.get(minioConfigHandlerI.getVolumeRootPath()).resolve(sysResourceDatasource.getAddr()).toString();
                return  new DatasourceConnectionInfo(filePath,sysResourceDatasource.getDsName() + UUID.randomUUID(),sysResourceDatasource.getPassword());
            } catch (AuthenticationException e) {
                e.printStackTrace();
                throw new GafException(e);
            }
        }
        String tns = sysResourceDatasource.getAddr();
        String database = sysResourceDatasource.getDbName();
        String alias = sysResourceDatasource.getDsName() + UUID.randomUUID();
        String user = sysResourceDatasource.getUserName();
        String password = sysResourceDatasource.getPassword();
        DatasourceConnectionInfo datasourceConnectionInfo = new DatasourceConnectionInfo(tns, database, alias, user, password);
        datasourceConnectionInfo.setEngineType(engineType);
        if (EngineType.SQLPLUS.equals(engineType)) {
            datasourceConnectionInfo.setDriver("SQL SERVER");
        }
        return datasourceConnectionInfo;
    }

}
