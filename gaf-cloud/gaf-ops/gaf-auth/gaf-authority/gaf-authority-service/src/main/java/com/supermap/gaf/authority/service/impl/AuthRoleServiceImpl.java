/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.authority.service.impl;

import com.supermap.gaf.authority.commontype.AuthRole;
import com.supermap.gaf.authority.commontype.AuthTenant;
import com.supermap.gaf.authority.constant.CacheGroupConstant;
import com.supermap.gaf.authority.constant.CommonConstant;
import com.supermap.gaf.authority.constant.DbFieldNameConstant;
import com.supermap.gaf.authority.dao.AuthRoleMapper;
import com.supermap.gaf.authority.enums.AuthRoleTypeEnum;
import com.supermap.gaf.authority.enums.NodeTypeEnum;
import com.supermap.gaf.authority.service.AuthRoleService;
import com.supermap.gaf.authority.util.TreeConvertUtil;
import com.supermap.gaf.authority.vo.AuthRoleSelectVo;
import com.supermap.gaf.authority.vo.TreeNode;
import com.supermap.gaf.data.access.service.BatchSortAndCodeService;
import com.supermap.gaf.exception.GafException;
import com.supermap.gaf.shiro.SecurityUtilsExt;
import com.supermap.gaf.shiro.commontypes.ShiroUser;
import com.supermap.gaf.sys.mgt.commontype.SysCatalog;
import com.supermap.gaf.sys.mgt.enums.CatalogTypeEnum;
import com.supermap.gaf.sys.mgt.service.SysCatalogService;
import org.springframework.aop.framework.AopContext;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 角色服务实现类
 * @author zhm
 * @date:2021/3/25
 *
 */
@Service
public class AuthRoleServiceImpl implements AuthRoleService {
    private final AuthRoleMapper authRoleMapper;

    private final SysCatalogService sysCatalogService;
    private final BatchSortAndCodeService batchSortAndCodeService;

    public AuthRoleServiceImpl(AuthRoleMapper authRoleMapper, SysCatalogService sysCatalogService, BatchSortAndCodeService batchSortAndCodeService) {
        this.authRoleMapper = authRoleMapper;
        this.sysCatalogService = sysCatalogService;
        this.batchSortAndCodeService = batchSortAndCodeService;
    }

    @Override
    public AuthRole getById(String roleId) {
        if (StringUtils.isEmpty(roleId)) {
            throw new GafException("roleId不能为空");
        }
        return authRoleMapper.select(roleId);
    }

    @Override
    public List<TreeNode> getRoleTree() {
        ShiroUser shiroUser = SecurityUtilsExt.getUser();
        String tenantId = Objects.requireNonNull(shiroUser).getTenantId();
        List<AuthRole> authRoles = authRoleMapper.listRoles(tenantId);
        List<TreeNode> collect = authRoles.stream().map(role -> {
            TreeNode node = new TreeNode();
            node.setTitle(role.getRoleName());
            node.setSortSn(role.getSortSn());
            node.setParentId(role.getRoleCatalogId());
            node.setType(NodeTypeEnum.ROLE.getValue());
            node.setKey(role.getRoleId());
            return node;
        }).collect(Collectors.toList());
        List<TreeNode> roleCatalogs = sysCatalogService.getNodesByType("3");
        collect.addAll(roleCatalogs);
        if (!AuthTenant.PLATFORM_DEDAULT_TENANT_ID.equals(tenantId)) {
            // 再获取内置租户（即平台管理员所在的租户） 的角色组 和角色 （排除平台管理员角色）
            List<AuthRole> defaultTenantRoles = authRoleMapper.listRoles(AuthTenant.PLATFORM_DEDAULT_TENANT_ID);
            List<TreeNode> roleTreeNodes = defaultTenantRoles.stream()
                    .filter(authRole -> !AuthRole.PLATFORM_ADMIN.getRoleId().equals(authRole.getRoleId()))
                    .map(role -> {
                        TreeNode node = new TreeNode();
                        node.setTitle(role.getRoleName());
                        node.setSortSn(role.getSortSn());
                        node.setParentId(role.getRoleCatalogId());
                        node.setType(NodeTypeEnum.ROLE.getValue());
                        node.setKey(role.getRoleId());
                        return node;
                    }).collect(Collectors.toList());
            List<SysCatalog> roleGroup = sysCatalogService.getByCombination(SysCatalog.builder()
                    .type(CatalogTypeEnum.ROLE_GROUP_TYPE.getValue())
                    .status(true)
                    .tenantId(AuthTenant.PLATFORM_DEDAULT_TENANT_ID)
                    .build()
            );
            List<TreeNode> roleGroupNodes = roleGroup.stream().map(sysCatalog -> {
                TreeNode node = new TreeNode();
                node.setTitle(sysCatalog.getName());
                node.setType(NodeTypeEnum.CATALOG.getValue());
                node.setKey(sysCatalog.getCatalogId());
                node.setParentId(sysCatalog.getParentId());
                if (TreeConvertUtil.ROOT_PARENT_ID.equals(sysCatalog.getParentId())) {
                    node.setSortSn(0);
                }
                return node;
            }).collect(Collectors.toList());
            collect.addAll(roleTreeNodes);
            collect.addAll(roleGroupNodes);
        }
        return collect;
    }

    @Override
    public List<TreeNode> getRoleTreeWithOutInnerRole() {
        ShiroUser shiroUser = SecurityUtilsExt.getUser();
        String tenantId = Objects.requireNonNull(shiroUser).getTenantId();
        List<AuthRole> authRoles = authRoleMapper.listRoles(tenantId);
        List<TreeNode> collect = authRoles.stream().map(role -> {
            TreeNode node = new TreeNode();
            node.setTitle(role.getRoleName());
            node.setSortSn(role.getSortSn());
            node.setParentId(role.getRoleCatalogId());
            node.setType(NodeTypeEnum.ROLE.getValue());
            node.setKey(role.getRoleId());
            return node;
        }).collect(Collectors.toList());
        List<TreeNode> roleCatalogs = sysCatalogService.getNodesByType("3");
        collect.addAll(roleCatalogs);
        return collect;
    }

    @Cacheable(value = CacheGroupConstant.ROLE, key = "#roleCatalogId", unless = "#result == null")
    @Override
    public List<AuthRole> listByRoleCatalog(String roleCatalogId) {
        if (StringUtils.isEmpty(roleCatalogId)) {
            throw new GafException("roleCatalogId不能为空");
        }
        AuthRoleSelectVo authRoleSelectVo = AuthRoleSelectVo.builder()
                .searchFieldName(DbFieldNameConstant.ROLE_CATALOG_ID)
                .searchFieldValue(roleCatalogId)
                .build();
        return authRoleMapper.searchWholeMatchList(authRoleSelectVo);
    }

    @Override
    public Map<String, Object> pageList(AuthRoleSelectVo authRoleSelectVo) {
        if (authRoleSelectVo.getPageSize() == null || authRoleSelectVo.getPageSize() == 0) {
            authRoleSelectVo.setPageSize(10);
        }
        List<AuthRole> pageList;
        if (authRoleSelectVo.getOffset() == null || authRoleSelectVo.getOffset() < CommonConstant.OFFSET_MAX_FOR_SQL_BETTER) {
            pageList = authRoleMapper.pageList(authRoleSelectVo);
        } else {
            pageList = authRoleMapper.bigOffsetPageList(authRoleSelectVo);
        }
        int totalCount = authRoleMapper.pageListCount();
        Map<String, Object> result = new HashMap<>(2);
        result.put(DbFieldNameConstant.PAGE_LIST, pageList);
        result.put(DbFieldNameConstant.TOTAL_COUNT, totalCount);
        return result;
    }

    @Override
    public Map<String, Object> searchList(AuthRoleSelectVo authRoleSelectVo) {
        if (authRoleSelectVo.getPageSize() == null || authRoleSelectVo.getPageSize() == 0) {
            authRoleSelectVo.setPageSize(50);
        }
        ShiroUser shiroUser = SecurityUtilsExt.getUser();
        String tenantId = Objects.requireNonNull(shiroUser).getTenantId();
        List<AuthRole> pageList = authRoleMapper.searchList(authRoleSelectVo, tenantId);
        Integer totalCount = authRoleMapper.countOneField(authRoleSelectVo.getSearchFieldName(), authRoleSelectVo.getSearchFieldValue());
        Map<String, Object> result = new HashMap<>(2);
        result.put(DbFieldNameConstant.PAGE_LIST, pageList);
        result.put(DbFieldNameConstant.TOTAL_COUNT, totalCount);
        return result;
    }

    @CacheEvict(value = CacheGroupConstant.ROLE, key = "#authRole.roleCatalogId")
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AuthRole insertAuthRole(AuthRole authRole) {
        // 此处创建的角色均是‘租户级’
        ShiroUser shiroUser = SecurityUtilsExt.getUser();
        String tenantId = Objects.requireNonNull(shiroUser).getTenantId();
        authRole.setTenantId(tenantId);
        authRole.setCreatedBy(shiroUser.getAuthUser().getName());
        authRole.setType(AuthRoleTypeEnum.Tenant.typeValue);
        authRole.setRoleId(UUID.randomUUID().toString());

        // 唯一性校验
        checkUniqueness(authRole, false);

        authRoleMapper.insert(authRole);
        batchSortAndCodeService.revisionSortSnForInsertOrDelete(AuthRole.class, Collections.singletonList(authRole.getRoleCatalogId()));
        return authRole;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void batchInsert(List<AuthRole> authRoles) {
        if (authRoles != null && authRoles.size() > 0) {
            authRoles.forEach(authRole -> ((AuthRoleService) AopContext.currentProxy()).insertAuthRole(authRole));
        }
    }

    @CacheEvict(value = CacheGroupConstant.ROLE, key = "#roleCatalogId")
    @Override
    public void deleteAuthRole(String roleCatalogId, String roleId) {
        if (StringUtils.isEmpty(roleCatalogId)) {
            throw new GafException("删除失败，角色组标识缺失");
        }
        if (StringUtils.isEmpty(roleId)) {
            throw new GafException("删除失败，角色标识缺失");
        }
        authRoleMapper.delete(roleId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void batchDelete(String roleCatalogId, List<String> roleIds) {
        if (!CollectionUtils.isEmpty(roleIds)) {
            roleIds.forEach(roleId -> ((AuthRoleService) AopContext.currentProxy()).deleteAuthRole(roleCatalogId, roleId));
        }
    }

    @CacheEvict(value = CacheGroupConstant.ROLE, key = "#authRole.roleCatalogId")
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AuthRole updateAuthRole(AuthRole authRole) {
        AuthRole authRoleExist = getById(authRole.getRoleId());
        if (null == authRoleExist || !authRoleExist.getStatus()) {
            throw new GafException("更新失败，不是有效的角色");
        }
        checkUniqueness(authRole, true);
        authRoleMapper.update(authRole);
        String parentId = authRole.getRoleCatalogId()!=null? authRole.getRoleCatalogId():authRoleExist.getRoleCatalogId();
        batchSortAndCodeService.revisionSortSnForUpdate(AuthRole.class,parentId,authRoleExist.getSortSn(),authRole.getSortSn());
        return authRole;
    }

    @Override
    public Integer countByRoleCatalogIdAndTenantId(String roleCatalogId, String tenantId) {
        return authRoleMapper.countByCombination(AuthRole.builder().roleCatalogId(roleCatalogId).tenantId(tenantId).status(true).build());
    }

    @Override
    public List<AuthRole> listByIds(Collection<String> roleIds) {
        return authRoleMapper.selectByIds(roleIds);
    }

    @Override
    public List<AuthRole> listByNameEn(Collection<String> nameEns) {
        return authRoleMapper.selectByNameEns(nameEns);
    }

    private void checkUniqueness(AuthRole authRole, boolean isUpdate) {
        // 角色组有效性校验
        SysCatalog roleCatalog = sysCatalogService.getById(authRole.getRoleCatalogId());
        if (null == roleCatalog || !roleCatalog.getStatus()) {
            throw new GafException("角色组无效");
        }
        // name，租户下不重复
        AuthRoleSelectVo nameSelectVo = AuthRoleSelectVo.builder()
                .searchFieldName(DbFieldNameConstant.ROLE_NAME)
                .searchFieldValue(authRole.getRoleName())
                .build();
        List<AuthRole> nameAuthRoles = authRoleMapper.searchWholeMatchList(nameSelectVo);
        if (!CollectionUtils.isEmpty(nameAuthRoles)) {
            boolean isNameExist;
            if (isUpdate) {
                isNameExist = nameAuthRoles.stream()
                        .anyMatch(user -> !authRole.getRoleId().equals(user.getRoleId()));
            } else {
                isNameExist = nameAuthRoles.size() > 0;
            }
            if (isNameExist) {
                throw new GafException("角色名称已存在");
            }
        }
    }

}
