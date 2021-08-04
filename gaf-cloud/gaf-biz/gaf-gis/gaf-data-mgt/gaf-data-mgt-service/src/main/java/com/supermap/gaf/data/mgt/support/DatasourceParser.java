package com.supermap.gaf.data.mgt.support;

import com.supermap.data.Datasource;
import com.supermap.data.DatasourceConnectionInfo;
import com.supermap.data.Workspace;
import com.supermap.gaf.data.mgt.commontype.SysResourceDatasource;
import com.supermap.gaf.data.mgt.entity.DataSourceInfo;
import com.supermap.gaf.exception.GafException;
import com.supermap.gaf.utils.LogUtil;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
* @author:yd
* @Date 2021-3-01
 * 数据源解组件
**/
@Component
public final class DatasourceParser {

    private static final Logger logger = LogUtil.getLocLogger(DatasourceParser.class);

    @Autowired
    private ConvertHelper convertHelper;


    /**
     * 解析supermap数据源ds
     *
     * @param dataSourceInfo
     * @return
     */
    public Datasource parserDatasource(DataSourceInfo dataSourceInfo) {
        SysResourceDatasource datasource = new SysResourceDatasource();
        BeanUtils.copyProperties(dataSourceInfo,datasource);
        datasource.setAddr(dataSourceInfo.getAddr()+":"+dataSourceInfo.getPort());
        DatasourceConnectionInfo datasourceConnectionInfo = convertHelper.conver2DatasourceConnectionInfo(datasource);
        datasourceConnectionInfo.setAlias(datasourceConnectionInfo.getAlias() + "_"+ UUID.randomUUID());
        return openDatasource(datasourceConnectionInfo);
    }

    public static Datasource openDatasource(DatasourceConnectionInfo datasourceConnectionInfo) {
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
}
