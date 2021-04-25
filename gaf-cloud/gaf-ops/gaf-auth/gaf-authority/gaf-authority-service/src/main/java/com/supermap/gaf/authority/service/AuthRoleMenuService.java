/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.authority.service;

import com.supermap.gaf.authority.commontype.AuthRoleMenu;
import com.supermap.gaf.authority.vo.AuthRoleMenuSelectVo;
import com.supermap.gaf.authority.vo.RoleMenuVo;

import java.util.List;
import java.util.Map;

/**
 * 角色菜单服务类
 * @author yangdong
 * @date:2021/3/25
 *
 */
public interface AuthRoleMenuService {
	
    /**
     * 根据id查询角色菜单
     * @param roleMenuId  角色菜单id
     * @return 角色菜单
     */
    AuthRoleMenu getById(String roleMenuId);

    /**
     * 查询角色已绑定的角色菜单
     * 即根据角色id条件查询角色菜单
     * @param roleId 角色id
     * @return 角色菜单集合 若未查询到则返回空集合
     */
    List<AuthRoleMenu> listByRole(String roleId);

    /**
     * 更新某角色与菜单的关联关系
     * 传入角色id和菜单id列表，查询到该角色已绑定的菜单，与传入的菜单id列表做对比更新
     * @param roleMenuVo 角色菜单关联对象
     */
    void handleRoleMenu(RoleMenuVo roleMenuVo);

    /**
     * 根据角色id和状态查询角色菜单
     * @param roleId 角色id
     * @param status true 有效 false 无效
     * @return 角色菜单集合 若未查询到则返回空集合
     */
    List<AuthRoleMenu> getByRoleId(String roleId, Boolean status);
	
    /**
     * 分页查询角色菜单
     * @param authRoleMenuSelectVo 分页参数 如偏移量，每页条数
     * @return 分页结果 如查询到的总数 分页后的结果
     */
    Map<String,Object> pageList(AuthRoleMenuSelectVo authRoleMenuSelectVo);

    /**
     * 分页模糊查询角色菜单
     * @param authRoleMenuSelectVo 分页参数 如偏移量，每页条数, 模糊查询字段名 字段值 排序字段名 排序方式
     * @return 分页结果 如查询到的总数 分页后的结果
     */
    Map<String,Object> searchList(AuthRoleMenuSelectVo authRoleMenuSelectVo);

    /**
     * 新增角色菜单
     * @param authRoleMenu 角色菜单
     * @return 角色菜单
     */
    AuthRoleMenu insertAuthRoleMenu(AuthRoleMenu authRoleMenu);


    /**
     * 批量插入角色菜单
     * @param authRoleMenus 角色菜单集合
     */
    void batchInsertRoleMenu(List<AuthRoleMenu> authRoleMenus);

    /**
     * 根据角色菜单id删除角色菜单
     * @param roleMenuId 角色菜单id
     */
    void deleteAuthRoleMenu(String roleMenuId);

    /**
     * 根据角色菜单id集合批量删除
     * @param roleMenuIds 角色菜单id集合
     */
    void batchDelete(List<String> roleMenuIds);

    /**
     * 更新角色菜单
     * @param authRoleMenu 角色菜单
     * @return 角色菜单
     */
    AuthRoleMenu updateAuthRoleMenu(AuthRoleMenu authRoleMenu);

}
