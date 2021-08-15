/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.data.mgt.service;

import com.supermap.gaf.data.mgt.entity.MmPhysics;
import com.supermap.gaf.data.mgt.model.PhysicsResult;
import com.supermap.gaf.data.mgt.util.Page;
import com.supermap.gaf.data.mgt.vo.MmPhysicsSelectVo;

import java.util.List;

/**
 * 物理服务类
 * @author wxl 
 * @date yyyy-mm-dd
 */
public interface MmPhysicsService {
	
	/**
    * id查询物理
    * @return
    */
    MmPhysics getById(String physicsId);
	
	/**
     * 分页条件查询
     * @param mmPhysicsSelectVo 查询条件
     * @param pageNum 当前页数
     * @param pageSize 页面大小
     * @return 分页对象
     */
	Page<MmPhysics> listByPageCondition(MmPhysicsSelectVo mmPhysicsSelectVo, int pageNum, int pageSize);
	

	/**
     * 模型id-分页查询
     * @param modelId 模型id
     * @param pageNum 当前页数
     * @param pageSize 页面大小
     * @return 分页对象
     */
	Page<MmPhysics> listByModelId(String modelId, int pageNum, int pageSize);


    /**
     * 新增物理
     * @return 
     */
    MmPhysics insertMmPhysics(MmPhysics mmPhysics);
	
	/**
     * 批量插入
     *
	 */
    void batchInsert(List<MmPhysics> mmPhysicss);

    /**
     * 删除物理
     * 
     */
    void deleteMmPhysics(String physicsId);

    /**
     * 批量删除
     * 
	 */
    void batchDelete(List<String> physicsIds);

    /**
     * 更新物理
     * @return 
     */
    MmPhysics updateMmPhysics(MmPhysics mmPhysics);

    /**
     * 批量物理化
     * @param mmPhysicsList 物理化表集合
     */
    PhysicsResult physicalization(List<MmPhysics> mmPhysicsList);
}
