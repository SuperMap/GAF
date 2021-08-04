/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.webgis.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.supermap.gaf.webgis.domain.WebgisServiceDo;
import com.supermap.gaf.webgis.entity.WebgisService;
import com.supermap.gaf.webgis.util.Page;
import com.supermap.gaf.webgis.vo.WebgisServiceSelectVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.supermap.gaf.webgis.service.WebgisServiceAssociationService;
import com.supermap.gaf.webgis.entity.WebgisServiceAssociation;
import com.supermap.gaf.webgis.vo.WebgisServiceAssociationSelectVo;
import com.supermap.gaf.webgis.dao.WebgisServiceAssociationMapper;

import java.util.*;

import com.supermap.gaf.shiro.SecurityUtilsExt;
import com.supermap.gaf.shiro.commontypes.ShiroUser;

/**
 * GIS服务关联服务实现类
 *
 * @author wangxiaolong
 * @date yyyy-mm-dd
 */
@Service
public class WebgisServiceAssociationServiceImpl implements WebgisServiceAssociationService {
    @Autowired
    private WebgisServiceAssociationMapper webgisServiceAssociationMapper;

    @Override
    public WebgisServiceAssociation getById(String gisServiceAssocId) {
        if (gisServiceAssocId == null) {
            throw new IllegalArgumentException("gisServiceAssocId不能为空");
        }
        return webgisServiceAssociationMapper.select(gisServiceAssocId);
    }

    @Override
    public Page<WebgisServiceAssociation> listByPageCondition(WebgisServiceAssociationSelectVo webgisServiceAssociationSelectVo, int pageNum, int pageSize) {
        PageInfo<WebgisServiceAssociation> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> {
            webgisServiceAssociationMapper.selectByOneField(webgisServiceAssociationSelectVo);
        });
        return Page.create(pageInfo.getPageNum(), pageInfo.getPageSize(), (int) pageInfo.getTotal(), pageInfo.getPages(), pageInfo.getList());
    }

    @Override
    public WebgisServiceAssociation insertWebgisServiceAssociation(WebgisServiceAssociation webgisServiceAssociation) {
        //TODO: 主键非GeneratedKey，此处添加自定义主键生成策略
        webgisServiceAssociation.setGisServiceAssocId(UUID.randomUUID().toString());

        ShiroUser shiroUser = SecurityUtilsExt.getUser();
        webgisServiceAssociation.setCreatedBy(shiroUser.getAuthUser().getName());
        webgisServiceAssociation.setUpdatedBy(shiroUser.getAuthUser().getName());
        webgisServiceAssociationMapper.insert(webgisServiceAssociation);
        return webgisServiceAssociation;
    }

    @Override
    public void batchInsert(List<WebgisServiceAssociation> webgisServiceAssociations) {
        if (webgisServiceAssociations != null && webgisServiceAssociations.size() > 0) {
            webgisServiceAssociations.forEach(webgisServiceAssociation -> {
                webgisServiceAssociation.setGisServiceAssocId(UUID.randomUUID().toString());
                ShiroUser shiroUser = SecurityUtilsExt.getUser();
                webgisServiceAssociation.setCreatedBy(shiroUser.getAuthUser().getName());
                webgisServiceAssociation.setUpdatedBy(shiroUser.getAuthUser().getName());
            });
            webgisServiceAssociationMapper.batchInsert(webgisServiceAssociations);
        }

    }

    @Override
    public void deleteWebgisServiceAssociation(String gisServiceAssocId) {
        webgisServiceAssociationMapper.delete(gisServiceAssocId);
    }

    @Override
    public void batchDelete(List<String> gisServiceAssocIds) {
        webgisServiceAssociationMapper.batchDelete(gisServiceAssocIds);
    }

    @Override
    public WebgisServiceAssociation updateWebgisServiceAssociation(WebgisServiceAssociation webgisServiceAssociation) {
        ShiroUser shiroUser = SecurityUtilsExt.getUser();
        webgisServiceAssociation.setUpdatedBy(shiroUser.getAuthUser().getName());
        webgisServiceAssociationMapper.update(webgisServiceAssociation);
        return webgisServiceAssociation;
    }

    @Override
    public Page<WebgisServiceDo> listAssociationServices(String webgisServiceId, WebgisServiceSelectVo webgisServiceSelectVo, Integer pageNum, Integer pageSize) {
        PageInfo<WebgisServiceDo> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> {
            webgisServiceAssociationMapper.selectAssociationServices(webgisServiceId, webgisServiceSelectVo);
        });
        return Page.create(pageInfo.getPageNum(), pageInfo.getPageSize(), (int) pageInfo.getTotal(), pageInfo.getPages(), pageInfo.getList());
    }

    @Override
    public Page<WebgisServiceDo> listUnrelatedServices(String webgisServiceId, WebgisServiceSelectVo webgisServiceSelectVo, Integer pageNum, Integer pageSize) {
        PageInfo<WebgisServiceDo> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> {
            webgisServiceAssociationMapper.selectUnrelatedServices(webgisServiceId, webgisServiceSelectVo);
        });
        return Page.create(pageInfo.getPageNum(), pageInfo.getPageSize(), (int) pageInfo.getTotal(), pageInfo.getPages(), pageInfo.getList());
    }

}
