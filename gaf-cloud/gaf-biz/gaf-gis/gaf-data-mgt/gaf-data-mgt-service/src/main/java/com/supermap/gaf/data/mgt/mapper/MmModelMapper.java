/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.data.mgt.mapper;

import com.supermap.gaf.data.mgt.entity.MmModel;
import com.supermap.gaf.data.mgt.vo.MmModelSelectVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 模型数据访问类
 * @author wxl 
 * @date yyyy-mm-dd
 */
@Mapper
@Component
public interface MmModelMapper{
	/**
     * 根据主键 modelId 查询
     *
	 */
    MmModel select(@Param("modelId")String modelId);
	
	/**
     * 多条件查询
     * @param mmModelSelectVo 查询条件
     * @return 若未查询到则返回空集合
     */
	List<MmModel> selectList(MmModelSelectVo mmModelSelectVo);

    /**
     * 新增
     *
	 */
    int insert(MmModel mmModel);
	
	/**
     * 批量插入
     * 
	 */
    int batchInsert(List<MmModel> mmModels);
	
	/**
     * 批量删除
     * 
	 */
    int batchDelete(List<String> modelIds);

	/**
     * 刪除
     *
	 */
    int delete(@Param("modelId")String modelId);

    /**
    * 更新
    * 
    **/
    int update(MmModel mmModel);
}
