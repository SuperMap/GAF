/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.webgis.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.supermap.gaf.exception.GafException;
import com.supermap.gaf.shiro.SecurityUtilsExt;
import com.supermap.gaf.shiro.commontypes.ShiroUser;
import com.supermap.gaf.webgis.dao.WebgisToolbarButtonMapper;
import com.supermap.gaf.webgis.domain.WebgisToolbarButtonDo;
import com.supermap.gaf.webgis.entity.WebgisToolbarButton;
import com.supermap.gaf.webgis.service.WebgisToolbarButtonService;
import com.supermap.gaf.webgis.util.Page;
import com.supermap.gaf.webgis.vo.WebgisToolbarButtonSelectVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.UUID;

/**
 * 工具条按钮服务实现类
 * @author zhurongcheng 
 * @date 2020-12-05
 */
@Service
public class WebgisToolbarButtonServiceImpl implements WebgisToolbarButtonService{
    @Autowired
    private WebgisToolbarButtonMapper webgisToolbarButtonMapper;
	
	@Override
    public WebgisToolbarButton getById(String toolbarButtonId){
        if(toolbarButtonId == null){
            throw new GafException("toolbarButtonId不能为空");
        }
        return  webgisToolbarButtonMapper.select(toolbarButtonId);
    }
	
	@Override
    public Page<WebgisToolbarButton> listByPageCondition(WebgisToolbarButtonSelectVo webgisToolbarButtonSelectVo, int pageNum, int pageSize) {
        PageInfo<WebgisToolbarButton> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> {
            webgisToolbarButtonMapper.selectList(webgisToolbarButtonSelectVo);
        });
        return Page.create(pageInfo.getPageNum(),pageInfo.getPageSize(),(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getList());
    }

	@Override
    public WebgisToolbarButton insertWebgisToolbarButton(WebgisToolbarButton webgisToolbarButton){
        //TODO: 主键非GeneratedKey，此处添加自定义主键生成策略
		webgisToolbarButton.setToolbarButtonId(UUID.randomUUID().toString());
		
		ShiroUser shiroUser = SecurityUtilsExt.getUser();
		webgisToolbarButton.setCreatedBy(shiroUser.getAuthUser().getName());
		webgisToolbarButton.setUpdatedBy(shiroUser.getAuthUser().getName());
        webgisToolbarButtonMapper.insert(webgisToolbarButton);
        return webgisToolbarButton;
    }
	
	@Override
    public void batchInsert(List<WebgisToolbarButton> webgisToolbarButtons){
		if (webgisToolbarButtons != null && webgisToolbarButtons.size() > 0) {
	        webgisToolbarButtons.forEach(webgisToolbarButton -> {
				webgisToolbarButton.setToolbarButtonId(UUID.randomUUID().toString());
				ShiroUser shiroUser = SecurityUtilsExt.getUser();
				webgisToolbarButton.setCreatedBy(shiroUser.getAuthUser().getName());
				webgisToolbarButton.setUpdatedBy(shiroUser.getAuthUser().getName());
            });
            webgisToolbarButtonMapper.batchInsert(webgisToolbarButtons);
        }
        
    }
	
	@Override
    public void deleteWebgisToolbarButton(String toolbarButtonId){
        webgisToolbarButtonMapper.delete(toolbarButtonId);
    }

	@Override
    public void batchDelete(List<String> toolbarButtonIds){
        if(!CollectionUtils.isEmpty(toolbarButtonIds)) {
            webgisToolbarButtonMapper.batchDelete(toolbarButtonIds);
        }
    }

    @Override
    public int deleteByToolBarId(String toolbarId) {
        return webgisToolbarButtonMapper.deleteByToolBarId(toolbarId);
    }

    @Override
    public WebgisToolbarButton updateWebgisToolbarButton(WebgisToolbarButton webgisToolbarButton){
		ShiroUser shiroUser = SecurityUtilsExt.getUser();
		webgisToolbarButton.setUpdatedBy(shiroUser.getAuthUser().getName());
		webgisToolbarButtonMapper.update(webgisToolbarButton);
        return webgisToolbarButton;
    }

    @Override
    public List<WebgisToolbarButtonDo> selectDosByToolbarId(String toolbarId) {
        return webgisToolbarButtonMapper.selectDosByToolbarId(toolbarId);
    }

    @Override
    public WebgisToolbarButtonDo getDoById(String toolbarButtonId) {
        return webgisToolbarButtonMapper.getDoById(toolbarButtonId);
    }


}
