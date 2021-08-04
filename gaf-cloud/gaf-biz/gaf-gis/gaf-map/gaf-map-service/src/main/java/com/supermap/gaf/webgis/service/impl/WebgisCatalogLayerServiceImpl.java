/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.webgis.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.supermap.gaf.data.access.service.BatchSortAndCodeService;
import com.supermap.gaf.shiro.SecurityUtilsExt;
import com.supermap.gaf.shiro.commontypes.ShiroUser;
import com.supermap.gaf.webgis.dao.WebgisCatalogLayerMapper;
import com.supermap.gaf.webgis.entity.WebgisCatalogLayer;
import com.supermap.gaf.webgis.entity.WebgisService;
import com.supermap.gaf.webgis.service.WebgisCatalogLayerService;
import com.supermap.gaf.webgis.service.WebgisServiceService;
import com.supermap.gaf.webgis.util.Page;
import com.supermap.gaf.webgis.vo.WebgisCatalogLayerSelectVo;
import com.supermap.gaf.webgis.vo.WebgisServiceToLayerVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 图层服务实现类
 *
 * @author wangxiaolong
 * @date 2020-12-05
 */
@Service
public class WebgisCatalogLayerServiceImpl implements WebgisCatalogLayerService {
    @Autowired
    private WebgisCatalogLayerMapper webgisCatalogLayerMapper;

    @Autowired
    private WebgisServiceService webgisServiceService;

    @Autowired
    private BatchSortAndCodeService batchSortAndCodeService;

    @Override
    public WebgisCatalogLayer getById(String catalogLayerId) {
        if (catalogLayerId == null) {
            throw new IllegalArgumentException("catalogLayerId不能为空");
        }
        WebgisCatalogLayer layer = webgisCatalogLayerMapper.select(catalogLayerId);
        WebgisService service = webgisServiceService.getById(layer.getGisServiceId());
        if (service != null) {
            layer.setServiceName(service.getName());
            layer.setServiceNameEn(service.getNameEn());
            layer.setAddress(service.getAddress());
            layer.setTypeCode(service.getTypeCode());
            layer.setMoreProperties(service.getMoreProperties());
        }
        return webgisCatalogLayerMapper.select(catalogLayerId);
    }

    @Override
    public Page<WebgisCatalogLayer> listByPageCondition(WebgisCatalogLayerSelectVo webgisCatalogLayerSelectVo, int pageNum, int pageSize) {
        PageInfo<WebgisCatalogLayer> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> {
            webgisCatalogLayerMapper.selectByCondition(webgisCatalogLayerSelectVo);
        });
        List<WebgisCatalogLayer> list = pageInfo.getList();
        if (list.size() > 0) {
            List<String> serviceIds = list.stream().map(WebgisCatalogLayer::getGisServiceId).collect(Collectors.toList());
            List<WebgisService> gisServices = webgisServiceService.listByIds(serviceIds);
            if (gisServices.size() > 0) {
                Map<String, WebgisService> map = gisServices.stream().collect(Collectors.toMap(WebgisService::getGisServiceId, o -> o));
                list.forEach(webgisCatalogLayer -> {
                    WebgisService webgisService = map.get(webgisCatalogLayer.getGisServiceId());
                    if (webgisService != null) {
                        webgisCatalogLayer.setServiceName(webgisService.getName());
                        webgisCatalogLayer.setAddress(webgisService.getAddress());
                        webgisCatalogLayer.setServiceNameEn(webgisService.getNameEn());
                        webgisCatalogLayer.setTypeCode(webgisService.getTypeCode());
                        webgisCatalogLayer.setMoreProperties(webgisService.getMoreProperties());
                    }
                });
            }
        }
        ;
        return Page.create(pageInfo.getPageNum(), pageInfo.getPageSize(), (int) pageInfo.getTotal(), pageInfo.getPages(), list);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public WebgisCatalogLayer insertWebgisCatalogLayer(WebgisCatalogLayer webgisCatalogLayer) {
        webgisCatalogLayer.setCatalogLayerId(UUID.randomUUID().toString());
        ShiroUser shiroUser = SecurityUtilsExt.getUser();
        webgisCatalogLayer.setCreatedBy(shiroUser.getAuthUser().getName());
        webgisCatalogLayer.setUpdatedBy(shiroUser.getAuthUser().getName());
        webgisCatalogLayerMapper.insert(webgisCatalogLayer);
        batchSortAndCodeService.revisionSortSnForInsertOrDelete(WebgisCatalogLayer.class, Collections.singletonList(webgisCatalogLayer.getLayerCatalogId()));
        return webgisCatalogLayer;
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void batchInsert(List<WebgisCatalogLayer> webgisCatalogLayers) {
        if (webgisCatalogLayers != null && webgisCatalogLayers.size() > 0) {
            webgisCatalogLayers.forEach(webgisCatalogLayer -> {
                webgisCatalogLayer.setCatalogLayerId(UUID.randomUUID().toString());
                ShiroUser shiroUser = SecurityUtilsExt.getUser();
                webgisCatalogLayer.setCreatedBy(shiroUser.getAuthUser().getName());
                webgisCatalogLayer.setUpdatedBy(shiroUser.getAuthUser().getName());
            });
            webgisCatalogLayerMapper.batchInsert(webgisCatalogLayers);
            batchSortAndCodeService.revisionSortSnForInsertOrDelete(WebgisCatalogLayer.class, Collections.singletonList(webgisCatalogLayers.get(0).getLayerCatalogId()));
        }

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteWebgisCatalogLayer(String catalogLayerId) {
        WebgisCatalogLayer layer = getById(catalogLayerId);
        if (layer != null) {
            webgisCatalogLayerMapper.delete(catalogLayerId);
            batchSortAndCodeService.revisionSortSnForInsertOrDelete(WebgisCatalogLayer.class, Collections.singletonList(layer.getLayerCatalogId()));
        }
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void batchDelete(List<String> catalogLayerIds) {
        List<WebgisCatalogLayer> layers = webgisCatalogLayerMapper.selectByIds(catalogLayerIds);
        if (layers != null && layers.size() > 0) {
            Set<String> catalogIds = layers.stream().map(WebgisCatalogLayer::getLayerCatalogId).collect(Collectors.toSet());
            webgisCatalogLayerMapper.batchDelete(catalogLayerIds);
            batchSortAndCodeService.revisionSortSnForInsertOrDelete(WebgisCatalogLayer.class, catalogIds);
        }
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public WebgisCatalogLayer updateWebgisCatalogLayer(WebgisCatalogLayer webgisCatalogLayer) {
        ShiroUser shiroUser = SecurityUtilsExt.getUser();
        webgisCatalogLayer.setUpdatedBy(shiroUser.getAuthUser().getName());
        WebgisCatalogLayer oldLayer = getById(webgisCatalogLayer.getCatalogLayerId());
        webgisCatalogLayerMapper.update(webgisCatalogLayer);
        if (!Objects.equals(oldLayer.getSortSn(), webgisCatalogLayer.getSortSn())) {
            batchSortAndCodeService.revisionSortSnForUpdate(WebgisCatalogLayer.class, webgisCatalogLayer.getLayerCatalogId(), oldLayer.getSortSn(), webgisCatalogLayer.getSortSn());
        }
        return webgisCatalogLayer;
    }

    @Override
    public List<WebgisCatalogLayer> getWebgisCatalogLayerByCatalogIds(List<String> catalogIds) {
        return webgisCatalogLayerMapper.getWebgisCatalogLayerByCatalogIds(catalogIds);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void batchInsertByService(List<WebgisServiceToLayerVo> webgisServiceToLayerVos) {
        // 查询已经关联的数据
        WebgisServiceToLayerVo webgisServiceToLayerVo = webgisServiceToLayerVos.get(0);
        String layerCatalogId = webgisServiceToLayerVo.getLayerCatalogId();
        WebgisCatalogLayerSelectVo webgisCatalogLayerSelectVo = new WebgisCatalogLayerSelectVo();
        webgisCatalogLayerSelectVo.setLayerCatalogId(layerCatalogId);
        long count = PageHelper.count(() -> webgisCatalogLayerMapper.selectByCondition(webgisCatalogLayerSelectVo));
        List<WebgisCatalogLayer> needInsertLayerList = new LinkedList<>();
        String name = SecurityUtilsExt.getUser().getAuthUser().getName();
        for (int i = 0; i < webgisServiceToLayerVos.size(); i++) {
            WebgisServiceToLayerVo vo = webgisServiceToLayerVos.get(i);
            WebgisCatalogLayer webgisCatalogLayer = new WebgisCatalogLayer();
            webgisCatalogLayer.setName(vo.getName());
            webgisCatalogLayer.setLayerCatalogId(vo.getLayerCatalogId());
            webgisCatalogLayer.setGisServiceId(vo.getGisServiceId());
            webgisCatalogLayer.setSortSn((int) (i + 1 + count));
            webgisCatalogLayer.setInitLoad(false);
            webgisCatalogLayer.setCatalogLayerId(UUID.randomUUID().toString());
            webgisCatalogLayer.setCreatedBy(name);
            webgisCatalogLayer.setUpdatedBy(name);
            needInsertLayerList.add(webgisCatalogLayer);
        }
        webgisCatalogLayerMapper.batchInsert(needInsertLayerList);
    }

    @Override
    public List<String> listGisIdsByCatalogId(String layerCatalogId) {
        return webgisCatalogLayerMapper.selectServiceIdsByCatalogId(layerCatalogId);
    }

    @Override
    public Integer countByCatalogId(String layerCatalogId) {
        WebgisCatalogLayerSelectVo webgisCatalogLayerSelectVo = new WebgisCatalogLayerSelectVo();
        webgisCatalogLayerSelectVo.setLayerCatalogId(layerCatalogId);
        long count = PageHelper.count(() -> webgisCatalogLayerMapper.selectByCondition(webgisCatalogLayerSelectVo));
        return (int) count;
    }

}
