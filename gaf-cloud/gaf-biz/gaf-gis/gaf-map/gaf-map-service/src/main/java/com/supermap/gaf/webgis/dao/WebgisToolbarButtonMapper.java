/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.webgis.dao;

import com.supermap.gaf.webgis.domain.WebgisToolbarButtonDo;
import com.supermap.gaf.webgis.entity.WebgisToolbarButton;
import com.supermap.gaf.webgis.vo.WebgisToolbarButtonSelectVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 工具条按钮数据访问类
 *
 * @author zhurongcheng
 * @date 2020-12-05
 */
@Mapper
@Component
public interface WebgisToolbarButtonMapper {
    /**
     * 根据主键 toolbarButtonId 查询
     */
    WebgisToolbarButton select(@Param("toolbarButtonId") String toolbarButtonId);

    /**
     * 单字段条件模糊查询
     *
     * @param webgisToolbarButtonSelectVo 查询条件
     * @return 若未查询到则返回空集合
     */
    List<WebgisToolbarButton> selectList(WebgisToolbarButtonSelectVo webgisToolbarButtonSelectVo);

    /**
     * 新增
     */
    int insert(WebgisToolbarButton webgisToolbarButton);

    /**
     * 批量插入
     */
    int batchInsert(List<WebgisToolbarButton> webgisToolbarButtons);

    /**
     * 批量删除
     */
    int batchDelete(List<String> toolbarButtonIds);

    /**
     * 刪除
     */
    int delete(@Param("toolbarButtonId") String toolbarButtonId);

    /**
     * 更新
     **/
    int update(WebgisToolbarButton webgisToolbarButton);

    int deleteByToolBarId(@Param("toolbarId") String toolbarId);

    int deletePhysicalByToolBarId(@Param("toolbarId") String toolbarId);

    List<WebgisToolbarButtonDo> selectDosByToolbarId(@Param("toolbarId") String toolbarId);

    WebgisToolbarButtonDo getDoById(@Param("toolbarButtonId") String toolbarButtonId);

    int deleteByButtonId(@Param("buttonId") String buttonId);

    int batchDeleteByButtonIds(List<String> buttonIds);

    int batchDeleteByToolbarIds(List<String> toolbarIds);
}
