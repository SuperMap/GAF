/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.authority.service;

import com.supermap.gaf.authority.commontype.AuthRoleModule;
import com.supermap.gaf.authority.vo.AuthRoleModuleSelectVo;
import com.supermap.gaf.authority.vo.RoleModuleVo;

import java.util.List;
import java.util.Map;

/**
 * @author yangdong
 * @date:2021/3/25
 * 角色模块服务类
 */
public interface AuthRoleModuleService {
	
    /**
     * 根据id查询角色模块
     * @param roleModuleId  角色模块id
     * @return 角色模块
     */
    AuthRoleModule getById(String roleModuleId);

    /**
     * 查询角色已绑定的角色模块
     * 即根据角色id条件查询角色模块
     * @param roleId 角色id
     * @return 角色模块集合 若未查询到则返回空集合
     */
    List<AuthRoleModule> listByRole(String roleId);

    /**
     * 更新某角色与模块的关联关系
     * 传入角色id和模块id列表，查询到该角色已绑定的模块，与传入的模块id列表做对比更新
     * @param roleModuleVo  角色模块关联对象
     */
    void handleRoleModule(RoleModuleVo roleModuleVo);

    /**
     * 根据角色id和状态查询角色模块
     * @param roleId 角色id
     * @param status true 有效 false 无效
     * @return 角色模块集合 若未查询到则返回空集合
     */
    List<AuthRoleModule> getByRoleId(String roleId, Boolean status);

    /**
     * 分页查询角色模块
     * @param authRoleModuleSelectVo 分页参数 如偏移量，每页条数
     * @return 分页结果 如查询到的总数 分页后的结果
     */
    Map<String,Object> pageList(AuthRoleModuleSelectVo authRoleModuleSelectVo);

    /**
     * 分页模糊查询角色模块
     * @param authRoleModuleSelectVo 分页参数 如偏移量，每页条数, 模糊查询字段名 字段值 排序字段名 排序方式
     * @return 分页结果 如查询到的总数 分页后的结果
     */
    Map<String,Object> searchList(AuthRoleModuleSelectVo authRoleModuleSelectVo);

    /**
     * 新增角色模块
     * @param authRoleModule 角色模块
     * @return 角色模块
     */
    AuthRoleModule insertAuthRoleModule(AuthRoleModule authRoleModule);

       /**
     * 批量插入角色模块
     * @param authRoleModules 角色模块集合
     */
    void batchInsertRoleModule(List<AuthRoleModule> authRoleModules);

    /**
     * 根据角色模块id删除角色模块
     * @param roleModuleId 角色模块id
     */
    void deleteAuthRoleModule(String roleModuleId);

    /**
     * 根据角色模块id集合批量删除
     * @param roleModuleIds 角色模块id集合
     */
    void batchDelete(List<String> roleModuleIds);
    /**
     * 更新角色模块
     * @param authRoleModule 角色模块
     * @return 角色模块
     */
    AuthRoleModule updateAuthRoleModule(AuthRoleModule authRoleModule);

}
