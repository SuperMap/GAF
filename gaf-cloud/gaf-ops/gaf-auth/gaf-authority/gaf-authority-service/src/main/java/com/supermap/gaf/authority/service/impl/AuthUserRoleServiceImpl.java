/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.authority.service.impl;

import com.supermap.gaf.authority.commontype.*;
import com.supermap.gaf.authority.constant.CacheGroupConstant;
import com.supermap.gaf.authority.constant.CommonConstant;
import com.supermap.gaf.authority.constant.DbFieldNameConstant;
import com.supermap.gaf.authority.dao.AuthUserRoleMapper;
import com.supermap.gaf.authority.enums.CodeBaseRoleEnum;
import com.supermap.gaf.authority.enums.NodeTypeEnum;
import com.supermap.gaf.authority.service.*;
import com.supermap.gaf.authority.util.TreeConvertUtil;
import com.supermap.gaf.authority.vo.AuthUserRoleSelectVo;
import com.supermap.gaf.authority.vo.TreeNode;
import com.supermap.gaf.authority.vo.TreeVo;
import com.supermap.gaf.commontypes.MessageResult;
import com.supermap.gaf.exception.GafException;
import com.supermap.gaf.project.client.ProjCodeBaseUsersClient;
import com.supermap.gaf.project.commontype.ProjCodeBaseUser;
import com.supermap.gaf.sys.mgt.enums.CatalogTypeEnum;
import com.supermap.gaf.sys.mgt.service.SysCatalogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author yangdong
 * @date:2021/3/25
 * 用户角色服务类
 */
@Service
public class AuthUserRoleServiceImpl implements AuthUserRoleService {

    private static final Logger logger = LoggerFactory.getLogger(AuthUserRoleServiceImpl.class);


    @Autowired
    private AuthUserRoleMapper authUserRoleMapper;
    @Autowired
    private AuthUserService authUserService;
    @Autowired
    private AuthRoleService authRoleService;
    @Autowired
    private AuthUserParttimeService  authUserParttimeService;
    @Autowired
    private AuthPostRoleService authPostRoleService;
    @Autowired
    private SysCatalogService sysCatalogService;
    @Autowired(required = false)
    private ProjCodeBaseUsersClient projCodeBaseUsersClient;

//    public AuthUserRoleServiceImpl(AuthUserRoleMapper authUserRoleMapper, AuthUserService authUserService, AuthRoleService authRoleService, AuthUserParttimeService authUserParttimeService, AuthPostRoleService authPostRoleService, SysCatalogService sysCatalogService, ProjCodeBaseUsersFeignService projCodeBaseUsersFeignService) {
//        this.authUserRoleMapper = authUserRoleMapper;
//        this.authUserService = authUserService;
//        this.authRoleService = authRoleService;
//        this.authUserParttimeService = authUserParttimeService;
//        this.authPostRoleService = authPostRoleService;
//        this.sysCatalogService = sysCatalogService;
//        this.projCodeBaseUsersFeignService = projCodeBaseUsersFeignService;
//    }

    @Override
    public AuthUserRole getById(String userRoleId) {
        if (StringUtils.isEmpty(userRoleId)) {
            throw new GafException("userRoleId不能为空");
        }
        return authUserRoleMapper.select(userRoleId);
    }

    @Override
    public List<String> getByUserIds(List<String> userIds) {
        if (CollectionUtils.isEmpty(userIds)) {
            return new ArrayList<>();
        }
        return authUserRoleMapper.getByUserIds("role_000001");
    }

    @Override
    public List<AuthUserRole> listByUser(String userId) {
        if (StringUtils.isEmpty(userId)) {
            throw new GafException("userId不能为空");
        }
        return authUserRoleMapper.listByUser(userId);
    }

    /**
     * 查询角色树
     *
     * @return 1、当前租户下有效的角色
     * 2、传入用户已绑定的角色
     */
    @Cacheable(value = CacheGroupConstant.USER_ROLE_TREE, key = "#userId", unless = "#result == null")
    @Override
    public TreeVo getUserRoleTree(String userId) {
        TreeVo treeVo = new TreeVo();
        // 角色组节点列表
        List<TreeNode> roleCatalogNodes = sysCatalogService.getNodesByType(CatalogTypeEnum.ROLE_GROUP_TYPE.getValue());
        if (!CollectionUtils.isEmpty(roleCatalogNodes)) {
            List<TreeNode> nodes = new ArrayList<>(8);
            for (TreeNode roleCatalogNode : roleCatalogNodes) {
                // 角色组下角色列表
                List<AuthRole> authRoles = authRoleService.listByRoleCatalog(roleCatalogNode.getKey());
                if (!CollectionUtils.isEmpty(authRoles)) {
                    authRoles.forEach(authRole -> {
                        TreeNode roleNode = new TreeNode();
                        roleNode.setKey(authRole.getRoleId());
                        roleNode.setTitle(authRole.getRoleName());
                        roleNode.setType(NodeTypeEnum.ROLE.getValue());
                        roleNode.setParentId(roleCatalogNode.getKey());
                        roleNode.setSortSn(authRole.getSortSn());
                        nodes.add(roleNode);
                    });
                }
            }
            nodes.addAll(roleCatalogNodes);
            treeVo.setRootTreeNodes(TreeConvertUtil.convertToTree(nodes));

            // 用户已关联的角色
            List<AuthUserRole> authUserRoles = listByUser(userId);
            if (!CollectionUtils.isEmpty(authUserRoles)) {
                List<String> checkedRoleKeys = authUserRoles.stream()
                        .map(AuthUserRole::getRoleId)
                        .collect(Collectors.toList());
                treeVo.setCheckedKeys(checkedRoleKeys);
            }
        }
        return treeVo;
    }

    @Override
    public Map<String, Object> pageList(AuthUserRoleSelectVo authUserRoleSelectVo) {
        if (authUserRoleSelectVo.getPageSize() == null || authUserRoleSelectVo.getPageSize() == 0) {
            authUserRoleSelectVo.setPageSize(50);
        }
        List<AuthUserRole> pageList;
        if (authUserRoleSelectVo.getOffset() == null || authUserRoleSelectVo.getOffset() < CommonConstant.OFFSET_MAX_FOR_SQL_BETTER) {
            pageList = authUserRoleMapper.pageList(authUserRoleSelectVo);
        } else {
            pageList = authUserRoleMapper.bigOffsetPageList(authUserRoleSelectVo);
        }
        int totalCount = authUserRoleMapper.pageListCount();
        Map<String, Object> result = new HashMap<>(2);
        result.put(DbFieldNameConstant.PAGE_LIST, pageList);
        result.put(DbFieldNameConstant.TOTAL_COUNT, totalCount);
        return result;
    }

    @Override
    public Map<String, Object> searchList(AuthUserRoleSelectVo authUserRoleSelectVo) {
        if (authUserRoleSelectVo.getPageSize() == null || authUserRoleSelectVo.getPageSize() == 0) {
            authUserRoleSelectVo.setPageSize(50);
        }
        List<AuthUserRole> pageList = authUserRoleMapper.searchList(authUserRoleSelectVo);
        Integer totalCount = authUserRoleMapper.countOneField(authUserRoleSelectVo.getSearchFieldName(), authUserRoleSelectVo.getSearchFieldValue());
        Map<String, Object> result = new HashMap<>(2);
        result.put(DbFieldNameConstant.PAGE_LIST, pageList);
        result.put(DbFieldNameConstant.TOTAL_COUNT, totalCount);
        return result;
    }

    @CacheEvict(value = CacheGroupConstant.USER_ROLE_TREE, key = "#authUserRole.userId")
    @Override
    public AuthUserRole insertAuthUserRole(AuthUserRole authUserRole) {
        // todo 获取当前登录人员的用户名称
        authUserRole.setUserRoleId(UUID.randomUUID().toString());
        checkUniqueness(authUserRole);
        authUserRoleMapper.insert(authUserRole);
        return authUserRole;
    }

    @CacheEvict(value = CacheGroupConstant.USER_ROLE_TREE, key = "#userId")
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void batchUpdate(List<AuthUserRole> authUserRoles, String userId) {
        if (authUserRoles == null) {
            authUserRoles = new ArrayList<>();
        }
        List<AuthUserRole> oldUserRoles = authUserRoleMapper.listByUser(userId);
        List<String> oldRoleIds = oldUserRoles.stream().map(AuthUserRole::getRoleId).collect(Collectors.toList());
        List<String> newRoleIds = authUserRoles.stream().map(AuthUserRole::getRoleId).collect(Collectors.toList());
        List<String> toRemove = oldRoleIds.stream().filter(oldRoleId -> !newRoleIds.contains(oldRoleId)).collect(Collectors.toList());
        List<String> toAdd = newRoleIds.stream().filter(newRoleId -> !oldRoleIds.contains(newRoleId)).collect(Collectors.toList());
        if (toRemove.size() == 0 && toAdd.size() == 0) {
            return;
        }
        if (toRemove.size() > 0) {
            authUserRoleMapper.deleteByUserIdAndRoleIds(userId,toRemove);
        }
        if (toAdd.size() > 0) {
            Set<String> distinctToAdd = new HashSet<>(toAdd);
            List<AuthUserRole> toAddUserRoles = distinctToAdd.stream().map(roleId -> AuthUserRole.builder()
                    .userId(userId)
                    .roleId(roleId)
                    .status(true)
                    .userRoleId(UUID.randomUUID().toString())
                    .build()
            ).collect(Collectors.toList());
            authUserRoleMapper.batchInsert(toAddUserRoles);
        }
        // 应该使用id去查询而不应该使用name
        List<String> nameEns = CodeBaseRoleEnum.getAllNames();
        List<AuthRole> appRoles = authRoleService.listByNameEn(nameEns);
        Set<String> appRoleIds = appRoles.stream().map(AuthRole::getRoleId).collect(Collectors.toSet());

        AuthUser user = authUserService.getById(userId);
        if (user == null) {
            throw new GafException("未找到该用户信息");
        }
        // todo: 获取用户所属租户  后序要改
        // 查询用户所在当前租户下是否有挂职信息
        // 查询岗位是否关联到App角色
        List<AuthUserParttime> userParttimes = authUserParttimeService.getByUserId(userId);
        List<String> postIds = userParttimes.stream().map(AuthUserParttime::getPostId).collect(Collectors.toList());
        if (!StringUtils.isEmpty(user.getPostId())) {
            postIds.add(user.getPostId());
        }
        if (postIds.size() > 0) {
            List<AuthPostRole>  authPostRoles = authPostRoleService.listByPostIds(postIds);
            List<AuthPostRole> authPostRoleInAppRole = authPostRoles.stream().filter(authPostRole -> appRoleIds.contains(authPostRole.getRoleId())).collect(Collectors.toList());
            if (authPostRoleInAppRole.size() > 0) {
                return;
            }
        }

        List<String> intersection = oldRoleIds.stream().filter(newRoleIds::contains).collect(Collectors.toList());
        boolean hasAppRole = intersection.stream().anyMatch(appRoleIds::contains);
        if (!hasAppRole) {
            if (toAdd.size() > 0 && toRemove.size() == 0) {
                boolean hasAppRoleInAdd = toAdd.stream().anyMatch(appRoleIds::contains);
                if (hasAppRoleInAdd) {
                    // 新增
                    try {
                        addDevUser(user);
                    } catch (Exception e) {
                        logger.info(e.getMessage());
                    }
                }
            } else if (toAdd.size() == 0 && toRemove.size() > 0) {
                boolean hasAppRoleInRemove = toRemove.stream().anyMatch(appRoleIds::contains);
                if (hasAppRoleInRemove) {
                    // 删除
                    try {
                        blockDevUser(user);
                    } catch (Exception e) {
                        logger.info(e.getMessage());
                    }
                }
            }
            if (toAdd.size() > 0 && toRemove.size() > 0) {
                boolean hasAppRoleInAdd = toAdd.stream().anyMatch(appRoleIds::contains);
                boolean hasAppRoleInRemove = toRemove.stream().anyMatch(appRoleIds::contains);
                if (hasAppRoleInAdd && !hasAppRoleInRemove) {
                    // 新增
                    try {
                        addDevUser(user);
                    } catch (Exception e) {
                        logger.info(e.getMessage());
                    }
                } else if (!hasAppRoleInAdd && hasAppRoleInRemove) {
                    // 删除
                    try {
                        blockDevUser(user);
                    } catch (Exception e) {
                        logger.info(e.getMessage());
                    }
                }
            }
        }
    }

    /**
     * todo: 需要使用分布式事务 或者由于只有一个数据库直接把代码考过来或打包
     */
    private void addDevUser(AuthUser user) {
        try{
            MessageResult<ProjCodeBaseUser> result = projCodeBaseUsersClient.addDevUser(user.getRealName(), user.getName(), user.getEmail());
            if (!result.isSuccessed()) {
                throw new GafException("新增代码库用户失败");
            }
        } catch (Exception e) {
            throw new GafException("新增代码库用户失败");
        }
    }

    //todo: 需要使用分布式事务 或者由于只有一个数据库直接把代码考过来或打包
    /**
     * 新增用户对应的代码库用户
     * @param user 用户
     */
    private void blockDevUser(AuthUser user) {
        try{
            MessageResult<String> result = projCodeBaseUsersClient.blockDevUser(user.getName());
            if (!result.isSuccessed()) {
                throw new GafException("删除代码库用户失败");
            }
        } catch (Exception e) {
            throw new GafException("删除代码库用户失败");
        }
    }

    @Override
    public void deleteAuthUserRole(String userRoleId) {
        authUserRoleMapper.delete(userRoleId);
    }

    @Override
    public void batchDelete(List<String> userRoleIds) {
        authUserRoleMapper.batchDelete(userRoleIds);
    }

    @Override
    public AuthUserRole updateAuthUserRole(AuthUserRole authUserRole) {
        authUserRoleMapper.update(authUserRole);
        return authUserRole;
    }

    @Override
    public void deleteByUserId(String userId) {
        authUserRoleMapper.deleteByUserId(userId);
    }

    /**
     * 唯一性校验，每个用户与每个角色的关系只能出现一次
     *
     * @param authUserRole 用户角色
     */
    private void checkUniqueness(AuthUserRole authUserRole) {
        List<AuthUserRole> authUserRoles = authUserRoleMapper.listUserRole(authUserRole.getRoleId(), authUserRole.getUserId());
        if (!CollectionUtils.isEmpty(authUserRoles)) {
            boolean isUserRoleExist = authUserRoles.size() > 0;
            if (isUserRoleExist) {
                throw new GafException("该用户已绑定该角色");
            }
        }
    }
}
