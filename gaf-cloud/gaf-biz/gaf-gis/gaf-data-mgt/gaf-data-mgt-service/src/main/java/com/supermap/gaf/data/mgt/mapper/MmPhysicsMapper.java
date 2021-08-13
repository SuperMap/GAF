package com.supermap.gaf.data.mgt.mapper;

import com.supermap.gaf.data.mgt.entity.MmPhysics;
import com.supermap.gaf.data.mgt.vo.MmPhysicsSelectVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 物理数据访问类
 * @author wxl 
 * @date yyyy-mm-dd
 */
@Mapper
@Component
public interface MmPhysicsMapper{
	/**
     * 根据主键 physicsId 查询
     *
	 */
    MmPhysics select(@Param("physicsId")String physicsId);
	
	/**
     * 多条件查询
     * @param mmPhysicsSelectVo 查询条件
     * @return 若未查询到则返回空集合
     */
	List<MmPhysics> selectList(MmPhysicsSelectVo mmPhysicsSelectVo);

    /**
     * 新增
     *
	 */
    int insert(MmPhysics mmPhysics);
	
	/**
     * 批量插入
     * 
	 */
    int batchInsert(List<MmPhysics> mmPhysicss);
	
	/**
     * 批量删除
     * 
	 */
    int batchDelete(List<String> physicsIds);

	/**
     * 刪除
     *
	 */
    int delete(@Param("physicsId")String physicsId);

    /**
    * 更新
    * 
    **/
    int update(MmPhysics mmPhysics);
}
