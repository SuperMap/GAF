/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.authority.dao;

import com.supermap.gaf.authority.commontype.AuthUser;
import com.supermap.gaf.authority.vo.AuthUserSelectVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 用户mapper
 *
 * @author dqc
 * @date:2021/3/25
 */
@Mapper
@Component
public interface AuthUserMapper {
    /**
     * 根据id查询用户
     *
     * @param userId 用户id
     * @return 用户 未查询到则返回null
     */
    AuthUser select(@Param("userId") String userId);

    /**
     * 根据用户id查询
     *
     * @param userId 用户id
     * @return 用户 返回对象中包含加密后的密码
     */
    AuthUser selectWithPassword(@Param("userId") String userId);

    /**
     * 查询某租户下的所有用户
     * 根据租户id查询
     *
     * @param tenantId 租户id
     * @return 所有用户
     */
    List<AuthUser> selectByTenantId(@Param("tenantId") String tenantId);

    int countGetListByTenantId(@Param("tenantId") String tenantId, @Param("realName") String realName);

    /**
     * 分页查询某租户下的用户
     *
     * @param tenantId 租户ID
     * @param realName 模糊查询
     * @param size     每页显示多少条
     * @param current  当前页
     * @return
     */
    List<AuthUser> pageListByTenantId(@Param("tenantId") String tenantId, @Param("realName") String realName, @Param("size") Integer size, @Param("current") Integer current);

    /**
     * 分页查询用户
     *
     * @param selectVo 分页参数 如偏移量，每页条数
     * @return 用户集合
     */
    List<AuthUser> pageList(AuthUserSelectVo selectVo);


    /**
     * 分页模糊查询用户
     *
     * @param selectVo 分页参数 如偏移量，每页条数, 模糊查询字段名 字段值 排序字段名 排序方式
     * @return 用户集合
     */
    List<AuthUser> searchList(@Param("selectVo") AuthUserSelectVo selectVo);

    /**
     * 分页等值查询用户
     *
     * @param selectVo 分页参数 如偏移量，每页条数, 等值查询字段名 字段值 排序字段名 排序方式
     * @return 用户集合
     */
    List<AuthUser> searchWholeMatchList(@Param("selectVo") AuthUserSelectVo selectVo);

    /**
     * 分页查询
     * 针对偏移量过大的分页查询sql优化
     *
     * @param selectVo 分页参数 如偏移量，每页条数
     * @return 用户集合
     */
    List<AuthUser> bigOffsetPageList(AuthUserSelectVo selectVo);

    /**
     * 查询用户总数
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
     * 新增用户
     *
     * @param authUser 用户
     * @return 新增的数量
     */
    int insert(AuthUser authUser);

    /**
     * 批量新增用户
     *
     * @param authUsers 用户集合
     * @return 新增的数量
     */
    int batchInsert(List<AuthUser> authUsers);

    /**
     * 根据id集合批量删除用户
     *
     * @param userIds 用户id集合
     * @return 删除的数量
     */
    int batchDelete(List<String> userIds);

    /**
     * 根据用户id删除用户
     *
     * @param userId 用户id
     * @return 影响的行数即删除的数量
     */
    int delete(@Param("userId") String userId);

    /**
     * 更新用户
     * 使用时请注意 若authUser 属性中postId为null 则会更新为null
     * 其他属性若未null,则不会更新
     *
     * @param authUser 用户
     * @return 影响的行数即更新的数量
     */
    int update(AuthUser authUser);


    /**
     * 组合条件 等值查询
     *
     * @param queryAuthUser 查询参数 至少有一个属性值不为null
     * @return 若未查询到数据则返回空集合
     */
    List<AuthUser> selectByCombination(AuthUser queryAuthUser);

    /**
     * 组合条件 等值查询数量
     *
     * @param queryAuthUser 查询参数 至少有一个属性值不为null
     * @return 若未查询到数据则返回空集合
     */
    Integer countByCombination(AuthUser queryAuthUser);

    /**
     * 查询租户某部门下的用户，按照状态 创建时间降序
     *
     * @param tenantId     租户id
     * @param departmentId 部门id
     * @return 用户集合
     */
    List<AuthUser> selectByDepartmentIdAndTenantIdWithSort(@Param("tenantId") String tenantId, @Param("departmentId") String departmentId);

    /**
     * 通过用户id物理删除用户
     *
     * @param userId 用户id
     */
    void deletePhysicsById(String userId);
}
