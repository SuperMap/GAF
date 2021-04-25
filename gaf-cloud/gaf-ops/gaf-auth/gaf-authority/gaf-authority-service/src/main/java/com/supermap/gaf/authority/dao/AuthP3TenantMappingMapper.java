/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.authority.dao;

import com.supermap.gaf.authority.commontype.AuthP3TenantMapping;
import com.supermap.gaf.authority.vo.AuthP3TenantMappingSelectVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 第三方租户映射mapper
 * @date:2021/3/25
 * @author yangdong
 */
@Mapper
@Component
public interface AuthP3TenantMappingMapper {
    /**
     * 新增第三方租户映射
     * @param authP3TenantMapping 第三方租户映射
     * @return 新增的数量
     */
    int insert(AuthP3TenantMapping authP3TenantMapping);

    /**
     * 根据第三方租户映射id删除第三方租户映射
     * @param tenantMappingId 第三方租户映射id
     * @return 影响的行数即删除的数量
     */
    int delete(@Param("tenantMappingId") String tenantMappingId);

    /**
     * 更新第三方租户映射
     * @param authP3TenantMapping   第三方租户映射
     * @return 影响的行数即更新的数量
     */
    int update(AuthP3TenantMapping authP3TenantMapping);

    /**
     * 根据id查询第三方租户映射
     * @param tenantMappingId 第三方租户映射id
     * @return 第三方租户映射 未查询到则返回null
     */
    AuthP3TenantMapping select(@Param("tenantMappingId") String tenantMappingId);

    /**
     * 根据第三方组件id和租户id查询第三方租户映射
     * @param p3ComponentId 第三方组件id
     * @param tenantId 租户id
     * @return 第三方租户映射
     */
    AuthP3TenantMapping getByTenantId(@Param("p3ComponentId") String p3ComponentId, @Param("tenantId") String tenantId);

    /**
     * 分页查询第三方租户映射
     * @param selectVo 分页参数 如偏移量，每页条数
     * @return 第三方租户映射集合
     */
    List<AuthP3TenantMapping> pageList(AuthP3TenantMappingSelectVo selectVo);

    /**
     * 分页模糊查询第三方租户映射
     * @param selectVo 分页参数 如偏移量，每页条数, 模糊查询字段名 字段值 排序字段名 排序方式
     * @return 第三方租户映射集合
     */
    List<AuthP3TenantMapping> searchList(AuthP3TenantMappingSelectVo selectVo);

    /**
     * 分页查询
     * 针对偏移量过大的分页查询sql优化
     * @param selectVo 分页参数 如偏移量，每页条数
     * @return 第三方租户映射集合
     */
    List<AuthP3TenantMapping> bigOffsetPageList(AuthP3TenantMappingSelectVo selectVo);

    /**
     * 查询第三方租户映射总数
     * @return 数量
     */
    int pageListCount();

    /**
     * 批量新增第三方租户映射
     * @param authP3TenantMappings  第三方租户映射集合
     * @return 新增的数量
     */
    int batchInsert(List<AuthP3TenantMapping> authP3TenantMappings);

    /**
     * 根据id集合批量删除第三方租户映射
     * @param tenantMappingIds 第三方租户映射id集合
     * @return 删除的数量
     */
    int batchDelete(List<String> tenantMappingIds);

}
