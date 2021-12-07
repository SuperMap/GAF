/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.authority.service.impl;

import com.supermap.gaf.authority.commontype.AuthResourceMenu;
import com.supermap.gaf.authority.commontype.AuthResourceMenuNode;
import com.supermap.gaf.authority.commontype.AuthTenant;
import com.supermap.gaf.authority.constant.CommonConstant;
import com.supermap.gaf.authority.dao.AuthResourceMenuMapper;
import com.supermap.gaf.authority.enums.NodeTypeEnum;
import com.supermap.gaf.authority.service.AuthResourceMenuService;
import com.supermap.gaf.authority.service.AuthRoleMenuService;
import com.supermap.gaf.authority.vo.AuthResourceMenuSelectVo;
import com.supermap.gaf.authority.vo.TreeNode;
import com.supermap.gaf.data.access.service.BatchSortAndCodeService;
import com.supermap.gaf.shiro.SecurityUtilsExt;
import com.supermap.gaf.shiro.commontypes.ShiroUser;
import com.supermap.gaf.utils.TreeUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 菜单服务实现类
 *
 * @author wxl
 * @date:2021/3/25
 */
@Service
public class AuthResourceMenuServiceImpl implements AuthResourceMenuService {

    @Autowired
    private AuthResourceMenuMapper authResourceMenuMapper;

    @Autowired
    private AuthRoleMenuService authRoleMenuService;

    @Autowired
    private BatchSortAndCodeService batchSortAndCodeService;


    @Override
    public AuthResourceMenu getById(String resourceMenuId) {
        if (resourceMenuId == null) {
            throw new IllegalArgumentException("resourceMenuId不能为空");
        }
        return authResourceMenuMapper.select(resourceMenuId);
    }

    @Override
    public AuthResourceMenu getRealById(String resourceMenuId) {
        if (resourceMenuId == null) {
            throw new IllegalArgumentException("resourceMenuId不能为空");
        }
        return authResourceMenuMapper.selectReal(resourceMenuId);
    }

    @Override
    public List<TreeNode> getMenuNodes() {
        List<AuthResourceMenu> authResourceMenus = listMenus(null);
        return authResourceMenus.stream().map(authResourceMenu -> {
            TreeNode node = new TreeNode();
            node.setSortSn(authResourceMenu.getSortSn());
            // todo: 修改
            if (Objects.isNull(authResourceMenu.getParentId())) {
                node.setParentId(TreeUtil.ROOT_PARENT_NODE_KEY);
            } else {
                node.setParentId(authResourceMenu.getParentId());
            }
            node.setKey(authResourceMenu.getResourceMenuId());
            node.setTitle(authResourceMenu.getName());
            node.setType(NodeTypeEnum.MENU.getValue());
            return node;
        }).collect(Collectors.toList());

    }


    @Override
    public Map<String, Object> pageList(AuthResourceMenuSelectVo authResourceMenuSelectVo) {
        if (authResourceMenuSelectVo.getPageSize() == null || authResourceMenuSelectVo.getPageSize() == 0) {
            authResourceMenuSelectVo.setPageSize(50);
        }
        List<AuthResourceMenu> pageList;
        if (authResourceMenuSelectVo.getOffset() == null || authResourceMenuSelectVo.getOffset() < CommonConstant.OFFSET_MAX_FOR_SQL_BETTER) {
            pageList = authResourceMenuMapper.pageList(authResourceMenuSelectVo);
        } else {
            pageList = authResourceMenuMapper.bigOffsetPageList(authResourceMenuSelectVo);
        }
        int totalCount = authResourceMenuMapper.pageListCount();
        Map<String, Object> result = new HashMap<>(2);
        result.put("pageList", pageList);
        result.put("totalCount", totalCount);
        return result;
    }

    @Override
    public Map<String, Object> searchList(AuthResourceMenuSelectVo authResourceMenuSelectVo) {
        if (authResourceMenuSelectVo.getPageSize() == null || authResourceMenuSelectVo.getPageSize() == 0) {
            authResourceMenuSelectVo.setPageSize(50);
        }
        List<AuthResourceMenu> pageList;
        pageList = authResourceMenuMapper.searchList(authResourceMenuSelectVo);
        Integer totalCount = authResourceMenuMapper.countOneField(authResourceMenuSelectVo.getSearchFieldName(), authResourceMenuSelectVo.getSearchFieldValue());
        Map<String, Object> result = new HashMap<>(2);
        result.put("pageList", pageList);
        result.put("totalCount", totalCount);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AuthResourceMenu insertAuthResourceMenu(AuthResourceMenu authResourceMenu) {
        authResourceMenu.setResourceMenuId(UUID.randomUUID().toString());
        String name = SecurityUtilsExt.getUser().getAuthUser().getName();
        Date now = new Date();
        authResourceMenu.setCreatedTime(now);
        authResourceMenu.setCreatedBy(name);
        authResourceMenu.setUpdatedTime(now);
        authResourceMenu.setUpdatedBy(name);
        authResourceMenuMapper.insert(authResourceMenu);
        batchSortAndCodeService.revisionSortSnForInsertOrDelete(AuthResourceMenu.class, Collections.singletonList(authResourceMenu.getParentId()));
        return authResourceMenu;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void batchInsert(List<AuthResourceMenu> authResourceMenus) {
        if (authResourceMenus != null && authResourceMenus.size() > 0) {
            Set<String> parentIds = new HashSet<>();
            String name = SecurityUtilsExt.getUser().getAuthUser().getName();
            Date now = new Date();
            authResourceMenus.forEach(authResourceMenu -> {
                parentIds.add(authResourceMenu.getParentId());
                authResourceMenu.setCreatedTime(now);
                authResourceMenu.setCreatedBy(name);
                authResourceMenu.setUpdatedTime(now);
                authResourceMenu.setUpdatedBy(name);
                authResourceMenu.setResourceMenuId(UUID.randomUUID().toString());
            });
            authResourceMenuMapper.batchInsert(authResourceMenus);
            batchSortAndCodeService.revisionSortSnForInsertOrDelete(AuthResourceMenu.class, parentIds);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteAuthResourceMenu(String resourceMenuId) {
        AuthResourceMenu resourceMenu = getById(resourceMenuId);
        if (resourceMenu == null) {
            return;
        }
        Set<String> allIds = new HashSet<>();
        listChildrenIdsByParentIds(Collections.singletonList(resourceMenuId),allIds);
        allIds.add(resourceMenuId);
        authResourceMenuMapper.deleteByIds(allIds);
        batchSortAndCodeService.revisionSortSnForInsertOrDelete(AuthResourceMenu.class, Collections.singleton(resourceMenu.getParentId()));
        // 删除角色菜单
        authRoleMenuService.removeByMenuIds(allIds);
    }


    private void listChildrenIdsByParentIds(List<String> parentIds,Set<String> allIds) {
        List<String> ids = authResourceMenuMapper.selectIdsByParentIds(parentIds);
        allIds.addAll(ids);
        if (!ids.isEmpty()) {
            listChildrenIdsByParentIds(ids,allIds);
        }
    }



    @Transactional(rollbackFor = Exception.class)
    @Override
    public void batchDelete(List<String> resourceMenuIds) {
        if (resourceMenuIds == null || resourceMenuIds.isEmpty()) return;
        resourceMenuIds.forEach(this::deleteAuthResourceMenu);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AuthResourceMenu updateAuthResourceMenu(AuthResourceMenu authResourceMenu) {
        AuthResourceMenu oldAuthResourceMenu = getRealById(authResourceMenu.getResourceMenuId());
        String name = SecurityUtilsExt.getUser().getAuthUser().getName();
        authResourceMenu.setUpdatedBy(name);
        authResourceMenu.setUpdatedTime(new Date());
        int count = authResourceMenuMapper.update(authResourceMenu);
        if (count > 0) {
            if (Objects.equals(authResourceMenu.getParentId(),oldAuthResourceMenu.getParentId())) {
                batchSortAndCodeService.revisionSortSnForUpdate(AuthResourceMenu.class, oldAuthResourceMenu.getParentId(), oldAuthResourceMenu.getSortSn(), authResourceMenu.getSortSn());
            } else {
                batchSortAndCodeService.revisionSortSnForInsertOrDelete(AuthResourceMenu.class, Collections.singleton(authResourceMenu.getParentId()));
                batchSortAndCodeService.revisionSortSnForInsertOrDelete(AuthResourceMenu.class, Collections.singleton(oldAuthResourceMenu.getParentId()));
            }
        }
        return authResourceMenu;
    }


    @Override
    public List<AuthResourceMenu> listByIds(List<String> menuIdList) {
        return authResourceMenuMapper.selectByIds(menuIdList);
    }

    @Override
    public List<AuthResourceMenuNode> getMenuTree(String name) {
        List<AuthResourceMenu> authResourceMenus = listMenus(name);
        List<AuthResourceMenuNode> nodes = authResourceMenus.stream().map(menu -> {
            AuthResourceMenuNode node = new AuthResourceMenuNode();
            BeanUtils.copyProperties(menu, node);
            return node;
        }).collect(Collectors.toList());
        return TreeUtil.build(nodes, Comparator.comparingInt(AuthResourceMenu::getSortSn));
    }

    private List<AuthResourceMenu> listMenus(String name) {
        ShiroUser shiroUser = SecurityUtilsExt.getUser();
        String tenantId = Objects.requireNonNull(shiroUser).getTenantId();
        // 平台管理员所在租户获取所有菜单 //其他租户获取
        List<AuthResourceMenu> authResourceMenus;
        if (AuthTenant.PLATFORM_DEDAULT_TENANT_ID.equals(tenantId)) {
            authResourceMenus = authResourceMenuMapper.listMenusByName(name);
            appendAllSuperior(authResourceMenus);
        } else {
            authResourceMenus = shiroUser.getAuthResourceMenus();
        }
        return authResourceMenus;
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
            List<AuthResourceMenu> parentMenus = listByIds(needSelectIds);
            authResourceMenus.addAll(parentMenus);
            ids.addAll(needSelectIds);
            parentIds = parentMenus.stream().map(AuthResourceMenu::getParentId).collect(Collectors.toSet());
        }
    }

}
