/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.webgis.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.supermap.gaf.shiro.SecurityUtilsExt;
import com.supermap.gaf.shiro.commontypes.ShiroUser;
import com.supermap.gaf.webgis.dao.WebgisRoamStopMapper;
import com.supermap.gaf.webgis.entity.WebgisRoamStop;
import com.supermap.gaf.webgis.service.WebgisRoamStopService;
import com.supermap.gaf.webgis.util.Page;
import com.supermap.gaf.webgis.vo.WebgisRoamStopSelectVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * 漫游站点服务实现类
 * @author wangxiaolong 
 * @date yyyy-mm-dd
 */
@Service
public class WebgisRoamStopServiceImpl implements WebgisRoamStopService{
    
	private static final Logger  log = LoggerFactory.getLogger(WebgisRoamStopServiceImpl.class);
	
	@Autowired
    private WebgisRoamStopMapper webgisRoamStopMapper;
	
	@Override
    public WebgisRoamStop getById(String gisRoamStopId){
        if(gisRoamStopId == null){
            throw new IllegalArgumentException("gisRoamStopId不能为空");
        }
        return  webgisRoamStopMapper.select(gisRoamStopId);
    }

    @Override
    public List<WebgisRoamStop> listStops(String routeId) {
        WebgisRoamStopSelectVo selectVo = new WebgisRoamStopSelectVo();
        selectVo.setGisRoamRouteId(routeId);
        return list(selectVo);
    }

    @Override
    public List<WebgisRoamStop> list(WebgisRoamStopSelectVo webgisRoamStopSelectVo) {
        return webgisRoamStopMapper.selectList(webgisRoamStopSelectVo);
    }

    @Override
    public Page<WebgisRoamStop> listByPageCondition(WebgisRoamStopSelectVo webgisRoamStopSelectVo, int pageNum, int pageSize) {
        PageInfo<WebgisRoamStop> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> {
            webgisRoamStopMapper.selectList(webgisRoamStopSelectVo);
        });
        return Page.create(pageInfo.getPageNum(),pageInfo.getPageSize(),(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getList());
    }

	@Override
    public WebgisRoamStop insertWebgisRoamStop(WebgisRoamStop webgisRoamStop){
        // 主键非GeneratedKey，此处添加自定义主键生成策略
		webgisRoamStop.setGisRoamStopId(UUID.randomUUID().toString());
		
		ShiroUser shiroUser = SecurityUtilsExt.getUser();
		webgisRoamStop.setCreatedBy(shiroUser.getAuthUser().getName());
		webgisRoamStop.setUpdatedBy(shiroUser.getAuthUser().getName());
        webgisRoamStopMapper.insert(webgisRoamStop);
        return webgisRoamStop;
    }
	
	@Override
    public void batchInsert(List<WebgisRoamStop> webgisRoamStops){
		if (webgisRoamStops != null && webgisRoamStops.size() > 0) {
	        webgisRoamStops.forEach(webgisRoamStop -> {
				webgisRoamStop.setGisRoamStopId(UUID.randomUUID().toString());
				ShiroUser shiroUser = SecurityUtilsExt.getUser();
				webgisRoamStop.setCreatedBy(shiroUser.getAuthUser().getName());
				webgisRoamStop.setUpdatedBy(shiroUser.getAuthUser().getName());
            });
            webgisRoamStopMapper.batchInsert(webgisRoamStops);
        }
        
    }
	
	@Override
    public void deleteWebgisRoamStop(String gisRoamStopId){
        webgisRoamStopMapper.deleteById(gisRoamStopId);
    }



    @Override
    public void batchDelete(List<String> gisRoamStopIds){
        webgisRoamStopMapper.batchDelete(gisRoamStopIds);
    }
	
	@Override
    public WebgisRoamStop updateWebgisRoamStop(WebgisRoamStop webgisRoamStop){
		ShiroUser shiroUser = SecurityUtilsExt.getUser();
		webgisRoamStop.setUpdatedBy(shiroUser.getAuthUser().getName());
		webgisRoamStopMapper.update(webgisRoamStop);
        return webgisRoamStop;
    }

    @Transactional
    @Override
    public void removeByRouteId(String routeId) {
        WebgisRoamStop query = new WebgisRoamStop();
        query.setGisRoamRouteId(routeId);
        webgisRoamStopMapper.delete(query);
    }


}
