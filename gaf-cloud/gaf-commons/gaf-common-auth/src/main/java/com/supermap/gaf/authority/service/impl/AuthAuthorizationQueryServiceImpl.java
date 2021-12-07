/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.authority.service.impl;

import com.supermap.gaf.authority.commontype.*;
import com.supermap.gaf.authority.service.*;
import com.supermap.gaf.utils.TreeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author : duke
 * @date:2021/3/25
 * @since 2020/10/25 9:37 PM
 */
@Service
public class AuthAuthorizationQueryServiceImpl implements AuthAuthorizationQueryService {

    @Autowired
    private AuthUserQueryService authUserQueryService;

    @Autowired
    private AuthUserParttimeQueryService authUserParttimeQueryService;
    @Autowired
    private AuthPostRoleQueryService authPostRoleQueryService;

    @Autowired
    private AuthUserRoleQueryService authUserRoleQueryService;


    @Autowired
    private AuthRoleQueryService authRoleQueryService;
    @Autowired
    private AuthRoleMenuQueryService authRoleMenuQueryService;
    @Autowired
    private AuthResourceMenuQueryService authResourceMenuQueryService;

    @Autowired
    private AuthRoleApiQueryService authRoleApiQueryService;
    @Autowired
    private AuthResourceApiQueryService authResourceApiQueryService;


    /**
     * 查询用户权限api列表
     *
     * @param userId
     * @return
     */
    @Override
    public List<AuthResourceApi> listAuthorizationApi(String userId) {
        List<AuthRole> allRoles = listAuthorizationRole(userId);
        List<AuthRoleApi> authRoleApis = new ArrayList<>();
        for (AuthRole role : allRoles) {
            authRoleApis.addAll(authRoleApiQueryService.listByRole(role.getRoleId()));
        }
        Set<String> allApiIds = new HashSet<>();
        authRoleApis.forEach(authRoleApi -> allApiIds.add(authRoleApi.getResourceApiId()));
        if (!allApiIds.isEmpty()) {
            return authResourceApiQueryService.listByIds(allApiIds);
        }
        return Collections.emptyList();
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
            //1、获取人员角色列表
            List<AuthRole> allRoles = listAuthorizationRole(userId);
            //2、获取角色对应菜单关系列表
            final List<AuthRoleMenu> authRoleMenus = new ArrayList<>();
            allRoles.forEach(role -> {
                authRoleMenus.addAll(authRoleMenuQueryService.listByRole(role.getRoleId()));
            });
            // 菜单去重
            Set<String> menuIds = new HashSet<>(authRoleMenus.size());
            authRoleMenus.forEach(authRoleMenu -> menuIds.add(authRoleMenu.getResourceMenuId()));
            List<AuthResourceMenu> authResourceMenus = authResourceMenuQueryService.listByIds(menuIds);
            appendAllSuperior(authResourceMenus);
            return authResourceMenus;
        } catch (Exception e) {
            return null;
        }
    }

    private void appendAllSuperior(List<AuthResourceMenu> authResourceMenus) {
        Set<String> ids = authResourceMenus.stream().map(AuthResourceMenu::getResourceMenuId).collect(Collectors.toSet());
        Set<String> parentIds = authResourceMenus.stream().map(AuthResourceMenu::getParentId).collect(Collectors.toSet());
        while (true) {
            List<String> needSelectIds = parentIds.stream().filter(parentId -> {
                return !ids.contains(parentId) && !StringUtils.isEmpty(parentId) && !TreeUtil.ROOT_PARENT_NODE_KEY.equals(parentId);
            }).collect(Collectors.toList());
            if (needSelectIds.isEmpty()) {
                break;
            }
            List<AuthResourceMenu> parentMenus = authResourceMenuQueryService.listByIds(needSelectIds);
            authResourceMenus.addAll(parentMenus);
            ids.addAll(needSelectIds);
            parentIds = parentMenus.stream().map(AuthResourceMenu::getParentId).collect(Collectors.toSet());
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
