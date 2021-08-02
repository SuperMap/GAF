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

    public boolean isFileType(EngineType engineType) {
        return EngineType.UDB.equals(engineType) || EngineType.UDBX.equals(engineType);
    }

    private void checkSdx(SysResourceDatasource sysResourceDatasource) {
        if (!sysResourceDatasource.getIsSdx()) {
            throw new GafException("不支持非空间数据源连接转换为空间数据源连接");
        }
    }

    // 不支持非空间数据源
    public DatasourceConnectionInfo conver2DatasourceConnectionInfo(SysResourceDatasource sysResourceDatasource) {
        checkSdx(sysResourceDatasource);
        EngineType engineType = (EngineType) EngineType.parse(EngineType.class, sysResourceDatasource.getTypeCode());
        if (isFileType(engineType)) {
            String realPath = resolve(sysResourceDatasource.getAddr());
            return  new DatasourceConnectionInfo(realPath,sysResourceDatasource.getDsName() + UUID.randomUUID(),sysResourceDatasource.getPassword());
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


    /**
     * 解析文件型数据源在oss存储里的真实路径
     *
     * @param addr 文件型数据源的路径
     * @return oss存储里的真实路径
     */
    public String resolve(String addr) {
        try {
            return Paths.get(minioConfigHandlerI.getVolumeRootPath()).resolve(addr).toString();
        } catch (AuthenticationException e) {
            e.printStackTrace();
            throw new GafException(e);
        }
    }

}
