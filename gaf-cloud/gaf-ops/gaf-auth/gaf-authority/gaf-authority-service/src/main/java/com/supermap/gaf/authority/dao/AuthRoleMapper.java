/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.authority.dao;

import com.supermap.gaf.authority.commontype.AuthRole;
import com.supermap.gaf.authority.vo.AuthRoleSelectVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

/**
 * 角色mapper
 *
 * @author zhm
 * @date:2021/3/25
 */
@Mapper
@Component
public interface AuthRoleMapper {
    /**
     * 根据id查询角色
     *
     * @param roleId 角色id
     * @return 角色 未查询到则返回null
     */
    AuthRole select(@Param("roleId") String roleId);

    /**
     * 查询某租户下的所有
     *
     * @param tenantId 租户id
     * @return 角色集合
     */
    List<AuthRole> listRoles(@Param("tenantId") String tenantId);

    /**
     * 分页查询角色
     *
     * @param selectVo 分页参数 如偏移量，每页条数
     * @return 角色集合
     */
    List<AuthRole> pageList(AuthRoleSelectVo selectVo);

    /**
     * 分页模糊查询某租户下的角色
     *
     * @param selectVo 分页参数 如偏移量，每页条数, 模糊查询字段名 字段值 排序字段名 排序方式
     * @param tenantId 租户id
     * @return 接口集合
     */
    List<AuthRole> searchList(@Param("selectVo") AuthRoleSelectVo selectVo, @Param("tenantId") String tenantId);

    /**
     * 分页等值查询接口
     *
     * @param selectVo 分页参数 如偏移量，每页条数, 等值查询字段名 字段值 排序字段名 排序方式
     * @return 接口集合
     */
    List<AuthRole> searchWholeMatchList(@Param("selectVo") AuthRoleSelectVo selectVo);

    /**
     * 分页查询
     * 针对偏移量过大的分页查询sql优化
     *
     * @param selectVo 分页参数 如偏移量，每页条数
     * @return 角色集合
     */
    List<AuthRole> bigOffsetPageList(AuthRoleSelectVo selectVo);


    /**
     * 查询角色总数
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
     * 新增角色
     *
     * @param authRole 角色
     * @return 新增的数量
     */
    int insert(AuthRole authRole);

    /**
     * 批量新增角色
     *
     * @param authRoles 角色集合
     * @return 新增的数量
     */
    int batchInsert(List<AuthRole> authRoles);

    /**
     * 根据id集合批量删除角色
     *
     * @param roleIds 角色id集合
     * @return 删除的数量
     */
    int batchDelete(List<String> roleIds);

    /**
     * 根据角色id删除角色
     *
     * @param roleId 角色id
     * @return 影响的行数即删除的数量
     */
    int delete(@Param("roleId") String roleId);

    /**
     * 更新角色
     *
     * @param authRole 角色
     * @return 影响的行数即更新的数量
     */
    int update(AuthRole authRole);

    /**
     * 统计等值组合条件查询的角色数量
     *
     * @param queryRole 查询条件 注意：queryRole至少有一个属性值不能为null
     * @return 若未查询到则返回0
     */
    Integer countByCombination(AuthRole queryRole);

    /**
     * 通过角色id集合查询角色
     *
     * @param roleIds 角色id集合
     * @return 若未查询到则返回为空
     */
    List<AuthRole> selectByIds(@Param("roleIds") Collection<String> roleIds);

    /**
     * 通过角色英文名集合查询角色
     *
     * @param nameEns 角色英文名集合
     * @return 若未查询到则返回为空
     */
    List<AuthRole> selectByNameEns(@Param("nameEns") Collection<String> nameEns);
}
