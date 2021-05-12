/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.data.mgt.dao;

import com.supermap.gaf.data.mgt.entity.DataDataset;
import com.supermap.gaf.data.mgt.vo.DataDatasetSelectVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 数据集数据访问类
 * @author yw 
 * @date yyyy-mm-dd
 */
@Mapper
@Component
public interface DataDatasetMapper {
	/**
     * 根据主键 datasetId 查询
     *
	 */
    DataDataset select(@Param("datasetId") String datasetId);

	/**
     * 多条件查询
     * @param dataDatasetSelectVo 查询条件
     * @return 若未查询到则返回空集合
     */
	List<DataDataset> selectList(DataDatasetSelectVo dataDatasetSelectVo);

    /**
     * 新增
     *
	 */
    int insert(DataDataset dataDataset);

	/**
     * 批量插入
     *
	 */
    int batchInsert(List<DataDataset> dataDatasets);

	/**
     * 批量删除
     *
	 */
    int batchDelete(List<String> datasetIds);

	/**
     * 刪除
     *
	 */
    int delete(@Param("datasetId") String datasetId);

    /**
    * 更新
    * 
    **/
    int update(DataDataset dataDataset);
}
