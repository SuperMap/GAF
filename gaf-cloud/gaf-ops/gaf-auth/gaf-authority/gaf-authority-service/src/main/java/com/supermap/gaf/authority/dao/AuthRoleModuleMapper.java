/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.authority.dao;

import com.supermap.gaf.authority.commontype.AuthRoleModule;
import com.supermap.gaf.authority.vo.AuthRoleModuleSelectVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 角色模块mapper
 *
 * @author yangdong
 * @date:2021/3/25
 */
@Mapper
@Component
public interface AuthRoleModuleMapper {
    /**
     * 根据id查询角色模块
     *
     * @param roleModuleId 角色模块id
     * @return 角色模块 未查询到则返回null
     */
    AuthRoleModule select(@Param("roleModuleId") String roleModuleId);

    /**
     * 根据角色id及状态查询角色模块
     *
     * @param roleId 角色id
     * @param status 状态
     * @return 角色模块集合
     */
    List<AuthRoleModule> getByRoleId(@Param("roleId") String roleId, @Param("status") Boolean status);


    /**
     * 根据角色id和模块id及状态查询角色模块
     *
     * @param moduleId 模块id
     * @param roleId   角色id
     * @param status   状态
     * @return 角色模块
     */
    AuthRoleModule getByRoleAndModule(@Param("roleId") String roleId, @Param("moduleId") String moduleId, @Param("status") Boolean status);

    /**
     * 分页查询角色模块
     *
     * @param selectVo 分页参数 如偏移量，每页条数
     * @return 角色模块集合
     */
    List<AuthRoleModule> pageList(AuthRoleModuleSelectVo selectVo);

    /**
     * 分页模糊查询接口
     *
     * @param selectVo 分页参数 如偏移量，每页条数, 模糊查询字段名 字段值 排序字段名 排序方式
     * @return 接口集合
     */
    List<AuthRoleModule> searchList(AuthRoleModuleSelectVo selectVo);

    /**
     * 分页查询
     * 针对偏移量过大的分页查询sql优化
     *
     * @param selectVo 分页参数 如偏移量，每页条数
     * @return 角色模块集合
     */
    List<AuthRoleModule> bigOffsetPageList(AuthRoleModuleSelectVo selectVo);

    /**
     * 根据角色id和模块id查询角色模块
     *
     * @param resourceModuleId 模块id
     * @param roleId           角色id
     * @return 角色模块集合
     */
    List<AuthRoleModule> listRoleModule(@Param("roleId") String roleId, @Param("resourceModuleId") String resourceModuleId);

    /**
     * 根据角色id查询角色模块
     *
     * @param roleId 角色id
     * @return 角色模块集合
     */
    List<AuthRoleModule> listByRole(@Param("roleId") String roleId);

    /**
     * 查询角色模块总数
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
     * 新增角色模块
     *
     * @param authRoleModule 角色模块
     * @return 新增的数量
     */
    int insert(AuthRoleModule authRoleModule);

    /**
     * 批量新增角色模块
     *
     * @param authRoleModules 角色模块集合
     * @return 新增的数量
     */
    int batchInsert(List<AuthRoleModule> authRoleModules);

    /**
     * 根据id集合批量删除角色模块
     *
     * @param roleModuleIds 角色模块id集合
     * @return 删除的数量
     */
    int batchDelete(List<String> roleModuleIds);

    /**
     * 批量逻辑删除
     *
     * @param roleModuleIds 角色模块id集合
     * @return 删除的数量
     */
    int batchUpdate(List<String> roleModuleIds);

    /**
     * 根据角色模块id删除角色模块
     *
     * @param roleModuleId 角色模块id
     * @return 影响的行数即删除的数量
     */
    int delete(@Param("roleModuleId") String roleModuleId);

    /**
     * 根据角色id删除角色模块
     *
     * @param roleId 角色id
     * @return 影响的行数即删除的数量
     */
    int deleteByRoleId(@Param("roleId") String roleId);

    /**
     * 更新角色模块
     *
     * @param authRoleModule 角色模块
     * @return 影响的行数即更新的数量
     */
    int update(AuthRoleModule authRoleModule);

}
