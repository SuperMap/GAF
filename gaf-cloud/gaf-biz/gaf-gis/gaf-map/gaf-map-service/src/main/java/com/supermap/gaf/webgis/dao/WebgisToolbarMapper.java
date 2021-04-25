/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.webgis.dao;

import com.supermap.gaf.webgis.domain.WebgisToolbarDo;
import com.supermap.gaf.webgis.entity.WebgisToolbar;
import com.supermap.gaf.webgis.vo.WebgisToolbarSelectVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 工具条数据访问类
 * @author zhurongcheng 
 * @date 2020-12-05
 */
@Mapper
@Component
public interface WebgisToolbarMapper{
	/**
     * 根据主键 toolbarId 查询
     *
	 */
    WebgisToolbar select(@Param("toolbarId")String toolbarId);
	
	/**
     * 单字段条件模糊查询
     * @param webgisToolbarSelectVo 查询条件
     * @return 若未查询到则返回空集合
     */
//	List<WebgisToolbar> selectByOneField(WebgisToolbarSelectVo webgisToolbarSelectVo);

    /**
     * 新增
     *
	 */
    int insert(WebgisToolbar webgisToolbar);
	
	/**
     * 批量插入
     * 
	 */
    int batchInsert(List<WebgisToolbar> webgisToolbars);
	
	/**
     * 批量删除
     * 
	 */
    int batchDelete(List<String> toolbarIds);

	/**
     * 刪除
     *
	 */
    int delete(@Param("toolbarId")String toolbarId);

    /**
    * 更新
    * 
    **/
    int update(WebgisToolbar webgisToolbar);

	WebgisToolbarDo getDoById(@Param("toolbarId")String toolbarId);


	List<WebgisToolbar> selectList(WebgisToolbarSelectVo webgisToolbarSelectVo);
}
