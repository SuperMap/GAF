/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.authority.dao;

import com.supermap.gaf.authority.commontype.AuthRoleApi;
import com.supermap.gaf.authority.vo.AuthRoleApiSelectVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 角色接口mapper
 *
 * @author yangdong
 * @date:2021/3/25
 */
@Mapper
@Component
public interface AuthRoleApiMapper {
    /**
     * 根据id查询角色接口
     *
     * @param roleApiId 角色接口id
     * @return 角色接口 未查询到则返回null
     */
    AuthRoleApi select(@Param("roleApiId") String roleApiId);

    /**
     * 分页查询角色接口
     *
     * @param selectVo 分页参数 如偏移量，每页条数
     * @return 角色接口集合
     */
    List<AuthRoleApi> pageList(AuthRoleApiSelectVo selectVo);

    /**
     * 分页模糊查询接口
     *
     * @param selectVo 分页参数 如偏移量，每页条数, 模糊查询字段名 字段值 排序字段名 排序方式
     * @return 接口集合
     */
    List<AuthRoleApi> searchList(AuthRoleApiSelectVo selectVo);

    /**
     * 分页查询
     * 针对偏移量过大的分页查询sql优化
     *
     * @param selectVo 分页参数 如偏移量，每页条数
     * @return 角色接口集合
     */
    List<AuthRoleApi> bigOffsetPageList(AuthRoleApiSelectVo selectVo);

    /**
     * 根据角色id和接口id查询角色接口
     *
     * @param resourceApiId 接口id
     * @param roleId        角色id
     * @return 角色接口集合
     */
    List<AuthRoleApi> listRoleApi(@Param("roleId") String roleId, @Param("resourceApiId") String resourceApiId);

    /**
     * 根据角色id查询角色接口
     *
     * @param roleId 角色id
     * @return 角色接口集合
     */
    List<AuthRoleApi> listByRole(@Param("roleId") String roleId);

    /**
     * 根据角色id及状态查询角色接口
     *
     * @param roleId 角色id
     * @param status 状态
     * @return 角色接口集合
     */
    List<AuthRoleApi> getByRoleId(@Param("roleId") String roleId, @Param("status") Boolean status);

    /**
     * 根据接口id和角色id及状态查询角色接口
     *
     * @param apiId  接口id
     * @param roleId 角色id
     * @param status 状态
     * @return 角色接口
     */
    AuthRoleApi getByRoleAndApi(@Param("roleId") String roleId, @Param("apiId") String apiId, @Param("status") Boolean status);

    /**
     * 查询角色接口总数
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
     * 新增角色接口
     *
     * @param authRoleApi 角色接口
     * @return 新增的数量
     */
    int insert(AuthRoleApi authRoleApi);

    /**
     * 批量新增角色接口
     *
     * @param authRoleApis 角色接口集合
     * @return 新增的数量
     */
    int batchInsert(List<AuthRoleApi> authRoleApis);

    /**
     * 根据id集合批量删除角色接口
     *
     * @param roleApiIds 角色接口id集合
     * @return 删除的数量
     */
    int batchDelete(List<String> roleApiIds);

    /**
     * 批量逻辑删除
     *
     * @param roleApiIds 角色接口id集合
     * @return 删除的数量
     */
    int batchUpdate(List<String> roleApiIds);

    /**
     * 根据角色id删除角色接口
     *
     * @param roleId 角色id
     * @return 影响的行数即删除的数量
     */
    int deleteByRoleId(@Param("roleId") String roleId);

    /**
     * 根据接口id集合删除角色接口
     * @param apiIds  接口id集合
     * @return  影响的行数即删除的数量
     */
    int deleteByApiIds(List<String> apiIds);
}
