/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.authority.service.impl;

import com.supermap.gaf.authority.commontype.AuthPostRole;
import com.supermap.gaf.authority.commontype.AuthRole;
import com.supermap.gaf.authority.commontype.AuthUser;
import com.supermap.gaf.authority.constant.CommonConstant;
import com.supermap.gaf.authority.constant.DbFieldNameConstant;
import com.supermap.gaf.authority.dao.AuthPostRoleMapper;
import com.supermap.gaf.authority.enums.NodeTypeEnum;
import com.supermap.gaf.authority.service.AuthPostRoleService;
import com.supermap.gaf.authority.service.AuthPostService;
import com.supermap.gaf.authority.service.AuthRoleService;
import com.supermap.gaf.authority.util.TreeConvertUtil;
import com.supermap.gaf.authority.vo.AuthPostRoleSelectVo;
import com.supermap.gaf.authority.vo.PostRoleVo;
import com.supermap.gaf.authority.vo.TreeNode;
import com.supermap.gaf.authority.vo.TreeVo;
import com.supermap.gaf.exception.GafException;
import com.supermap.gaf.sys.mgt.enums.CatalogTypeEnum;
import com.supermap.gaf.sys.mgt.service.SysCatalogService;
import com.supermap.gaf.utils.LogUtil;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 岗位角色服务实现类
 *
 * @author yangdong
 * @date:2021/3/25
 */
@Service
public class AuthPostRoleServiceImpl implements AuthPostRoleService {
    private final AuthPostRoleMapper authPostRoleMapper;

    private final AuthRoleService authRoleService;

    private final SysCatalogService sysCatalogService;

    private final AuthPostService authPostService;

    private static final Logger logger = LogUtil.getLocLogger(AuthPostRoleServiceImpl.class);

    public AuthPostRoleServiceImpl(AuthPostRoleMapper authPostRoleMapper, AuthRoleService authRoleService, SysCatalogService sysCatalogService, AuthPostService authPostService) {
        this.authPostRoleMapper = authPostRoleMapper;
        this.authRoleService = authRoleService;
        this.sysCatalogService = sysCatalogService;
        this.authPostService = authPostService;
    }

    @Override
    public AuthPostRole getById(String postRoleId) {
        if (postRoleId == null) {
            throw new GafException("postRoleId不能为空");
        }
        return authPostRoleMapper.select(postRoleId);
    }

    @Override
    public List<String> getByPostIds(List<String> postIds) {
        if (postIds == null || CollectionUtils.isEmpty(postIds)) {
            return new ArrayList<>();
        }
        return authPostRoleMapper.getByPostIds("role_000001");
    }

    @Override
    public List<AuthPostRole> listByPost(String postId) {
        if (StringUtils.isEmpty(postId)) {
            throw new GafException("postId不能为空");
        }
        return authPostRoleMapper.listByPost(postId);
    }

    @Override
    public List<AuthPostRole> getByPostId(String postId, Boolean status) {
        if (StringUtils.isEmpty(postId)) {
            return new ArrayList<>();
        }
        return authPostRoleMapper.getByPostId(postId, status);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void handlePostRole(PostRoleVo postRoleVo) {
        String postId = postRoleVo.getPostId();
        if (postId == null || StringUtils.isEmpty(postId)) {
            throw new GafException("岗位id为空");
        }
        logger.info(postId);
        if (authPostService.getById(postId) == null) {
            throw new GafException("角色不存在：" + postId);
        }
        List<String> newList = postRoleVo.getRoleList();
        List<String> oldList = getByPostId(postId, true).stream().map(AuthPostRole::getRoleId).collect(Collectors.toList());
        List<String> addList = new ArrayList<>();
        List<String> removeList = new ArrayList<>();
        newList.forEach(item -> {
            if (!oldList.contains(item)) {
                addList.add(item);
            }
        });
        logger.info("toadd:");
        System.out.println(addList);
        oldList.forEach(item -> {
            if (!newList.contains(item)) {
                removeList.add(item);
            }
        });
        logger.info("toremove:");
        //新增或修改
        if (!CollectionUtils.isEmpty(addList)) {
            List<AuthPostRole> addPostRoleList = new ArrayList<>();
            List<String> updateList = new ArrayList<>();
            addList.forEach(item -> {
                AuthPostRole oldPostRole = getByPostAndRole(postId, item, false);
                if (oldPostRole != null) {
                    updateList.add(oldPostRole.getPostRoleId());
                } else {
                    AuthPostRole authPostRole = AuthPostRole.builder()
                            .roleId(item)
                            .postId(postId)
                            .status(true)
                            .sortSn(1)
                            .build();
                    addPostRoleList.add(authPostRole);
                }
            });
            //批量修改
            if (!CollectionUtils.isEmpty(updateList)) {
                batchUpdate(updateList);
            }
            //批量新增
            if (!CollectionUtils.isEmpty(addPostRoleList)) {
                batchInsertPostRole(addPostRoleList);
            }
        }

        //禁用
        if (!CollectionUtils.isEmpty(removeList)) {
            //根据岗位id和角色id获取岗位角色关联id
            List<String> postRoleIds = new ArrayList<>();
            removeList.forEach(item -> {
                AuthPostRole authPostRole = getByPostAndRole(postId, item, true);
                if (authPostRole != null && !AuthUser.TENANT_ADMIN_ROLE_ID.equals(authPostRole.getRoleId())) {
                    postRoleIds.add(authPostRole.getPostRoleId());
                }
            });
            if (!CollectionUtils.isEmpty(postRoleIds)) {
                batchDelete(postRoleIds);
            }
        }
    }

    public AuthPostRole getByPostAndRole(String postId, String roleId, Boolean status) {
        if (StringUtils.isEmpty(postId) || StringUtils.isEmpty(roleId)) {
            return null;
        }
        return authPostRoleMapper.getByPostAndRole(postId, roleId, status);
    }

    public void batchUpdate(List<String> authPostRoleIds) {
        authPostRoleMapper.batchUpdate(authPostRoleIds);
    }

    @Override
    public Map<String, Object> pageList(AuthPostRoleSelectVo authPostRoleSelectVo) {
        if (authPostRoleSelectVo.getPageSize() == null || authPostRoleSelectVo.getPageSize() == 0) {
            authPostRoleSelectVo.setPageSize(50);
        }
        List<AuthPostRole> pageList;
        if (authPostRoleSelectVo.getOffset() == null || authPostRoleSelectVo.getOffset() < CommonConstant.OFFSET_MAX_FOR_SQL_BETTER) {
            pageList = authPostRoleMapper.pageList(authPostRoleSelectVo);
        } else {
            pageList = authPostRoleMapper.bigOffsetPageList(authPostRoleSelectVo);
        }
        int totalCount = authPostRoleMapper.pageListCount();
        Map<String, Object> result = new HashMap<>(2);
        result.put(DbFieldNameConstant.PAGE_LIST, pageList);
        result.put(DbFieldNameConstant.TOTAL_COUNT, totalCount);
        return result;
    }

    @Override
    public Map<String, Object> searchList(AuthPostRoleSelectVo authPostRoleSelectVo) {
        if (authPostRoleSelectVo.getPageSize() == null || authPostRoleSelectVo.getPageSize() == 0) {
            authPostRoleSelectVo.setPageSize(50);
        }
        List<AuthPostRole> pageList = authPostRoleMapper.searchList(authPostRoleSelectVo);
        Integer totalCount = authPostRoleMapper.countOneField(authPostRoleSelectVo.getSearchFieldName(), authPostRoleSelectVo.getSearchFieldValue());
        Map<String, Object> result = new HashMap<>(2);
        result.put(DbFieldNameConstant.PAGE_LIST, pageList);
        result.put(DbFieldNameConstant.TOTAL_COUNT, totalCount);
        return result;
    }


    @Override
    public AuthPostRole insertAuthPostRole(AuthPostRole authPostRole) {
        authPostRole.setPostRoleId(UUID.randomUUID().toString());
        // 唯一性校验
        checkUniqueness(authPostRole);
        authPostRoleMapper.insert(authPostRole);
        return authPostRole;
    }

    @Override
    public void batchInsertPostRole(List<AuthPostRole> authPostRoles) {
        if (authPostRoles != null && !CollectionUtils.isEmpty(authPostRoles)) {
            authPostRoles.forEach(authPostRole -> authPostRole.setPostRoleId(UUID.randomUUID().toString()));
            authPostRoleMapper.batchInsert(authPostRoles);
        }
    }

    @Override
    public void deleteAuthPostRole(String postRoleId) {
        authPostRoleMapper.delete(postRoleId);
    }

    @Override
    public void batchDelete(List<String> postRoleIds) {
        authPostRoleMapper.batchDelete(postRoleIds);
    }

    @Override
    public AuthPostRole updateAuthPostRole(AuthPostRole authPostRole) {
        authPostRoleMapper.update(authPostRole);
        return authPostRole;
    }

    @Override
    public List<AuthPostRole> listByPostIds(Collection<String> postIds) {
        return authPostRoleMapper.selectByPostIds(postIds);
    }

    @Override
    public TreeVo getRoleTree(String postId) {
        TreeVo treeVo = new TreeVo();
        // 角色组节点列表
        List<TreeNode> roleCatalogNodes = sysCatalogService.getNodesByType(CatalogTypeEnum.ROLE_GROUP_TYPE.getValue());
        if (!CollectionUtils.isEmpty(roleCatalogNodes)) {
            List<TreeNode> nodes = new ArrayList<>(8);
            for (TreeNode roleCatalogNode : roleCatalogNodes) {
                // 角色组下角色节点列表
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
            // 岗位已关联的角色
            List<AuthPostRole> authPostRoles = listByPost(postId);
            if (!CollectionUtils.isEmpty(authPostRoles)) {
                List<String> checkedRoleKeys = authPostRoles.stream()
                        .map(AuthPostRole::getRoleId)
                        .collect(Collectors.toList());
                treeVo.setCheckedKeys(checkedRoleKeys);
            }
        }
        return treeVo;
    }

    private void checkUniqueness(AuthPostRole authPostRole) {
        List<AuthPostRole> authPostRoles = authPostRoleMapper.listPostRole(authPostRole.getPostId(), authPostRole.getRoleId());
        if (!CollectionUtils.isEmpty(authPostRoles)) {
            boolean isPostRoleExist = authPostRoles.size() > 0;
            if (isPostRoleExist) {
                throw new GafException("该岗位已绑定该角色");
            }
        }
    }

}
