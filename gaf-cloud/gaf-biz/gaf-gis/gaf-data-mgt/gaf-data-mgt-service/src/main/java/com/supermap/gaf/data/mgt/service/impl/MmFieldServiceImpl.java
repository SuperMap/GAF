/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.data.mgt.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.supermap.gaf.data.access.service.BatchSortAndCodeService;
import com.supermap.gaf.data.mgt.entity.MmField;
import com.supermap.gaf.data.mgt.enums.DatasourceTypeEnum;
import com.supermap.gaf.data.mgt.enums.SdxFieldTypeEnum;
import com.supermap.gaf.data.mgt.mapper.MmFieldMapper;
import com.supermap.gaf.data.mgt.model.FieldTypeInfo;
import com.supermap.gaf.data.mgt.service.MmFieldService;
import com.supermap.gaf.data.mgt.util.Page;
import com.supermap.gaf.data.mgt.vo.MmFieldSelectVo;
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
 * 字段服务实现类
 * @author wxl 
 * @date yyyy-mm-dd
 */
@Service
public class MmFieldServiceImpl implements MmFieldService{
    
	private static final Logger  log = LoggerFactory.getLogger(MmFieldServiceImpl.class);
	
	@Autowired
    private MmFieldMapper mmFieldMapper;

    @Autowired
    private BatchSortAndCodeService batchSortAndCodeService;
	
	@Override
    public MmField getById(String fieldId){
        if(fieldId == null){
            throw new IllegalArgumentException("fieldId不能为空");
        }
        return  mmFieldMapper.select(fieldId);
    }

	@Override
    public Page<MmField> listByPageCondition(MmFieldSelectVo mmFieldSelectVo, int pageNum, int pageSize) {
        PageInfo<MmField> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> {
            mmFieldMapper.selectList(mmFieldSelectVo);
        });
        return Page.create(pageInfo.getPageNum(),pageInfo.getPageSize(),(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getList());
    }

    @Override
    public List<MmField> selectList(MmFieldSelectVo mmFieldSelectVo) {
        return mmFieldMapper.selectList(mmFieldSelectVo);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public MmField insertMmField(MmField mmField){
		mmField.setFieldId(UUID.randomUUID().toString());
		ShiroUser shiroUser = SecurityUtilsExt.getUser();
		mmField.setCreatedBy(shiroUser.getAuthUser().getName());
		mmField.setUpdatedBy(shiroUser.getAuthUser().getName());
        mmFieldMapper.insert(mmField);
        batchSortAndCodeService.revisionSortSnForInsertOrDelete(MmField.class, Collections.singleton(mmField.getTableId()));
        return getById(mmField.getFieldId());
    }

    @Transactional(rollbackFor = Exception.class)
	@Override
    public void batchInsert(List<MmField> mmFields){
		if (mmFields != null && mmFields.size() > 0) {
	        mmFields.forEach(mmField -> {
				mmField.setFieldId(UUID.randomUUID().toString());
				ShiroUser shiroUser = SecurityUtilsExt.getUser();
				mmField.setCreatedBy(shiroUser.getAuthUser().getName());
				mmField.setUpdatedBy(shiroUser.getAuthUser().getName());
            });
            mmFieldMapper.batchInsert(mmFields);
            batchSortAndCodeService.revisionSortSnForInsertOrDelete(MmField.class, Collections.singleton(mmFields.get(0).getTableId()));
        }
        
    }

    @Transactional(rollbackFor = Exception.class)
	@Override
    public void deleteMmField(String fieldId){
        MmField mmField = getById(fieldId);
        if (mmField != null) {
            mmFieldMapper.delete(fieldId);
            batchSortAndCodeService.revisionSortSnForInsertOrDelete(MmField.class, Collections.singleton(mmField.getTableId()));
        }
    }

    @Transactional(rollbackFor = Exception.class)
	@Override
    public void batchDelete(List<String> fieldIds){
        if (fieldIds == null || fieldIds.size() == 0) {
            throw new IllegalArgumentException("字段id不能为空");
        }
        MmField mmField = getById(fieldIds.get(0));
        mmFieldMapper.batchDelete(fieldIds);
        if (mmField != null) {
            batchSortAndCodeService.revisionSortSnForInsertOrDelete(MmField.class, Collections.singleton(mmField.getTableId()));
        }
    }

    @Transactional(rollbackFor = Exception.class)
	@Override
    public MmField updateMmField(MmField mmField){
        MmField oldMmField = getById(mmField.getFieldId());

        ShiroUser shiroUser = SecurityUtilsExt.getUser();
		mmField.setUpdatedBy(shiroUser.getAuthUser().getName());
		mmFieldMapper.update(mmField);
        batchSortAndCodeService.revisionSortSnForUpdate(MmField.class,oldMmField.getTableId(),oldMmField.getSortSn(),mmField.getSortSn());
        return mmField;
    }

    @Override
    public List<FieldTypeInfo> listTypeInfos(String modelType) {
        if ("sdx".equals(modelType)) {
            return SdxFieldTypeEnum.toFieldTypeInfoList();
        } else {
            DatasourceTypeEnum datasourceTypeEnum = DatasourceTypeEnum.fromCode(modelType);
            return datasourceTypeEnum.getFieldTypes();
        }
    }

}
