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
import com.supermap.gaf.authority.dao.AuthDepartmentMapper;
import com.supermap.gaf.authority.enums.NodeTypeEnum;
import com.supermap.gaf.authority.service.AuthDepartmentService;
import com.supermap.gaf.authority.service.AuthPostService;
import com.supermap.gaf.authority.service.AuthUserService;
import com.supermap.gaf.authority.util.TreeConvertUtil;
import com.supermap.gaf.authority.vo.AuthDepartmentSelectVo;
import com.supermap.gaf.authority.vo.TreeNode;
import com.supermap.gaf.commontypes.ShiroUser;
import com.supermap.gaf.exception.GafException;
import com.supermap.gaf.shiro.SecurityUtilsExt;
import com.supermap.gaf.data.access.service.BatchSortAndCodeService;
import org.springframework.aop.framework.AopContext;
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
 * 部门服务实现类
 * @date:2021/3/25
 * @author zhm
 */
@Service
public class AuthDepartmentServiceImpl implements AuthDepartmentService {
    @Autowired
    private AuthDepartmentMapper authDepartmentMapper;
    @Autowired
    private AuthPostService authPostService;
    @Autowired
    private AuthUserService authUserService;
    @Autowired
    private BatchSortAndCodeService batchSortAndCodeService;

//    public AuthDepartmentServiceImpl(AuthDepartmentMapper authDepartmentMapper, AuthPostService authPostService, AuthUserService authUserService, BatchSortAndCodeService batchSortAndCodeService) {
//        this.authDepartmentMapper = authDepartmentMapper;
//        this.authPostService = authPostService;
//        this.authUserService = authUserService;
//        this.batchSortAndCodeService = batchSortAndCodeService;
//    }

    @Override
    public AuthDepartment getById(String departmentId) {
        if (departmentId == null) {
            throw new IllegalArgumentException("departmentId不能为空");
        }
        return authDepartmentMapper.select(departmentId);
    }

    @Cacheable(value = CacheGroupConstant.DEPARTMENT, key = "#tenantId", unless = "#result == null")
    @Override
    public List<AuthDepartment> listDepartment(String search, String tenantId) {
        if (StringUtils.isEmpty(tenantId)) {
            throw new GafException("租户标识缺失");
        }
        AuthDepartmentSelectVo authDepartmentSelectVo = AuthDepartmentSelectVo.builder()
                .orderFieldName("sort_sn")
                .searchFieldName(DbFieldNameConstant.DEPARTMENT_NAME)
                .searchFieldValue(search)
                .build();
        return authDepartmentMapper.searchList(authDepartmentSelectVo, tenantId);
    }


    @Override
    public Map<String, Object> pageList(AuthDepartmentSelectVo authDepartmentSelectVo) {
        if (authDepartmentSelectVo.getPageSize() == null || authDepartmentSelectVo.getPageSize() == 0) {
            authDepartmentSelectVo.setPageSize(50);
        }
        List<AuthDepartment> pageList;
        if (authDepartmentSelectVo.getOffset() == null || authDepartmentSelectVo.getOffset() < CommonConstant.OFFSET_MAX_FOR_SQL_BETTER) {
            pageList = authDepartmentMapper.pageList(authDepartmentSelectVo);
        } else {
            pageList = authDepartmentMapper.bigOffsetPageList(authDepartmentSelectVo);
        }
        int totalCount = authDepartmentMapper.pageListCount();
        Map<String, Object> result = new HashMap<>(2);
        result.put(DbFieldNameConstant.PAGE_LIST, pageList);
        result.put(DbFieldNameConstant.TOTAL_COUNT, totalCount);
        return result;
    }

    @Override
    public Map<String, Object> searchList(AuthDepartmentSelectVo authDepartmentSelectVo) {
        if (authDepartmentSelectVo.getPageSize() == null || authDepartmentSelectVo.getPageSize() == 0) {
            authDepartmentSelectVo.setPageSize(50);
        }
        ShiroUser shiroUser = SecurityUtilsExt.getUser();
        String tenantId = Objects.requireNonNull(shiroUser).getTenantId();
        List<AuthDepartment> pageList = authDepartmentMapper.searchList(authDepartmentSelectVo, tenantId);
        Integer totalCount = authDepartmentMapper.countOneField(authDepartmentSelectVo.getSearchFieldName(), authDepartmentSelectVo.getSearchFieldValue());
        Map<String, Object> result = new HashMap<>(2);
        result.put(DbFieldNameConstant.PAGE_LIST, pageList);
        result.put(DbFieldNameConstant.TOTAL_COUNT, totalCount);
        return result;
    }

    @CacheEvict(value = CacheGroupConstant.DEPARTMENT, key = "#authDepartment.tenantId")
    @Transactional(rollbackFor = Exception.class)
    @Override
    public AuthDepartment insertAuthDepartment(AuthDepartment authDepartment) {
        authDepartment.setDepartmentId(UUID.randomUUID().toString());
        // 唯一性校验
        checkUniqueness(authDepartment, false);
        if (authDepartment.getStatus() == null) {
            authDepartment.setStatus(true);
        }
        authDepartmentMapper.insert(authDepartment);
        batchSortAndCodeService.revisionSortSnForInsertOrDelete(AuthDepartment.class, Collections.singletonList(authDepartment.getParentId()));
        return authDepartment;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchInsert(List<AuthDepartment> authDepartments) {
        if(!CollectionUtils.isEmpty(authDepartments)){
            Set<String> parentIds = new HashSet<>();
            for (AuthDepartment authDepartment : authDepartments) {
                authDepartment.setDepartmentId(UUID.randomUUID().toString());
                parentIds.add(authDepartment.getParentId());
            }
            authDepartmentMapper.batchInsert(authDepartments);
            batchSortAndCodeService.revisionSortSnForInsertOrDelete(AuthDepartment.class,parentIds);
        }

    }

    @CacheEvict(value = CacheGroupConstant.DEPARTMENT, key = "#tenantId")
    @Transactional(rollbackFor = Exception.class)
    @Override
    public AuthDepartment deleteAuthDepartment(String tenantId, String departmentId) {
        if (StringUtils.isEmpty(tenantId)) {
            throw new GafException("删除失败，租户标识不能为空");
        }
        if (StringUtils.isEmpty(departmentId)) {
            throw new GafException("删除失败，部门标识不能为空");
        }
        // 查询子部门
        AuthDepartmentSelectVo childSelectVo = AuthDepartmentSelectVo.builder()
                .searchFieldName(DbFieldNameConstant.PARENT_ID)
                .searchFieldValue(departmentId)
                .build();
        List<AuthDepartment> childrenDepartments = authDepartmentMapper.searchWholeMatchList(childSelectVo, tenantId);
        if (!CollectionUtils.isEmpty(childrenDepartments) && childrenDepartments.size() > 0) {
            throw new GafException("删除失败，该部门存在子部门");
        }
        List<AuthPost> authPosts = authPostService.listPost(null, departmentId);
        if (!CollectionUtils.isEmpty(authPosts) && authPosts.size() > 0) {
            throw new GafException("删除失败，该部门存在岗位");
        }
        List<AuthUser> users = authUserService.listUserByDepartment(tenantId, departmentId);
        if (!CollectionUtils.isEmpty(users) && users.size() > 0) {
            throw new GafException("删除失败，该部门存在用户");
        }
        List<AuthDepartment> departmentList = authDepartmentMapper.selectByCombination(AuthDepartment.builder().status(true).departmentId(departmentId).build());
        if (departmentList.size() == 0) {
            throw new GafException("删除失败，找不到该部门");
        } else if (departmentList.size() > 1) {
            throw new GafException("删除失败，找到该部门多条数据，请联系管理员核对");
        }
        AuthDepartment department = departmentList.get(0);
        if (department == null) {
            throw new GafException("删除失败，找不到该部门");
        }
        authDepartmentMapper.delete(departmentId);
        return department;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void batchDelete(String tenantId, List<String> departmentIds) {
        if (!CollectionUtils.isEmpty(departmentIds)) {
            departmentIds.forEach(departmentId -> ((AuthDepartmentService)AopContext.currentProxy()).deleteAuthDepartment(tenantId, departmentId));
        }
    }

    @CacheEvict(value = CacheGroupConstant.DEPARTMENT, key = "#authDepartment.tenantId")
    @Transactional(rollbackFor = Exception.class)
    @Override
    public AuthDepartment updateAuthDepartment(AuthDepartment authDepartment) {
        AuthDepartment authDepartmentExist = getById(authDepartment.getDepartmentId());
        if (null == authDepartmentExist || !authDepartmentExist.getStatus()) {
            throw new GafException("更新失败，不是有效部门");
        }
        // 唯一性校验
        checkUniqueness(authDepartment, true);
        Integer sortSn = authDepartment.getSortSn();
        Integer oldSortSn = authDepartmentExist.getSortSn();
        authDepartmentMapper.update(authDepartment);
        batchSortAndCodeService.revisionSortSnForUpdate(AuthDepartment.class,authDepartmentExist.getParentId(),oldSortSn,sortSn);
        return authDepartment;
    }

    @Override
    public List<AuthDepartment> getByIds(Set<String> departmentIds) {
        return authDepartmentMapper.selectByIds(departmentIds);
    }

    @Override
    public List<TreeNode> listDepartmentTreeNode(String tenantId) {
        List<AuthDepartment> departments = this.listDepartment(null, tenantId);
        return departments.stream().map(authDepartment -> {
            TreeNode treeNode = new TreeNode();
            treeNode.setKey(authDepartment.getDepartmentId());
            treeNode.setTitle(authDepartment.getDepartmentName());
            treeNode.setType(NodeTypeEnum.DEPARTMENT.getValue());
            if (StringUtils.isEmpty(authDepartment.getParentId())) {
                treeNode.setParentId(TreeConvertUtil.ROOT_PARENT_ID);
            } else {
                treeNode.setParentId(authDepartment.getParentId());
            }
            if (StringUtils.isEmpty(authDepartment.getSortSn())) {
                treeNode.setSortSn(1);
            } else {
                treeNode.setSortSn(authDepartment.getSortSn());
            }
            return treeNode;
        }).collect(Collectors.toList());
    }

    @Override
    public List<String> getDepartmentPath(String departmentId) {
        String parentId = departmentId;
        List<String> departmentPath = new LinkedList<>();
        while (!StringUtils.isEmpty(parentId) && !TreeConvertUtil.ROOT_PARENT_ID.equals(parentId)) {
            List<AuthDepartment> departments = this.authDepartmentMapper.selectByCombination(AuthDepartment.builder().status(true).departmentId(parentId).build());
            if (departments.size() == 0) {
                throw new GafException("未找到部门");
            }
            AuthDepartment authDepartment = departments.get(0);
            departmentPath.add(0, authDepartment.getDepartmentId());
            parentId = authDepartment.getParentId();
        }
        return departmentPath;
    }

    /**
     * 唯一性校验
     * @param authDepartment 部门
     * @param isUpdate       是否为更新，如是需要在校验时排除当前部门
     */
    private void checkUniqueness(AuthDepartment authDepartment, boolean isUpdate) {
        if (StringUtils.isEmpty(authDepartment.getTenantId())) {
            throw new GafException("租户信息缺失");
        }
        // 上级部门有效性校验
        String parentId = authDepartment.getParentId();
        if (!StringUtils.isEmpty(parentId) && !TreeConvertUtil.ROOT_PARENT_ID.equals(parentId)) {
            AuthDepartment parentDepartment = getById(parentId);
            if (null == parentDepartment || !parentDepartment.getStatus()) {
                throw new GafException("不是有效的上级部门");
            }
        }
        // departmentName，租户下不重复
        AuthDepartmentSelectVo nameSelectVo = AuthDepartmentSelectVo.builder()
                .searchFieldName(DbFieldNameConstant.DEPARTMENT_NAME)
                .searchFieldValue(authDepartment.getDepartmentName())
                .build();
        List<AuthDepartment> nameDepartments = authDepartmentMapper.searchWholeMatchList(nameSelectVo, authDepartment.getTenantId());
        if (!CollectionUtils.isEmpty(nameDepartments)) {
            boolean isNameExist;
            if (isUpdate) {
                isNameExist = nameDepartments.stream()
                        .anyMatch(department -> !department.getDepartmentId().equals(authDepartment.getDepartmentId()));
            } else {
                isNameExist = nameDepartments.size() > 0;
            }
            if (isNameExist) {
                throw new GafException("部门名称已存在");
            }
        }
    }
}
