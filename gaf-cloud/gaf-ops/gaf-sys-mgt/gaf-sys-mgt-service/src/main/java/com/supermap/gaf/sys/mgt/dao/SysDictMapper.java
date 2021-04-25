/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.sys.mgt.dao;

import com.supermap.gaf.sys.mgt.commontype.SysDict;
import com.supermap.gaf.sys.mgt.vo.SysDictSelectVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 字典mapper
 * @author wangxiaolong 
 * @date 2021-01-08
 */
@Mapper
@Component
public interface SysDictMapper {
	/**
	 * 根据id查询字典
	 * @param dataDictId 字典id
	 * @return 字典 未查询到则返回null
	 */
    SysDict select(@Param("dataDictId") String dataDictId);

	/**
	 * 根据id集合查询
	 * @param ids id集合
	 * @return 字典集合 若未查询到则返回空集合
	 */
	List<SysDict> selectByIds(List<String> ids);

	/**
	 * 根据父id集合查询
	 * @param parentIds  父id集合
	 * @return 字典集合 若未查询到则返回空集合
	 */
	List<SysDict> selectByParentIds(List<String> parentIds);


	/**
     * 等值条件查询和模糊查询
     * @param sysDictSelectVo 查询条件
     * @return 若未查询到则返回空集合
     */
	List<SysDict> selectList(SysDictSelectVo sysDictSelectVo);

	/**
	 * 新增字典
	 * @param sysDict 字典
	 * @return 新增的数量
	 */
    int insert(SysDict sysDict);

	/**
	 * 批量新增字典
	 * @param sysDicts  字典集合
	 * @return 新增的数量
	 */
    int batchInsert(List<SysDict> sysDicts);
	/**
	 * 根据字典id集合删除字典
	 * @param dataDictIds 字典id集合
	 * @return 影响的行数即删除的数量
	 */
    int batchDelete(List<String> dataDictIds);

	/**
	 * 根据字典id删除字典
	 * @param dataDictId 字典id
	 * @return 影响的行数即删除的数量
	 */
    int delete(@Param("dataDictId") String dataDictId);

	/**
	 * 更新字典
	 * @param sysDict   字典
	 * @return 影响的行数即更新的数量
	 */
    int update(SysDict sysDict);


	/**
	 * 根据字典类别编码获取所有字典数据，不包括字典类别
	 * @param dictTypeCode 字典类别编码
	 * @param dictTypeId 字典类别id
	 * @return 字典集合 若未查询到则返回空集合
	 */
	List<SysDict> selectChildren(@Param("dictTypeCode") String dictTypeCode, @Param("dictTypeId") String dictTypeId);
}
