/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.authority.service.impl;

import com.supermap.gaf.authority.commontype.*;
import com.supermap.gaf.authority.constant.CommonConstant;
import com.supermap.gaf.authority.constant.DbFieldNameConstant;
import com.supermap.gaf.authority.dao.AuthTenantMapper;
import com.supermap.gaf.authority.service.*;
import com.supermap.gaf.authority.vo.AuthTenantSelectVo;
import com.supermap.gaf.authority.vo.AuthUserSelectVo;
import com.supermap.gaf.authority.vo.TenantInitVo;
import com.supermap.gaf.exception.GafException;
import com.supermap.gaf.sys.mgt.commontype.SysCatalog;
import com.supermap.gaf.sys.mgt.enums.CatalogTypeEnum;
import com.supermap.gaf.sys.mgt.service.SysCatalogService;
import com.supermap.gaf.utils.LogUtil;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

import static com.supermap.gaf.authority.util.TreeConvertUtil.ROOT_PARENT_ID;

/**
 * @author zhm
 * @date:2021/3/25 租户服务类
 */
@Service
public class AuthTenantServiceImpl implements AuthTenantService {
    private final AuthTenantMapper authTenantMapper;

    private final AuthUserService authUserService;

    private final AuthDepartmentService authDepartmentService;

    private final AuthUserRoleService authUserRoleService;

    private final AuthPostService authPostService;

    private final SysCatalogService sysCatalogService;

    private final AuthPostRoleService authPostRoleService;

    private static final Logger logger = LogUtil.getLocLogger(AuthTenantServiceImpl.class);

    public AuthTenantServiceImpl(AuthTenantMapper authTenantMapper, AuthUserService authUserService, AuthDepartmentService authDepartmentService, AuthUserRoleService authUserRoleService, AuthPostService authPostService, SysCatalogService sysCatalogService, AuthPostRoleService authPostRoleService) {
        this.authTenantMapper = authTenantMapper;
        this.authUserService = authUserService;
        this.authDepartmentService = authDepartmentService;
        this.authUserRoleService = authUserRoleService;
        this.authPostService = authPostService;
        this.sysCatalogService = sysCatalogService;
        this.authPostRoleService = authPostRoleService;
    }

    @Override
    public Map<String, Object> tenantSearch(String searchFieldName, String searchFieldValue, String orderFieldName, String orderMethod, Integer pageNum, Integer pageSize) {
        if (pageNum == null || pageNum < 1) {
            pageNum = 1;
        }
        Integer offset = (pageNum - 1) * pageSize;
        AuthTenantSelectVo selectVo = AuthTenantSelectVo.builder()
                .searchFieldName(searchFieldName)
                .searchFieldValue(searchFieldValue)
                .orderFieldName(orderFieldName)
                .orderMethod(orderMethod)
                .offset(offset)
                .pageSize(pageSize)
                .build();
        Map<String, Object> result;
        if (searchFieldName == null & orderFieldName == null) {
            result = pageList(selectVo);
        } else if (DbFieldNameConstant.ADMIN_ID.equals(searchFieldName)) {
            result = getByAdminName(selectVo);
        } else if (DbFieldNameConstant.STATUS.equals(searchFieldName)) {
            result = getByStatus(selectVo);
        } else {
            result = searchList(selectVo);
        }
        return result;
    }


    @Override
    public AuthTenant getById(String tenantId) {
        if (tenantId == null) {
            throw new IllegalArgumentException("tenantId不能为空");
        }
        return authTenantMapper.select(tenantId);
    }

    @Override
    public List<AuthUser> getAllAdmin(String tenantId) {
        List<AuthUser> admins = authUserService.getByTenantId(tenantId);
        if (admins == null || CollectionUtils.isEmpty(admins)) {
            return new ArrayList<>();
        }
        List<String> userIds = admins.stream().map(AuthUser::getUserId).collect(Collectors.toList());
        List<String> postIds = admins.stream().map(AuthUser::getPostId).collect(Collectors.toList());
        List<String> resUserIds = authUserRoleService.getByUserIds(userIds).stream().filter(userIds::contains).collect(Collectors.toList());
        List<String> resUserPostIds = authPostRoleService.getByPostIds(postIds).stream().filter(postIds::contains).collect(Collectors.toList());

        List<AuthUser> res1 = admins.stream().filter(user -> resUserIds.contains(user.getUserId())).collect(Collectors.toList());
        List<AuthUser> res2 = admins.stream().filter(user -> resUserPostIds.contains(user.getPostId())).collect(Collectors.toList());
        res1.addAll(res2);
        res2.forEach(user -> {
            if (!res1.contains(user)) {
                res1.add(user);
            }
        });
        return res1;
    }

    @Override
    public Map<String, Object> getByAdminName(AuthTenantSelectVo authTenantSelectVo) {
        List<AuthTenant> pageList;
        AuthUserSelectVo selectVo = AuthUserSelectVo.builder()
                .searchFieldName("name")
                .searchFieldValue(authTenantSelectVo.getSearchFieldValue())
                .orderFieldName(authTenantSelectVo.getOrderFieldName())
                .orderMethod(authTenantSelectVo.getOrderMethod())
                .offset(authTenantSelectVo.getOffset())
                .pageSize(authTenantSelectVo.getPageSize())
                .build();
        List<AuthUser> users = (List<AuthUser>) authUserService.searchList(selectVo).get("pageList");
        List<String> adminIds = users.stream().map(AuthUser::getUserId).collect(Collectors.toList());
        Integer totalCount = 0;
        if (adminIds.size() > 0) {
            pageList = authTenantMapper.selectByAdminId(adminIds, authTenantSelectVo.getPageSize(), authTenantSelectVo.getOffset());
            totalCount = authTenantMapper.countByAdmin(adminIds);
        } else {
            pageList = new LinkedList<>();
        }
        for (AuthTenant authTenant : pageList) {
            authTenant.setAdminName(authUserService.getById(authTenant.getAdminId()).getName());
        }
        Map<String, Object> result = new HashMap<>(2);
        result.put("pageList", pageList);
        result.put("totalCount", totalCount);
        return result;
    }

    @Override
    public Map<String, Object> getByStatus(AuthTenantSelectVo authTenantSelectVo) {
        List<AuthTenant> pageList;
        List<String> statusValue = Arrays.asList("true", "false");
        if (!statusValue.contains(authTenantSelectVo.getSearchFieldValue().trim())) {
            throw new GafException("状态输入错误");
        }
        AuthUserSelectVo.builder()
                .searchFieldName("status")
                .searchFieldValue(authTenantSelectVo.getSearchFieldValue())
                .orderFieldName(authTenantSelectVo.getOrderFieldName())
                .orderMethod(authTenantSelectVo.getOrderMethod())
                .offset(authTenantSelectVo.getOffset())
                .pageSize(authTenantSelectVo.getPageSize())
                .build();
        pageList = authTenantMapper.selectByStatus(Boolean.parseBoolean(authTenantSelectVo.getSearchFieldValue()), authTenantSelectVo.getPageSize(), authTenantSelectVo.getOffset());
        Integer totalCount = authTenantMapper.countByStatus(Boolean.parseBoolean(authTenantSelectVo.getSearchFieldValue()));
        for (AuthTenant authTenant : pageList) {
            authTenant.setAdminName(authUserService.getById(authTenant.getAdminId()).getName());
        }
        Map<String, Object> result = new HashMap<>(2);
        result.put("pageList", pageList);
        result.put("totalCount", totalCount);
        return result;
    }

    @Override
    public Map<String, Object> pageList(AuthTenantSelectVo authTenantSelectVo) {
        List<AuthTenant> pageList;
        if (authTenantSelectVo.getOffset() == null || authTenantSelectVo.getOffset() < CommonConstant.OFFSET_MAX_FOR_SQL_BETTER) {
            pageList = authTenantMapper.pageList(authTenantSelectVo);
        } else {
            pageList = authTenantMapper.bigOffsetPageList(authTenantSelectVo);
        }
        int totalCount = authTenantMapper.pageListCount();
        for (AuthTenant authTenant : pageList) {
            AuthUser admin = authUserService.getById(authTenant.getAdminId());
            if (admin == null) {
                authTenant.setAdminName(authTenant.getTenantName() + "-admin");
            } else {
                authTenant.setAdminName(authUserService.getById(authTenant.getAdminId()).getName());
            }
        }
        Map<String, Object> result = new HashMap<>(2);
        result.put("pageList", pageList);
        result.put("totalCount", totalCount);
        return result;
    }

    @Override
    public Map<String, Object> searchList(AuthTenantSelectVo authTenantSelectVo) {
        List<AuthTenant> pageList;
        pageList = authTenantMapper.searchList(authTenantSelectVo);
        Integer totalCount = authTenantMapper.countOneField(authTenantSelectVo.getSearchFieldName(), authTenantSelectVo.getSearchFieldValue());
        for (AuthTenant authTenant : pageList) {
            AuthUser tenantAdmin = authUserService.getById(authTenant.getAdminId());
            if (tenantAdmin != null) {
                authTenant.setAdminName(tenantAdmin.getName());
            }
        }
        Map<String, Object> result = new HashMap<>(2);
        result.put("pageList", pageList);
        result.put("totalCount", totalCount);
        return result;
    }

    @Override
    public AuthTenant insertAuthTenant(AuthTenant authTenant) {
        // todo 获取当前登录人员的租户id，用户名称
        authTenant.setTenantId(UUID.randomUUID().toString());
        // 唯一性校验
        checkUniqueness(authTenant, false);
        authTenantMapper.insert(authTenant);
        return authTenant;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean initAuthTenantTran(TenantInitVo tenantInitVo) {
        logger.info("新增租户");
        AuthTenant authTenant = insertAuthTenant(tenantInitVo.getAuthTenant());
        String tenantId = authTenant.getTenantId();

        logger.info("新增租户根部门");
        AuthDepartment authDepartment = new AuthDepartment();
        authDepartment.setTenantId(tenantId);
        authDepartment.setDepartmentName(authTenant.getTenantName());
        authDepartment.setParentId(ROOT_PARENT_ID);
        authDepartment.setSortSn(1);
        authDepartment.setIsThirdParty(false);
        authDepartment.setNameEn(authTenant.getNameEn());
        authDepartment.setStatus(true);
        authDepartment.setDescription(authTenant.getTenantName() + "默认根节点");
        AuthDepartment rootDepartment = authDepartmentService.insertAuthDepartment(authDepartment);

        logger.info("新增根部门岗位");
        AuthPost authPost = new AuthPost();
        authPost.setDepartmentId(rootDepartment.getDepartmentId());
        authPost.setPostName("租户管理员");
        authPost.setTenantId(tenantId);
        authPost.setDescription("租户管理员岗位绑定有内置的租户管理员角色");
        AuthPost administratorPost = authPostService.insertAuthPost(authPost);

        // 岗位绑定角色
        AuthPostRole tenantAdminRole = new AuthPostRole();
        tenantAdminRole.setPostId(administratorPost.getPostId());
        tenantAdminRole.setRoleId(AuthRole.TENANT_ADMIN.getRoleId());
        authPostRoleService.insertAuthPostRole(tenantAdminRole);
        logger.info("新增租户管理员");
        AuthUser authUser = tenantInitVo.getAuthUser();
        authUser.setTenantId(tenantId);
        authUser.setDepartmentId(rootDepartment.getDepartmentId());
        authUser.setPostId(administratorPost.getPostId());
        AuthUser tenantAdmin = authUserService.insertAuthUser(authUser);

        //设置租户初始管理员id
        logger.info("设置租户初始管理员id");
        authTenant.setAdminId(tenantAdmin.getUserId());
        authTenantMapper.update(authTenant);
        // todo 在harbar中用简称创建一个同名的目录
        logger.info("初始化租户角色组");
        SysCatalog build = SysCatalog.builder()
                .parentId(ROOT_PARENT_ID)
                .type(CatalogTypeEnum.ROLE_GROUP_TYPE.getValue())
                .tenantId(tenantId)
                .name(authTenant.getTenantName())
                .status(true)
                .description(authTenant.getTenantName() + "默认角色组")
                .build();


        sysCatalogService.insertSysCatalog(build);
        logger.info("租户初始化完成");
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void initAuthTenant(TenantInitVo tenantInitVo) {
        initAuthTenantTran(tenantInitVo);
    }

    @Override
    public void batchInsert(List<AuthTenant> authTenants) {
        if (authTenants != null && authTenants.size() > 0) {
            authTenants.forEach(authTenant -> authTenant.setTenantId(UUID.randomUUID().toString()));
            authTenantMapper.batchInsert(authTenants);
        }
    }

    @Override
    public void deleteAuthTenant(String tenantId) {
        AuthTenant authTenant = getById(tenantId);
        if (authTenant == null) {
            throw new GafException("租户不存在");
        }
        // 将租户相关的都删除？
        authTenantMapper.delete(tenantId);
    }

    @Override
    public void setAuthTenantStatus(String tenantId, Boolean status) {
        AuthTenant authTenant = getById(tenantId);
        if (authTenant == null) {
            throw new GafException("租户不存在");
        }
        authTenantMapper.setAuthTenantStatus(tenantId, status);
    }

    @Override
    public void batchDelete(List<String> tenantIds) {
        authTenantMapper.batchDelete(tenantIds);
    }

    @Override
    public AuthTenant updateAuthTenant(AuthTenant authTenant) {
        // 唯一性校验
        checkUniqueness(authTenant, true);
        // todo 获取当前登录人员的用户名称
        // 更新租户默认角色根目录
        SysCatalog queryCatalog = SysCatalog.builder().parentId(ROOT_PARENT_ID).tenantId(authTenant.getTenantId()).type(CatalogTypeEnum.ROLE_GROUP_TYPE.getValue()).status(true).build();
        List<SysCatalog> sysCatalogs = sysCatalogService.getByCombination(queryCatalog);
        if (sysCatalogs.size() != 1) {
            throw new GafException("租户更新失败，未查询到该租户默认角色组");
        }
        SysCatalog sysCatalog = sysCatalogs.get(0);
        sysCatalog.setName(authTenant.getTenantName());
        sysCatalogService.updateSysCatalog(sysCatalog);
        authTenantMapper.update(authTenant);
        return authTenant;
    }

    /**
     * 唯一性校验
     *
     * @param authTenant 租户
     * @param isUpdate   是否为更新，更新时需要排除当前用户
     */
    private void checkUniqueness(AuthTenant authTenant, boolean isUpdate) {
        // tenant_name
        AuthTenantSelectVo nameTenantSelectVo = AuthTenantSelectVo.builder()
                .searchFieldName("tenant_name")
                .searchFieldValue(authTenant.getTenantName())
                .build();
        List<AuthTenant> nameAuthTenants = authTenantMapper.searchWholeMatchList(nameTenantSelectVo);
        if (!CollectionUtils.isEmpty(nameAuthTenants)) {
            boolean isNameExist;
            if (isUpdate) {
                isNameExist = nameAuthTenants.stream()
                        .anyMatch(tenant -> tenant.getStatus() && !authTenant.getTenantId().equals(tenant.getTenantId()));
            } else {
                isNameExist = nameAuthTenants.stream()
                        .anyMatch(AuthTenant::getStatus);
            }
            if (isNameExist) {
                throw new GafException("租户名称已存在");
            }
        }

        // brief_name_en
        AuthTenantSelectVo brifNameTenantSelectVo = AuthTenantSelectVo.builder()
                .searchFieldName("brief_name_en")
                .searchFieldValue(authTenant.getBriefNameEn())
                .build();
        List<AuthTenant> brifNameAuthTenants = authTenantMapper.searchWholeMatchList(brifNameTenantSelectVo);
        if (!CollectionUtils.isEmpty(brifNameAuthTenants)) {
            boolean isBrifNameExist;
            if (isUpdate) {
                isBrifNameExist = brifNameAuthTenants.stream()
                        .anyMatch(tenant -> tenant.getStatus() && !authTenant.getTenantId().equals(tenant.getTenantId()));
            } else {
                isBrifNameExist = brifNameAuthTenants.stream()
                        .anyMatch(AuthTenant::getStatus);
            }
            if (isBrifNameExist) {
                throw new GafException("租户英文简称已存在");
            }
        }
    }
}
