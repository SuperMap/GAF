/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.authority.service;

import com.supermap.gaf.authority.commontype.AuthResourceModule;
import com.supermap.gaf.authority.vo.AuthResourceModuleSelectVo;
import com.supermap.gaf.authority.vo.TreeNode;

import java.util.List;
import java.util.Map;

/**
 * 模块服务类
 *
 * @author zhm
 * @date:2021/3/25
 */
public interface AuthResourceModuleService {

    /**
     * 根据id查询模块
     *
     * @param resourceModuleId 模块id
     * @return 模块 若为查询到则返回null
     */
    AuthResourceModule getById(String resourceModuleId);

    /**
     * 获取模块树节点集合
     * 注意:未组织为树结构. 若未查询到则返回空集合。返回数据中type为12的表示分组目录节点，type为7表示模块树节点
     *
     * @return 模块树节点集合
     */
    List<TreeNode> getModuleNodes();

    /**
     * 分页查询模块
     *
     * @param authResourceModuleSelectVo 分页参数 如偏移量，每页条数
     * @return 分页结果 如查询到的总数 分页后的结果
     */
    Map<String, Object> pageList(AuthResourceModuleSelectVo authResourceModuleSelectVo);

    /**
     * 分页模糊查询模块
     *
     * @param authResourceModuleSelectVo 分页参数 如偏移量，每页条数, 模糊查询字段名 字段值 排序字段名 排序方式
     * @return 分页结果 如查询到的总数 分页后的结果
     */
    Map<String, Object> searchList(AuthResourceModuleSelectVo authResourceModuleSelectVo);

    /**
     * 新增模块
     *
     * @param authResourceModule 模块
     * @return 模块
     */
    AuthResourceModule insertAuthResourceModule(AuthResourceModule authResourceModule);

    /**
     * 批量新增模块
     *
     * @param authResourceModules 模块集合
     */
    void batchInsert(List<AuthResourceModule> authResourceModules);

    /**
     * 根据id删除模块
     *
     * @param resourceModuleId 模块id
     */
    void deleteAuthResourceModule(String resourceModuleId);

    /**
     * 根据模块id集合批量删除
     *
     * @param resourceModuleIds 模块id集合
     */
    void batchDelete(List<String> resourceModuleIds);

    /**
     * 更新模块
     *
     * @param authResourceModule 模块
     * @return 模块
     */
    AuthResourceModule updateAuthResourceModule(AuthResourceModule authResourceModule);

    /**
     * 查询某模块分组下模块的数量
     *
     * @param moduleCatalogId 模块分组目录id
     * @return 若未查询到则返回0
     */
    Integer countByModuleCatalogId(String moduleCatalogId);
}
