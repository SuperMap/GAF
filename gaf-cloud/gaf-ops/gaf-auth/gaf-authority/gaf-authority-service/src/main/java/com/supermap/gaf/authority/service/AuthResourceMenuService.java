/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.authority.service;

import com.supermap.gaf.authority.commontype.AuthResourceMenu;
import com.supermap.gaf.authority.commontype.AuthResourceMenuNode;
import com.supermap.gaf.authority.vo.AuthResourceMenuSelectVo;
import com.supermap.gaf.authority.vo.TreeNode;

import java.util.List;
import java.util.Map;

/**
 * 菜单服务类
 *
 * @author wxl
 * @date:2021/3/25
 */
public interface AuthResourceMenuService {

    /**
     * 根据id查询菜单
     *
     * @param resourceMenuId 菜单id
     * @return 菜单
     */
    AuthResourceMenu getById(String resourceMenuId);

    /**
     * 根据id查询菜单 能查到已被逻辑删除的
     *
     * @param resourceMenuId 菜单id
     * @return 菜单
     */
    AuthResourceMenu getRealById(String resourceMenuId);

    /**
     * 查询菜单树节点集合
     * 查询当前用户的菜单并转换为树节点（未组织为树结构）。若当前用户所属租户是平台管理员所属租户，则获取所有菜单
     *
     * @return 菜单树节点集合
     */
    List<TreeNode> getMenuNodes();

    /**
     * 分页查询菜单
     *
     * @param authResourceMenuSelectVo 分页参数 如偏移量，每页条数
     * @return 分页结果 如查询到的总数 分页后的结果
     */
    Map<String, Object> pageList(AuthResourceMenuSelectVo authResourceMenuSelectVo);

    /**
     * 分页模糊查询菜单
     *
     * @param authResourceMenuSelectVo 分页参数 如偏移量，每页条数, 模糊查询字段名 字段值 排序字段名 排序方式
     * @return 分页结果 如查询到的总数 分页后的结果
     */
    Map<String, Object> searchList(AuthResourceMenuSelectVo authResourceMenuSelectVo);

    /**
     * 新增菜单
     *
     * @param authResourceMenu 菜单
     * @return 菜单
     */
    AuthResourceMenu insertAuthResourceMenu(AuthResourceMenu authResourceMenu);

    /**
     * 批量插入菜单
     *
     * @param authResourceMenus 菜单集合
     */
    void batchInsert(List<AuthResourceMenu> authResourceMenus);

    /**
     * 根据id删除菜单
     *
     * @param resourceMenuId 菜单id
     */
    void deleteAuthResourceMenu(String resourceMenuId);

    /**
     * 根据id集合批量删除菜单
     *
     * @param resourceMenuIds 菜单id集合
     */
    void batchDelete(List<String> resourceMenuIds);

    /**
     * 更新菜单
     *
     * @param authResourceMenu 菜单
     * @return 菜单
     */
    AuthResourceMenu updateAuthResourceMenu(AuthResourceMenu authResourceMenu);


    /**
     * 通过id集合查询菜单
     *
     * @param addMenuIdList 菜单id集合
     * @return 若未查询到则返回空集合
     */
    List<AuthResourceMenu> listByIds(List<String> addMenuIdList);

    /**
     * 条件查询菜单树
     * @param name 菜单名
     * @return
     */
    List<AuthResourceMenuNode> getMenuTree(String name);
}
