/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.data.mgt.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.supermap.gaf.data.mgt.dao.DataDatasetMapper;
import com.supermap.gaf.data.mgt.entity.DataDataset;
import com.supermap.gaf.data.mgt.service.DataDatasetService;
import com.supermap.gaf.data.mgt.util.Page;
import com.supermap.gaf.data.mgt.vo.DataDatasetSelectVo;
import com.supermap.gaf.shiro.SecurityUtilsExt;
import com.supermap.gaf.shiro.commontypes.ShiroUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * 数据集服务实现类
 * @author yw 
 * @date yyyy-mm-dd
 */
@Service
public class DataDatasetServiceImpl implements DataDatasetService{
    
	private static final Logger  log = LoggerFactory.getLogger(DataDatasetServiceImpl.class);
	
	@Autowired
    private DataDatasetMapper dataDatasetMapper;
	
	@Override
    public DataDataset getById(String datasetId){
        if(datasetId == null){
            throw new IllegalArgumentException("datasetId不能为空");
        }
        return  dataDatasetMapper.select(datasetId);
    }
	
	@Override
    public Page<DataDataset> listByPageCondition(DataDatasetSelectVo dataDatasetSelectVo, int pageNum, int pageSize) {
        PageInfo<DataDataset> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> {
            dataDatasetMapper.selectList(dataDatasetSelectVo);
        });
        return Page.create(pageInfo.getPageNum(),pageInfo.getPageSize(),(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getList());
    }

	@Override
    public DataDataset insertDataDataset(DataDataset dataDataset){
        // 主键非GeneratedKey，此处添加自定义主键生成策略
		dataDataset.setDatasetId(UUID.randomUUID().toString());
		
		ShiroUser shiroUser = SecurityUtilsExt.getUser();
		dataDataset.setCreatedBy(shiroUser.getAuthUser().getName());
		dataDataset.setUpdatedBy(shiroUser.getAuthUser().getName());
        dataDatasetMapper.insert(dataDataset);
        return dataDataset;
    }
	
	@Override
    public void batchInsert(List<DataDataset> dataDatasets){
		if (dataDatasets != null && dataDatasets.size() > 0) {
	        dataDatasets.forEach(dataDataset -> {
				dataDataset.setDatasetId(UUID.randomUUID().toString());
				ShiroUser shiroUser = SecurityUtilsExt.getUser();
				dataDataset.setCreatedBy(shiroUser.getAuthUser().getName());
				dataDataset.setUpdatedBy(shiroUser.getAuthUser().getName());
            });
            dataDatasetMapper.batchInsert(dataDatasets);
        }
        
    }
	
	@Override
    public void deleteDataDataset(String datasetId){
        dataDatasetMapper.delete(datasetId);
    }

	@Override
    public void batchDelete(List<String> datasetIds){
        dataDatasetMapper.batchDelete(datasetIds);
    }
	
	@Override
    public DataDataset updateDataDataset(DataDataset dataDataset){
		ShiroUser shiroUser = SecurityUtilsExt.getUser();
		dataDataset.setUpdatedBy(shiroUser.getAuthUser().getName());
		dataDatasetMapper.update(dataDataset);
        return dataDataset;
    }
    
}
