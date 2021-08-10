/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.webgis.service;

import com.supermap.gaf.webgis.entity.WebgisDataServiceField;
import com.supermap.gaf.webgis.vo.DataServiceFieldsVo;
import com.supermap.gaf.webgis.vo.WebgisDataServiceFieldSelectVo;
import com.supermap.gaf.webgis.util.Page;

import java.util.*;

/**
 * GIS数据服务字段服务类
 *
 * @author wangxiaolong
 * @date yyyy-mm-dd
 */
public interface WebgisDataServiceFieldService {

    /**
     * id查询GIS数据服务字段
     *
     * @return
     */
    WebgisDataServiceField getById(String gisServiceFieldId);

    /**
     * 分页条件查询
     *
     * @param webgisDataServiceFieldSelectVo 查询条件
     * @param pageNum                        当前页数
     * @param pageSize                       页面大小
     * @return 分页对象
     */
    Page<WebgisDataServiceField> listByPageCondition(WebgisDataServiceFieldSelectVo webgisDataServiceFieldSelectVo, int pageNum, int pageSize);


    /**
     * 新增GIS数据服务字段
     *
     * @return
     */
    WebgisDataServiceField insertWebgisDataServiceField(WebgisDataServiceField webgisDataServiceField);

    /**
     * 批量插入
     */
    void batchInsert(List<WebgisDataServiceField> webgisDataServiceFields);

    /**
     * 删除GIS数据服务字段
     */
    void deleteWebgisDataServiceField(String gisServiceFieldId);

    /**
     * 批量删除
     */
    void batchDelete(List<String> gisServiceFieldIds);

    /**
     * 更新GIS数据服务字段
     *
     * @return
     */
    WebgisDataServiceField updateWebgisDataServiceField(WebgisDataServiceField webgisDataServiceField);

    /**
     * 根据Webgis服务(只能是数据服务)id查询服务的数据集的所有字段列表及已选择的字段名
     *
     * @param webgisServiceId Webgis服务id
     * @return 若未查询到则返回null
     */
    DataServiceFieldsVo listFieldsAndSelectFieldNames(String webgisServiceId);

    /**
     * 配置webgis数据服务的字段
     *
     * @param fields          数据服务数据集的字段列表
     * @param webgisServiceId webgis数据服务id
     */
    void configFields(List<WebgisDataServiceField> fields, String webgisServiceId);
}
