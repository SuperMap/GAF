/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.authority.service.impl;

import com.supermap.gaf.authority.commontype.*;
import com.supermap.gaf.authority.service.*;
import com.supermap.gaf.sys.mgt.commontype.SysCatalog;
import com.supermap.gaf.sys.mgt.enums.CatalogTypeEnum;
import com.supermap.gaf.sys.mgt.service.SysCatalogQueryService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author : duke
 * @date:2021/3/25
 * @since 2020/10/25 9:37 PM
 */
@Service
public class AuthAuthorizationQueryServiceImpl implements AuthAuthorizationQueryService {

    private static final String ROOT_PARENT_ID = "0";

    @Autowired
    private AuthUserQueryService authUserQueryService;
    @Autowired
    private AuthPostRoleQueryService authPostRoleQueryService;
    @Autowired
    private AuthUserRoleQueryService authUserRoleQueryService;
    @Autowired
    private AuthRoleModuleQueryService authRoleModuleQueryService;
    @Autowired
    private AuthModuleApiQueryService authModuleApiQueryService;
    @Autowired
    private AuthRoleApiQueryService authRoleApiQueryService;
    @Autowired
    private AuthUserParttimeQueryService authUserParttimeQueryService;
    @Autowired
    private AuthResourceApiQueryService authResourceApiQueryService;
    @Autowired
    private AuthResourceModuleQueryService authResourceModuleQueryService;
    @Autowired
    private AuthResourceMenuQueryService authResourceMenuQueryService;
    @Autowired
    private AuthRoleMenuQueryService authRoleMenuQueryService;
    @Autowired
    private AuthRoleQueryService authRoleQueryService;
    @Autowired
    private SysCatalogQueryService sysCatalogQueryService;


    /**
     * 查询用户权限api列表
     *
     * @param userId
     * @return
     */
    @Override
    public List<AuthResourceApi> listAuthorizationApi(String userId) {
        List<AuthRole> allRoles = listAuthorizationRole(userId);
        //5.d in c,获取d绑定的所有模块资源
        List<AuthRoleModule> authRoleModules = new ArrayList<>();
        for (AuthRole role : allRoles) {
            authRoleModules.addAll(authRoleModuleQueryService.listByRole(role.getRoleId()));
        }
        //5.1获取d绑定的api列表api1
        List<AuthModuleApi> authModuleApis = new ArrayList<>();
        for (AuthRoleModule module : authRoleModules) {
            authModuleApis.addAll(authModuleApiQueryService.getByModuleId(module.getResourceModuleId(), true));
        }
        //6.d in c,获取d绑定的api列表api2
        List<AuthRoleApi> authRoleApis = new ArrayList<>();
        for (AuthRole role : allRoles) {
            authRoleApis.addAll(authRoleApiQueryService.listByRole(role.getRoleId()));
        }
        //7.角色d所有d.api=api1+api2
        Set<String> allApiIds = new HashSet<>();
        authModuleApis.forEach(authModuleApi -> allApiIds.add(authModuleApi.getResourceApiId()));
        authRoleApis.forEach(authRoleApi -> allApiIds.add(authRoleApi.getResourceApiId()));
        //8.获取角色列表c的所有api
        //9.普通用户权限api列表c.api=[d.api]
        List<AuthResourceApi> resourceApis = new ArrayList<>();
        for (String apiId : allApiIds) {
            resourceApis.add(authResourceApiQueryService.getById(apiId));
        }
        return resourceApis;
    }

    /**
     * 查询用户权限module列表
     *
     * @param userId
     * @return
     */
    @Override
    public List<AuthResourceModule> listAuthorizationModule(String userId) {
        try {
            List<AuthResourceMenu> menus = listAuthorizationMenu(userId);
            if (CollectionUtils.isEmpty(menus)) {
                return null;
            }

            Set<String> moduleIds = new HashSet<>();
            List<AuthResourceModule> moduleList = new LinkedList<>();
            menus.forEach(authResourceMenu -> {
                AuthResourceModule authResourceModule = authResourceMenu.getAuthResourceModule();
                if (authResourceModule != null) {
                    if (!moduleIds.contains(authResourceModule.getResourceModuleId())) {
                        moduleIds.add(authResourceModule.getResourceModuleId());
                        moduleList.add(authResourceModule);
                    }
                }
            });
            return moduleList;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 查询用户menu列表
     *
     * @param userId
     * @return
     */
    @Override
    public List<AuthResourceMenu> listAuthorizationMenu(String userId) {
        try {
            //0、获取平台下所有有效的菜单分组
            List<SysCatalog> sysCatalogs = sysCatalogQueryService.getByType(null, CatalogTypeEnum.MENU_GROUP_TYPE.getValue());
            if (CollectionUtils.isEmpty(sysCatalogs)) {
                return null;
            }
            // 目录list转map
            Map<String, SysCatalog> sysCatalogMap = sysCatalogs.stream().collect(Collectors.toMap(SysCatalog::getCatalogId, sysCatalog -> sysCatalog));
            final List<AuthResourceMenu> authResourceMenus = new ArrayList<>();
            //1、获取人员角色列表
            List<AuthRole> allRoles = listAuthorizationRole(userId);
            //2、获取角色对应菜单关系列表
            final List<AuthRoleMenu> authRoleMenus = new ArrayList<>();
            allRoles.forEach(role -> {
                authRoleMenus.addAll(authRoleMenuQueryService.listByRole(role.getRoleId()));
            });
            // 菜单去重
            List<AuthRoleMenu> distinctRoleMenus = new LinkedList<>();
            Set<String> menuIds = new HashSet<>(authRoleMenus.size());
            authRoleMenus.forEach(authRoleMenu -> {
                if (!menuIds.contains(authRoleMenu.getResourceMenuId())) {
                    menuIds.add(authRoleMenu.getResourceMenuId());
                    distinctRoleMenus.add(authRoleMenu);
                }
            });
            // 模块资源直接关联的目录数据
            Map<String, SysCatalog> latestSysCatalogMap = new HashMap<>();
            //3、获取菜单列表
            distinctRoleMenus.forEach(authRoleMenu -> {
                AuthResourceMenu authResourceMenu = authResourceMenuQueryService.getById(authRoleMenu.getResourceMenuId());
                if (null != authResourceMenu && authResourceMenu.getStatus()) {
                    // 菜单关联的模块
                    AuthResourceModule authResourceModule = authResourceModuleQueryService.getById(authResourceMenu.getResourceModuleId());
                    // 排除不存在上级目录的模块资源
                    SysCatalog sysCatalog = sysCatalogMap.get(authResourceMenu.getMenuCatalogId());
                    if (null != sysCatalog && sysCatalog.getStatus() && null != authResourceModule && authResourceModule.getStatus()) {
                        authResourceMenu.setAuthResourceModule(authResourceModule);
                        authResourceMenus.add(authResourceMenu);
                        latestSysCatalogMap.put(sysCatalog.getCatalogId(), sysCatalog);
                    }
                }
            });
            //4、根据菜单资源直接关联的目录数据找到上级目录，直至根目录
            Map<String, SysCatalog> menuCatalogMap = new HashMap<>();
            if (MapUtils.isNotEmpty(latestSysCatalogMap)) {
                for (SysCatalog sysCatalog : latestSysCatalogMap.values()) {
                    findParentSysCatalog(sysCatalogMap, menuCatalogMap, sysCatalog);
                }
            }
            // 未找到任何的目录，直接返回null
            if (MapUtils.isEmpty(menuCatalogMap)) {
                return null;
            }
            //4、目录数据转为模块数据结构，作为菜单一部分
            for (SysCatalog sysCatalog : menuCatalogMap.values()) {
                AuthResourceMenu authResourceMenu = AuthResourceMenu.builder()
                        .resourceMenuId(sysCatalog.getCatalogId())
                        .menuCatalogId(sysCatalog.getParentId())
                        .name(sysCatalog.getName())
                        .icon(sysCatalog.getIconUrl())
                        .sortSn(sysCatalog.getSortSn())
                        .authResourceModule(null)
                        .build();
                authResourceMenus.add(authResourceMenu);
            }
            return authResourceMenus;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 查找当前目录的上级目录
     *
     * @param sysCatalogMap     所有的目录数据
     * @param allSysCatalogMap  找出的符合条件的目录数据集合
     * @param currentSysCatalog 当前目录
     */
    private void findParentSysCatalog(Map<String, SysCatalog> sysCatalogMap, Map<String, SysCatalog> allSysCatalogMap, SysCatalog currentSysCatalog) {
        allSysCatalogMap.put(currentSysCatalog.getCatalogId(), currentSysCatalog);
        // 还存在上级目录，继续查找
        if (!currentSysCatalog.getParentId().equals(ROOT_PARENT_ID)) {
            SysCatalog parentSysCatalog = sysCatalogMap.get(currentSysCatalog.getParentId());
            if (null != parentSysCatalog) {
                findParentSysCatalog(sysCatalogMap, allSysCatalogMap, parentSysCatalog);
            }
        }
    }

    @Override
    public List<AuthRole> listAuthorizationRole(String userId) {
        //0.获取用户user
        AuthUser authUser = authUserQueryService.getById(userId);
        //1.获取用户岗位post
        String postId = authUser.getPostId();
        //1.1获取挂职表用户岗位post
        List<AuthUserParttime> authUserParttimes = authUserParttimeQueryService.listByUserId(userId);
        //2.获取用户岗位所有角色列表a
        List<AuthPostRole> userPostRoles = authPostRoleQueryService.listByPost(postId);
        List<AuthPostRole> userPattimePostRoles = new ArrayList<>();
        for (AuthUserParttime authUserParttime : authUserParttimes) {
            userPattimePostRoles.addAll(authPostRoleQueryService.listByPost(authUserParttime.getPostId()));
        }
        Set<AuthPostRole> postRoles = new HashSet<>();
        postRoles.addAll(userPostRoles);
        postRoles.addAll(userPattimePostRoles);
        //3.获取用户单独绑定的角色列表b
        List<AuthUserRole> userRoles = authUserRoleQueryService.listByUser(userId);
        //4.取得a、b角色列表并集c
        Set<String> postRoleIds = postRoles.stream().map(AuthPostRole::getRoleId).collect(Collectors.toSet());
        Set<String> userRoleIds = userRoles.stream().map(AuthUserRole::getRoleId).collect(Collectors.toSet());
        Set<String> allRoleIds = new HashSet<>();
        allRoleIds.addAll(postRoleIds);
        allRoleIds.addAll(userRoleIds);

        List<AuthRole> authRoles = new ArrayList<>();
        allRoleIds.forEach(roleId -> {
            AuthRole authRole = authRoleQueryService.getById(roleId);
            if (authRole != null) {
                authRoles.add(authRole);
            }
        });
        return authRoles;
    }


}
