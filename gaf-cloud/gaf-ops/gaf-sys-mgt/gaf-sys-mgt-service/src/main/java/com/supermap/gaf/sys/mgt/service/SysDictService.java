/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.sys.mgt.service;

import com.supermap.gaf.authority.vo.TreeNode;
import com.supermap.gaf.commontypes.Page;
import com.supermap.gaf.sys.mgt.commontype.SysDict;
import com.supermap.gaf.sys.mgt.model.DictData;
import com.supermap.gaf.sys.mgt.model.DictDataNode;
import com.supermap.gaf.sys.mgt.model.DictType;
import com.supermap.gaf.sys.mgt.vo.SysDictSelectVo;

import java.util.List;

/**
 * 字典服务类
 * @author wangxiaolong 
 * @date 2021-01-08
 */
public interface SysDictService {

    /**
     * 根据字典类别编码和是否只可见获取字典数据，
     * @param dictTypeCode 字典类别编码
     * @return 若未查询到则返回null
     */
    List<DictData> getDictData(String dictTypeCode);

    /**
     * 查询树形结构的字典数据
     * 根据字典类别编码查询树形结构的字典数据,若未查到则返回的字典数据为null
     * @param dictTypeCode 字典类别编码
     * @param dictValue 字典值, 表示从字典树的哪个节点获取子字典树，默认为null,表示直接获取字典类别下的子字典树
     * @param level 控制获取字典值对应字典数据的子字典树最大层级,默认值0表示获取不限制层级，获取字典值下的所有子字典树
     * @param onlyVisible 表示是否只可见，控制获取属性visible为true的字典值
     * @return 字典数据树节点集合
     */
    List<DictDataNode> getDictDataTree(String dictTypeCode, String dictValue, int level, boolean onlyVisible);

    /**
     * 根据字典类别编码获取字典树
     * @param dictTypeCode 字典类别编码
     * @return 若未查询到则返回null
     */
    List<DictDataNode> getDictDataTree(String dictTypeCode);

    /**
     * 查找路径
     * @param dictTypeCode 字典类别编码
     * @param value 字典值
     * @return  路径
     */
    List<DictDataNode> getPath(String dictTypeCode, String value);


    /**
     * 根据id查询字典
     * @param dataDictId 字典id
     * @return  字典 若未查询到则返回null
     */
    SysDict getById(String dataDictId);
	
	/**
     * 分页条件查询
     * @param sysDictSelectVo 查询条件
     * @param pageNum 当前页数
     * @param pageSize 页面大小
     * @return 分页对象
     */
	Page<DictDataNode> listByPageCondition(SysDictSelectVo sysDictSelectVo, int pageNum, int pageSize);


    /**
     * 新增字典
     * @param sysDict 字典
     * @return 字典
     */
    SysDict insertSysDict(SysDict sysDict);

    /**
     * 批量新增字典
     * @param sysDicts 字典集合
     */
    void batchInsert(List<SysDict> sysDicts);

    /**
     * 根据字典id删除字典
     * @param dataDictId  字典id
     * @return 字典
     */
    SysDict deleteSysDict(String dataDictId);

    /**
     * 条件删除字典
     * @param sysDict 查询条件
     * @return 字典
     */
    SysDict deleteSysDict(SysDict sysDict);
    /**
     * 批量删除
     * @param dataDictIds 字典id集合
     */
    void batchDelete(List<String> dataDictIds);

    /**
     * 更新字典
     * @param sysDict 字典
     * @return 字典
     */
    SysDict updateSysDict(SysDict sysDict);

    /**
     * 查询所有字典节点和目录节点
     * @return 若没有查询到节点则返回空集合
     */
    List<TreeNode> listAllNodes();

    /**
     * 根据字典类型编码查询字典类型
     * @param dictTypeCode  字典类型编码
     * @return 若为查询到则返回null
     */
    DictType getDictType(String dictTypeCode);
}
