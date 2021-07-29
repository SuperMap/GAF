package com.supermap.gaf.storage.dao;
import com.supermap.gaf.storage.entity.Space;
import com.supermap.gaf.storage.entity.SpaceConfig;
import com.supermap.gaf.storage.entity.vo.SpaceConfigSelectVo;
import com.supermap.gaf.storage.entity.vo.SpaceSelectVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 数据访问类
 * @author zrc 
 * @date yyyy-mm-dd
 */
@Mapper
@Component
public interface SpaceMapper{
	/**
     * 根据主键 id查询
     * 
	 */
    Space select(@Param("id") String id);

	Space selectByIdAndTargetType(@Param("id") String id,@Param("targetType") String targetType);
	List<Space> selectByIdsAndTargetTypeAndTargetId(@Param("list") List<String> ids,@Param("targetType") String targetType,@Param("targetId") String targetId);

	List<SpaceConfig> selectSpaceConfig(SpaceConfigSelectVo spaceConfigSelectVo);

	/**
     * 多条件查询
     * @param space 查询条件
     * @return 若未查询到则返回空集合
     */
	List<Space> selectList(SpaceSelectVo spaceSelectVo);

    /**
	 * 新增
	 * 
	 */
    int insert(Space space);
	
	/**
     * 批量插入
     * 
	 */
    int batchInsert(List<Space> spaces);
	
	/**
     * 批量删除
     *
	 */
    int batchDelete(@Param("targetType")List<String> ids);
	int batchDeleteByIdsAndTargetType(@Param("list")List<String> ids,@Param("targetType") String targetType);
	/**
     * 刪除
     * 
	 */
    int delete(@Param("id") String id);
	int deleteByIdAndTargetType(@Param("id") String id,@Param("targetType") String targetType);
    /**
     * 更新
     *
	 */
    int update(Space space);

	int updateByIdAndTargetType(Space space);
}
