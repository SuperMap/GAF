/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.authority.dao;

import com.supermap.gaf.authority.commontype.AuthPost;
import com.supermap.gaf.authority.vo.AuthPostSelectVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * 岗位mapper
 *
 * @author zhm
 * @date:2021/3/25
 */
@Mapper
@Component
public interface AuthPostMapper {
    /**
     * 根据id查询岗位
     *
     * @param postId 岗位id
     * @return 岗位 未查询到则返回null
     */
    AuthPost select(@Param("postId") String postId);

    /**
     * 分页查询岗位
     *
     * @param selectVo 分页参数 如偏移量，每页条数
     * @return 岗位集合
     */
    List<AuthPost> pageList(AuthPostSelectVo selectVo);

    /**
     * 分页模糊查询某部门下的岗位
     *
     * @param selectVo     分页参数 如偏移量，每页条数, 模糊查询字段名 字段值 排序字段名 排序方式
     * @param departmentId 部门id
     * @return 岗位集合
     */
    List<AuthPost> searchList(@Param("selectVo") AuthPostSelectVo selectVo, @Param("departmentId") String departmentId);

    /**
     * 分页等值查询某部门下的岗位
     *
     * @param selectVo     分页参数 如偏移量，每页条数, 等值查询字段名 字段值 排序字段名 排序方式
     * @param departmentId 部门id
     * @return 岗位集合
     */
    List<AuthPost> searchWholeMatchList(@Param("selectVo") AuthPostSelectVo selectVo, @Param("departmentId") String departmentId);

    /**
     * 分页查询
     * 针对偏移量过大的分页查询sql优化
     *
     * @param selectVo 分页参数 如偏移量，每页条数
     * @return 岗位集合
     */
    List<AuthPost> bigOffsetPageList(AuthPostSelectVo selectVo);


    /**
     * 查询岗位总数
     *
     * @return 数量
     */
    int pageListCount();

    /**
     * 统计单字段模糊匹配条件查询后的结果数量
     *
     * @param fieldName  字段名
     * @param fieldValue 字段值
     * @return 数量
     */
    Integer countOneField(@Param("fieldName") String fieldName, @Param("fieldValue") Object fieldValue);


    /**
     * 新增岗位
     *
     * @param authPost 岗位
     * @return 新增的数量
     */
    int insert(AuthPost authPost);

    /**
     * 批量新增岗位
     *
     * @param authPosts 岗位集合
     * @return 新增的数量
     */
    int batchInsert(List<AuthPost> authPosts);

    /**
     * 根据岗位id集合删除岗位
     *
     * @param postIds 岗位id集合
     * @return 影响的行数即删除的数量
     */
    int batchDelete(List<String> postIds);

    /**
     * 根据岗位id删除岗位
     *
     * @param postId 岗位id
     * @return 影响的行数即删除的数量
     */
    int delete(@Param("postId") String postId);

    /**
     * 更新岗位
     *
     * @param authPost 岗位
     * @return 影响的行数即更新的数量
     */
    int update(AuthPost authPost);

    /**
     * 根据岗位id集合批量查询有效岗位
     *
     * @param postIds 岗位id集合
     * @return 若未查询到则返回空集合
     */
    List<AuthPost> selectByIds(Set<String> postIds);

    /**
     * 根据部门id查询该部门下的岗位
     *
     * @param departmentId 部门id
     * @return 若未查询到则返回空集合
     */
    List<AuthPost> selectByDepartmentId(String departmentId);
}
