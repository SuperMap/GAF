/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.authority.service;

import com.supermap.gaf.authority.commontype.AuthP3TenantMapping;
import com.supermap.gaf.authority.vo.AuthP3TenantMappingSelectVo;

import java.util.List;
import java.util.Map;

/**
 * 第三方租户映射服务类
 * @date:2021/3/25
 * @author yangdong
 */
public interface AuthP3TenantMappingService {
    /**
     * 新增第三方租户映射
     * @param authP3TenantMapping 第三方租户映射
     * @return 第三方租户映射
     */
    AuthP3TenantMapping insertAuthP3TenantMapping(AuthP3TenantMapping authP3TenantMapping);

    /**
     * 根据id删除第三方租户映射
     * @param tenantMappingId 第三方租户映射id
     */
    void deleteAuthP3TenantMapping(String tenantMappingId);

    /**
     * 更新第三方租户映射
     * @param authP3TenantMapping 第三方租户映射
     * @return 第三方租户映射
     */
    AuthP3TenantMapping updateAuthP3TenantMapping(AuthP3TenantMapping authP3TenantMapping);

    /**
     * 根据id查询第三方租户映射
     * @param tenantMappingId  第三方租户映射id
     * @return  第三方租户映射 若未查询到则返回null
     */
    AuthP3TenantMapping selectById(String tenantMappingId);

    /**
     * 根据第三方组件id、租户id查询第三方租户映射
     * @param p3ComponentId 第三方组件id
     * @param tenantId 租户id
     * @return 第三方租户映射
     */
    AuthP3TenantMapping getByTenantId(String p3ComponentId, String tenantId);

    /**
     * 分页查询第三方租户映射
     * @param authP3TenantMappingSelectVo 分页参数 如偏移量，每页条数
     * @return 分页结果 如查询到的总数 分页后的结果
     */
    Map<String, Object> pageList(AuthP3TenantMappingSelectVo authP3TenantMappingSelectVo);

    /**
     * 分页模糊查询第三方租户映射
     * @param authP3TenantMappingSelectVo 分页参数 如偏移量，每页条数, 模糊查询字段名 字段值 排序字段名 排序方式
     * @return 分页结果 如查询到的总数 分页后的结果
     */
    Map<String, Object> searchList(AuthP3TenantMappingSelectVo authP3TenantMappingSelectVo);

    /**
     * 批量插入
     * @param authP3TenantMappings  第三方租户映射集合
     */
    void batchInsert(List<AuthP3TenantMapping> authP3TenantMappings);

    /**
     * 批量删除
     * @param tenantMappingIds 第三方租户映射id集合
     */
    void batchDelete(List<String> tenantMappingIds);

}
