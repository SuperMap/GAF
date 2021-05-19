/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.sys.mgt.service.impl;


import com.supermap.gaf.authority.constant.CacheGroupConstant;
import com.supermap.gaf.authority.constant.CommonConstant;
import com.supermap.gaf.authority.constant.DbFieldNameConstant;
import com.supermap.gaf.authority.enums.NodeTypeEnum;
import com.supermap.gaf.authority.util.TreeConvertUtil;
import com.supermap.gaf.authority.vo.TreeNode;
import com.supermap.gaf.commontypes.Page;
import com.supermap.gaf.exception.GafException;
import com.supermap.gaf.shiro.SecurityUtilsExt;
import com.supermap.gaf.shiro.commontypes.ShiroUser;
import com.supermap.gaf.sys.mgt.commontype.SysDict;
import com.supermap.gaf.sys.mgt.dao.SysDictMapper;
import com.supermap.gaf.sys.mgt.enums.CatalogTypeEnum;
import com.supermap.gaf.sys.mgt.model.DictData;
import com.supermap.gaf.sys.mgt.model.DictDataNode;
import com.supermap.gaf.sys.mgt.model.DictType;
import com.supermap.gaf.sys.mgt.service.SysCatalogService;
import com.supermap.gaf.sys.mgt.service.SysDictService;
import com.supermap.gaf.sys.mgt.vo.SysDictSelectVo;
import com.supermap.gaf.data.access.commontypes.RevisionSortSnParam;
import com.supermap.gaf.data.access.dao.BatchSortAndCodeMapper;
import com.supermap.gaf.utils.TreeUtil;
import org.springframework.aop.framework.AopContext;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * 字典服务实现类
 * @author wangxiaolong 
 * @date 2021-01-08
 */
@Service
public class SysDictServiceImpl implements SysDictService {
    private final SysDictMapper sysDictMapper;

    private final SysCatalogService sysCatalogService;

    private final BatchSortAndCodeMapper batchSortAndCodeMapper;

    public SysDictServiceImpl(SysDictMapper sysDictMapper, SysCatalogService sysCatalogService, BatchSortAndCodeMapper batchSortAndCodeMapper) {
        this.sysDictMapper = sysDictMapper;
        this.sysCatalogService = sysCatalogService;
        this.batchSortAndCodeMapper = batchSortAndCodeMapper;
    }


    @Override
    @Cacheable(value = CacheGroupConstant.DICT_DATA, key = "#dictTypeCode", unless = "#result == null")
    public List<DictData> getDictData(String dictTypeCode) {
        SysDictSelectVo selectVo = new SysDictSelectVo();
        selectVo.setPid(TreeConvertUtil.ROOT_PARENT_ID);
        selectVo.setDictCode(dictTypeCode);
        List<SysDict> dictTypes = sysDictMapper.selectList(selectVo);
        if(dictTypes.size() == 0) {
            throw new GafException("字典类别"+ dictTypeCode+"不存在");
        }
        SysDict sysDictType = dictTypes.get(0);
        SysDictSelectVo queryVo = new SysDictSelectVo();
        queryVo.setPid(sysDictType.getDataDictId());
        List<SysDict> dicts = sysDictMapper.selectList(queryVo);
        return dicts.stream().map(sysDict -> {
            DictData dictData = new DictData();
            dictData.setKey(sysDict.getDataDictId());
            dictData.setDictTypeCode(sysDict.getDictCode());
            dictData.setLabel(sysDict.getDictName());
            dictData.setValue(sysDict.getDictValue());
            dictData.setKey(sysDict.getDataDictId());
            dictData.setDictTypeCode(sysDict.getDictCode());
            dictData.setLabel(sysDict.getDictName());
            dictData.setValue(sysDict.getDictValue());
            dictData.setDictDesc(sysDict.getDictDesc());
            dictData.setSeq(sysDict.getSeq());
            dictData.setVisibility(sysDict.getVisibility());
            dictData.setExtProperties(sysDict.getExtProperties());
            return dictData;
        }).sorted(Comparator.comparingInt(DictData::getSeq)).collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = CacheGroupConstant.DICT_DATA_TREE, key = "#dictTypeCode", unless = "#result == null")
    public List<DictDataNode> getDictDataTree(String dictTypeCode) {
        SysDictSelectVo selectVo = new SysDictSelectVo();
        selectVo.setDictCode(dictTypeCode);
        selectVo.setPid(TreeConvertUtil.ROOT_PARENT_ID);
        List<SysDict> sysDicts = sysDictMapper.selectList(selectVo);
        if(sysDicts.size() == 0) {
            return null;
        }
        SysDict dictType = sysDicts.get(0);
        return getDictDataTree(dictType.getDictCode(),dictType.getDataDictId());
    }


    @Override
    public List<DictDataNode> getDictDataTree(String dictTypeCode, String dictValue, int level, boolean onlyVisible) {
        if (dictTypeCode == null) {
            throw new IllegalArgumentException("[Assertion failed] - this argument is required, named dictionary category code; it must not be null");
        }
        if(level < 0) {
            throw new IllegalArgumentException("the level cannot be less than 0");
        }
        List<DictDataNode> all = ((SysDictService)AopContext.currentProxy()).getDictDataTree(dictTypeCode);
        if(all == null || all.size() == 0) {
            return null;
        }
        if(onlyVisible) {
            TreeUtil.deepFirstTraverseTree(all,(dictDataNode, iterator) -> {
                Boolean visibility = dictDataNode.getVisibility();
                if(!visibility) {
                    iterator.remove();
                    return TreeUtil.VisitResult.SKIP_SUBTREE;
                }
                return TreeUtil.VisitResult.CONTINUE;
            });
            if(all.size() == 0) {
                return null;
            }
        }
        if(dictValue == null) {
            if(level != 0) {
                TreeUtil.deepFirstTraverseTree(all, (dictDataNode, currentLevel) -> {
                    dictDataNode.setLevel(currentLevel);
                    if(currentLevel.equals(level)) {
                        dictDataNode.setChildren(null);
                        return TreeUtil.VisitResult.SKIP_SUBTREE;
                    }
                    return TreeUtil.VisitResult.CONTINUE;
                },0);
            }
            return all;
        }
        DictDataNode node = TreeUtil.getTreeNode(all, dictDataNode -> Objects.equals(dictDataNode.getValue(), dictValue));
        if(node == null) {
            return null;
        }
        if(level != 0) {
            TreeUtil.deepFirstTraverseTree(node.getChildren(), (dictDataNode, currentLevel) -> {
                dictDataNode.setLevel(currentLevel);
                if(currentLevel.equals(level)) {
                    dictDataNode.setChildren(null);
                    return TreeUtil.VisitResult.SKIP_SUBTREE;
                }
                return TreeUtil.VisitResult.CONTINUE;
            },0);
        }
        return node.getChildren();
    }

    @Override
    public List<DictDataNode> getPath(String dictTypeCode, String value) {
        List<DictDataNode> dictDataTree = ((SysDictService) AopContext.currentProxy()).getDictDataTree(dictTypeCode);
        if(dictDataTree== null) {
            return null;
        }
        List<DictDataNode> path = new LinkedList<>();
        DictDataNode hit = TreeUtil.getPath(dictDataTree, dictDataNode -> Objects.equals(dictDataNode.getValue(),value),path);
        return hit == null? null: path;
    }

    private List<DictDataNode> getDictDataTree(String dictTypeCode, String dictTypeId) {
        List<SysDict> sysDictList = sysDictMapper.selectChildren(dictTypeCode,dictTypeId);
        List<DictDataNode> dictDataNodeList = sysDictList.stream().map(sysDict -> {
            DictDataNode dictDataNode = new DictDataNode();
            dictDataNode.setKey(sysDict.getDataDictId());
            dictDataNode.setDictTypeCode(sysDict.getDictCode());
            dictDataNode.setParentId(sysDict.getPid());
            dictDataNode.setLabel(sysDict.getDictName());
            dictDataNode.setValue(sysDict.getDictValue());
            dictDataNode.setDictDesc(sysDict.getDictDesc());
            dictDataNode.setSeq(sysDict.getSeq());
            dictDataNode.setVisibility(sysDict.getVisibility());
            dictDataNode.setExtProperties(sysDict.getExtProperties());
            return dictDataNode;
        }).collect(Collectors.toList());
        DictDataNode rootParent = new DictDataNode();
        rootParent.setKey(dictTypeId);
        return TreeUtil.getChildren(rootParent, dictDataNodeList, Comparator.comparingInt(DictData::getSeq));
    }


    @Override
    public SysDict getById(String dataDictId){
        if(dataDictId == null){
            throw new IllegalArgumentException("dataDictId不能为空");
        }
        return  sysDictMapper.select(dataDictId);
    }

	@Override
    public Page<DictDataNode> listByPageCondition(SysDictSelectVo sysDictSelectVo, int pageNum, int pageSize) {
        List<DictDataNode> dictDataNodeTreeList = ((SysDictService) AopContext.currentProxy()).getDictDataTree(sysDictSelectVo.getDictCode());
        if(dictDataNodeTreeList == null) {
            Page<DictDataNode> page = new Page<>();
            if(pageSize <= 0) {
                pageSize = 10;
            }
            page.setPageSize(pageSize);
            page.setTotal(0);
            page.calculateTotalPage();
            if(pageNum > page.getTotalPage()) {
                pageNum = page.getTotalPage();
            }
            if(pageNum<1) {
                pageNum = 1;
            }
            page.setPageIndex(pageNum);
            page.setContent(null);
            return page;
        }
        String dictName = sysDictSelectVo.getSearchFieldValue();
        if(!StringUtils.isEmpty(dictName)  && dictDataNodeTreeList.size() > 0) {
            Iterator<DictDataNode> iterator = dictDataNodeTreeList.iterator();
            while (iterator.hasNext()) {
                DictDataNode current = iterator.next();
                final AtomicBoolean hasName = new AtomicBoolean(false);
                if(current.getLabel().contains(dictName)) {
                    hasName.set(true);
                } else {
                    TreeUtil.deepFirstTraverseTree(current.getChildren(), (dictDataNode, currentLevel) -> {
                        if(dictDataNode.getLabel().contains(dictName)) {
                            hasName.set(true);
                            return TreeUtil.VisitResult.TERMINATE;
                        }
                        return TreeUtil.VisitResult.CONTINUE;
                    },0);
                }
                if (!hasName.get()) {
                    iterator.remove();
                }
            }
        }
        if(!StringUtils.isEmpty(sysDictSelectVo.getOrderFieldName()) &&
            !StringUtils.isEmpty(sysDictSelectVo.getOrderMethod())) {
            if(CommonConstant.DESC.equalsIgnoreCase(sysDictSelectVo.getOrderMethod())) {
                dictDataNodeTreeList.sort((o1, o2) -> o2.getSeq().compareTo(o1.getSeq()));
            } else {
                dictDataNodeTreeList.sort(Comparator.comparing(DictDataNode::getSeq));
            }
        }

        Page<DictDataNode> page = new Page<>();
        if(pageSize <= 0) {
            pageSize = 10;
        }
        page.setPageSize(pageSize);
        page.setTotal(dictDataNodeTreeList.size());
        page.calculateTotalPage();
        if(pageNum > page.getTotalPage()) {
            pageNum = page.getTotalPage();
        }
        if(pageNum<1) {
            pageNum = 1;
        }
        page.setPageIndex(pageNum);
        int startIndex = (pageNum-1)*pageSize;
        int endIndex =  pageNum*pageSize;
        List<DictDataNode> pageResult = new LinkedList<>();
        for (int i = 0; i < dictDataNodeTreeList.size(); i++) {
            if(i >= startIndex && i< endIndex) {
                pageResult.add(dictDataNodeTreeList.get(i));
            } else if (i >= endIndex) {
                break;
            }
        }
        page.setContent(pageResult);
        return page;
    }


    @Transactional(rollbackFor = Exception.class)
	@Override
    @CacheEvict(value = {CacheGroupConstant.DICT_DATA,CacheGroupConstant.DICT_DATA_TREE }, key = "#sysDict.dictCode")
    public SysDict insertSysDict(SysDict sysDict){
        // 字典类别 或 字典值重名校验
        checkName(sysDict);

		sysDict.setDataDictId(UUID.randomUUID().toString());
		ShiroUser shiroUser = SecurityUtilsExt.getUser();
		sysDict.setCreatedBy(Objects.requireNonNull(shiroUser).getAuthUser().getName());
		sysDict.setUpdatedBy(shiroUser.getAuthUser().getName());
        sysDictMapper.insert(sysDict);

        // 更新排序
        RevisionSortSnParam baseSortSnParam = getBaseSortSnParam(sysDict);
        batchSortAndCodeMapper.revisionSortSn(baseSortSnParam);
        return getById(sysDict.getDataDictId());
    }

    private void checkName(SysDict sysDict) {
        SysDictSelectVo selectVo = new SysDictSelectVo();
        selectVo.setDictCode(sysDict.getDictCode());
        if(TreeConvertUtil.ROOT_PARENT_ID.equals(sysDict.getPid())) {
            // 若为字典类别时 例如表示 长度单位
            selectVo.setPid(sysDict.getPid());
            List<SysDict> sysDicts = sysDictMapper.selectList(selectVo);
            if(sysDicts.size() > 0) {
                throw new GafException("字典类别英文重复");
            }
        } else {
            // 若为字典数据时 例如表示 米,千米
            selectVo.setDictValue(sysDict.getDictValue());
            List<SysDict> sysDicts = sysDictMapper.selectList(selectVo);
            if(sysDicts.size() > 0) {
                throw new GafException("字典数据字典值重复");
            }
        }

    }

    /**
     * 获取基本的更新排序的参数
     * @return 更新排序参数
     */
    private RevisionSortSnParam getBaseSortSnParam(SysDict sysDict) {
        RevisionSortSnParam sortSnParam = new RevisionSortSnParam();
        sortSnParam.setIdFieldName(DbFieldNameConstant.DATA_DICT_ID);
        sortSnParam.setLogicDeleteFieldName(DbFieldNameConstant.STATUS);
        sortSnParam.setTableName(DbFieldNameConstant.SYS_DICT);
        sortSnParam.setUpdatedTimeFieldName(DbFieldNameConstant.UPDATED_TIME);
        sortSnParam.setSortSnFieldName(DbFieldNameConstant.SEQ);
        if(TreeConvertUtil.ROOT_PARENT_ID.equals(sysDict.getPid()) || sysDict.getPid() == null) {
            // 若为字典类别时 例如表示 长度单位
            sortSnParam.setParentIdFieldName(DbFieldNameConstant.CATALOG_ID);
            sortSnParam.setParentId(sysDict.getCatalogId());
        } else {
            // 若为字典数据时 例如表示 米,千米
            sortSnParam.setParentIdFieldName(DbFieldNameConstant.PID);
            sortSnParam.setParentId(sysDict.getPid());
        }
        return sortSnParam;
    }

    @Transactional(rollbackFor = Exception.class)
	@Override
    public void batchInsert(List<SysDict> sysDicts){
		if (sysDicts != null && sysDicts.size() > 0) {
	        sysDicts.forEach(sysDict -> {
				sysDict.setDataDictId(UUID.randomUUID().toString());
				ShiroUser shiroUser = SecurityUtilsExt.getUser();
				sysDict.setCreatedBy(Objects.requireNonNull(shiroUser).getAuthUser().getName());
				sysDict.setUpdatedBy(shiroUser.getAuthUser().getName());
            });
            sysDictMapper.batchInsert(sysDicts);
        }
        
    }

    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = {CacheGroupConstant.DICT_DATA,CacheGroupConstant.DICT_DATA_TREE }, key = "#sysDict.dictCode")
    @Override
    public SysDict deleteSysDict(SysDict sysDict) {
        if(TreeConvertUtil.ROOT_PARENT_ID.equals(sysDict.getPid()) || sysDict.getPid() == null) {
            // 若为字典类别
            SysDictSelectVo selectVo = new SysDictSelectVo();
            selectVo.setDictCode(sysDict.getDictCode());
            List<SysDict> sysDicts = sysDictMapper.selectList(selectVo);
            List<String> ids = sysDicts.stream().map(SysDict::getDataDictId).collect(Collectors.toList());
            sysDictMapper.batchDelete(ids);
        } else {
            // 若为字典数据
            List<String> parentIds = new LinkedList<>();
            parentIds.add(sysDict.getDataDictId());
            List<String> deleteIds = new LinkedList<>(parentIds);
            while (true) {
                List<SysDict> children = sysDictMapper.selectByParentIds(parentIds);
                if(children.size() > 0) {
                    parentIds = children.stream().map(SysDict::getDataDictId).collect(Collectors.toList());
                    deleteIds.addAll(parentIds);
                } else {
                    break;
                }
            }
            sysDictMapper.batchDelete(deleteIds);
        }
        batchSortAndCodeMapper.revisionSortSn(getBaseSortSnParam(sysDict));
        return sysDict;
    }

    @Transactional(rollbackFor = Exception.class)
	@Override
    public SysDict deleteSysDict(String dataDictId){
        SysDict sysDict = sysDictMapper.select(dataDictId);
        if(Objects.isNull(sysDict)) {
            return null;
        }
        return ((SysDictService) AopContext.currentProxy()).deleteSysDict(sysDict);
    }

    @Transactional(rollbackFor = Exception.class)
	@Override
    public void batchDelete(List<String> dataDictIds){
        if(dataDictIds!= null && dataDictIds.size() > 0) {
            for (String dataDictId : dataDictIds) {
                deleteSysDict(dataDictId);
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
	@Override
    @CacheEvict(value = {CacheGroupConstant.DICT_DATA,CacheGroupConstant.DICT_DATA_TREE }, key = "#sysDict.dictCode")
    public SysDict updateSysDict(SysDict sysDict){
        SysDict old = getById(sysDict.getDataDictId());
        if(old == null) {
            throw new GafException("未找到该条数据");
        }
        if(!Objects.equals(old.getDictCode(), sysDict.getDictCode())
           ||!Objects.equals(old.getDictValue(), sysDict.getDictValue())) {
            checkName(sysDict);
        }
        ShiroUser shiroUser = SecurityUtilsExt.getUser();
        sysDict.setUpdatedBy(Objects.requireNonNull(shiroUser).getAuthUser().getName());
        if(sysDict.getExtProperties()!= null && "".equals(sysDict.getExtProperties().trim())) {
            sysDict.setExtProperties(null);
        }
        sysDictMapper.update(sysDict);
        if(!Objects.equals(sysDict.getCatalogId(), old.getCatalogId())) {
            RevisionSortSnParam baseSortSnParam = getBaseSortSnParam(old);
            batchSortAndCodeMapper.revisionSortSn(baseSortSnParam);
            RevisionSortSnParam sortSnParam = getBaseSortSnParam(sysDict);
            batchSortAndCodeMapper.revisionSortSn(sortSnParam);
        } else if(!Objects.equals(old.getSeq(),sysDict.getSeq())){
            RevisionSortSnParam baseSortSnParam = getBaseSortSnParam(sysDict);
            baseSortSnParam.setOldSortSn(old.getSeq());
            baseSortSnParam.setCurSortSn(sysDict.getSeq());
            batchSortAndCodeMapper.revisionSortSn(baseSortSnParam);
        }
        return getById(sysDict.getDataDictId());
    }

    @Override
    public List<TreeNode> listAllNodes() {
        List<TreeNode> result = new LinkedList<>();
        List<TreeNode> catalogNodes = sysCatalogService.getNodesByType(CatalogTypeEnum.DICTIONARY_GROUP_TYPE.getValue());
        SysDictSelectVo selectVo = new SysDictSelectVo();
        selectVo.setPid(TreeConvertUtil.ROOT_PARENT_ID);
        List<SysDict> sysDicts = sysDictMapper.selectList(selectVo);
        List<TreeNode> dictNodes = sysDicts.stream().map(sysDict -> {
            TreeNode node = new TreeNode();
            node.setTitle(sysDict.getDictName());
            node.setSortSn(sysDict.getSeq());
            node.setParentId(sysDict.getCatalogId());
            node.setType(NodeTypeEnum.DICTIONARY.getValue());
            node.setKey(sysDict.getDataDictId());
            return node;
        }).collect(Collectors.toList());
        result.addAll(catalogNodes);
        result.addAll(dictNodes);
        return result;
    }

    @Override
    public DictType getDictType(String dictTypeCode) {
        SysDictSelectVo selectVo = new SysDictSelectVo();
        selectVo.setPid(TreeConvertUtil.ROOT_PARENT_ID);
        selectVo.setDictCode(dictTypeCode);
        List<SysDict> dictTypes = sysDictMapper.selectList(selectVo);
        if (dictTypes.isEmpty()) {
            return null;
        }
        SysDict sysDict = dictTypes.get(0);
        DictType dictType = new DictType();
        dictType.setDictTypeId(sysDict.getDataDictId());
        dictType.setCatalogId(sysDict.getCatalogId());
        dictType.setDictTypeCode(sysDict.getDictCode());
        dictType.setDictTypeDesc(sysDict.getDictDesc());
        dictType.setDictTypeName(sysDict.getDictName());
        dictType.setSeq(sysDict.getSeq());
        return dictType;
    }

}
