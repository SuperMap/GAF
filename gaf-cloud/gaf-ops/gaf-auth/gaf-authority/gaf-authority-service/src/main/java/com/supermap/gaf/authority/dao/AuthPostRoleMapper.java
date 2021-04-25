/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.authority.dao;

import com.supermap.gaf.authority.commontype.AuthPostRole;
import com.supermap.gaf.authority.vo.AuthPostRoleSelectVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
/**
 * 岗位角色mapper
 * @date:2021/3/25
 * @author yangdong
 */
@Mapper
@Component
public interface AuthPostRoleMapper{
    /**
     * 根据id查询岗位角色
     * @param postRoleId 岗位角色id
     * @return 岗位角色 未查询到则返回null
     */
    AuthPostRole select(@Param("postRoleId") String postRoleId);

    /**
     * 根据角色id查询岗位
     * @param roleId 角色id
     * @return 岗位集合
     */
    List<String> getByPostIds(String roleId);
    /**
     * 分页查询岗位角色
     * @param selectVo 分页参数 如偏移量，每页条数
     * @return 岗位角色集合
     */
    List<AuthPostRole> pageList(AuthPostRoleSelectVo selectVo);

    /**
     * 根据岗位id和角色id及状态查询岗位角色
     * @param postId 岗位id
     * @param roleId  角色id
     * @param status  状态
     * @return 岗位角色
     */
    AuthPostRole getByPostAndRole(@Param("postId") String postId, @Param("roleId") String roleId, @Param("status") Boolean status);

    /**
     * 批量逻辑删除
     * @param authPostRoleIds 岗位角色id集合
     * @return 影响的行数即删除的数量
     */
    int batchUpdate(List<String> authPostRoleIds);

    /**
     * 分页模糊查询岗位角色
     * @param selectVo 分页参数 如偏移量，每页条数, 模糊查询字段名 字段值 排序字段名 排序方式
     * @return 岗位角色集合
     */
    List<AuthPostRole> searchList(AuthPostRoleSelectVo selectVo);

    /**
     * 分页查询
     * 针对偏移量过大的分页查询sql优化
     * @param selectVo 分页参数 如偏移量，每页条数
     * @return 岗位角色集合
     */
    List<AuthPostRole> bigOffsetPageList(AuthPostRoleSelectVo selectVo);

    /**
     * 根据岗位id和角色id询岗位角色
     * @param postId 岗位id
     * @param roleId  角色id
     * @return 岗位角色集合
     */
    List<AuthPostRole> listPostRole(@Param("postId") String postId, @Param("roleId") String roleId);

    /**
     * 根据岗位id查询岗位角色
     * @param postId  岗位id
     * @return 岗位角色集合
     */
    List<AuthPostRole> listByPost(@Param("postId") String postId);


    /**
     *
     * 根据岗位id和状态询岗位角色
     * @param postId 岗位id
     * @param status 状态
     * @return 岗位角色集合
     */
    List<AuthPostRole> getByPostId(@Param("postId") String postId, @Param("status") Boolean status);

    /**
     * 查询岗位角色总数
     * @return 数量
     */
    int pageListCount();
    /**
     * 统计单字段模糊匹配条件查询后的结果数量
     * @param fieldName 字段名
     * @param fieldValue 字段值
     * @return 数量
     */
	Integer countOneField(@Param("fieldName") String fieldName, @Param("fieldValue") Object fieldValue);
    /**
     * 新增岗位角色
     * @param authPostRole 岗位角色
     * @return 新增的数量
     */
    int insert(AuthPostRole authPostRole);
    /**
     * 批量新增岗位角色
     * @param authPostRoles  岗位角色集合
     * @return 新增的数量
     */
    int batchInsert(List<AuthPostRole> authPostRoles);
    /**
     * 根据id集合批量删除岗位角色
     * @param postRoleIds 岗位角色id集合
     * @return 删除的数量
     */
    int batchDelete(List<String> postRoleIds);
    /**
     * 根据岗位角色id删除岗位角色
     * @param postRoleId 岗位角色id
     * @return 影响的行数即删除的数量
     */
    int delete(@Param("postRoleId") String postRoleId);

    /**
     * 按岗位删除
     * @param postId 岗位id
     * @return 影响的行数即删除的数量
     */
    int deleteByPostId(@Param("postId") String postId);
    /**
     * 更新岗位角色
     * @param authPostRole   岗位角色
     * @return 影响的行数即更新的数量
     */
    int update(AuthPostRole authPostRole);

    /**
     * 通过岗位id集合获取岗位角色
     * @param postIds 岗位id集合
     * @return 若未查询到则返回空集合
     */
    List<AuthPostRole> selectByPostIds(@Param("postIds") Collection<String> postIds);
}
