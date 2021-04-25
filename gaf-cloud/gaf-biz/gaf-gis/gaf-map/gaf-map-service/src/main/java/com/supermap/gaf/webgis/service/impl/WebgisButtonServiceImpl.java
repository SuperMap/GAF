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
import com.supermap.gaf.webgis.dao.WebgisButtonMapper;
import com.supermap.gaf.webgis.dao.WebgisToolbarButtonMapper;
import com.supermap.gaf.webgis.entity.WebgisButton;
import com.supermap.gaf.webgis.service.WebgisButtonService;
import com.supermap.gaf.webgis.util.Page;
import com.supermap.gaf.webgis.vo.WebgisButtonSelectVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.UUID;

/**
 * 地图按钮服务实现类
 * @author zhurongcheng 
 * @date 2020-12-05
 */
@Service
public class WebgisButtonServiceImpl implements WebgisButtonService{
    @Autowired
    private WebgisButtonMapper webgisButtonMapper;
    @Autowired
    private WebgisToolbarButtonMapper webgisToolbarButtonMapper;

	
	@Override
    public WebgisButton getById(String buttonId){
        if(buttonId == null){
            throw new GafException("buttonId不能为空");
        }
        WebgisButton button =  webgisButtonMapper.select(buttonId);
        if(button == null){
            throw new RuntimeException("指定button不存在："+buttonId);
        }
        return button;
    }
	
	@Override
    public Page<WebgisButton> listByPageCondition(WebgisButtonSelectVo webgisButtonSelectVo, int pageNum, int pageSize) {
        PageInfo<WebgisButton> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> {
            webgisButtonMapper.selectList(webgisButtonSelectVo);
        });
        return Page.create(pageInfo.getPageNum(),pageInfo.getPageSize(),(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getList());
    }

    /**
     * 检查传入字段是否可用
     * @param button
     * @param isUpdate
     */
    public void checkAvailable(WebgisButton button, boolean isUpdate){
        if(isUpdate && button.getName()==null){
            throw new GafException("参数buttonId不能为空");
        }
	    if(button.getName()!=null){
	        // 检查名称
            List<WebgisButton> buttons = webgisButtonMapper.selectList(WebgisButtonSelectVo.builder()
                    .equalFieldName("type").equalFieldValue(button.getType())
                    .name(button.getName()).build());
            if(!CollectionUtils.isEmpty(buttons) && (!isUpdate || buttons.size()>1)){
                throw new GafException("指定名称已存在");
            }else if(!CollectionUtils.isEmpty(buttons) && !button.getButtonId().equals(buttons.get(0).getButtonId())){
                throw new GafException("指定名称已存在");
            }
        }
    }
	@Override
    public WebgisButton insertWebgisButton(WebgisButton webgisButton){
        //TODO: 主键非GeneratedKey，此处添加自定义主键生成策略
		webgisButton.setButtonId(UUID.randomUUID().toString());
        checkAvailable(webgisButton,false);
		ShiroUser shiroUser = SecurityUtilsExt.getUser();
		webgisButton.setCreatedBy(shiroUser.getAuthUser().getName());
		webgisButton.setUpdatedBy(shiroUser.getAuthUser().getName());
        webgisButtonMapper.insert(webgisButton);
        return webgisButton;
    }
	
	@Override
    public void batchInsert(List<WebgisButton> webgisButtons){
		if (webgisButtons != null && webgisButtons.size() > 0) {
	        webgisButtons.forEach(webgisButton -> {
				webgisButton.setButtonId(UUID.randomUUID().toString());
                checkAvailable(webgisButton,false);
				ShiroUser shiroUser = SecurityUtilsExt.getUser();
				webgisButton.setCreatedBy(shiroUser.getAuthUser().getName());
				webgisButton.setUpdatedBy(shiroUser.getAuthUser().getName());
            });
            webgisButtonMapper.batchInsert(webgisButtons);
        }
        
    }
	
	@Override
    @Transactional
    public void deleteWebgisButton(String buttonId){
        webgisButtonMapper.delete(buttonId);
        webgisToolbarButtonMapper.deleteByButtonId(buttonId);
    }

	@Override
    @Transactional
    public void batchDelete(List<String> buttonIds){
        if(!CollectionUtils.isEmpty(buttonIds)) {
            webgisButtonMapper.batchDelete(buttonIds);
            webgisToolbarButtonMapper.batchDeleteByButtonIds(buttonIds);
        }
    }
	
	@Override
    public WebgisButton updateWebgisButton(WebgisButton webgisButton){
        checkAvailable(webgisButton,true);
		ShiroUser shiroUser = SecurityUtilsExt.getUser();
		webgisButton.setUpdatedBy(shiroUser.getAuthUser().getName());
		webgisButtonMapper.update(webgisButton);
        return webgisButton;
    }
    
}
