/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.data.mgt.service;

import com.supermap.gaf.data.mgt.entity.MmFieldAssociate;
import com.supermap.gaf.data.mgt.util.Page;
import com.supermap.gaf.data.mgt.vo.MmFieldAssociateSelectVo;

import java.util.List;

/**
 * 字段关联服务类
 * @author wxl 
 * @date yyyy-mm-dd
 */
public interface MmFieldAssociateService {
	
	/**
    * id查询字段关联
    * @return
    */
    MmFieldAssociate getById(String fieldAssociateId);
	
	/**
     * 分页条件查询
     * @param mmFieldAssociateSelectVo 查询条件
     * @param pageNum 当前页数
     * @param pageSize 页面大小
     * @return 分页对象
     */
	Page<MmFieldAssociate> listByPageCondition(MmFieldAssociateSelectVo mmFieldAssociateSelectVo, int pageNum, int pageSize);

    /**
     * 条件查询
     * @param mmFieldAssociateSelectVo
     * @return
     */
	List<MmFieldAssociate> selectList(MmFieldAssociateSelectVo mmFieldAssociateSelectVo);

    /**
     * 新增字段关联
     * @return 
     */
    MmFieldAssociate insertMmFieldAssociate(MmFieldAssociate mmFieldAssociate);
	
	/**
     * 批量插入
     *
	 */
    void batchInsert(List<MmFieldAssociate> mmFieldAssociates);

    /**
     * 删除字段关联
     * 
     */
    void deleteMmFieldAssociate(String fieldAssociateId);

    /**
     * 批量删除
     * 
	 */
    void batchDelete(List<String> fieldAssociateIds);

    /**
     * 更新字段关联
     * @return 
     */
    MmFieldAssociate updateMmFieldAssociate(MmFieldAssociate mmFieldAssociate);

    /**
     * 更新两表之间的所有字段关联
     * @param mmFieldAssociates
     */
    void refreshMmFieldAssociateByTables(String sourceFieldTableId,String targetFieldTableId,List<MmFieldAssociate> mmFieldAssociates);
}
