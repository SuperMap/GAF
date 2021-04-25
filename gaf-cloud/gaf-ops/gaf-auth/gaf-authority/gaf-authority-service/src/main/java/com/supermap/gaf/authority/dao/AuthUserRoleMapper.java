/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.authority.dao;

import com.supermap.gaf.authority.commontype.AuthUserRole;
import com.supermap.gaf.authority.vo.AuthUserRoleSelectVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 用户角色mapper
 * @date:2021/3/25
 * @author yangdong
 */
@Mapper
@Component
public interface AuthUserRoleMapper {

    /**
     * 根据id查询用户角色
     * @param userRoleId 用户角色id
     * @return 用户角色 未查询到则返回null
     */
    AuthUserRole select(@Param("userRoleId") String userRoleId);


    /**
     * 分页查询用户角色
     * @param selectVo 分页参数 如偏移量，每页条数
     * @return 用户角色集合
     */
    List<AuthUserRole> pageList(AuthUserRoleSelectVo selectVo);

    /**
     * 分页模糊查询接口
     * @param selectVo 分页参数 如偏移量，每页条数, 等值模糊字段名 字段值 排序字段名 排序方式
     * @return 用户角色集合
     */
    List<AuthUserRole> searchList(AuthUserRoleSelectVo selectVo);

    /**
     * 分页查询
     * 针对偏移量过大的分页查询sql优化
     * @param selectVo 分页参数 如偏移量，每页条数
     * @return 用户角色集合
     */
    List<AuthUserRole> bigOffsetPageList(AuthUserRoleSelectVo selectVo);


    /**
     * 根据角色id和用户id查询用户角色
     * @param roleId 角色id
     * @param userId 用户id
     * @return 用户角色集合
     */
    List<AuthUserRole> listUserRole(@Param("roleId") String roleId, @Param("userId") String userId);
    /**
     * 根据用户id查询用户角色
     * @param userId 用户id
     * @return 用户角色集合
     */
    List<AuthUserRole> listByUser(@Param("userId") String userId);

    /**
     * 根据角色id查询关联的用户id集合
     * @param roleId 角色id
     * @return 用户id集合
     */
    List<String> getByUserIds(String roleId);

    /**
     * 查询用户角色总数
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
     * 新增用户角色
     * @param authUserRole 用户角色
     * @return 新增的数量
     */
    int insert(AuthUserRole authUserRole);

    /**
     * 批量新增用户角色
     * @param authUserRoles  用户角色集合
     * @return 新增的数量
     */
    int batchInsert(List<AuthUserRole> authUserRoles);

    /**
     * 根据id集合批量删除用户角色
     * @param userRoleIds 用户角色id集合
     * @return 删除的数量
     */
    int batchDelete(List<String> userRoleIds);

    /**
     * 根据用户角色id删除用户角色
     * @param userRoleId 用户角色id
     * @return 影响的行数即删除的数量
     */
    int delete(@Param("userRoleId") String userRoleId);

    /**
     * 根据用户id删除与该用户相关的用户角色
     * @param userId 用户id
     * @return 影响的行数即删除的数量
     */
    int deleteByUserId(@Param("userId") String userId);


    /**
     * 更新用户角色
     * @param authUserRole 用户角色
     * @return 影响的行数即更新的数量
     */
    int update(AuthUserRole authUserRole);

    /**
     * 根据用户id和角色ids集合逻辑删除用户角色关联关系
     * @param userId 用户id
     * @param roleIds 角色ids集合
     * @return 删除的行数
     */
    int deleteByUserIdAndRoleIds(@Param("userId") String userId, @Param("roleIds") List<String> roleIds);
}
