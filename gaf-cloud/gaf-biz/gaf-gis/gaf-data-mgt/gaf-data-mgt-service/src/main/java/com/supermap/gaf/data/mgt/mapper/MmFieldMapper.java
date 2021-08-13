package com.supermap.gaf.data.mgt.mapper;

import com.supermap.gaf.data.mgt.entity.MmField;
import com.supermap.gaf.data.mgt.vo.MmFieldSelectVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 字段数据访问类
 * @author wxl 
 * @date yyyy-mm-dd
 */
@Mapper
@Component
public interface MmFieldMapper{
	/**
     * 根据主键 fieldId 查询
     *
	 */
    MmField select(@Param("fieldId")String fieldId);
	
	/**
     * 多条件查询
     * @param mmFieldSelectVo 查询条件
     * @return 若未查询到则返回空集合
     */
	List<MmField> selectList(MmFieldSelectVo mmFieldSelectVo);

    /**
     * 新增
     *
	 */
    int insert(MmField mmField);
	
	/**
     * 批量插入
     * 
	 */
    int batchInsert(List<MmField> mmFields);
	
	/**
     * 批量删除
     * 
	 */
    int batchDelete(List<String> fieldIds);

	/**
     * 刪除
     *
	 */
    int delete(@Param("fieldId")String fieldId);

    /**
    * 更新
    * 
    **/
    int update(MmField mmField);
}
