/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.webgis.service.impl;

import cn.hutool.core.util.URLUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.supermap.gaf.exception.GafException;
import com.supermap.gaf.shiro.SecurityUtilsExt;
import com.supermap.gaf.shiro.commontypes.ShiroUser;
import com.supermap.gaf.utils.MybatisBatchUtil;
import com.supermap.gaf.webgis.dao.WebgisDataServiceFieldMapper;
import com.supermap.gaf.webgis.entity.WebgisDataServiceField;
import com.supermap.gaf.webgis.entity.WebgisService;
import com.supermap.gaf.webgis.enums.ServiceTypeEnum;
import com.supermap.gaf.webgis.service.WebgisDataServiceFieldService;
import com.supermap.gaf.webgis.service.WebgisServiceService;
import com.supermap.gaf.webgis.util.Page;
import com.supermap.gaf.webgis.vo.DataServiceFieldsVo;
import com.supermap.gaf.webgis.vo.WebgisDataServiceFieldSelectVo;
import com.supermap.services.components.commontypes.FieldInfo;
import com.supermap.services.rest.commontypes.FieldContent;
import com.supermap.services.rest.commontypes.FieldsContent;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

/**
 * GIS数据服务字段服务实现类
 * @author wangxiaolong 
 * @date yyyy-mm-dd
 */
@Service
public class WebgisDataServiceFieldServiceImpl implements WebgisDataServiceFieldService{
    @Autowired
    private WebgisDataServiceFieldMapper webgisDataServiceFieldMapper;

    @Autowired
    private WebgisServiceService webgisServiceService;

    @Autowired
    RestTemplate restTemplate;

	@Override
    public WebgisDataServiceField getById(String gisServiceFieldId){
        if(gisServiceFieldId == null){
            throw new IllegalArgumentException("gisServiceFieldId不能为空");
        }
        return  webgisDataServiceFieldMapper.select(gisServiceFieldId);
    }
	
	@Override
    public Page<WebgisDataServiceField> listByPageCondition(WebgisDataServiceFieldSelectVo webgisDataServiceFieldSelectVo, int pageNum, int pageSize) {
        PageInfo<WebgisDataServiceField> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> {
            webgisDataServiceFieldMapper.selectByOneField(webgisDataServiceFieldSelectVo);
        });
        return Page.create(pageInfo.getPageNum(),pageInfo.getPageSize(),(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getList());
    }

	@Override
    public WebgisDataServiceField insertWebgisDataServiceField(WebgisDataServiceField webgisDataServiceField){
		webgisDataServiceField.setGisServiceFieldId(UUID.randomUUID().toString());
		
		ShiroUser shiroUser = SecurityUtilsExt.getUser();
		webgisDataServiceField.setCreatedBy(shiroUser.getAuthUser().getName());
		webgisDataServiceField.setUpdatedBy(shiroUser.getAuthUser().getName());
        webgisDataServiceFieldMapper.insert(webgisDataServiceField);
        return webgisDataServiceField;
    }
	
	@Override
    public void batchInsert(List<WebgisDataServiceField> webgisDataServiceFields){
		if (webgisDataServiceFields != null && webgisDataServiceFields.size() > 0) {
	        webgisDataServiceFields.forEach(webgisDataServiceField -> {
				webgisDataServiceField.setGisServiceFieldId(UUID.randomUUID().toString());
				ShiroUser shiroUser = SecurityUtilsExt.getUser();
				webgisDataServiceField.setCreatedBy(shiroUser.getAuthUser().getName());
				webgisDataServiceField.setUpdatedBy(shiroUser.getAuthUser().getName());
            });
            webgisDataServiceFieldMapper.batchInsert(webgisDataServiceFields);
        }
        
    }
	
	@Override
    public void deleteWebgisDataServiceField(String gisServiceFieldId){
        webgisDataServiceFieldMapper.delete(gisServiceFieldId);
    }

	@Override
    public void batchDelete(List<String> gisServiceFieldIds){
        webgisDataServiceFieldMapper.batchDelete(gisServiceFieldIds);
    }
	
	@Override
    public WebgisDataServiceField updateWebgisDataServiceField(WebgisDataServiceField webgisDataServiceField){
		ShiroUser shiroUser = SecurityUtilsExt.getUser();
		webgisDataServiceField.setUpdatedBy(shiroUser.getAuthUser().getName());
		webgisDataServiceFieldMapper.update(webgisDataServiceField);
        return webgisDataServiceField;
    }

    @Override
    public DataServiceFieldsVo listFieldsAndSelectFieldNames(String webgisServiceId) {
        WebgisService webgisService = webgisServiceService.getById(webgisServiceId);
        RestTemplate rt = new RestTemplate();
        if(!ServiceTypeEnum.RESTDATA.getCode().equals(webgisService.getTypeCode())) {
            throw new GafException("服务类型错误，应为数据服务");
        }
        String normalizeUrl = URLUtil.normalize(webgisService.getAddress() + "/fields.json");
        FieldsContent fieldsContent = rt.getForObject(normalizeUrl, FieldsContent.class);
        if(fieldsContent == null) {
            return null;
        }
        WebgisDataServiceField query = WebgisDataServiceField.builder().status(true).gisDataServiceId(webgisServiceId).build();
        List<WebgisDataServiceField> selectedFields = this.webgisDataServiceFieldMapper.selectByCombination(query);
        Map<String, WebgisDataServiceField> nameAndFieldMap = selectedFields.stream().collect(Collectors.toMap(WebgisDataServiceField::getFieldName, obj -> obj));
        // 可以优化为异步编排同时请求
        List<WebgisDataServiceField> allFields = fieldsContent.childUriList.stream().map(fieldUri -> {
            FieldInfo fieldInfo = Objects.requireNonNull(rt.getForObject(URLUtil.normalize(fieldUri + ".json"), FieldContent.class)).fieldInfo;
            WebgisDataServiceField webgisDataServiceField = new WebgisDataServiceField();
            webgisDataServiceField.setFieldName(fieldInfo.name);
            WebgisDataServiceField webgisFiled = nameAndFieldMap.get(fieldInfo.name);
            if (webgisFiled!=null) {
                webgisDataServiceField.setFieldAlias(webgisFiled.getFieldAlias());
            } else {
                webgisDataServiceField.setFieldAlias(fieldInfo.caption);
            }
            webgisDataServiceField.setFieldTypeCode(fieldInfo.type.name());
            return webgisDataServiceField;
        }).collect(Collectors.toList());
        // 过滤系统字段SmX、SmY、SmLibTileID、SmUserID、SmGeometrySize
        Set<String> systemFieldSet = new HashSet<>(5);
        systemFieldSet.add("SmX");
        systemFieldSet.add("SmY");
        systemFieldSet.add("SmLibTileID");
        systemFieldSet.add("SmUserID");
        systemFieldSet.add("SmGeometrySize");
        List<WebgisDataServiceField> filterFields = allFields.stream().filter(field ->
            !systemFieldSet.contains(field.getFieldName())
        ).collect(Collectors.toList());
        return new DataServiceFieldsVo(filterFields, new ArrayList<>(nameAndFieldMap.keySet()));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void configFields(List<WebgisDataServiceField> fields, String webgisServiceId) {
        WebgisService webgisService = webgisServiceService.getById(webgisServiceId);
        if(!ServiceTypeEnum.RESTDATA.getCode().equals(webgisService.getTypeCode())) {
            throw new GafException("服务类型错误，应为数据服务");
        }
        ShiroUser shiroUser = SecurityUtilsExt.getUser();

        WebgisDataServiceField query = WebgisDataServiceField.builder().status(true).gisDataServiceId(webgisServiceId).build();
        List<WebgisDataServiceField> oldFields = this.webgisDataServiceFieldMapper.selectByCombination(query);
        List<WebgisDataServiceField> toUpdate = new LinkedList<>();
        List<String> toDelete = new LinkedList<>();
        oldFields.forEach(old -> {
            boolean isInFields = false;
            for (WebgisDataServiceField field : fields) {
                if (old.getFieldName().equals(field.getFieldName())) {
                    WebgisDataServiceField serviceField = new WebgisDataServiceField();
                    BeanUtils.copyProperties(old,serviceField);
                    serviceField.setFieldAlias(field.getFieldAlias());
                    toUpdate.add(serviceField);
                    isInFields = true;
                    break;
                }
            }
            if(!isInFields) {
                toDelete.add(old.getGisServiceFieldId());
            }
        });
        List<WebgisDataServiceField> toAdd = fields.stream().filter(field -> {
            for (WebgisDataServiceField oldField : oldFields) {
                if (oldField.getFieldName().equals(field.getFieldName())) {
                    return false;
                }
            }
            return true;
        }).collect(Collectors.toList());

        toUpdate.forEach(updateField-> {
            updateField.setUpdatedBy(shiroUser.getAuthUser().getName());
        });
        toAdd.forEach(addField -> {
            addField.setGisDataServiceId(webgisServiceId);
        });
        if(toDelete.size() > 0) {
            this.batchDelete(toDelete);
        }
        if(toAdd.size() > 0) {
            this.batchInsert(toAdd);
        }
        if(toUpdate.size() > 0) {
            MybatisBatchUtil.insertOrUpdateBatch(WebgisDataServiceFieldMapper.class,toUpdate, WebgisDataServiceFieldMapper::update);
        }
    }

}
