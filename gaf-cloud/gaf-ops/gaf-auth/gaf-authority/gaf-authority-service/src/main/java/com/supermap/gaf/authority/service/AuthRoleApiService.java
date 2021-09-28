/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.authority.service;

import com.supermap.gaf.authority.commontype.AuthRoleApi;
import com.supermap.gaf.authority.vo.AuthRoleApiSelectVo;
import com.supermap.gaf.authority.vo.RoleApiVo;

import java.util.List;
import java.util.Map;

/**
 * 角色接口服务类
 *
 * @author yangdong
 * @date:2021/3/25
 */
public interface AuthRoleApiService {

    /**
     * 根据id查询角色接口
     *
     * @param roleApiId 角色接口id
     * @return 角色接口 若为查询到则返回null
     */
    AuthRoleApi getById(String roleApiId);

    /**
     * 查询角色已绑定的角色接口
     * 即根据角色id条件查询角色接口
     *
     * @param roleId 角色id
     * @return 角色接口集合 若未查询到则返回空集合
     */
    List<AuthRoleApi> listByRole(String roleId);

    /**
     * 根据角色id和状态查询角色接口
     *
     * @param roleId 角色id
     * @param status true 有效 false 无效
     * @return 角色接口集合 若为查询到则返回空集合
     */
    List<AuthRoleApi> getByRoleId(String roleId, Boolean status);


    /**
     * 更新某角色与接口(api)的关联关系
     * 传入角色id和接口(api)id集合，查询到该角色已绑定的接口(api)，与传入的接口id列表做对比更新
     *
     * @param roleApiVo 角色与接口(api)的关联对象
     */
    void handleRoleApi(RoleApiVo roleApiVo);

    /**
     * 分页查询角色接口
     *
     * @param authRoleApiSelectVo 分页参数 如偏移量，每页条数
     * @return 分页结果 如查询到的总数 分页后的结果
     */
    Map<String, Object> pageList(AuthRoleApiSelectVo authRoleApiSelectVo);

    /**
     * 分页模糊查询角色接口
     *
     * @param authRoleApiSelectVo 分页参数 如偏移量，每页条数, 模糊查询字段名 字段值 排序字段名 排序方式
     * @return 分页结果 如查询到的总数 分页后的结果
     */
    Map<String, Object> searchList(AuthRoleApiSelectVo authRoleApiSelectVo);


    /**
     * 批量插入角色接口
     *
     * @param authRoleApis 角色接口集合
     */
    void batchInsertRoleApi(List<AuthRoleApi> authRoleApis);

    /**
     * 批量删除角色接口
     *
     * @param roleApiIds 角色接口id集合
     */
    void batchDelete(List<String> roleApiIds);

    /**
     * 根据接口id集合删除角色接口
     *
     * @param apiIds 接口id集合
     */
    int deleteByApiIds(List<String> apiIds);

}
