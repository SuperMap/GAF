/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.authority.service;

import com.supermap.gaf.authority.commontype.AuthUserRole;
import com.supermap.gaf.authority.vo.AuthUserRoleSelectVo;
import com.supermap.gaf.authority.vo.TreeVo;

import java.util.List;
import java.util.Map;

/**
 * 用户角色服务类
 *
 * @author yangdong
 * @date:2021/3/25
 */
public interface AuthUserRoleService {

    /**
     * 根据id查询用户角色
     *
     * @param userRoleId 用户角色id
     * @return 用户角色
     */
    AuthUserRole getById(String userRoleId);

    /**
     * TODO: 2021/3/12 方法说明与实现不一致
     * 根据userIds查询租户管理员
     *
     * @param userIds 用户id集合
     * @return 租户管理员集合
     */
    List<String> getByUserIds(List<String> userIds);

    /**
     * 根据用户id查询用户角色
     *
     * @param userId 用户id
     * @return 用户角色集合
     */
    List<AuthUserRole> listByUser(String userId);

    /**
     * 查询角色树和用户已分配的角色id集合
     *
     * @param userId 用户id
     * @return 返回的数据中rootTreeNodes表示角色树, checkedKeys表示当前用户已分配的角色id集合
     */
    TreeVo getUserRoleTree(String userId);


    /**
     * 分页查询用户角色
     *
     * @param authUserRoleSelectVo 分页参数 如偏移量，每页条数
     * @return 分页结果 如查询到的总数 分页后的结果
     */
    Map<String, Object> pageList(AuthUserRoleSelectVo authUserRoleSelectVo);

    /**
     * 分页模糊查询用户角色
     *
     * @param authUserRoleSelectVo 分页参数 如偏移量，每页条数, 模糊查询字段名 字段值 排序字段名 排序方式
     * @return 分页结果 如查询到的总数 分页后的结果
     */
    Map<String, Object> searchList(AuthUserRoleSelectVo authUserRoleSelectVo);


    /**
     * 新增用户角色
     *
     * @param authUserRole 用户角色
     * @return 用户角色
     */
    AuthUserRole insertAuthUserRole(AuthUserRole authUserRole);

    /**
     * 批量更新用户角色关联关系
     * 根据用户id查询已绑定的用户角色，与传入的用户角色对比进行更新。
     * 包括是否需要新增或删除代码库用户
     *
     * @param authUserRoles 用户角色集合
     * @param userId        用户id
     */
    void batchUpdate(List<AuthUserRole> authUserRoles, String userId);

    /**
     * 删除用户角色
     *
     * @param userRoleId 用户角色id
     */
    void deleteAuthUserRole(String userRoleId);

    /**
     * 根据用户角色id集合批量删除
     *
     * @param userRoleIds 用户角色id集合
     */
    void batchDelete(List<String> userRoleIds);

    /**
     * 更新用户角色
     *
     * @param authUserRole 用户角色
     * @return 用户角色
     */
    AuthUserRole updateAuthUserRole(AuthUserRole authUserRole);

    /**
     * 根据用户id逻辑删除用户角色关系
     *
     * @param userId 用户id
     */
    void deleteByUserId(String userId);
}
