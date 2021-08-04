package com.supermap.gaf.data.mgt.service;

import com.supermap.gaf.data.mgt.commontype.SysResourceDatasource;
import com.supermap.gaf.data.mgt.conversion.result.DataExportResult;
import com.supermap.gaf.data.mgt.conversion.result.DataImportResult;
import com.supermap.gaf.data.mgt.entity.DataSourceInfo;
import com.supermap.gaf.data.mgt.entity.GDataset;

import java.util.List;

/**
 * 空间数据源功能
 */
public interface SpaceDatasourceService {

    /**
     * 检查数据库别名是否与已注册的数据源别名重复
     * @param dsName
     */
    boolean checkNameRepeat(String dsName);


    /**
     * 创建空的空间数据源
     * @param sysResourceDatasource 数据源
     * @return 数据源
     */
    SysResourceDatasource createEmptySpaceDatasource(SysResourceDatasource sysResourceDatasource);

    /**
     * 根据空间数据源模板创建空间数据源
     * @param templateId 空间数据源模板id
     * @param targetId 目标数据源id
     * @return 创建后的数据源
     */
    void createSpaceDatasourceByTemplate(String templateId,String targetId);

    /**
     * 根据数据导入设置信息 导入数据
     * @param importSettingJsonArray 数据导入设置信息
     * @return 导入结果
     */
    DataImportResult importData(String importSettingJsonArray);


    /**
     * 根据数据导出设置信息 导出数据
     *
     * @param exportSettingJsonArray 数据导出设置信息
     * @return 导出结果
     */
    DataExportResult exportData(String exportSettingJsonArray);


    /**
     * 获取数据源下所有数据集列表
     *
     * @param dataSourceInfo
     * @return
     */
    List<GDataset> listDataset(DataSourceInfo dataSourceInfo);

    /**
     * 根据数据源id获取数据源连接信息，然后获取数据源下的数据集
     *
     * @param datasourceId 数据源id
     * @return 数据集集合
     */
    List<GDataset> listDataset(String datasourceId);

}
