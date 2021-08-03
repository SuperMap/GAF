/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.authority.service;

import com.supermap.gaf.authority.commontype.AuthUser;
import com.supermap.gaf.authority.vo.AuthUserSelectVo;
import com.supermap.gaf.authority.vo.EmailChangeVo;
import com.supermap.gaf.authority.vo.TreeNode;

import java.util.List;
import java.util.Map;

/**
 * 用户服务类
 *
 * @author dqc
 * @date:2021/3/25
 */
public interface AuthUserService {

    /**
     * 根据id查询用户
     *
     * @param userId 用户id
     * @return 用户 若未查询到则返回null
     */
    AuthUser getById(String userId);

    /**
     * 查询当前租户下的所有用户信息
     *
     * @param tenantId 租户id
     * @return 用户集合
     */
    List<AuthUser> getByTenantId(String tenantId);

    /**
     * 分页查询当前租户下的所有用户信息
     *
     * @param tenantId 租户ID
     * @param realName 分页参数 如偏移量，每页条数
     * @param size     每页条数
     * @param offset   当前页
     * @return 分页结果 如查询到的总数 分页后的结果
     */
    Map<String, Object> pageListByTenantId(String tenantId, String realName, Integer size, Integer offset);

    /**
     * 查询某租户某部门下的用户
     *
     * @param tenantId     租户id
     * @param departmentId 部门id
     * @return 用户集合
     */
    List<AuthUser> listUserByDepartment(String tenantId, String departmentId);

    /**
     * 查询某岗位下的用户
     *
     * @param postId 岗位id
     * @return 用户集合
     */
    List<AuthUser> listUserByPost(String postId);

    /**
     * 查询某租户下的部门用户树
     *
     * @param tenantId 租户id
     * @return 查询部门用户树 返回的节点已组织为树形结构
     */
    List<TreeNode> getUserTree(String tenantId);

    /**
     * 分页查询用户
     *
     * @param authUserSelectVo 分页参数 如偏移量，每页条数
     * @return 分页结果 如查询到的总数 分页后的结果
     */
    Map<String, Object> pageList(AuthUserSelectVo authUserSelectVo);

    /**
     * 分页模糊查询用户
     *
     * @param authUserSelectVo 分页参数 如偏移量，每页条数, 模糊查询字段名 字段值 排序字段名 排序方式
     * @return 分页结果 如查询到的总数 分页后的结果
     */
    Map<String, Object> searchList(AuthUserSelectVo authUserSelectVo);

    /**
     * 新增用户
     *
     * @param authUser 用户
     * @return 用户
     */
    AuthUser insertAuthUser(AuthUser authUser);

    /**
     * 批量插入用户
     *
     * @param authUsers 用户集合
     */
    void batchInsert(List<AuthUser> authUsers);

    /**
     * 禁用用户即删除用户，给用户的分配的角色、岗位、挂职都会被清空
     *
     * @param userId 用户id
     * @return 用户
     */
    AuthUser deleteAuthUser(String userId);

    /**
     * 批量删除用户
     * 根据id批量删除用户。注意：给用户的分配的角色、岗位、挂职都会被清空
     *
     * @param userIds 用户id集合
     */
    void batchDelete(List<String> userIds);

    /**
     * 更新用户
     *
     * @param authUser 用户
     * @return 用户
     */
    AuthUser updateAuthUser(AuthUser authUser);

    /**
     * 重置密码
     *
     * @param userId 用户id
     * @return 用户
     */
    AuthUser resetPassword(String userId);

    /**
     * 根据部门id查询用户 并携带部门名和岗位名
     *
     * @param tenantId     租户id
     * @param departmentId 部门id
     * @return 用户集合
     */
    List<AuthUser> listUserByDepartmentWithName(String tenantId, String departmentId);

    /**
     * 启用用户
     *
     * @param userId 用户id
     * @return 返回启用后的信息
     */
    AuthUser active(String userId);

    /**
     * 通过用户id物理删除用户
     *
     * @param userId 用户id
     */
    void deletePhysicsById(String userId);

    /**
     * 获取当前用户的信息
     *
     * @return 用户
     */
    AuthUser getUserInfo();

    /**
     * 变更用户密码
     *
     * @param authUser    用户
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     */
    void changePassword(AuthUser authUser, String oldPassword, String newPassword);

    /**
     * 当前用户变更邮箱
     *
     * @param emailChangeVo 变更邮箱参数
     */
    String changeEmail(EmailChangeVo emailChangeVo);

    /**
     * 发送校验码
     *
     * @param email 邮箱 若邮箱为空则默认使用当前用户邮箱
     * @return 若为空则表示成功，否则表示失败原因
     */
    void sendCheckCode(String email);
}
