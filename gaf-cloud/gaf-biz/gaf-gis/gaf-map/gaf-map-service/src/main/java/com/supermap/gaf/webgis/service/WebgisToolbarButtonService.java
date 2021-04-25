/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.webgis.service;
import com.supermap.gaf.webgis.domain.WebgisToolbarButtonDo;
import com.supermap.gaf.webgis.entity.WebgisToolbarButton;
import com.supermap.gaf.webgis.vo.WebgisToolbarButtonSelectVo;
import com.supermap.gaf.webgis.util.Page;
import java.util.*;

/**
 * 工具条按钮服务类
 * @author zhurongcheng 
 * @date 2020-12-05
 */
public interface WebgisToolbarButtonService {
	
	/**
    * id查询工具条按钮
    * @return
    */
    WebgisToolbarButton getById(String toolbarButtonId);
	
	/**
     * 分页条件查询
     * @param webgisToolbarButtonSelectVo 查询条件
     * @param pageNum 当前页数
     * @param pageSize 页面大小
     * @return 分页对象
     */
	Page<WebgisToolbarButton> listByPageCondition(WebgisToolbarButtonSelectVo webgisToolbarButtonSelectVo, int pageNum, int pageSize);
	
	
    /**
     * 新增工具条按钮
     * @return 
     */
    WebgisToolbarButton insertWebgisToolbarButton(WebgisToolbarButton webgisToolbarButton);
	
	/**
     * 批量插入
     *
	 */
    void batchInsert(List<WebgisToolbarButton> webgisToolbarButtons);

    /**
     * 删除工具条按钮
     * 
     */
    void deleteWebgisToolbarButton(String toolbarButtonId);

    /**
     * 批量删除
     * 
	 */
    void batchDelete(List<String> toolbarButtonIds);


    int deleteByToolBarId(String toolbarId);

    /**
     * 更新工具条按钮
     * @return 
     */
    WebgisToolbarButton updateWebgisToolbarButton(WebgisToolbarButton webgisToolbarButton);

    List<WebgisToolbarButtonDo> selectDosByToolbarId(String toolbarId);

    WebgisToolbarButtonDo getDoById(String toolbarButtonId);
}
