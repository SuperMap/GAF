/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.authority.service.impl;

import com.supermap.gaf.authority.commontype.AuthDepartment;
import com.supermap.gaf.authority.commontype.AuthPost;
import com.supermap.gaf.authority.commontype.AuthUser;
import com.supermap.gaf.authority.constant.CacheGroupConstant;
import com.supermap.gaf.authority.constant.CommonConstant;
import com.supermap.gaf.authority.constant.DbFieldNameConstant;
import com.supermap.gaf.authority.dao.AuthPostMapper;
import com.supermap.gaf.authority.enums.NodeTypeEnum;
import com.supermap.gaf.authority.service.AuthDepartmentService;
import com.supermap.gaf.authority.service.AuthPostService;
import com.supermap.gaf.authority.service.AuthUserService;
import com.supermap.gaf.authority.util.TreeConvertUtil;
import com.supermap.gaf.authority.vo.AuthPostSelectVo;
import com.supermap.gaf.authority.vo.SelectOptionVo;
import com.supermap.gaf.authority.vo.TreeNode;
import com.supermap.gaf.commontypes.ShiroUser;
import com.supermap.gaf.exception.GafException;
import com.supermap.gaf.shiro.SecurityUtilsExt;
import com.supermap.gaf.data.access.service.BatchSortAndCodeService;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 岗位服务实现类
 * @author zhm
 * @date:2021/3/25
 * 
 */
@Service
public class AuthPostServiceImpl implements AuthPostService {
    @Autowired
    private AuthPostMapper authPostMapper;
    @Autowired
    private AuthDepartmentService authDepartmentService;
    @Autowired
    private AuthUserService authUserService;
    @Autowired
    private BatchSortAndCodeService batchSortAndCodeService;


    @Override
    public AuthPost getById(String postId) {
        if (postId == null) {
            throw new IllegalArgumentException("postId不能为空");
        }
        return authPostMapper.select(postId);
    }

    @Override
    public List<AuthPost> listPost(String search, String departmentId) {
        if (StringUtils.isEmpty(departmentId)) {
            throw new GafException("部门标识缺失");
        }
        AuthDepartment parentDepartment = authDepartmentService.getById(departmentId);
        if (null == parentDepartment || !parentDepartment.getStatus()) {
            throw new GafException("部门无效");
        }
        AuthPostSelectVo authPostSelectVo = AuthPostSelectVo.builder()
                .searchFieldName(DbFieldNameConstant.POST_NAME)
                .searchFieldValue(search)
                .build();
        return authPostMapper.searchList(authPostSelectVo, departmentId);
    }

    @Override
    public List<TreeNode> getPostTree(String tenantId) {
        if (StringUtils.isEmpty(tenantId)) {
            throw new GafException("租户标识缺失");
        }
        List<TreeNode> nodes = new ArrayList<>(8);
        // 当前用户对应租户下的部门
        List<AuthDepartment> authDepartments = authDepartmentService.listDepartment(null, tenantId);
        if (!CollectionUtils.isEmpty(authDepartments)) {
            authDepartments.forEach(authDepartment -> {
                //TODO too much request to db
                TreeNode departmentNode = new TreeNode();
                departmentNode.setKey(authDepartment.getDepartmentId());
                departmentNode.setTitle(authDepartment.getDepartmentName());
                departmentNode.setType(NodeTypeEnum.DEPARTMENT.getValue());
                if (StringUtils.isEmpty(authDepartment.getParentId())) {
                    departmentNode.setParentId("0");
                } else {
                    departmentNode.setParentId(authDepartment.getParentId());
                }
                if (StringUtils.isEmpty(authDepartment.getSortSn())) {
                    departmentNode.setSortSn(1);
                } else {
                    departmentNode.setSortSn(authDepartment.getSortSn());
                }
                nodes.add(departmentNode);
                // 部门下岗位
                List<AuthPost> authPosts = listPost(null, authDepartment.getDepartmentId());
                if (!CollectionUtils.isEmpty(authPosts)) {
                    authPosts.forEach(authPost -> {
                        TreeNode postNode = new TreeNode();
                        postNode.setKey(authPost.getPostId());
                        postNode.setParentId(authPost.getDepartmentId());
                        postNode.setTitle(authPost.getPostName());
                        postNode.setType(NodeTypeEnum.POST.getValue());
                        postNode.setSortSn(authPost.getSortSn());
                        nodes.add(postNode);
                    });
                }
            });
        }
        // 一级排序 type  二级排序 序号
        return TreeConvertUtil.convertToTree(nodes,Comparator.comparing(n->""+n.getType()+n.getSortSn()));
    }

    @Override
    public Map<String, Object> pageList(AuthPostSelectVo authPostSelectVo) {
        if (authPostSelectVo.getPageSize() == null || authPostSelectVo.getPageSize() == 0) {
            authPostSelectVo.setPageSize(50);
        }
        List<AuthPost> pageList;
        if (authPostSelectVo.getOffset() == null || authPostSelectVo.getOffset() < CommonConstant.OFFSET_MAX_FOR_SQL_BETTER) {
            pageList = authPostMapper.pageList(authPostSelectVo);
        } else {
            pageList = authPostMapper.bigOffsetPageList(authPostSelectVo);
        }
        int totalCount = authPostMapper.pageListCount();
        Map<String, Object> result = new HashMap<>(2);
        result.put(DbFieldNameConstant.PAGE_LIST, pageList);
        result.put(DbFieldNameConstant.TOTAL_COUNT, totalCount);
        return result;
    }

    @Override
    public Map<String, Object> searchList(AuthPostSelectVo authPostSelectVo) {
        if (authPostSelectVo.getPageSize() == null || authPostSelectVo.getPageSize() == 0) {
            authPostSelectVo.setPageSize(50);
        }
        ShiroUser shiroUser = SecurityUtilsExt.getUser();
        String tenantId = Objects.requireNonNull(shiroUser).getTenantId();
        List<AuthPost> pageList = authPostMapper.searchList(authPostSelectVo, tenantId);
        Integer totalCount = authPostMapper.countOneField(authPostSelectVo.getSearchFieldName(), authPostSelectVo.getSearchFieldValue());
        Map<String, Object> result = new HashMap<>(2);
        result.put(DbFieldNameConstant.PAGE_LIST, pageList);
        result.put(DbFieldNameConstant.TOTAL_COUNT, totalCount);
        return result;
    }

    @CacheEvict(value = CacheGroupConstant.POST, key = "#authPost.departmentId")
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AuthPost insertAuthPost(AuthPost authPost) {
        authPost.setPostId(UUID.randomUUID().toString());
        // 唯一性校验
        checkUniqueness(authPost, false);

        authPostMapper.insert(authPost);
        batchSortAndCodeService.revisionSortSnForInsertOrDelete(AuthPost.class, Collections.singletonList(authPost.getDepartmentId()));
        return authPost;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchInsert(List<AuthPost> authPosts) {
        if(!CollectionUtils.isEmpty(authPosts)){
            Set<String> parentIds = new HashSet<>();
            for (AuthPost authPost : authPosts) {
                authPost.setPostId(UUID.randomUUID().toString());
                parentIds.add(authPost.getDepartmentId());
            }
            authPostMapper.batchInsert(authPosts);
            batchSortAndCodeService.revisionSortSnForInsertOrDelete(AuthPost.class,parentIds);
        }

    }

    @CacheEvict(value = CacheGroupConstant.POST, key = "#departmentId")
    @Transactional(rollbackFor = Exception.class)
    @Override
    public AuthPost deleteAuthPost(String departmentId, String postId) {
        if (StringUtils.isEmpty(departmentId)) {
            throw new GafException("删除失败，部门标识缺失");
        }
        if (StringUtils.isEmpty(postId)) {
            throw new GafException("删除失败，岗位标识缺失");
        }
        List<AuthUser> authUsers = authUserService.listUserByPost(postId);
        if (!CollectionUtils.isEmpty(authUsers) && authUsers.size() > 0) {
            throw new GafException("删除失败，该岗位存在用户");
        }
        AuthPost authPost = authPostMapper.select(postId);
        if (authPost == null) {
            throw new GafException("找不到该岗位");
        }
        authPostMapper.delete(postId);
        return authPost;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(String departmentId, List<String> postIds) {
        if (!CollectionUtils.isEmpty(postIds)) {
            postIds.forEach(postId -> ((AuthPostService) AopContext.currentProxy()).deleteAuthPost(departmentId, postId));
        }
    }

    @CacheEvict(value = CacheGroupConstant.POST, key = "#authPost.departmentId")
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AuthPost updateAuthPost(AuthPost authPost) {
        AuthPost authPostExist = getById(authPost.getPostId());
        if (null == authPostExist || !authPostExist.getStatus()) {
            throw new GafException("更新失败，不是有效的岗位");
        }
        // 唯一性校验
        checkUniqueness(authPost, true);

        authPostMapper.update(authPost);
        String parentId = authPost.getDepartmentId()!=null? authPost.getDepartmentId():authPostExist.getDepartmentId();
        batchSortAndCodeService.revisionSortSnForUpdate(AuthPost.class,parentId,authPostExist.getSortSn(),authPost.getSortSn());
        return authPost;
    }

    @Override
    public List<AuthPost> getByIds(Set<String> postIds) {
        return authPostMapper.selectByIds(postIds);
    }

    @Override
    public List<SelectOptionVo> listPostOptionsByDepartment(String departmentId) {
        List<AuthPost> posts = this.listPost(null, departmentId);
        return posts.stream().map(post -> {
            SelectOptionVo selectOptionVo = new SelectOptionVo();
            selectOptionVo.setValue(post.getPostId());
            selectOptionVo.setLabel(post.getPostName());
            selectOptionVo.setKey(post.getPostId());
            return selectOptionVo;
        }).collect(Collectors.toList());

    }

    @Override
    public List<AuthPost> listPostByDepartmentId(String departmentId) {
        return authPostMapper.selectByDepartmentId(departmentId);
    }

    private void checkUniqueness(AuthPost authPost, boolean isUpdate) {
        // 部门有效性校验
        if (StringUtils.isEmpty(authPost.getDepartmentId())) {
            throw new GafException("部门标识缺失");
        }
        AuthDepartment authDepartment = authDepartmentService.getById(authPost.getDepartmentId());
        if (null == authDepartment || !authDepartment.getStatus()) {
            throw new GafException("不是有效的岗位");
        }

        // name，部门下不重复
        AuthPostSelectVo authPostSelectVo = AuthPostSelectVo.builder()
                .searchFieldName(DbFieldNameConstant.POST_NAME)
                .searchFieldValue(authPost.getPostName())
                .build();
        List<AuthPost> authPosts = authPostMapper.searchWholeMatchList(authPostSelectVo, authPost.getDepartmentId());
        if (!CollectionUtils.isEmpty(authPosts)) {
            boolean isNameExist;
            if (isUpdate) {
                isNameExist = authPosts.stream()
                        .anyMatch(post -> !post.getPostId().equals(authPost.getPostId()));
            } else {
                isNameExist = authPosts.size() > 0;
            }
            if (isNameExist) {
                throw new GafException("岗位名称已存在");
            }
        }
    }
}
