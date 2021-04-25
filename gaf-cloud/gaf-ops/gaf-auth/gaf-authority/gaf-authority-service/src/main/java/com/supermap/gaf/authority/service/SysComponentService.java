/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.authority.service;

import com.supermap.gaf.authority.commontype.SysComponent;
import com.supermap.gaf.authority.vo.SelectOptionVo;
import com.supermap.gaf.authority.vo.SysComponentSelectVo;

import java.util.List;
import java.util.Map;

/**
 * 组件服务类
 * @date:2021/3/25
 * @author zhm
 */
public interface SysComponentService {

    /**
     * 根据id查询组件
     * @param sysComponentId 组件id
     * @return  组件 若未查询到则返回null
     */
    SysComponent getById(String sysComponentId);

    /**
     * 分页查询组件
     * @param sysComponentSelectVo 分页参数 如偏移量，每页条数
     * @return 分页结果 如查询到的总数 分页后的结果
     */
    Map<String,Object> pageList(SysComponentSelectVo sysComponentSelectVo);

    /**
     * 分页模糊查询组件
     * @param sysComponentSelectVo 分页参数 如偏移量，每页条数, 模糊查询字段名 字段值 排序字段名 排序方式
     * @return 分页结果 如查询到的总数 分页后的结果
     */
    Map<String,Object> searchList(SysComponentSelectVo sysComponentSelectVo);

    /**
     * 新增组件
     * @param sysComponent 组件
     * @return 组件
     */
    SysComponent insertSysComponent(SysComponent sysComponent);

    /**
     * 批量插入组件
     * @param sysComponents 组件集合
     */
    void batchInsert(List<SysComponent> sysComponents);

    /**
     * 根据组件id删除组件
     * @param sysComponentId  组件id
     * @return 组件
     */
    void deleteSysComponent(String sysComponentId);

    /**
     * 批量删除
     * @param sysComponentIds 组件id集合
     */
    void batchDelete(List<String> sysComponentIds);
    /**
     * 更新组件
     * @param sysComponent 组件
     * @return 组件
     */
    SysComponent updateSysComponent(SysComponent sysComponent);

    /**
     * 获取所有组件可选项
     * @return 所有组件可选项
     */
    List<SelectOptionVo> getAllOptions();

    /**
     * 单字段等值条件查询 若字段名为null则查询所有数据
     * @param fieldName 字段名
     * @param fieldValue 字段值
     * @return 组件集合
     */
    List<SysComponent> selectByOneField(String fieldName, String fieldValue);

}
