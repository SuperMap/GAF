/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.data.mgt.service;

import com.supermap.gaf.data.mgt.entity.DataDataset;
import com.supermap.gaf.data.mgt.util.Page;
import com.supermap.gaf.data.mgt.vo.DataDatasetSelectVo;

import java.util.List;

/**
 * 数据集服务类
 * @author yw 
 * @date yyyy-mm-dd
 */
public interface DataDatasetService {
	
	/**
    * id查询数据集
    * @return
    */
    DataDataset getById(String datasetId);
	
	/**
     * 分页条件查询
     * @param dataDatasetSelectVo 查询条件
     * @param pageNum 当前页数
     * @param pageSize 页面大小
     * @return 分页对象
     */
	Page<DataDataset> listByPageCondition(DataDatasetSelectVo dataDatasetSelectVo, int pageNum, int pageSize);
	
	
    /**
     * 新增数据集
     * @return 
     */
    DataDataset insertDataDataset(DataDataset dataDataset);
	
	/**
     * 批量插入
     *
	 */
    void batchInsert(List<DataDataset> dataDatasets);

    /**
     * 删除数据集
     * 
     */
    void deleteDataDataset(String datasetId);

    /**
     * 批量删除
     * 
	 */
    void batchDelete(List<String> datasetIds);

    /**
     * 更新数据集
     * @return 
     */
    DataDataset updateDataDataset(DataDataset dataDataset);
    
}
