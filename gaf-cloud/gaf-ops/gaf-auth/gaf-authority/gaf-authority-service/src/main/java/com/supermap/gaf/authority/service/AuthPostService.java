/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.authority.service;

import com.supermap.gaf.authority.commontype.AuthPost;
import com.supermap.gaf.authority.vo.AuthPostSelectVo;
import com.supermap.gaf.authority.vo.SelectOptionVo;
import com.supermap.gaf.authority.vo.TreeNode;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 岗位服务类
 *
 * @author zhm
 * @date:2021/3/25
 */
public interface AuthPostService {

    /**
     * 根据id查询岗位
     *
     * @param postId 岗位id
     * @return 岗位 若未查询到则返回null
     */
    AuthPost getById(String postId);

    /**
     * 查询当前用户所属的租户下的部门岗位。若传入岗位名可增加岗位名的模糊查询条件
     *
     * @param search       岗位名
     * @param departmentId 部门id
     * @return 岗位集合 若未查询到则返回空集合
     */
    List<AuthPost> listPost(String search, String departmentId);

    /**
     * 查询部门岗位树
     * 返回的树节点数据中，type为2表示部门节点,type为3表示岗位节点
     *
     * @param tenantId 租户id
     * @return 部门岗位树
     */
    List<TreeNode> getPostTree(String tenantId);

    /**
     * 分页查询岗位
     *
     * @param authPostSelectVo 分页参数 如偏移量，每页条数
     * @return 分页结果 如查询到的总数 分页后的结果
     */
    Map<String, Object> pageList(AuthPostSelectVo authPostSelectVo);

    /**
     * 分页模糊查询岗位
     *
     * @param authPostSelectVo 分页参数 如偏移量，每页条数, 模糊查询字段名 字段值 排序字段名 排序方式
     * @return 分页结果 如查询到的总数 分页后的结果
     */
    Map<String, Object> searchList(AuthPostSelectVo authPostSelectVo);

    /**
     * 新增岗位
     *
     * @param authPost 岗位
     * @return 岗位
     */
    AuthPost insertAuthPost(AuthPost authPost);

    /**
     * 批量插入岗位
     *
     * @param authPosts 岗位集合
     */
    void batchInsert(List<AuthPost> authPosts);

    /**
     * 根据部门id和岗位id删除岗位
     *
     * @param departmentId 部门id
     * @param postId       岗位id
     * @return 岗位
     */
    AuthPost deleteAuthPost(String departmentId, String postId);

    /**
     * 根据部门id和岗位id集合批量删除岗位
     *
     * @param departmentId 部门id
     * @param postIds      岗位id集合
     */
    void batchDelete(String departmentId, List<String> postIds);

    /**
     * 更新岗位
     *
     * @param authPost 岗位
     * @return 岗位
     */
    AuthPost updateAuthPost(AuthPost authPost);

    /**
     * 根据岗位id集合批量查询有效岗位
     *
     * @param postIds 岗位id集合
     * @return 若未查询到则返回空集合
     */
    List<AuthPost> getByIds(Set<String> postIds);

    /**
     * 查询部门下的岗位,然后将其转换为选择项对象
     *
     * @param departmentId 部门id
     * @return 若未查询到则返回空集合
     */
    List<SelectOptionVo> listPostOptionsByDepartment(String departmentId);

    /**
     * 根据部门id查询该部门下的岗位
     *
     * @param departmentId 部门id
     * @return 若未查询到则返回空集合
     */
    List<AuthPost> listPostByDepartmentId(String departmentId);
}
