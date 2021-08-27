/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.data.mgt.service;

import com.supermap.gaf.data.mgt.entity.MmTable;
import com.supermap.gaf.data.mgt.util.Page;
import com.supermap.gaf.data.mgt.vo.MmTableSelectVo;

import java.util.List;

/**
 * 逻辑服务类
 * @author wxl 
 * @date yyyy-mm-dd
 */
public interface MmTableService {
	
	/**
    * id查询逻辑
    * @return
    */
    MmTable getById(String tableId);
	
	/**
     * 分页条件查询
     * @param mmTableSelectVo 查询条件
     * @param pageNum 当前页数
     * @param pageSize 页面大小
     * @return 分页对象
     */
	Page<MmTable> listByPageCondition(MmTableSelectVo mmTableSelectVo, int pageNum, int pageSize);

    /**
     * 查询MmTable列表
     * @param mmTableSelectVo
     * @return
     */
	List<MmTable> selectList(MmTableSelectVo mmTableSelectVo);
	
    /**
     * 新增逻辑
     * @return 
     */
    MmTable insertMmTable(MmTable mmTable);
	
	/**
     * 批量插入
     *
	 */
    void batchInsert(List<MmTable> mmTables);

    /**
     * 删除逻辑
     * 
     */
    MmTable deleteMmTable(String tableId);

    /**
     * 批量删除
     * 
	 */
    void batchDelete(List<String> tableIds);

    /**
     * 更新逻辑
     * @return 
     */
    MmTable updateMmTable(MmTable mmTable);
    
}
