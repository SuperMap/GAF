/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.authority.service.impl;

import com.supermap.gaf.authority.commontype.AuthResourceMenu;
import com.supermap.gaf.authority.commontype.AuthResourceModule;
import com.supermap.gaf.authority.commontype.AuthTenant;
import com.supermap.gaf.authority.constant.CommonConstant;
import com.supermap.gaf.authority.dao.AuthResourceMenuMapper;
import com.supermap.gaf.authority.enums.NodeTypeEnum;
import com.supermap.gaf.authority.service.AuthResourceMenuService;
import com.supermap.gaf.authority.util.TreeConvertUtil;
import com.supermap.gaf.authority.vo.AuthResourceMenuSelectVo;
import com.supermap.gaf.authority.vo.TreeNode;
import com.supermap.gaf.shiro.SecurityUtilsExt;
import com.supermap.gaf.shiro.commontypes.ShiroUser;
import com.supermap.gaf.sys.mgt.commontype.SysCatalog;
import com.supermap.gaf.sys.mgt.enums.CatalogTypeEnum;
import com.supermap.gaf.sys.mgt.service.SysCatalogService;
import com.supermap.gaf.data.access.service.BatchSortAndCodeService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 菜单服务实现类
 * @author wxl
 * @date:2021/3/25
 *
 */
@Service
public class AuthResourceMenuServiceImpl implements AuthResourceMenuService {

    private final AuthResourceMenuMapper authResourceMenuMapper;

    private final SysCatalogService sysCatalogService;

    private final BatchSortAndCodeService batchSortAndCodeService;

    public AuthResourceMenuServiceImpl(AuthResourceMenuMapper authResourceMenuMapper, SysCatalogService sysCatalogService, BatchSortAndCodeService batchSortAndCodeService) {
        this.authResourceMenuMapper = authResourceMenuMapper;
        this.sysCatalogService = sysCatalogService;
        this.batchSortAndCodeService = batchSortAndCodeService;
    }


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
        ShiroUser shiroUser = SecurityUtilsExt.getUser();
        String tenantId = Objects.requireNonNull(shiroUser).getTenantId();
        // 平台管理员所在租户获取所有菜单 //其他租户获取
        if (AuthTenant.PLATFORM_DEDAULT_TENANT_ID.equals(tenantId)) {
            List<AuthResourceMenu> authResourceMenus = authResourceMenuMapper.listMenus();
            List<TreeNode> collect = authResourceMenus.stream().map(menu -> {
                TreeNode node = new TreeNode();
                node.setTitle(menu.getName());
                node.setSortSn(menu.getSortSn());
                node.setParentId(menu.getMenuCatalogId());
                node.setType(NodeTypeEnum.MENU.getValue());
                node.setKey(menu.getResourceMenuId());
                return node;
            }).collect(Collectors.toList());

            List<TreeNode> menuCatalogs = sysCatalogService.getNodesByType(CatalogTypeEnum.MENU_GROUP_TYPE.getValue());
            collect.addAll(menuCatalogs);
            return collect;
        } else {
            List<AuthResourceModule> authResourceModules = shiroUser.getAuthResourceModules();
            if (authResourceModules == null || authResourceModules.size() == 0) {
                return new LinkedList<>();
            }
            List<String> moduleIds = authResourceModules.stream().map(AuthResourceModule::getResourceModuleId).collect(Collectors.toList());
            List<AuthResourceMenu> menus = this.authResourceMenuMapper.selectByModuleIds(moduleIds);
            if (menus == null || menus.size() == 0) {
                return new LinkedList<>();
            }
            // 菜单去重
            List<SysCatalog> sysCatalogs = sysCatalogService.getByType(CatalogTypeEnum.MENU_GROUP_TYPE.getValue());
            if (CollectionUtils.isEmpty(sysCatalogs)) {
                return new LinkedList<>();
            }
            Map<String, SysCatalog> allCatalog = sysCatalogs.stream().collect(Collectors.toMap(SysCatalog::getCatalogId, sysCatalog -> sysCatalog));
            //4、根据菜单资源直接关联的目录数据找到上级目录，直至根目录
            Map<String, SysCatalog> needCatalog = new HashMap<>(allCatalog.size());
            for (AuthResourceMenu menu : menus) {
                String parentId = menu.getMenuCatalogId();
                while (true) {
                    if (TreeConvertUtil.ROOT_PARENT_ID.equals(parentId) || StringUtils.isEmpty(parentId)) {
                        break;
                    }
                    SysCatalog sysCatalog = allCatalog.get(parentId);
                    if (sysCatalog != null) {
                        needCatalog.putIfAbsent(sysCatalog.getCatalogId(), sysCatalog);
                        parentId = sysCatalog.getParentId();
                    } else {
                        break;
                    }
                }
            }
            if (MapUtils.isEmpty(needCatalog)) {
                return new LinkedList<>();
            }
            //4、转换为树节点
            List<TreeNode> collect = menus.stream().map(authResourceMenu -> {
                        TreeNode node = new TreeNode();
                        node.setSortSn(authResourceMenu.getSortSn());
                        node.setParentId(authResourceMenu.getMenuCatalogId());
                        node.setKey(authResourceMenu.getResourceMenuId());
                        node.setTitle(authResourceMenu.getName());
                        node.setType(NodeTypeEnum.MENU.getValue());
                        return node;
            }).collect(Collectors.toList());
            List<TreeNode> collect2 = needCatalog.values().stream().map(sysCatalog -> {
                TreeNode node = new TreeNode();
                node.setSortSn(sysCatalog.getSortSn());
                node.setParentId(sysCatalog.getParentId());
                node.setKey(sysCatalog.getCatalogId());
                node.setTitle(sysCatalog.getName());
                node.setType(NodeTypeEnum.CATALOG.getValue());
                return node;
            }).collect(Collectors.toList());
            collect.addAll(collect2);
            return collect;
        }

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
        authResourceMenuMapper.insert(authResourceMenu);
        batchSortAndCodeService.revisionSortSnForInsertOrDelete(AuthResourceMenu.class, Collections.singletonList(authResourceMenu.getMenuCatalogId()));
        return authResourceMenu;
    }

    @Override
    public void batchInsert(List<AuthResourceMenu> authResourceMenus) {
        if (authResourceMenus != null && authResourceMenus.size() > 0) {
            Set<String> parentIds = new HashSet<>();
            authResourceMenus.forEach(authResourceMenu -> {
                parentIds.add(authResourceMenu.getMenuCatalogId());
                authResourceMenu.setResourceMenuId(UUID.randomUUID().toString());
            });
            authResourceMenuMapper.batchInsert(authResourceMenus);
            batchSortAndCodeService.revisionSortSnForInsertOrDelete(AuthResourceMenu.class,parentIds);
        }
    }

    @Override
    public void deleteAuthResourceMenu(String resourceMenuId) {
        authResourceMenuMapper.delete(resourceMenuId);
    }

    @Override
    public void batchDelete(List<String> resourceMenuIds) {
        authResourceMenuMapper.batchDelete(resourceMenuIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AuthResourceMenu updateAuthResourceMenu(AuthResourceMenu authResourceMenu) {

        AuthResourceMenu oldAuthResourceMenu= getRealById(authResourceMenu.getResourceMenuId());
        int count = authResourceMenuMapper.update(authResourceMenu);
        if(count >0 ){
            String parentId = authResourceMenu.getMenuCatalogId();
            if(StringUtils.isEmpty(parentId)){
                parentId = oldAuthResourceMenu.getMenuCatalogId();
            }
            batchSortAndCodeService.revisionSortSnForUpdate(AuthResourceMenu.class,parentId,oldAuthResourceMenu.getSortSn(),authResourceMenu.getSortSn());
        }
        return authResourceMenu;
    }


    @Override
    public Integer countByMenuCatalogId(String menuCatalogId) {
        return authResourceMenuMapper.countByCombination(AuthResourceMenu.builder().menuCatalogId(menuCatalogId).status(true).build());
    }

    @Override
    public List<AuthResourceMenu> getByIds(List<String> menuIdList) {
        return authResourceMenuMapper.selectByIds(menuIdList);
    }

}
