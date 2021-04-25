/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.sys.mgt.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.supermap.gaf.authority.commontype.SysComponent;
import com.supermap.gaf.authority.constant.DbFieldNameConstant;
import com.supermap.gaf.authority.enums.NodeTypeEnum;
import com.supermap.gaf.authority.vo.SelectOptionVo;
import com.supermap.gaf.authority.vo.TreeNode;
import com.supermap.gaf.commontypes.Page;
import com.supermap.gaf.commontypes.ShiroUser;
import com.supermap.gaf.exception.GafException;
import com.supermap.gaf.shiro.SecurityUtilsExt;
import com.supermap.gaf.sys.mgt.commontype.SysCatalog;
import com.supermap.gaf.sys.mgt.dao.SysCatalogMapper;
import com.supermap.gaf.sys.mgt.enums.BizTypeEnum;
import com.supermap.gaf.sys.mgt.enums.CatalogTypeEnum;
import com.supermap.gaf.sys.mgt.service.SysCatalogService;
import com.supermap.gaf.data.access.commontypes.ExtendSortSnParam;
import com.supermap.gaf.data.access.dao.BatchSortAndCodeMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

import static com.supermap.gaf.authority.util.TreeConvertUtil.ROOT_PARENT_ID;

/**
 * 目录服务实现类
 * @author wangxiaolong
 * @date:2021/3/25
 *
 */
@Service
public class SysCatalogServiceImpl implements SysCatalogService {

    private final SysCatalogMapper sysCatalogMapper;

    private final BatchSortAndCodeMapper batchSortAndCodeMapper;

    public SysCatalogServiceImpl(SysCatalogMapper sysCatalogMapper, BatchSortAndCodeMapper batchSortAndCodeMapper) {
        this.sysCatalogMapper = sysCatalogMapper;
        this.batchSortAndCodeMapper = batchSortAndCodeMapper;
    }

    @Override
    public SysCatalog getById(String catalogId) {
        if (catalogId == null) {
            throw new IllegalArgumentException("catalogId不能为空");
        }
        return sysCatalogMapper.select(catalogId);
    }



    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean insertSysCatalog(SysComponent sysComponent, CatalogTypeEnum catalogType) {
        SysCatalog build = SysCatalog.builder()
                .parentId(ROOT_PARENT_ID)
                .type(catalogType.getValue())
                .sysComponentId(sysComponent.getSysComponentId())
                .name(sysComponent.getNameCn())
                .status(true)
                .description(sysComponent.getNameCn() + "默认根目录")
                .build();
        this.insertSysCatalog(build);
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public SysCatalog insertSysCatalog(SysCatalog sysCatalog) {
        if (sysCatalog.getSysComponentId() != null && sysCatalog.getTenantId() != null) {
            throw new GafException("插入目录异常，目录数据不能同时包含组件id和租户id");
        }
        sysCatalog.setCatalogId(UUID.randomUUID().toString());

        SysCatalog.SysCatalogBuilder queryBuilder = SysCatalog.builder().type(sysCatalog.getType()).parentId(sysCatalog.getParentId()).status(true);
        if ( CatalogTypeEnum.ROLE_GROUP_TYPE.getValue().equals(sysCatalog.getType()) ) {
            ShiroUser shiroUser = SecurityUtilsExt.getUser();
            String tenantId = Objects.requireNonNull(shiroUser).getTenantId();
            queryBuilder.tenantId(tenantId);
        }
        Integer sameLevelCount = sysCatalogMapper.countByCombination(queryBuilder.build());
        if (sameLevelCount <= 0) {
            sysCatalog.setSortSn(1);
        } else if(sysCatalog.getSortSn() == null || sysCatalog.getSortSn() > sameLevelCount + 1) {
            sysCatalog.setSortSn(sameLevelCount + 1);
        }
        if (!ROOT_PARENT_ID.equalsIgnoreCase(sysCatalog.getParentId())) {
            SysCatalog parentCatalog = sysCatalogMapper.selectByIdAndStatus(sysCatalog.getParentId(), true);
            if (parentCatalog == null) {
                throw new GafException("未找到上级目录");
            }
            if (parentCatalog.getTenantId() != null) {
                sysCatalog.setTenantId(parentCatalog.getTenantId());
            }
            if (parentCatalog.getSysComponentId() != null) {
                sysCatalog.setSysComponentId(parentCatalog.getSysComponentId());
            }
            if (!StringUtils.isEmpty(parentCatalog.getBizTypeCode())) {
                sysCatalog.setBizTypeCode(parentCatalog.getBizTypeCode());
            }
        }
        if (sysCatalog.getStatus() == null) {
            sysCatalog.setStatus(true);
        }
        sysCatalogMapper.insert(sysCatalog);
        ExtendSortSnParam baseSortSnParam = getBaseSortSnParam(sysCatalog);
        batchSortAndCodeMapper.revisionSortSnMutiCondition(baseSortSnParam);
        return sysCatalog;
    }

    private ExtendSortSnParam getBaseSortSnParam(SysCatalog sysCatalog) {
        ExtendSortSnParam extendSortSnParam = new ExtendSortSnParam();
        extendSortSnParam.setTableName(DbFieldNameConstant.SYS_CATALOG);
        extendSortSnParam.setIdFieldName(DbFieldNameConstant.CATALOG_ID);
        extendSortSnParam.setLogicDeleteFieldName(DbFieldNameConstant.STATUS);
        extendSortSnParam.setParentIdFieldName(DbFieldNameConstant.PARENT_ID);
        extendSortSnParam.setSortSnFieldName(DbFieldNameConstant.SORT_SN);
        extendSortSnParam.setUpdatedTimeFieldName(DbFieldNameConstant.UPDATED_TIME);
        extendSortSnParam.setParentId(sysCatalog.getParentId());
        if(ROOT_PARENT_ID.equalsIgnoreCase(sysCatalog.getParentId())) {
            List<String> conditions = new LinkedList<>();
            conditions.add(DbFieldNameConstant.TYPE + " = '" + sysCatalog.getType()+"'");
            if(CatalogTypeEnum.ROLE_GROUP_TYPE.getValue().equals(sysCatalog.getType())) {
                ShiroUser shiroUser = SecurityUtilsExt.getUser();
                String tenantId = Objects.requireNonNull(shiroUser).getTenantId();
                conditions.add(DbFieldNameConstant.TENANT_ID + " = '" + tenantId + "'");
            }
            extendSortSnParam.setConditions(conditions);
        }
        return extendSortSnParam;
    }


    @Override
    public void batchInsert(List<SysCatalog> sysCatalogs) {
        if (sysCatalogs != null && sysCatalogs.size() > 0) {
            Set<String> parentIds = new HashSet<>();
            List<ExtendSortSnParam> extendSortSnParamList = new LinkedList<>();
            sysCatalogs.forEach(sysCatalog -> {
                sysCatalog.setCatalogId(UUID.randomUUID().toString());
                if(!parentIds.contains(sysCatalog.getParentId())) {
                    parentIds.add(sysCatalog.getParentId());
                    extendSortSnParamList.add(getBaseSortSnParam(sysCatalog));
                    // 暂时支持同一种类型 同一租户的目录批量添加后的修复排序
                }
            });
            sysCatalogMapper.batchInsert(sysCatalogs);
            for (ExtendSortSnParam extendSortSnParam : extendSortSnParamList) {
                batchSortAndCodeMapper.revisionSortSnMutiCondition(extendSortSnParam);
            }

        }

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteSysCatalog(SysComponent sysComponent,CatalogTypeEnum catalogType) {
        SysCatalog queryCatalog = SysCatalog.builder().parentId(ROOT_PARENT_ID).sysComponentId(sysComponent.getSysComponentId()).type(catalogType.getValue()).status(true).build();
        List<SysCatalog> sysCatalogs = this.sysCatalogMapper.selectByCombination(queryCatalog);
        if (sysCatalogs.size() != 1) {
            return false;
        }
        SysCatalog sysCatalog = sysCatalogs.get(0);
        this.deleteSysCatalog(sysCatalog.getCatalogId());
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public SysCatalog deleteSysCatalog(String catalogId) {
        SysCatalog needLogicDelete = sysCatalogMapper.selectByIdAndStatus(catalogId, true);
        if ( needLogicDelete == null) {
            throw new GafException("找不到该目录");
        }
        Set<String> all = new HashSet<>();
        all.add(catalogId);
        Set<String> parentIds = all;
        do {
            List<SysCatalog> sysCatalogs = sysCatalogMapper.selectByParentIdsAndStatus(parentIds,true);
            if (sysCatalogs != null && sysCatalogs.size() > 0) {
                Set<@NotNull String> collect = sysCatalogs.stream().map(SysCatalog::getCatalogId).collect(Collectors.toSet());
                all.addAll(collect);
                parentIds = collect;
            } else {
                break;
            }
        } while (true);
        sysCatalogMapper.updateStatusBatch(all, false);

        ExtendSortSnParam baseSortSnParam = getBaseSortSnParam(needLogicDelete);
        batchSortAndCodeMapper.revisionSortSnMutiCondition(baseSortSnParam);
        return needLogicDelete;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void batchDelete(List<String> catalogIds) {
        for (String catalogId : catalogIds) {
            deleteSysCatalog(catalogId);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateSysCatalog(SysComponent sysComponent,CatalogTypeEnum catalogType) {
        SysCatalog queryCatalog = SysCatalog.builder().parentId(ROOT_PARENT_ID).sysComponentId(sysComponent.getSysComponentId()).type(catalogType.getValue()).status(true).build();
        List<SysCatalog> sysCatalogs = this.sysCatalogMapper.selectByCombination(queryCatalog);
        if (sysCatalogs.size() != 1) {
            return false;
        }
        SysCatalog sysCatalog = sysCatalogs.get(0);
        if (sysComponent.getNameCn() != null && sysComponent.getNameCn().equals(sysCatalog.getName())) {
            return true;
        }
        sysCatalog.setName(sysComponent.getNameCn());
        this.updateSysCatalog(sysCatalog);
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public SysCatalog updateSysCatalog(SysCatalog sysCatalog) {
        SysCatalog current = sysCatalogMapper.selectByIdAndStatus(sysCatalog.getCatalogId(), true);
        if (current == null || !current.getStatus()) {
            throw new GafException("找不到目录");
        }
        sysCatalogMapper.update(sysCatalog);
        if(!Objects.equals(current.getParentId(), sysCatalog.getParentId())) {
            ExtendSortSnParam baseSortSnParam = getBaseSortSnParam(current);
            batchSortAndCodeMapper.revisionSortSnMutiCondition(baseSortSnParam);
            ExtendSortSnParam extendSortSnParam = getBaseSortSnParam(sysCatalog);
            batchSortAndCodeMapper.revisionSortSnMutiCondition(extendSortSnParam);
        } else if(!Objects.equals(sysCatalog.getSortSn(), current.getSortSn()) ) {
            ExtendSortSnParam baseSortSnParam = getBaseSortSnParam(current);
            baseSortSnParam.setOldSortSn(current.getSortSn());
            baseSortSnParam.setCurSortSn(sysCatalog.getSortSn());
            batchSortAndCodeMapper.revisionSortSnMutiCondition(baseSortSnParam);
        }
        return sysCatalogMapper.selectByIdAndStatus(sysCatalog.getCatalogId(),true);
    }




    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<String> getParentPath(String catalogId) {
        List<String> parentPath = new LinkedList<>();
        SysCatalog sysCatalog = this.sysCatalogMapper.selectByIdAndStatus(catalogId, true);
        if (sysCatalog == null || ROOT_PARENT_ID.equalsIgnoreCase(sysCatalog.getParentId())) {
            parentPath.add(0, ROOT_PARENT_ID);
            return parentPath;
        }
        String parentId = sysCatalog.getParentId();
        while (true) {
            SysCatalog parentCatalog = this.sysCatalogMapper.selectByIdAndStatus(parentId, true);
            if (parentCatalog == null) {
                throw new RuntimeException("找不到上级目录");
            } else if (ROOT_PARENT_ID.equals(parentCatalog.getParentId())) {
                parentPath.add(0, parentCatalog.getCatalogId());
                break;
            } else {
                parentPath.add(0, parentCatalog.getCatalogId());
                parentId = parentCatalog.getParentId();
            }
        }
        return parentPath;
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<TreeNode> getNodes(CatalogTypeEnum type, String tenantId) {
        SysCatalog.SysCatalogBuilder queryCondition = SysCatalog.builder().status(true).type(type.getValue());
        if (tenantId != null) {
            queryCondition.tenantId(tenantId);
        }
        List<SysCatalog> sysCatalogs = this.sysCatalogMapper.selectByCombination(queryCondition.build());
        List<TreeNode> nodeList = new LinkedList<>();
        if (sysCatalogs.size() == 0) {
            return nodeList;
        }
        Map<@NotNull String, SysCatalog> map = sysCatalogs.stream().collect(Collectors.toMap(
                SysCatalog::getCatalogId, sysCatalog -> sysCatalog
        ));
        for (SysCatalog current : sysCatalogs) {
            String parentId = current.getParentId();
            if (!ROOT_PARENT_ID.equals(parentId)) {
                TreeNode node = new TreeNode();
                node.setKey(current.getCatalogId());
                node.setTitle(current.getName());
                node.setType(NodeTypeEnum.CATALOG.getValue());
                node.setSortSn(current.getSortSn());
                SysCatalog parent = map.get(parentId);
                if (parent != null && ROOT_PARENT_ID.equals(parent.getParentId())) {
                    if (parent.getTenantId() != null) {
                        node.setParentId(parent.getTenantId());
                    } else {
                        node.setParentId(parent.getSysComponentId());
                    }
                } else {
                    node.setParentId(current.getParentId());
                }
                nodeList.add(node);
            }
        }
        return nodeList;
    }
    @Override
    public List<TreeNode> getNodesByType(String type) {
        // 若没查到或者判断为平台管理员 则认为是平台级 若有租户则查询租户的所有分类目录
        SysCatalog querySysCatalog;
        if ( CatalogTypeEnum.ROLE_GROUP_TYPE.getValue().equals(type) ) {
            ShiroUser shiroUser = SecurityUtilsExt.getUser();
            String tenantId = Objects.requireNonNull(shiroUser).getTenantId();
            querySysCatalog = SysCatalog.builder().type(type).tenantId(tenantId).status(true).build();
        } else {
            querySysCatalog = SysCatalog.builder().type(type).status(true).build();
        }
        List<SysCatalog> sysCatalogList = sysCatalogMapper.selectByCombination(querySysCatalog);
        return sysCatalogList.stream().map(sysCatalog -> {
            TreeNode node = new TreeNode();
            node.setTitle(sysCatalog.getName());
            node.setSortSn(sysCatalog.getSortSn());
            node.setParentId(sysCatalog.getParentId());
            node.setType(NodeTypeEnum.CATALOG.getValue());
            node.setKey(sysCatalog.getCatalogId());
            return node;
        }).collect(Collectors.toList());
    }

    @Override
    public List<SysCatalog> getByCombination(SysCatalog queryCatalog) {
        return this.sysCatalogMapper.selectByCombination(queryCatalog);
    }

    @Override
    public List<SysCatalog> getByType(String type) {
        SysCatalog querySysCatalog;
        if ( CatalogTypeEnum.ROLE_GROUP_TYPE.getValue().equals(type) ) {
            // 若没查到或者判断为平台管理员 则认为是平台级 若有租户则查询租户的所有分类目录
            ShiroUser shiroUser = SecurityUtilsExt.getUser();
            String tenantId = Objects.requireNonNull(shiroUser).getTenantId();
            querySysCatalog = SysCatalog.builder().type(type).tenantId(tenantId).status(true).build();
        } else {
            querySysCatalog = SysCatalog.builder().type(type).status(true).build();
        }
        return sysCatalogMapper.selectByCombination(querySysCatalog);
    }

    @Override
    public List<SysCatalog> getByComponentAndType(String componentId, String type) {
        return sysCatalogMapper.getByComponentAndType(componentId,type);
    }

    @Override
    public Page<SysCatalog> listByPageCondition(String searchFieldName, String searchFieldValue, String orderFieldName, String orderMethod, Integer pageNum, Integer pageSize, CatalogTypeEnum catalogType) {
        SysCatalog.SysCatalogBuilder builder = SysCatalog.builder().status(true).type(catalogType.getValue()).parentId(ROOT_PARENT_ID);
        if (CatalogTypeEnum.ROLE_GROUP_TYPE.equals(catalogType)) {
            builder.tenantId(Objects.requireNonNull(SecurityUtilsExt.getUser()).getTenantId());
        }
        PageInfo<SysCatalog> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> sysCatalogMapper.selectByCondition(searchFieldName, searchFieldValue ,orderFieldName, orderMethod,builder.build()));
        Page<SysCatalog> page = new Page<>();
        page.setPageIndex(pageInfo.getPageNum());
        page.setPageSize(pageInfo.getPageSize());
        page.setTotal((int)pageInfo.getTotal());
        page.setContent(pageInfo.getList());
        page.calculateTotalPage();
        return page;
    }

    @Override
    public List<TreeNode> getTreeByCatalogId(String catalogId) {
        List<TreeNode> all = new LinkedList<>();
        SysCatalog sysCatalog = this.sysCatalogMapper.selectByIdAndStatus(catalogId, true);
        if (sysCatalog != null) {
            TreeNode node = new TreeNode();
            node.setKey(sysCatalog.getCatalogId());
            node.setTitle(sysCatalog.getName());
            node.setType(NodeTypeEnum.CATALOG.getValue());
            node.setSortSn(1);
            node.setParentId(ROOT_PARENT_ID);
            all.add(node);
            Set<String> parentIds = new HashSet<>();
            parentIds.add(sysCatalog.getCatalogId());
            while (true) {
                List<SysCatalog> sysCatalogs = this.sysCatalogMapper.selectByParentIdsAndStatus(parentIds, true);
                if (sysCatalogs.isEmpty()) {
                    break;
                }
                Set<String> catalogIds = new HashSet<>(sysCatalogs.size());
                sysCatalogs.forEach(catalog -> {
                    catalogIds.add(catalog.getCatalogId());
                    TreeNode treeNode = new TreeNode();
                    treeNode.setKey(catalog.getCatalogId());
                    treeNode.setTitle(catalog.getName());
                    treeNode.setType(NodeTypeEnum.CATALOG.getValue());
                    treeNode.setSortSn(catalog.getSortSn());
                    treeNode.setParentId(catalog.getParentId());
                    all.add(treeNode);
                });
                parentIds = catalogIds;
            }
        }
        return all;
    }

    @Override
    public List<SelectOptionVo> getBizTypes() {
        BizTypeEnum[] values = BizTypeEnum.values();
        List<SelectOptionVo> optionVos = new LinkedList<>();
        for (BizTypeEnum value: values) {
            SelectOptionVo selectOptionVo = new SelectOptionVo();
            selectOptionVo.setKey(value.getValue());
            selectOptionVo.setValue(value.getValue());
            selectOptionVo.setLabel(value.getName());
            optionVos.add(selectOptionVo);
        }
        return optionVos;
    }

    @Override
    public List<SysCatalog> getCatalogTreeListByRootId(String rootCatalogId){
        return sysCatalogMapper.getCatalogTreeListByRootId(rootCatalogId);
    }


}
