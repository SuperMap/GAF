/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.webgis.service;

import com.supermap.gaf.webgis.domain.WebgisToolbarDo;
import com.supermap.gaf.webgis.entity.WebgisToolbar;
import com.supermap.gaf.webgis.util.Page;
import com.supermap.gaf.webgis.vo.WebgisToolbarSelectVo;
import com.supermap.gaf.webgis.vo.WebgisToolbarVo;

import java.util.List;

/**
 * 工具条服务类
 *
 * @author zhurongcheng
 * @date yyyy-mm-dd
 */
public interface WebgisToolbarService {

    /**
     * id查询工具条
     *
     * @return
     */
    WebgisToolbar getById(String toolbarId);

    /**
     * 分页条件查询
     *
     * @param pageNum  当前页数
     * @param pageSize 页面大小
     * @return 分页对象
     */
    Page<WebgisToolbar> listByPageCondition(WebgisToolbarSelectVo selectVo, int pageNum, int pageSize);


    /**
     * 新增工具条
     *
     * @return
     */
    WebgisToolbar insertWebgisToolbar(WebgisToolbarVo toolbarVo);

    /**
     * 批量插入
     */
    @Deprecated
    void batchInsert(List<WebgisToolbar> webgisToolbars);

    /**
     * 删除工具条
     */
    void deleteWebgisToolbar(String toolbarId);

    /**
     * 批量删除
     */
    void batchDelete(List<String> toolbarIds);

    /**
     * 更新工具条
     *
     * @return
     */
    WebgisToolbar updateWebgisToolbar(WebgisToolbarVo toolbarVo);

    WebgisToolbarDo getDoById(String toolbarId);

    void checkAvailable(WebgisToolbar toolbar, boolean isUpdate);
}
