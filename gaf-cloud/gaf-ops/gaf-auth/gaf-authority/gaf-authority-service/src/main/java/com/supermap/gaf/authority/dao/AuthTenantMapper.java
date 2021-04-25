/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.authority.dao;

import com.supermap.gaf.authority.commontype.AuthTenant;
import com.supermap.gaf.authority.vo.AuthTenantSelectVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
/**
 * 租户mapper
 * @date:2021/3/25
 * @author zhm
 */
@Mapper
@Component
public interface AuthTenantMapper{
    /**
     * 根据id查询租户
     * @param tenantId 租户id
     * @return 租户 未查询到则返回null
     */
    AuthTenant select(@Param("tenantId") String tenantId);

    /**
     * 根据管理员id集合分页查询租户 
     * @param adminIds 管理员id集合
     * @param pageSize 每页条数
     * @param offset 偏移量
     * @return 租户集合
     */
    List<AuthTenant> selectByAdminId(@Param("adminIds") List<String> adminIds, @Param("pageSize") Integer pageSize, @Param("offset") Integer offset);

    /**
     * 根据状态分页查询租户 
     * @param status 状态
     * @param pageSize 每页条数
     * @param offset 偏移量
     * @return 租户集合
     */
    List<AuthTenant> selectByStatus(@Param("status") Boolean status, @Param("pageSize") Integer pageSize, @Param("offset") Integer offset);

    /**
     * 分页查询租户
     * @param selectVo 分页参数 如偏移量，每页条数
     * @return 租户集合
     */
    List<AuthTenant> pageList(AuthTenantSelectVo selectVo);

    /**
     * 分页模糊查询租户
     * @param selectVo 分页参数 如偏移量，每页条数, 模糊查询字段名 字段值 排序字段名 排序方式
     * @return 租户集合
     */
    List<AuthTenant> searchList(AuthTenantSelectVo selectVo);

    /**
     * 分页等值查询租户
     * @param selectVo 分页参数 如偏移量，每页条数, 等值查询字段名 字段值 排序字段名 排序方式
     * @return 租户集合
     */
    List<AuthTenant> searchWholeMatchList(@Param("selectVo") AuthTenantSelectVo selectVo);

    /**
     * 分页查询
     * 针对偏移量过大的分页查询sql优化
     * @param selectVo 分页参数 如偏移量，每页条数
     * @return 租户集合
     */
    List<AuthTenant> bigOffsetPageList(AuthTenantSelectVo selectVo);

    /**
     * 查询租户总数
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
     * 查询管理员相关的租户的数量
     * 即根据管理员id集合查询租户的数量
     * @param adminIds 管理员id集合
     * @return 数量
     */
    Integer countByAdmin(@Param("adminIds") List<String> adminIds);

    /**
     * 根据状态查询租户数量
     * @param status 状态
     * @return 租户数量
     */
    Integer countByStatus(Boolean status);

    /**
     * 新增租户
     * @param authTenant 租户
     * @return 新增的数量
     */
    int insert(AuthTenant authTenant);

    /**
     * 批量新增租户
     * @param authTenants  租户集合
     * @return 新增的数量
     */
    int batchInsert(List<AuthTenant> authTenants);
    /**
     * 根据id集合批量删除租户
     * @param tenantIds 租户id集合
     * @return 删除的数量
     */
    int batchDelete(List<String> tenantIds);
    /**
     * 根据租户id删除租户
     * @param tenantId 租户id
     * @return 影响的行数即删除的数量
     */
    int delete(@Param("tenantId") String tenantId);

    /**
     * 禁用或启用租户 
     * @param tenantId 租户
     * @param status true 启动 false 禁用
     * @return 影响的行数
     */
    int setAuthTenantStatus(@Param("tenantId") String tenantId, @Param("status") Boolean status);
    /**
     * 更新租户
     * @param authTenant   租户
     * @return 影响的行数即更新的数量
     */
    int update(AuthTenant authTenant);
}
