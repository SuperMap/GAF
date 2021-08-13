package com.supermap.gaf.data.mgt.mapper;

import com.supermap.gaf.data.mgt.entity.MmTable;
import com.supermap.gaf.data.mgt.vo.MmTableSelectVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 逻辑数据访问类
 * @author wxl 
 * @date yyyy-mm-dd
 */
@Mapper
@Component
public interface MmTableMapper{
	/**
     * 根据主键 tableId 查询
     *
	 */
    MmTable select(@Param("tableId")String tableId);
	
	/**
     * 多条件查询
     * @param mmTableSelectVo 查询条件
     * @return 若未查询到则返回空集合
     */
	List<MmTable> selectList(MmTableSelectVo mmTableSelectVo);

    /**
     * 新增
     *
	 */
    int insert(MmTable mmTable);
	
	/**
     * 批量插入
     * 
	 */
    int batchInsert(List<MmTable> mmTables);
	
	/**
     * 批量删除
     * 
	 */
    int batchDelete(List<String> tableIds);

	/**
     * 刪除
     *
	 */
    int delete(@Param("tableId")String tableId);

    /**
    * 更新
    * 
    **/
    int update(MmTable mmTable);
}
