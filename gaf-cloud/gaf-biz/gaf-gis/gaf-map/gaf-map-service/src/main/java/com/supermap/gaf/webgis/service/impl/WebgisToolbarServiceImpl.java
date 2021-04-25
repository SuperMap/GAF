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
import com.supermap.gaf.data.access.service.BatchSortAndCodeService;
import com.supermap.gaf.webgis.dao.WebgisToolbarButtonMapper;
import com.supermap.gaf.webgis.dao.WebgisToolbarMapper;
import com.supermap.gaf.webgis.domain.WebgisToolbarDo;
import com.supermap.gaf.webgis.entity.*;
import com.supermap.gaf.webgis.service.WebgisButtonService;
import com.supermap.gaf.webgis.service.WebgisConfigService;
import com.supermap.gaf.webgis.service.WebgisToolbarService;
import com.supermap.gaf.webgis.util.Page;
import com.supermap.gaf.webgis.vo.WebgisToolbarSelectVo;
import com.supermap.gaf.webgis.vo.WebgisToolbarVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * 工具条服务实现类
 * @author zhurongcheng 
 * @date yyyy-mm-dd
 */
@Service
public class WebgisToolbarServiceImpl implements WebgisToolbarService{

    @Autowired
    private WebgisConfigService webgisConfigService;
    @Autowired
    private WebgisToolbarMapper webgisToolbarMapper;

    @Autowired
    private WebgisToolbarButtonMapper webgisToolbarButtonMapper;
    @Autowired
    private WebgisButtonService webgisButtonService;

    @Autowired
    private BatchSortAndCodeService batchSortAndCodeService;

	@Override
    public WebgisToolbar getById(String toolbarId){
        if(toolbarId == null){
            throw new GafException("toolbarId不能为空");
        }
        WebgisToolbar webgisToolbar = webgisToolbarMapper.select(toolbarId);
        if(webgisToolbar==null){
            throw new GafException("指定工具条不存在");
        }
        return webgisToolbar;
    }
	
	@Override
    public Page<WebgisToolbar> listByPageCondition(WebgisToolbarSelectVo webgisToolbarSelectVo, int pageNum, int pageSize) {
        PageInfo<WebgisToolbar> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> {
            webgisToolbarMapper.selectList(webgisToolbarSelectVo);
        });
        return Page.create(pageInfo.getPageNum(),pageInfo.getPageSize(),(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getList());
    }

    @Override
    public void checkAvailable(WebgisToolbar toolbar, boolean isUpdate){
	    if(isUpdate && toolbar.getToolbarId() == null){
            throw new GafException("参数toolbarId不能为空");
        }
	    if(toolbar.getName()!=null){
            List<WebgisToolbar> toolbars = webgisToolbarMapper.selectList(WebgisToolbarSelectVo.builder()
                    .equalFieldName("type").equalFieldValue(toolbar.getType())
                    .name(toolbar.getName()).build());
            if(!CollectionUtils.isEmpty(toolbars) && (!isUpdate || toolbars.size()>1)){
                throw new GafException("指定名称已存在");
            }else if(!CollectionUtils.isEmpty(toolbars) && !toolbar.getToolbarId().equals(toolbars.get(0).getToolbarId())){
                throw new GafException("指定名称已存在");
            }
        }
    }

	@Override
    @Transactional
    public WebgisToolbar insertWebgisToolbar(WebgisToolbarVo toolbarVo){
        // 尝试解析配置，提前告知格式问题，特别是json串
        webgisConfigService.parseConfig(toolbarVo);

        //TODO: 主键非GeneratedKey，此处添加自定义主键生成策略
        WebgisToolbar webgisToolbar = new WebgisToolbar();
        BeanUtils.copyProperties(toolbarVo,webgisToolbar);
        String toolBarId = UUID.randomUUID().toString();
        webgisToolbar.setToolbarId(toolBarId);
        checkAvailable(webgisToolbar,false);
        ShiroUser shiroUser = SecurityUtilsExt.getUser();
        webgisToolbar.setCreatedBy(shiroUser.getAuthUser().getName());
        webgisToolbar.setUpdatedBy(shiroUser.getAuthUser().getName());

        List<WebgisToolbarButton> toolbarButtons = toolbarVo.getWebgisToolbarButtons();
        Set<String> buttonIds = new HashSet<>();
        for(WebgisToolbarButton toolbarButton:toolbarButtons){
            if(buttonIds.contains(toolbarButton.getButtonId())){
                throw new GafException("一个工具条不能添加重复的按钮");
            }
            buttonIds.add(toolbarButton.getButtonId());
            WebgisButton button = webgisButtonService.getById(toolbarButton.getButtonId());
            boolean typeValid = true;
            if(StringUtils.isEmpty(button.getType())){
                typeValid = StringUtils.isEmpty(webgisToolbar.getType());
            }else{
                typeValid = button.getType().equals(webgisToolbar.getType());
            }
            if(!typeValid){
                throw new GafException("工具条、按钮类型不匹配");
            }
            toolbarButton.setToolbarButtonId(UUID.randomUUID().toString());
            toolbarButton.setToolbarId(toolBarId);
            toolbarButton.setCreatedBy(shiroUser.getAuthUser().getName());
            toolbarButton.setUpdatedBy(shiroUser.getAuthUser().getName());
        }
        webgisToolbarMapper.insert(webgisToolbar);
        webgisToolbarButtonMapper.batchInsert(toolbarButtons);
        batchSortAndCodeService.revisionSortSnForInsertOrDelete(WebgisToolbarButton.class,Arrays.asList(webgisToolbar.getToolbarId()));
        return webgisToolbar;
    }
	
	@Override
    public void batchInsert(List<WebgisToolbar> webgisToolbars){
		if (webgisToolbars != null && webgisToolbars.size() > 0) {
	        webgisToolbars.forEach(webgisToolbar -> {
				webgisToolbar.setToolbarId(UUID.randomUUID().toString());
                checkAvailable(webgisToolbar,false);
				ShiroUser shiroUser = SecurityUtilsExt.getUser();
				webgisToolbar.setCreatedBy(shiroUser.getAuthUser().getName());
				webgisToolbar.setUpdatedBy(shiroUser.getAuthUser().getName());
            });
            webgisToolbarMapper.batchInsert(webgisToolbars);
        }
        
    }
	
	@Override
    @Transactional
    public void deleteWebgisToolbar(String toolbarId){
        webgisToolbarMapper.delete(toolbarId);
        webgisToolbarButtonMapper.deleteByToolBarId(toolbarId);
    }

	@Override
    @Transactional
    public void batchDelete(List<String> toolbarIds){
        if(!CollectionUtils.isEmpty(toolbarIds)) {
            webgisToolbarMapper.batchDelete(toolbarIds);
            webgisToolbarButtonMapper.batchDeleteByToolbarIds(toolbarIds);
        }

    }
	
	@Override
    @Transactional
    public WebgisToolbar updateWebgisToolbar(WebgisToolbarVo toolbarVo){
        WebgisToolbar webgisToolbar = new WebgisToolbar();
        BeanUtils.copyProperties(toolbarVo,webgisToolbar);
        checkAvailable(webgisToolbar,true);
		ShiroUser shiroUser = SecurityUtilsExt.getUser();
        webgisToolbar.setUpdatedBy(shiroUser.getAuthUser().getName());
        Set<String> buttonIds = new HashSet<>();
        for(WebgisToolbarButton toolbarButton:toolbarVo.getWebgisToolbarButtons()){
            if(buttonIds.contains(toolbarButton.getButtonId())){
                throw new GafException("一个工具条不能添加重复的按钮");
            }
            buttonIds.add(toolbarButton.getButtonId());
            WebgisButton button = webgisButtonService.getById(toolbarButton.getButtonId());
            boolean typeValid = true;
            if(StringUtils.isEmpty(button.getType())){
                typeValid = StringUtils.isEmpty(webgisToolbar.getType());
            }else{
                typeValid = button.getType().equals(webgisToolbar.getType());
            }
            if(!typeValid){
                throw new GafException("工具条、按钮类型不匹配");
            }
            toolbarButton.setToolbarButtonId(UUID.randomUUID().toString());
            toolbarButton.setToolbarId(webgisToolbar.getToolbarId());
            toolbarButton.setCreatedBy(webgisToolbar.getCreatedBy());
            toolbarButton.setUpdatedBy(shiroUser.getAuthUser().getName());
        }
        webgisToolbarMapper.update(webgisToolbar);
        webgisToolbarButtonMapper.deletePhysicalByToolBarId(webgisToolbar.getToolbarId());
        webgisToolbarButtonMapper.batchInsert(toolbarVo.getWebgisToolbarButtons());
        batchSortAndCodeService.revisionSortSnForInsertOrDelete(WebgisToolbarButton.class,Arrays.asList(webgisToolbar.getToolbarId()));
        return webgisToolbar;
    }

    @Override
    public WebgisToolbarDo getDoById(String toolbarId) {
	    getById(toolbarId);
        return webgisToolbarMapper.getDoById(toolbarId);
    }


}
