package com.supermap.gaf.data.mgt.mapper;

import com.supermap.gaf.data.mgt.entity.MmFieldAssociate;
import com.supermap.gaf.data.mgt.vo.MmFieldAssociateSelectVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 字段关联数据访问类
 * @author wxl 
 * @date yyyy-mm-dd
 */
@Mapper
@Component
public interface MmFieldAssociateMapper{
	/**
     * 根据主键 fieldAssociateId 查询
     *
	 */
    MmFieldAssociate select(@Param("fieldAssociateId")String fieldAssociateId);
	
	/**
     * 多条件查询
     * @param mmFieldAssociateSelectVo 查询条件
     * @return 若未查询到则返回空集合
     */
	List<MmFieldAssociate> selectList(MmFieldAssociateSelectVo mmFieldAssociateSelectVo);

    /**
     * 新增
     *
	 */
    int insert(MmFieldAssociate mmFieldAssociate);
	
	/**
     * 批量插入
     * 
	 */
    int batchInsert(List<MmFieldAssociate> mmFieldAssociates);
	
	/**
     * 批量删除
     * 
	 */
    int batchDelete(List<String> fieldAssociateIds);

	/**
     * 刪除
     *
	 */
    int delete(@Param("fieldAssociateId")String fieldAssociateId);

    /**
    * 更新
    * 
    **/
    int update(MmFieldAssociate mmFieldAssociate);
}
