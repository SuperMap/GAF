/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.authority.service;

import com.supermap.gaf.authority.commontype.AuthDepartment;
import com.supermap.gaf.authority.vo.AuthDepartmentSelectVo;
import com.supermap.gaf.authority.vo.TreeNode;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 部门服务类
 *
 * @author zhm
 * @date:2021/3/25
 */
public interface AuthDepartmentService {

    /**
     * 根据id查询部门
     *
     * @param departmentId 部门id
     * @return 部门 未查询到则返回null
     */
    AuthDepartment getById(String departmentId);

    /**
     * 根据部门名称模糊查询和租户id查询部门。若未传入search（岗位名）则只根据租户id条件查询部门
     *
     * @param search   部门名
     * @param tenantId 租户id
     * @return 部门集合 未查询到则返回空集合
     */
    List<AuthDepartment> listDepartment(String search, String tenantId);

    /**
     * 分页查询部门
     *
     * @param authDepartmentSelectVo 分页参数 如偏移量，每页条数
     * @return 分页结果 如查询到的总数 分页后的结果
     */
    Map<String, Object> pageList(AuthDepartmentSelectVo authDepartmentSelectVo);

    /**
     * 分页模糊查询部门
     *
     * @param authDepartmentSelectVo 分页参数 如偏移量，每页条数, 模糊查询字段名 字段值 排序字段名 排序方式
     * @return 分页结果 如查询到的总数 分页后的结果
     */
    Map<String, Object> searchList(AuthDepartmentSelectVo authDepartmentSelectVo);

    /**
     * 新增部门
     *
     * @param authDepartment 部门
     * @return 部门
     */
    AuthDepartment insertAuthDepartment(AuthDepartment authDepartment);

    /**
     * 批量新增部门
     *
     * @param authDepartments 部门集合
     */
    void batchInsert(List<AuthDepartment> authDepartments);

    /**
     * 批量新增部门
     *
     * @param tenantId     租户id
     * @param departmentId 部门id
     * @return 部门
     */
    AuthDepartment deleteAuthDepartment(String tenantId, String departmentId);

    /**
     * 批量删除部门
     *
     * @param tenantId      租户id
     * @param departmentIds 部门id集合
     */
    void batchDelete(String tenantId, List<String> departmentIds);

    /**
     * 更新部门
     *
     * @param authDepartment 部门
     * @return 部门
     */
    AuthDepartment updateAuthDepartment(AuthDepartment authDepartment);

    /**
     * 根据id集合批量获取部门信息
     *
     * @param departmentIds 部门id集合
     * @return 部门集合 若未查询到则返回空集合
     */
    List<AuthDepartment> getByIds(Set<String> departmentIds);

    /**
     * 根据租户id获取租户下的所有部门，然后转换为树节点
     *
     * @param tenantId 租户id
     * @return 部门树节点集合 若未查询到部门则返回空集合
     */
    List<TreeNode> listDepartmentTreeNode(String tenantId);


    /**
     * 查询根部门到当前部门的路径（包括当前部门）。返回结果是部门id的有序集合，表示路径
     *
     * @param departmentId 部门id
     * @return 集合元素的顺序为上级部门id到当前部门id 若未查询到则返回空集合
     */
    List<String> getDepartmentPath(String departmentId);

}
