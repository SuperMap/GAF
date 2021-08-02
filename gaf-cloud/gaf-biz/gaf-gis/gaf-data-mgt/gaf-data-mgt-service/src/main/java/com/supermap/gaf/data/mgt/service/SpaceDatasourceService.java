package com.supermap.gaf.data.mgt.service;

import com.supermap.data.conversion.ImportResult;
import com.supermap.gaf.data.mgt.commontype.SysResourceDatasource;

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
     * @param importSettingJsonStr 数据导入设置信息
     * @return 是否成功 true 成功 false 失败
     */
    ImportResult importData(String importSettingJsonStr);
}
