/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.authority.service;

import com.supermap.gaf.authority.commontype.AuthRole;
import com.supermap.gaf.authority.vo.AuthRoleSelectVo;
import com.supermap.gaf.authority.vo.TreeNode;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 角色服务类
 * @date:2021/3/25
 * @author zhm
 */
public interface AuthRoleService {

    /**
     * 根据id查询角色
     * @param roleId 角色id
     * @return  角色 若未查询到则返回null
     */
    AuthRole getById(String roleId);

    /**
     * 查询角色树节点集合
     * 查询当前用户所属租户下的角色，然后转换为树节点。返回的数据中 ,节点类型type值为12表示角色组节点，type值为5表示角色节点。
     * 注意:查询到的角色包括平台内置角色和角色组。树节点集合未组织为树结构
     * @return 角色树节点集合
     */
    List<TreeNode> getRoleTree();

    /**
     * 查询角色树节点集合（不包含平台内置角色）
     * 查询当前用户所属租户下的角色，然后转换为树节点。返回的数据中 ,节点类型type值为12表示角色组节点，type值为5表示角色节点。
     * 注意:查询到的角色包括平台内置角色和角色组。树节点集合未组织为树结构
     * @return 角色树节点集合（不包含平台内置角色）
     */
    List<TreeNode> getRoleTreeWithOutInnerRole();

    /**
     * 查询角色组下角色
     * 根据角色组id条件查询角色组下的角色
     * @param roleCatalogId  角色组目录id
     * @return 角色集合 若未查询到则返回空集合
     */
    List<AuthRole> listByRoleCatalog(String roleCatalogId);

    /**
     * 分页查询角色
     * @param authRoleSelectVo 分页参数 如偏移量，每页条数
     * @return 分页结果 如查询到的总数 分页后的结果
     */
    Map<String, Object> pageList(AuthRoleSelectVo authRoleSelectVo);

    /**
     * 分页模糊查询角色
     * @param authRoleSelectVo 分页参数 如偏移量，每页条数, 模糊查询字段名 字段值 排序字段名 排序方式
     * @return 分页结果 如查询到的总数 分页后的结果
     */
    Map<String, Object> searchList(AuthRoleSelectVo authRoleSelectVo);

    /**
     * 新增角色
     * @param authRole 角色
     * @return 角色
     */
    AuthRole insertAuthRole(AuthRole authRole);

    /**
     * 批量插入角色
     * @param authRoles 角色集合
     */
    void batchInsert(List<AuthRole> authRoles);

    /**
     * 删除某分组目录下的某角色
     * @param roleCatalogId 分组目录id
     * @param roleId  角色id
     */
    void deleteAuthRole(String roleCatalogId, String roleId);

    /**
     * 批量删除角色
     * 根据角色组id和角色id集合批量删除角色
     * @param roleCatalogId  角色组目录id
     * @param roleIds  角色id集合
     */
    void batchDelete(String roleCatalogId, List<String> roleIds);
    /**
     * 更新角色
     * @param authRole 角色
     * @return 角色
     */
    AuthRole updateAuthRole(AuthRole authRole);

    /**
     * 查询角色分组下角色的数量
     * 根据角色分组id条件查询角色的数量
     * @param roleCatalogId 角色分组目录id
     * @param tenantId 租户id
     * @return 若未查询到则返回0
     */
    Integer countByRoleCatalogIdAndTenantId(String roleCatalogId, String tenantId);


    /**
     * 通过角色id集合查询角色
     * @param roleIds 角色id集合
     * @return 若未查询到则返回为空集合
     */
    List<AuthRole> listByIds(Collection<String> roleIds);

    /**
     * 通过角色英文名集合查询角色
     * @param nameEns 角色英文名集合
     * @return 若未查询到则返回为空集合
     */
    List<AuthRole> listByNameEn(Collection<String> nameEns);
}
