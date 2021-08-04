/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.data.access.service;

import com.supermap.data.DatasourceConnectionInfo;
import com.supermap.data.Recordset;


/**
 * @author:yj
 * @date:2021/3/25
 */
public interface RecordSetManager {

    /**
     * 查询
     *
     * @param datasourceConnectionInfo 数据源连接信息
     * @param datasetName              数据集名称
     * @param attributeFilter          查询条件，如果 attributeFilter 为空字符串，则查询表中所有的记录
     * @return
     */
    Recordset query(DatasourceConnectionInfo datasourceConnectionInfo, String datasetName, String attributeFilter);

    /**
     * 增加
     *
     * @param datasourceConnectionInfo 数据源连接信息
     * @param datasetName              数据集名称
     * @param recordset                记录
     * @return
     */
    boolean add(DatasourceConnectionInfo datasourceConnectionInfo, String datasetName, Recordset recordset);

    /**
     * 修改
     *
     * @param datasourceConnectionInfo 数据源连接信息
     * @param datasetName              数据集名称
     * @param fieldName                待更新字段名称，更新字段不可以为 sm 开头的字段（smUserID 除外）
     * @param value                    更新的字段值
     * @param attributeFilter          要更新记录的查询条件，如果 attributeFilter 为空字符串，则更新表中所有的记录
     * @return
     */
    boolean update(DatasourceConnectionInfo datasourceConnectionInfo, String datasetName, String fieldName, Object value, String attributeFilter);

    /**
     * 删除
     * 通过 ID 数组删除数据集中的记录
     *
     * @param datasourceConnectionInfo 数据源连接信息
     * @param datasetName              数据集名称
     * @param smIds                    记录主键
     * @return
     */
    boolean remove(DatasourceConnectionInfo datasourceConnectionInfo, String datasetName, int[] smIds);


}
