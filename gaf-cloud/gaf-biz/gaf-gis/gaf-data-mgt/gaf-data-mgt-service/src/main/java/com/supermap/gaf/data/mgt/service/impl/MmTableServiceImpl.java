/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.data.mgt.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.supermap.gaf.data.access.service.BatchSortAndCodeService;
import com.supermap.gaf.data.mgt.entity.MmTable;
import com.supermap.gaf.data.mgt.mapper.MmTableMapper;
import com.supermap.gaf.data.mgt.service.MmTableService;
import com.supermap.gaf.data.mgt.util.Page;
import com.supermap.gaf.data.mgt.vo.MmTableSelectVo;
import com.supermap.gaf.shiro.SecurityUtilsExt;
import com.supermap.gaf.shiro.commontypes.ShiroUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * 逻辑服务实现类
 * @author wxl 
 * @date yyyy-mm-dd
 */
@Service
public class MmTableServiceImpl implements MmTableService{
    
	private static final Logger  log = LoggerFactory.getLogger(MmTableServiceImpl.class);
	
	@Autowired
    private MmTableMapper mmTableMapper;
    @Autowired
    private BatchSortAndCodeService batchSortAndCodeService;
	
	@Override
    public MmTable getById(String tableId){
        if(tableId == null){
            throw new IllegalArgumentException("tableId不能为空");
        }
        return  mmTableMapper.select(tableId);
    }
	
	@Override
    public Page<MmTable> listByPageCondition(MmTableSelectVo mmTableSelectVo, int pageNum, int pageSize) {
        PageInfo<MmTable> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> {
            mmTableMapper.selectList(mmTableSelectVo);
        });
        return Page.create(pageInfo.getPageNum(),pageInfo.getPageSize(),(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getList());
    }

    @Override
    public List<MmTable> selectList(MmTableSelectVo mmTableSelectVo) {
        return mmTableMapper.selectList(mmTableSelectVo);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public MmTable insertMmTable(MmTable mmTable){
		mmTable.setTableId(UUID.randomUUID().toString());
		
		ShiroUser shiroUser = SecurityUtilsExt.getUser();
		mmTable.setCreatedBy(shiroUser.getAuthUser().getName());
		mmTable.setUpdatedBy(shiroUser.getAuthUser().getName());
        mmTableMapper.insert(mmTable);
        batchSortAndCodeService.revisionSortSnForInsertOrDelete(MmTable.class, Collections.singleton(mmTable.getTableId()));
        return mmTableMapper.select(mmTable.getTableId());
    }

    @Transactional(rollbackFor = Exception.class)
	@Override
    public void batchInsert(List<MmTable> mmTables){
		if (mmTables != null && mmTables.size() > 0) {
	        mmTables.forEach(mmTable -> {
				mmTable.setTableId(UUID.randomUUID().toString());
				ShiroUser shiroUser = SecurityUtilsExt.getUser();
				mmTable.setCreatedBy(shiroUser.getAuthUser().getName());
				mmTable.setUpdatedBy(shiroUser.getAuthUser().getName());
            });
            mmTableMapper.batchInsert(mmTables);
            batchSortAndCodeService.revisionSortSnForInsertOrDelete(MmTable.class,Collections.singleton(mmTables.get(0).getModelId()));
        }
        
    }

    @Transactional(rollbackFor = Exception.class)
	@Override
    public MmTable deleteMmTable(String tableId){
        MmTable mmTable = mmTableMapper.select(tableId);
        mmTableMapper.delete(tableId);
        batchSortAndCodeService.revisionSortSnForInsertOrDelete(MmTable.class,Collections.singleton(mmTable.getModelId()));
        return mmTable;
    }

    @Transactional(rollbackFor = Exception.class)
	@Override
    public void batchDelete(List<String> tableIds){
	    if (tableIds == null || tableIds.size() == 0) {
	        throw new IllegalArgumentException("逻辑表id不能为空");
        }
        MmTable mmTable = mmTableMapper.select(tableIds.get(0));
        mmTableMapper.batchDelete(tableIds);
        if (mmTable != null) {
            batchSortAndCodeService.revisionSortSnForInsertOrDelete(MmTable.class, Collections.singleton(mmTable.getModelId()));
        }
    }

    @Transactional(rollbackFor = Exception.class)
	@Override
    public MmTable updateMmTable(MmTable mmTable){
        MmTable oldMmTable = mmTableMapper.select(mmTable.getTableId());
        if (oldMmTable == null) {
            throw new IllegalArgumentException("逻辑表不存在");
        }
        ShiroUser shiroUser = SecurityUtilsExt.getUser();
		mmTable.setUpdatedBy(shiroUser.getAuthUser().getName());
		mmTableMapper.update(mmTable);
        batchSortAndCodeService.revisionSortSnForUpdate(MmTable.class,oldMmTable.getModelId(),oldMmTable.getSortSn(),mmTable.getSortSn());
        return mmTable;
    }
    
}
