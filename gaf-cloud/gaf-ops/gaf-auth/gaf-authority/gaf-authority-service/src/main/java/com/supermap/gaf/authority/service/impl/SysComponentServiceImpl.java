/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.authority.service.impl;

import com.supermap.gaf.authority.commontype.SysComponent;
import com.supermap.gaf.authority.constant.CommonConstant;
import com.supermap.gaf.authority.dao.SysComponentMapper;
import com.supermap.gaf.authority.service.SysComponentService;
import com.supermap.gaf.authority.vo.SelectOptionVo;
import com.supermap.gaf.authority.vo.SysComponentSelectVo;
import com.supermap.gaf.exception.GafException;
import com.supermap.gaf.sys.mgt.service.SysCatalogService;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.supermap.gaf.sys.mgt.enums.CatalogTypeEnum.API_GROUP_TYPE;
import static com.supermap.gaf.sys.mgt.enums.CatalogTypeEnum.MODULE_GROUP_TYPE;


/**
 * 组件服务实现类
 *
 * @author wangxiaolong
 * @date:2021/3/25
 */
@Service
public class SysComponentServiceImpl implements SysComponentService {
    private final SysComponentMapper sysComponentMapper;

    private final SysCatalogService sysCatalogService;

    public SysComponentServiceImpl(SysComponentMapper sysComponentMapper, SysCatalogService sysCatalogService) {
        this.sysComponentMapper = sysComponentMapper;
        this.sysCatalogService = sysCatalogService;
    }

    @Override
    public SysComponent getById(String sysComponentId) {
        if (sysComponentId == null) {
            throw new IllegalArgumentException("sysComponentId不能为空");
        }
        return sysComponentMapper.select(sysComponentId);
    }

    @Override
    public Map<String, Object> pageList(SysComponentSelectVo sysComponentSelectVo) {
        if (sysComponentSelectVo.getPageSize() == null || sysComponentSelectVo.getPageSize() == 0) {
            sysComponentSelectVo.setPageSize(50);
        }
        List<SysComponent> pageList;
        if (sysComponentSelectVo.getOffset() == null || sysComponentSelectVo.getOffset() < CommonConstant.OFFSET_MAX_FOR_SQL_BETTER) {
            pageList = sysComponentMapper.pageList(sysComponentSelectVo);
        } else {
            pageList = sysComponentMapper.bigOffsetPageList(sysComponentSelectVo);
        }
        int totalCount = sysComponentMapper.pageListCount();
        Map<String, Object> result = new HashMap<>(2);
        result.put("pageList", pageList);
        result.put("totalCount", totalCount);
        return result;
    }

    @Override
    public Map<String, Object> searchList(SysComponentSelectVo sysComponentSelectVo) {
        if (sysComponentSelectVo.getPageSize() == null || sysComponentSelectVo.getPageSize() == 0) {
            sysComponentSelectVo.setPageSize(50);
        }
        List<SysComponent> pageList;
        pageList = sysComponentMapper.searchList(sysComponentSelectVo);
        Integer totalCount = sysComponentMapper.countOneField(sysComponentSelectVo.getSearchFieldName(), sysComponentSelectVo.getSearchFieldValue());
        Map<String, Object> result = new HashMap<>(2);
        result.put("pageList", pageList);
        result.put("totalCount", totalCount);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysComponent insertSysComponent(SysComponent sysComponent) {
        String sysComponentId = UUID.randomUUID().toString();
        sysComponent.setSysComponentId(sysComponentId);
        // 唯一性校验
        checkUniqueness(sysComponent);
        sysComponentMapper.insert(sysComponent);
        //新增组件的模块树根节点
        if (!sysCatalogService.insertSysCatalog(sysComponent, MODULE_GROUP_TYPE)) {
            throw new GafException("新增模块树根节点失败");
        }
        //新增组件的API分组树根节点
        if (!sysCatalogService.insertSysCatalog(sysComponent, API_GROUP_TYPE)) {
            throw new GafException("新增API分组树根节点失败");
        }
        return sysComponent;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchInsert(List<SysComponent> sysComponents) {
        if (sysComponents != null && sysComponents.size() > 0) {
            sysComponents.forEach(sysComponent -> ((SysComponentService) AopContext.currentProxy()).insertSysComponent(sysComponent));
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteSysComponent(String sysComponentId) {
        deleteCatalog(sysComponentId);
        sysComponentMapper.delete(sysComponentId);
    }

    private void deleteCatalog(String sysComponentId) {
        SysComponent sysComponent = new SysComponent();
        sysComponent.setSysComponentId(sysComponentId);
        //更新组件的模块树根节点
        if (!sysCatalogService.deleteSysCatalog(sysComponent, MODULE_GROUP_TYPE)) {
            throw new GafException("删除模块树根节点失败");
        }
        //更新组件的API分组树根节点
        if (!sysCatalogService.deleteSysCatalog(sysComponent, API_GROUP_TYPE)) {
            throw new GafException("删除API分组树根节点失败");
        }
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void batchDelete(List<String> sysComponentIds) {
        sysComponentMapper.batchDelete(sysComponentIds);
        for (String sysComponentId : sysComponentIds) {
            ((SysComponentService) AopContext.currentProxy()).deleteSysComponent(sysComponentId);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysComponent updateSysComponent(SysComponent sysComponent) {
        //更新组件的模块树根节点
        if (!sysCatalogService.updateSysCatalog(sysComponent, MODULE_GROUP_TYPE)) {
            throw new GafException("更新模块树根节点失败");
        }
        //更新组件的API分组树根节点
        if (!sysCatalogService.updateSysCatalog(sysComponent, API_GROUP_TYPE)) {
            throw new GafException("更新API分组树根节点失败");
        }
        sysComponentMapper.update(sysComponent);
        return sysComponent;
    }

    @Override
    public List<SelectOptionVo> getAllOptions() {
        List<SysComponent> all = sysComponentMapper.getAllIdAndNameCn();
        if (all != null) {
            return all.stream().map(sysComponent -> {
                SelectOptionVo selectOptionVo = new SelectOptionVo();
                selectOptionVo.setValue(sysComponent.getSysComponentId());
                selectOptionVo.setLabel(sysComponent.getNameCn());
                selectOptionVo.setKey(sysComponent.getSysComponentId());
                return selectOptionVo;
            }).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public List<SysComponent> selectByOneField(String fieldName, String fieldValue) {
        return sysComponentMapper.selectByOneField(fieldName, fieldValue);
    }

    /**
     * 唯一性校验
     *
     * @param sysComponent 组件
     */
    private void checkUniqueness(SysComponent sysComponent) {
        // name，全局不重复
        SysComponentSelectVo sysComponentSelectVo = SysComponentSelectVo.builder()
                .searchFieldName("name")
                .searchFieldValue(sysComponent.getName())
                .build();
        List<SysComponent> nameSysComponents = sysComponentMapper.searchWholeMatchList(sysComponentSelectVo);
        if (!CollectionUtils.isEmpty(nameSysComponents)) {
            boolean isNameExist;
            isNameExist = nameSysComponents.stream()
                    .anyMatch(SysComponent::getStatus);
            if (isNameExist) {
                throw new GafException("组件名称已存在");
            }
        }
    }
}
