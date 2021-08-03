/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.authority.dao;

import com.supermap.gaf.authority.commontype.AuthRoleMenu;
import com.supermap.gaf.authority.vo.AuthRoleMenuSelectVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 角色菜单mapper
 *
 * @author yangdong
 * @date:2021/3/25
 */
@Mapper
@Component
public interface AuthRoleMenuMapper {
    /**
     * 根据id查询角色菜单
     *
     * @param roleMenuId 角色菜单id
     * @return 角色菜单 未查询到则返回null
     */
    AuthRoleMenu select(@Param("roleMenuId") String roleMenuId);

    /**
     * 根据角色id及状态查询角色菜单
     *
     * @param roleId 角色id
     * @param status 状态
     * @return 角色菜单集合
     */
    List<AuthRoleMenu> getByRoleId(@Param("roleId") String roleId, @Param("status") Boolean status);

    /**
     * 根据角色id和菜单id及状态查询角色菜单
     *
     * @param menuId 菜单id
     * @param roleId 角色id
     * @param status 状态
     * @return 角色菜单
     */
    AuthRoleMenu getByRoleAndMenu(@Param("roleId") String roleId, @Param("menuId") String menuId, @Param("status") Boolean status);

    /**
     * 分页查询角色菜单
     *
     * @param selectVo 分页参数 如偏移量，每页条数
     * @return 角色菜单集合
     */
    List<AuthRoleMenu> pageList(AuthRoleMenuSelectVo selectVo);

    /**
     * 分页模糊查询角色菜单
     *
     * @param selectVo 分页参数 如偏移量，每页条数, 模糊查询字段名 字段值 排序字段名 排序方式
     * @return 角色菜单集合
     */
    List<AuthRoleMenu> searchList(AuthRoleMenuSelectVo selectVo);

    /**
     * 分页查询
     * 针对偏移量过大的分页查询sql优化
     *
     * @param selectVo 分页参数 如偏移量，每页条数
     * @return 角色菜单集合
     */
    List<AuthRoleMenu> bigOffsetPageList(AuthRoleMenuSelectVo selectVo);

    /**
     * 根据角色id和菜单id查询角色菜单
     *
     * @param resourceMenuId 菜单id
     * @param roleId         角色id
     * @return 角色菜单集合
     */
    List<AuthRoleMenu> listRoleMenu(@Param("roleId") String roleId, @Param("resourceMenuId") String resourceMenuId);

    /**
     * 根据角色id查询角色菜单
     *
     * @param roleId 角色id
     * @return 角色菜单集合
     */
    List<AuthRoleMenu> listByRole(@Param("roleId") String roleId);

    /**
     * 查询角色菜单总数
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
     * 新增角色菜单
     *
     * @param authRoleMenu 角色菜单
     * @return 新增的数量
     */
    int insert(AuthRoleMenu authRoleMenu);

    /**
     * 批量新增角色菜单
     *
     * @param authRoleMenus 角色菜单集合
     * @return 新增的数量
     */
    int batchInsert(List<AuthRoleMenu> authRoleMenus);

    /**
     * 根据id集合批量删除角色菜单
     *
     * @param roleMenuIds 角色菜单id集合
     * @return 删除的数量
     */
    int batchDelete(List<String> roleMenuIds);

    /**
     * 批量逻辑删除
     *
     * @param roleMenuIds 角色菜单id集合
     * @return 删除的数量
     */
    int batchUpdate(List<String> roleMenuIds);

    /**
     * 根据角色菜单id删除角色菜单
     *
     * @param roleMenuId 角色菜单id
     * @return 影响的行数即删除的数量
     */
    int delete(@Param("roleMenuId") String roleMenuId);

    /**
     * 更新角色菜单
     *
     * @param authRoleMenu 角色菜单
     * @return 影响的行数即更新的数量
     */
    int update(AuthRoleMenu authRoleMenu);

}
