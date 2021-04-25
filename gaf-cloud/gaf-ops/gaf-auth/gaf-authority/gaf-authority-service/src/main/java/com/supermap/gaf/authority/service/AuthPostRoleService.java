/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.authority.service;

import com.supermap.gaf.authority.commontype.AuthPostRole;
import com.supermap.gaf.authority.vo.AuthPostRoleSelectVo;
import com.supermap.gaf.authority.vo.PostRoleVo;
import com.supermap.gaf.authority.vo.TreeVo;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 岗位角色服务类
 * @date:2021/3/25
 * @author yangdong
 */
public interface AuthPostRoleService {

    /**
     * 根据岗位角色id查询岗位角色
     * @param postRoleId 岗位角色id
     * @return 岗位角色 若未查询到则返回null
     */
    AuthPostRole getById(String postRoleId);

    /**
     * TODO: 2021/3/12 有问题，语义与代码实现不相符
     * 根据岗位id集合查询
     * @param postIds 岗位id集合
     * @return id集合
     */
    List<String> getByPostIds(List<String> postIds);

    /**
     * 根据岗位id条件查询岗位角色
     * @param postId  岗位id
     * @return 岗位角色集合 若未查询则返回空集合
     */
    List<AuthPostRole> listByPost(String postId);

    /**
     * 根据岗位id和状态查询岗位角色
     * @param postId  岗位id
     * @param status true 有效 false 无效
     * @return 岗位角色集合
     */
    List<AuthPostRole> getByPostId(String postId, Boolean status);

    /**
     * 更新某岗位与角色的关联关系
     * 传入岗位id和角色id列表，查询到该岗位已绑定的角色，与传入的角色id列表做对比更新
     * @param postRoleVo 岗位角色关联对象
     */
    void handlePostRole(PostRoleVo postRoleVo);

    /**
     * 查询角色树和当前岗位绑定的角色id集合
     * 返回的结果数据中，rootTreeNodes表示角色树，checkedKeys表示当前岗位绑定的角色id集合
     * @param postId 岗位id
     * @return 角色树和前岗位绑定的角色id集合
     */
    TreeVo getRoleTree(String postId);

    /**
     * 分页查询岗位角色
     * @param authPostRoleSelectVo 分页参数 如偏移量，每页条数
     * @return 分页结果 如查询到的总数 分页后的结果
     */
    Map<String, Object> pageList(AuthPostRoleSelectVo authPostRoleSelectVo);

    /**
     * 分页模糊查询岗位角色
     * @param authPostRoleSelectVo 分页参数 如偏移量，每页条数, 模糊查询字段名 字段值 排序字段名 排序方式
     * @return 分页结果 如查询到的总数 分页后的结果
     */
    Map<String, Object> searchList(AuthPostRoleSelectVo authPostRoleSelectVo);

    /**
     * 新增岗位角色
     * @param authPostRole 岗位角色
     * @return 岗位角色
     */
    AuthPostRole insertAuthPostRole(AuthPostRole authPostRole);

    /**
     * 批量插入岗位角色
     * @param authPostRoles 岗位角色集合
     */
    void batchInsertPostRole(List<AuthPostRole> authPostRoles);

    /**
     * 删除岗位角色
     * @param postRoleId  岗位角色id
     */
    void deleteAuthPostRole(String postRoleId);

    /**
     * 根据岗位角色id集合批量删除岗位角色
     * @param postRoleIds 岗位角色id集合
     */
    void batchDelete(List<String> postRoleIds);

    /**
     * 更新岗位角色
     * @param authPostRole  岗位角色
     * @return 岗位角色
     */
    AuthPostRole updateAuthPostRole(AuthPostRole authPostRole);

    /**
     * 通过岗位id集合查询岗位角色
     * @author wxl
     * @param postIds 岗位id集合
     * @return 若未查询到则返回空集合
     */
    List<AuthPostRole> listByPostIds(Collection<String> postIds);
}
