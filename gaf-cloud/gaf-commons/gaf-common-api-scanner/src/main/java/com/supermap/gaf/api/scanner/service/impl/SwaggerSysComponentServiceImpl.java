/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.api.scanner.service.impl;

import com.supermap.gaf.api.scanner.dao.SwaggerSysComponentMapper;
import com.supermap.gaf.api.scanner.entity.SysComponent;
import com.supermap.gaf.api.scanner.entity.SysComponentSelectVo;
import com.supermap.gaf.api.scanner.enums.CatalogTypeEnum;
import com.supermap.gaf.api.scanner.service.SwaggerSysCatalogService;
import com.supermap.gaf.api.scanner.service.SwaggerSysComponentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.UUID;

/**
 * @author wangxiaolong
 * @date:2021/3/25
 * 组件服务类
 */
@Service
public class SwaggerSysComponentServiceImpl implements SwaggerSysComponentService {
    @Autowired
    private SwaggerSysComponentMapper swaggerSysComponentMapper;

    @Autowired
    private SwaggerSysCatalogService swaggerSysCatalogService;

    /**
     * 新增组件
     * @return
     */
    @Override
    @Transactional
    public SysComponent insertSysComponent(SysComponent sysComponent){
        //TODO: 主键非GeneratedKey，此处添加自定义主键生成策略
        String sysComponentId = UUID.randomUUID().toString();
        sysComponent.setSysComponentId(sysComponentId);
        //唯一性校验
        checkUniqueness(sysComponent, false);
        swaggerSysComponentMapper.insert(sysComponent);
        //新增组件的模块树根节点
        if (!swaggerSysCatalogService.insertSysCatlog(sysComponent, CatalogTypeEnum.MODULE_GROUP_TYPE)){
            throw new RuntimeException("新增模块树根节点失败");
        }
        //新增组件的API分组树根节点
        if(!swaggerSysCatalogService.insertSysCatlog(sysComponent, CatalogTypeEnum.API_GROUP_TYPE)){
            throw new RuntimeException("新增API分组树根节点失败");
        }
        return sysComponent;
    }

    @Override
    public List<SysComponent> selectByOneField(String fieldName, String fieldValue) {
        return swaggerSysComponentMapper.selectByOneField(fieldName,fieldValue);
    }



    /**
     * 唯一性校验
     *
     * @param sysComponent
     * @param isUpdate 是否为更新，更新时需要排除当前组件
     */
    private void checkUniqueness(SysComponent sysComponent, boolean isUpdate) {
        // name，全局不重复
        SysComponentSelectVo sysComponentSelectVo = SysComponentSelectVo.builder()
                .searchFieldName("name")
                .searchFieldValue(sysComponent.getName())
                .build();
        List<SysComponent> nameSysComponents = swaggerSysComponentMapper.searchWholeMatchList(sysComponentSelectVo);
        if (!CollectionUtils.isEmpty(nameSysComponents)) {
            boolean isNameExist;
            if (isUpdate) {
                isNameExist = nameSysComponents.stream()
                        .anyMatch(component -> component.getStatus() && !sysComponent.getSysComponentId().equals(component.getSysComponentId()));
            } else {
                isNameExist = nameSysComponents.stream()
                        .anyMatch(component -> component.getStatus());
            }
            if (isNameExist) {
                throw new RuntimeException("组件名称已存在");
            }
        }
    }







}
