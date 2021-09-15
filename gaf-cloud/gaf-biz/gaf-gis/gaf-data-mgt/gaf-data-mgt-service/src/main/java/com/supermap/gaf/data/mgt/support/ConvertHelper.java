/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.data.mgt.support;

import com.supermap.data.DatasourceConnectionInfo;
import com.supermap.data.EngineType;
import com.supermap.gaf.common.storage.client.StorageClient;
import com.supermap.gaf.data.mgt.commontype.SysResourceDatasource;
import com.supermap.gaf.exception.GafException;
import com.supermap.gaf.shiro.SecurityUtilsExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.nio.file.Files;
import java.nio.file.Path;
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
    @Qualifier("DatamgtStorageClient")
    private StorageClient storageClient;

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
        String rootPath = null;
        if (isFileType(engineType)){
            rootPath = resolve("");
        }
        return conver2DatasourceConnectionInfo(sysResourceDatasource,rootPath);
    }

    // 不支持非空间数据源
    public DatasourceConnectionInfo conver2DatasourceConnectionInfo(SysResourceDatasource sysResourceDatasource,String rootPath) {
        checkSdx(sysResourceDatasource);
        EngineType engineType = (EngineType) EngineType.parse(EngineType.class, sysResourceDatasource.getTypeCode());
        if (isFileType(engineType)) {
            if(rootPath==null){
                throw new RuntimeException("rootPath 不能为空");
            }
            Path realPath = Paths.get(rootPath).resolve(sysResourceDatasource.getAddr());
            if(!Files.exists(realPath)){
                throw new RuntimeException("文件不存在");
            }
            return  new DatasourceConnectionInfo(realPath.toString(),sysResourceDatasource.getDsName() + UUID.randomUUID(),sysResourceDatasource.getPassword());
        }
        String tns = sysResourceDatasource.getAddr();
        String database = sysResourceDatasource.getDbName();
        String alias = sysResourceDatasource.getDsName();
        if (StringUtils.isEmpty(alias)) {
            alias = tns+"_"+database;
        }
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
        return storageClient.getVolumePath(addr, SecurityUtilsExt.getUser().getAuthUser().getTenantId(), false).getPath();
    }

}
