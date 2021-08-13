package com.supermap.gaf.data.mgt.service;

import com.supermap.gaf.data.mgt.entity.MmPhysics;
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
    
}
