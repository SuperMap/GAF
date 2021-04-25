/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.authority.service;

import com.supermap.gaf.authority.commontype.AuthTenant;
import com.supermap.gaf.authority.commontype.AuthUser;
import com.supermap.gaf.authority.vo.AuthTenantSelectVo;
import com.supermap.gaf.authority.vo.TenantInitVo;

import java.util.List;
import java.util.Map;

/**
 * 租户服务类
 * @author zhm
 * @date:2021/3/25
 *
 */
public interface AuthTenantService {

    /**
     * 分页模糊查询租户
     * @param searchFieldName 字段名
     * @param searchFieldValue 字段值
     * @param orderFieldName 排序字段名
     * @param orderMethod 排序方式 ASC DESC
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return 分页结果
     */
    Map<String,Object> tenantSearch(String searchFieldName, String searchFieldValue, String orderFieldName, String orderMethod, Integer pageNum, Integer pageSize);

    /**
     * 根据租户id查询租户
     * @param tenantId  租户id
     * @return 租户
     */
    AuthTenant getById(String tenantId);

    /**
     * 查询租户下的所有管理员
     * @param tenantId 租户id
     * @return 所有拥有管理员角色的用户
     */
    List<AuthUser> getAllAdmin(String tenantId);

    /**
     * 根据管理员名称查询租户
     * @param authTenantSelectVo 租户 分页条件查询实体
     * @return 分页结果 如分页结果 总数
     */
    Map<String,Object> getByAdminName(AuthTenantSelectVo authTenantSelectVo);

    /**
     * 状态查询租户
     * @param authTenantSelectVo 查询条件
     * @return  结果
     */
    Map<String,Object> getByStatus(AuthTenantSelectVo authTenantSelectVo);

    /**
     * 分页查询租户
     * @param authTenantSelectVo 分页参数 如偏移量，每页条数
     * @return 分页结果 如查询到的总数 分页后的结果
     */
    Map<String,Object> pageList(AuthTenantSelectVo authTenantSelectVo);
    /**
     * 分页模糊查询租户
     * @param authTenantSelectVo 分页参数 如偏移量，每页条数, 模糊查询字段名 字段值 排序字段名 排序方式
     * @return 分页结果 如查询到的总数 分页后的结果
     */
    Map<String,Object> searchList(AuthTenantSelectVo authTenantSelectVo);

    /**
     * 新增租户
     * @param authTenant 租户
     * @return 租户
     */
    AuthTenant insertAuthTenant(AuthTenant authTenant);

    /**
     * 租户初始化
     * @param tenantInitVo  租户初始化信息
     */
    void initAuthTenant(TenantInitVo tenantInitVo);


    /**
     * 批量插入租户
     * @param authTenants 租户集合
     */
    void batchInsert(List<AuthTenant> authTenants);

    /**
     * 根据id删除租户
     * @param tenantId 租户id
     */
    void deleteAuthTenant(String tenantId);

    /**
     * 禁用或启用租户
     * @param tenantId  租户id
     * @param status true 启用 false 禁用
     */
    void setAuthTenantStatus(String tenantId, Boolean status);

    /**
     * 批量删除租户
     * @param tenantIds  租户id集合
     */
    void batchDelete(List<String> tenantIds);

    /**
     * 更新租户
     * @param authTenant 租户
     * @return 租户
     */
    AuthTenant updateAuthTenant(AuthTenant authTenant);
}
