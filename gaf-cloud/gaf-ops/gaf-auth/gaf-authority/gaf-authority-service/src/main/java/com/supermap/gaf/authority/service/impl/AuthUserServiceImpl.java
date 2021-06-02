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
import com.supermap.gaf.authority.dao.AuthUserMapper;
import com.supermap.gaf.authority.enums.CodeBaseRoleEnum;
import com.supermap.gaf.authority.enums.NodeTypeEnum;
import com.supermap.gaf.authority.service.*;
import com.supermap.gaf.authority.util.TreeConvertUtil;
import com.supermap.gaf.authority.util.email.EmailConstant;
import com.supermap.gaf.authority.util.email.EmailService;
import com.supermap.gaf.authority.vo.AuthUserParttimeSelectVo;
import com.supermap.gaf.authority.vo.AuthUserSelectVo;
import com.supermap.gaf.authority.vo.EmailChangeVo;
import com.supermap.gaf.authority.vo.TreeNode;
import com.supermap.gaf.data.access.service.BatchSortAndCodeService;
import com.supermap.gaf.exception.GafException;
import com.supermap.gaf.project.client.ProjCodeBaseUsersClient;
import com.supermap.gaf.shiro.SecurityUtilsExt;
import com.supermap.gaf.shiro.commontypes.ShiroUser;
import com.supermap.gaf.utils.LogUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.supermap.gaf.authority.util.TreeConvertUtil.ROOT_PARENT_ID;

/**
 * 用户服务实现类
 * @date:2021/3/25
 * @author dqc
 */
@Service
public class AuthUserServiceImpl implements AuthUserService {
    private static final Logger logger = LogUtil.getLocLogger(AuthTenantServiceImpl.class);
    @Value("${gaf.authority.user.initPassword:}")
    private String initPassword;
    @Value("${spring.mail.enable:true}")
    private Boolean mailEnable;

    @Autowired
    private  AuthUserMapper authUserMapper;
    @Autowired
    private  AuthDepartmentService authDepartmentService;
    @Autowired
    private  AuthPostService authPostService;
    @Autowired
    private  AuthRoleService authRoleService;
    @Autowired
    private  AuthUserRoleService authUserRoleService;
    @Autowired
    private  AuthPostRoleService authPostRoleService;

    @Autowired
    private  AuthUserParttimeService authUserParttimeService;
    @Autowired
    private  EmailService emailService;

    @Autowired
    public RedisTemplate<String, Object> redisTemplate;

    @Autowired(required = false)
    private ProjCodeBaseUsersClient projCodeBaseUsersClient;
    @Autowired
    private  BatchSortAndCodeService batchSortAndCodeService;

    /**
     * 用户密码长度
     */
    private static final int USER_PASSWORD_LENGTH = 8;

//    public AuthUserServiceImpl(AuthUserMapper authUserMapper, AuthDepartmentService authDepartmentService, AuthPostService authPostService, AuthRoleService authRoleService, AuthUserRoleService authUserRoleService, AuthPostRoleService authPostRoleService, AuthUserParttimeService authUserParttimeService, EmailService emailService, ProjCodeBaseUsersFeignService projCodeBaseUsersFeignService, BatchSortAndCodeService batchSortAndCodeService) {
//        this.authUserMapper = authUserMapper;
//        this.authDepartmentService = authDepartmentService;
//        this.authPostService = authPostService;
//        this.authRoleService = authRoleService;
//        this.authUserRoleService = authUserRoleService;
//        this.authPostRoleService = authPostRoleService;
//        this.authUserParttimeService = authUserParttimeService;
//        this.emailService = emailService;
//        this.projCodeBaseUsersFeignService = projCodeBaseUsersFeignService;
//        this.batchSortAndCodeService = batchSortAndCodeService;
//    }

    @Override
    public AuthUser getById(String userId) {
        if (userId == null) {
            throw new IllegalArgumentException("userId不能为空");
        }
        return authUserMapper.select(userId);
    }

    @Override
    public List<AuthUser> getByTenantId(String tenantId) {
        if (tenantId == null) {
            throw new IllegalArgumentException("tenantId不能为空");
        }
        return authUserMapper.selectByTenantId(tenantId);
    }

    @Override
    public Map<String, Object> pageListByTenantId(String tenantId, String realName, Integer size, Integer offset) {
        if(size == null || size == 0) {
            size = 50;
        }
        List<AuthUser> pageList;
        if(offset == null || offset == 0) {
            offset = 1;
        }
        pageList = authUserMapper.pageListByTenantId(tenantId,realName,size,offset);
        int totalCount = authUserMapper.countGetListByTenantId(tenantId,realName);
        Map<String, Object> result = new HashMap<>(2);
        result.put(DbFieldNameConstant.PAGE_LIST, pageList);
        result.put(DbFieldNameConstant.TOTAL_COUNT, totalCount);
        return result;
    }


    @Override
    public Map<String, Object> pageList(AuthUserSelectVo authUserSelectVo) {
        if (authUserSelectVo.getPageSize() == null || authUserSelectVo.getPageSize() == 0) {
            authUserSelectVo.setPageSize(50);
        }
        List<AuthUser> pageList;
        if (authUserSelectVo.getOffset() == null || authUserSelectVo.getOffset() < CommonConstant.OFFSET_MAX_FOR_SQL_BETTER) {
            pageList = authUserMapper.pageList(authUserSelectVo);
        } else {
            pageList = authUserMapper.bigOffsetPageList(authUserSelectVo);
        }
        int totalCount = authUserMapper.pageListCount();
        Map<String, Object> result = new HashMap<>(2);
        result.put(DbFieldNameConstant.PAGE_LIST, pageList);
        result.put(DbFieldNameConstant.TOTAL_COUNT, totalCount);
        return result;
    }

    @Override
    public Map<String, Object> searchList(AuthUserSelectVo authUserSelectVo) {
        if (authUserSelectVo.getPageSize() == null || authUserSelectVo.getPageSize() == 0) {
            authUserSelectVo.setPageSize(50);
        }
        List<AuthUser> pageList = authUserMapper.searchList(authUserSelectVo);
        Integer totalCount = authUserMapper.countOneField(authUserSelectVo.getSearchFieldName(), authUserSelectVo.getSearchFieldValue());
        Map<String, Object> result = new HashMap<>(2);
        result.put(DbFieldNameConstant.PAGE_LIST, pageList);
        result.put(DbFieldNameConstant.TOTAL_COUNT, totalCount);
        return result;
    }

    @Override
    public List<AuthUser> listUserByDepartment(String tenantId, String departmentId) {
        if (StringUtils.isEmpty(tenantId)) {
            throw new GafException("租户标识不能为空");
        }
        if (StringUtils.isEmpty(departmentId)) {
            throw new GafException("部门标识不能为空");
        }
        AuthDepartment authDepartment = authDepartmentService.getById(departmentId);
        if (null == authDepartment || !authDepartment.getStatus()) {
            throw new GafException("部门无效");
        }
        List<AuthUser> authUsers = authUserMapper.selectByCombination(AuthUser.builder().departmentId(departmentId).status(true).tenantId(tenantId).build());
        for (AuthUser authUser : authUsers) {
            authUser.setDepartmentName(authDepartment.getDepartmentName());
        }
        return authUsers;
    }

    @Override
    public List<AuthUser> listUserByPost(String postId) {
        if (StringUtils.isEmpty(postId)) {
            throw new GafException("岗位标识不能为空");
        }
        //用户表通过岗位查询
        AuthUserSelectVo authUserSelectVo = AuthUserSelectVo.builder()
                .searchFieldName(DbFieldNameConstant.POST_ID)
                .searchFieldValue(postId)
                .build();
        List<AuthUser> authUsers = authUserMapper.searchWholeMatchList(authUserSelectVo);
        //挂职表通过岗位查询
        AuthUserParttimeSelectVo parttimeSelectVo = AuthUserParttimeSelectVo.builder()
                .searchFieldName(DbFieldNameConstant.POST_ID)
                .searchFieldValue(postId)
                .build();
        List<AuthUserParttime> authUserParttimes = null;
        try {
            authUserParttimes = (List<AuthUserParttime>) authUserParttimeService.pageList(parttimeSelectVo).get(DbFieldNameConstant.PAGE_LIST);
        }catch (Exception e){
            logger.error("查询用户挂职失败");
        }
        if(!CollectionUtils.isEmpty(authUserParttimes)){
            for (AuthUserParttime authUserParttime : authUserParttimes){
                AuthUser authUser = getById(authUserParttime.getUserId());
                authUser.setBelongsParttime(true);
                authUsers.add(authUser);
            }
        }
        if (!CollectionUtils.isEmpty(authUsers)) {
            String departmentName = null;
            AuthPost authPost = authPostService.getById(postId);
            if (null != authPost && authPost.getStatus()) {
                AuthDepartment authDepartment = authDepartmentService.getById(authPost.getDepartmentId());
                if (null != authDepartment && authDepartment.getStatus()) {
                    departmentName = authDepartment.getDepartmentName();
                }
            }
            for (AuthUser authUser : authUsers) {
                authUser.setDepartmentName(departmentName);
                if (authPost != null){
                    authUser.setPostName(authPost.getPostName());
                }
            }
        }
        return authUsers;
    }

    @Override
    public List<TreeNode> getUserTree(String tenantId) {
        List<TreeNode> nodes = new ArrayList<>(8);
        // 当前用户对应租户下的部门
        List<AuthDepartment> authDepartments = authDepartmentService.listDepartment(null, tenantId);
        if (!CollectionUtils.isEmpty(authDepartments)) {
            authDepartments.forEach(authDepartment -> {
                TreeNode departmentNode = new TreeNode();
                departmentNode.setKey(authDepartment.getDepartmentId());
                departmentNode.setTitle(authDepartment.getDepartmentName());
                departmentNode.setType(NodeTypeEnum.DEPARTMENT.getValue());
                if (StringUtils.isEmpty(authDepartment.getParentId())) {
                    departmentNode.setParentId(ROOT_PARENT_ID);
                } else {
                    departmentNode.setParentId(authDepartment.getParentId());
                }
                departmentNode.setSortSn(authDepartment.getSortSn());
                nodes.add(departmentNode);
                // 部门下用户
                List<AuthUser> authUsers = listUserByDepartment(tenantId, authDepartment.getDepartmentId());
                if (!CollectionUtils.isEmpty(authUsers)) {
                    authUsers.forEach(authUser -> {
                        TreeNode postNode = new TreeNode();
                        postNode.setKey(authUser.getUserId());
                        postNode.setParentId(authUser.getDepartmentId());
                        postNode.setTitle(authUser.getRealName());
                        postNode.setType(NodeTypeEnum.USER.getValue());
                        postNode.setSortSn(authUser.getSortSn());
                        nodes.add(postNode);
                    });
                }
            });
        }
        // 一级排序 type  二级排序 序号
        return TreeConvertUtil.convertToTree(nodes, Comparator.comparing(n -> ("" + n.getType() + n.getSortSn())));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public AuthUser insertAuthUser(AuthUser authUser) {
        // 唯一性校验
        checkUniqueness(authUser, false);

        // 生成初始密码
        String password = generatePassword();
        authUser.setPassword(password);

        String userId = UUID.randomUUID().toString();
        authUser.setUserId(userId);
        // 加密后的密码，存入数据库
        String bCryptPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        authUser.setPassword(bCryptPassword);
        // 设置用户排序
        if (authUser.getSortSn() != null && authUser.getSortSn() < 1) {
            authUser.setSortSn(1);
        } else {
            Integer count = authUserMapper.countByCombination(AuthUser.builder().status(true).departmentId(authUser.getDepartmentId()).build());
            if (authUser.getSortSn() == null || authUser.getSortSn() > count + 1) {
                authUser.setSortSn(count + 1);
            }
        }
        authUserMapper.insert(authUser);
        batchSortAndCodeService.revisionSortSnForInsertOrDelete(AuthUser.class, Collections.singletonList(authUser.getDepartmentId()));

        AuthUser insertedAuthUser = authUserMapper.select(userId);

        // 将初始密码发送至邮箱中
        if(mailEnable){
            emailService.sendPassword(authUser.getEmail(), password);
        }
        //判断岗位是否有app相关的+角色，若有则需要在gitlab中创建用户
        try {
            String postId = authUser.getPostId();
            if (!StringUtils.isEmpty(postId)) {
                List<AuthPostRole> authPostRoles = authPostRoleService.getByPostId(postId, true);
                if (authPostRoles.size() > 0) {
                    // 应该使用id常量而不应该使用name查询
                    List<String> nameEns = CodeBaseRoleEnum.getAllNames();
                    List<AuthRole> appRoles = authRoleService.listByNameEn(nameEns);
                    Set<String> appRoleIds = appRoles.stream().map(AuthRole::getRoleId).collect(Collectors.toSet());
                    List<AuthPostRole> authPostRolesInAppRole = authPostRoles.stream().filter(authPostRole -> appRoleIds.contains(authPostRole.getRoleId())).collect(Collectors.toList());
                    if (authPostRolesInAppRole.size() > 0) {
                        try {
                            projCodeBaseUsersClient.addDevUser(authUser.getRealName(), authUser.getName(), authUser.getEmail());
                        } catch (Exception e) {
                            logger.info("创建代码库用户失败", e);
                        }
                    }

                }
            }
        } catch (Exception e) {
            logger.info("创建代码库用户失败", e);
            // 不管真假失败都不影响创建用户
        }
        return insertedAuthUser;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void batchInsert(List<AuthUser> authUsers) {
        if (!CollectionUtils.isEmpty(authUsers)) {
            ShiroUser shiroUser = SecurityUtilsExt.getUser();
            String tenantId = Objects.requireNonNull(shiroUser).getTenantId();
            Set<String> parentIds = new HashSet<>();
            authUsers.forEach(authUser -> {
                parentIds.add(authUser.getDepartmentId());
                authUser.setUserId(UUID.randomUUID().toString());
                authUser.setTenantId(tenantId);
            });
            checkUniqueness(authUsers);
            authUserMapper.batchInsert(authUsers);
            batchSortAndCodeService.revisionSortSnForInsertOrDelete(AuthUser.class,parentIds);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public AuthUser deleteAuthUser(String userId) {
        if (StringUtils.isEmpty(userId)) {
            throw new GafException("删除异常，用户标识缺失");
        }
        AuthUser oldAuthUser = authUserMapper.select(userId);
        if (oldAuthUser == null) {
            throw new GafException("找不到用户");
        }
        // 查询用户的所有角色 若有租户管理员的角色则不允许禁用
        Set<String> roleIdSet = listRole(oldAuthUser);
        if (roleIdSet.contains(AuthUser.TENANT_ADMIN_ROLE_ID)) {
            throw new GafException("该用户有租户管理员的角色，不能禁用");
        }
        // 清空岗位
        authUserMapper.update(AuthUser.builder().userId(userId).postId(null).build());
        // 删除用户
        authUserMapper.delete(userId);
        // 清空挂职和角色
        authUserParttimeService.deleteByUserId(userId);
        authUserRoleService.deleteByUserId(userId);
        try {
            // 到代码库用户表和gitlab中禁用用户
            projCodeBaseUsersClient.blockDevUser(oldAuthUser.getName());
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
        return oldAuthUser;
    }


    private Set<String> listRole(AuthUser authUser) {
        //1.获取用户岗位post
        String userId = authUser.getUserId();
        String postId = authUser.getPostId();
        List<String> postIds = new LinkedList<>();
        if (!StringUtils.isEmpty(postId)) {
            postIds.add(postId);
        }
        List<AuthUserParttime> authUserParttimes = authUserParttimeService.getByUserId(userId);
        for (AuthUserParttime authUserParttime : authUserParttimes) {
            String parttimePostId = authUserParttime.getPostId();
            if (!StringUtils.isEmpty(parttimePostId)) {
                postIds.add(parttimePostId);
            }
        }
        Set<String> roleIdSet = new HashSet<>();
        if (postIds.size() > 0) {
            List<AuthPostRole> authPostRoleList = authPostRoleService.listByPostIds(postIds);
            for (AuthPostRole authPostRole: authPostRoleList) {
                String roleId = authPostRole.getRoleId();
                if (!StringUtils.isEmpty(roleId)) {
                    roleIdSet.add(roleId);
                }
            }
        }
        List<AuthUserRole> authUserRoles = authUserRoleService.listByUser(userId);
        for (AuthUserRole authUserRole: authUserRoles) {
            if (!StringUtils.isEmpty(authUserRole.getRoleId())) {
                roleIdSet.add(authUserRole.getRoleId());
            }
        }
        return roleIdSet;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void batchDelete(List<String> userIds) {
        if (!CollectionUtils.isEmpty(userIds)) {
            userIds.forEach(this::deleteAuthUser);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AuthUser updateAuthUser(AuthUser authUser) {
        AuthUser authUserExist = getById(authUser.getUserId());
        if (null == authUserExist || !authUserExist.getStatus()) {
            throw new GafException("更新失败，不是有效的用户");
        }

        checkUniqueness(authUser, true);
        authUserMapper.update(authUser);
        String parentId = authUser.getDepartmentId()!=null? authUser.getDepartmentId():authUserExist.getDepartmentId();
        batchSortAndCodeService.revisionSortSnForUpdate(AuthUser.class,parentId,authUserExist.getSortSn(),authUser.getSortSn());
        // 是否更新部门
        String newDepartmentId = authUser.getDepartmentId();
        String oldDepartmentId = authUserExist.getDepartmentId();
        if (newDepartmentId == null) {
            throw new GafException("部门不能为空");
        }
        if (!newDepartmentId.equals(oldDepartmentId)) {
            // todo：更新老部门用户的排序
            // todo：更新新部门下用户的排序
        }
        // 是否更新岗位
        String newPostId = authUser.getPostId();
        String oldPostId = authUserExist.getPostId();
        if (!StringUtils.isEmpty(newPostId) && !newPostId.equals( oldPostId )) {
            // 应该使用id去查询而不应该使用name
            List<String> nameEns = CodeBaseRoleEnum.getAllNames();
            List<AuthRole> appRoles = authRoleService.listByNameEn(nameEns);
            Set<String> appRoleIds = appRoles.stream().map(AuthRole::getRoleId).collect(Collectors.toSet());
            // 在查看用户挂职 和角色有没有app角色
            boolean hasAppRoleBefore = hasAppRoleOnParttimeOrRole(authUser, appRoleIds);
            if (!hasAppRoleBefore) {
                if (StringUtils.isEmpty(newPostId)) {
                    // 查看原来的岗位是否有app角色
                    List<AuthPostRole> authPostRoles = authPostRoleService.getByPostId(oldPostId, true);
                    boolean oldPostHasAppRole = authPostRoles.stream().anyMatch(authPostRole -> appRoleIds.contains(authPostRole.getRoleId()));
                    if (oldPostHasAppRole) {
                        try {
                            projCodeBaseUsersClient.blockDevUser(authUserExist.getName());
                        } catch (Exception e) {
                            logger.info("删除代码库用户失败", e);
                        }
                    }
                } else {
                    // 查看新的岗位是否有app角色
                    List<AuthPostRole> authNewPostRoles = authPostRoleService.getByPostId(newPostId, true);
                    boolean newPostHasAppRole = authNewPostRoles.stream().anyMatch(authPostRole -> appRoleIds.contains(authPostRole.getRoleId()));
                    // 查看原来的岗位是否有app角色
                    boolean oldPostHasAppRole = false;
                    if (!StringUtils.isEmpty(oldPostId)) {
                        List<AuthPostRole> authPostRoles = authPostRoleService.getByPostId(oldPostId, true);
                        oldPostHasAppRole = authPostRoles.stream().anyMatch(authPostRole -> appRoleIds.contains(authPostRole.getRoleId()));
                    }
                    if (newPostHasAppRole && !oldPostHasAppRole) {
                        try {
                            projCodeBaseUsersClient.addDevUser(authUser.getRealName(), authUser.getName(), authUser.getEmail());
                        } catch (Exception e) {
                            logger.info("添加代码库用户失败", e);
                        }
                    } else if (!newPostHasAppRole && oldPostHasAppRole) {
                        try {
                            projCodeBaseUsersClient.blockDevUser(authUserExist.getName());
                        } catch (Exception e) {
                            logger.info("删除代码库用户失败", e);
                        }
                    }

                }

            }
        }

        return authUser;
    }

    private boolean hasAppRoleOnParttimeOrRole(AuthUser authUser, Set<String> appRoleIds) {
        boolean hasAppRoleBefore = false;
        List<AuthUserParttime> userParttimes = authUserParttimeService.getByUserId(authUser.getUserId());
        List<String> postIds = userParttimes.stream().map(AuthUserParttime::getPostId).collect(Collectors.toList());
        if (postIds.size() > 0) {
            List<AuthPostRole> authParttimePostRoles = authPostRoleService.listByPostIds(postIds);
            hasAppRoleBefore = authParttimePostRoles.stream().anyMatch(authPostRole -> appRoleIds.contains(authPostRole.getRoleId()));
        }
        if (!hasAppRoleBefore) {
            List<AuthUserRole> authUserRoles = authUserRoleService.listByUser(authUser.getUserId());
            hasAppRoleBefore = authUserRoles.stream().anyMatch(authUserRole -> appRoleIds.contains(authUserRole.getRoleId()));

        }
        return hasAppRoleBefore;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public AuthUser resetPassword(String userId) {
        AuthUser authUser = getById(userId);
        if (authUser == null || !authUser.getStatus()) {
            throw new GafException("用户不存在");
        }
        // 生成初始密码
        String newPassword = generatePassword();
        // 加密后的密码，存入数据库
        String bCryptPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
        authUser.setPassword(bCryptPassword);
        authUserMapper.update(authUser);
        // 将新密码发送至邮箱中
        if(mailEnable){
            emailService.sendPassword(authUser.getEmail(), newPassword);
        }
        return authUser;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<AuthUser> listUserByDepartmentWithName(String tenantId, String departmentId) {
        AuthDepartment department = authDepartmentService.getById(departmentId);
        if (department == null) {
            throw new GafException("找不到部门信息");
        }
        List<AuthUser> authUsers = authUserMapper.selectByDepartmentIdAndTenantIdWithSort(tenantId, departmentId);
        if (authUsers.size() > 0) {
            for (AuthUser authUser : authUsers) {
                authUser.setDepartmentName(department.getDepartmentName());
            }
        }
        List<AuthPost> authPosts = authPostService.listPost(null, departmentId);
        if (authUsers.size() > 0 && authPosts != null && authPosts.size() > 0) {
            Map<String, AuthPost> idAndPostMap = authPosts.stream().collect(Collectors.toMap(AuthPost::getPostId, authPost -> authPost));
            for (AuthUser authUser : authUsers) {
                AuthPost authPost = idAndPostMap.get(authUser.getPostId());
                if (authPost != null) {
                    authUser.setPostName(authPost.getPostName());
                }
            }
        }
        return authUsers;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public AuthUser active(String userId) {
        List<AuthUser> authUsers = authUserMapper.selectByCombination(AuthUser.builder().userId(userId).build());
        if (authUsers.size() != 1) {
            throw new GafException("未找到该用户");
        }
        AuthUser authUser = authUsers.get(0);
        if (!authUser.getStatus()) {
            authUser.setStatus(true);
            this.deletePhysicsById(userId);
            this.insertAuthUser(authUser);
        }
        return authUser;
    }

    @Override
    public void deletePhysicsById(String userId) {
        authUserMapper.deletePhysicsById(userId);
    }

    @Override
    public AuthUser getUserInfo() {
        ShiroUser shiroUser = SecurityUtilsExt.getUser();
        AuthUser authUser = Objects.requireNonNull(shiroUser).getAuthUser();
        String departmentId = authUser.getDepartmentId();
        if (!StringUtils.isEmpty(departmentId)) {
            AuthDepartment department = authDepartmentService.getById(departmentId);
            authUser.setDepartmentName(department.getDepartmentName());
        }
        String postId = authUser.getPostId();
        if (!StringUtils.isEmpty(postId)) {
            AuthPost post = authPostService.getById(postId);
            authUser.setPostName(post.getPostName());
        }
        authUser.setPassword(null);
        return authUser;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void changePassword(AuthUser authUser, String oldPassword, String newPassword) {
        AuthUser user = authUserMapper.selectWithPassword(authUser.getUserId());
        boolean isSame = BCrypt.checkpw(oldPassword, user.getPassword());
        if (isSame) {
            String bCryptPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
            authUserMapper.update(AuthUser.builder().userId(user.getUserId()).postId(user.getPostId()).password(bCryptPassword).sortSn(null).build());
        } else {
            throw new GafException("旧密码不正确");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String changeEmail(EmailChangeVo emailChangeVo) {
        ShiroUser shiroUser = SecurityUtilsExt.getUser();
        AuthUser authUser = Objects.requireNonNull(shiroUser).getAuthUser();
        if(Objects.equals(authUser.getEmail(),emailChangeVo.getNewEmail())) {
            return "新旧邮箱相同";
        }



        if (!StringUtils.isEmpty(authUser.getEmail())) {
            // 校验
            Object oldEmailCheckCode = redisTemplate.opsForValue().get(CacheGroupConstant.CHECK_CODE + ":" + authUser.getEmail());
            if (Objects.isNull(oldEmailCheckCode)) {
                return "原邮箱校验码已过时";
            }
            if(!Objects.equals(emailChangeVo.getOldEmailCheckCode(),oldEmailCheckCode)) {
                return "校验码:"+ emailChangeVo.getOldEmailCheckCode()+"错误";
            }
        }


        Object newEmailCheckCode = redisTemplate.opsForValue().get(CacheGroupConstant.CHECK_CODE + ":" + emailChangeVo.getNewEmail());
        if (Objects.isNull(newEmailCheckCode)) {
            return "新邮箱校验码已过时";
        }
        if(!Objects.equals(emailChangeVo.getNewEmailCheckCode(),newEmailCheckCode)) {
            return "校验码:"+ emailChangeVo.getNewEmailCheckCode()+"错误";
        }
        AuthUser user = getById(authUser.getUserId());
        user.setPassword(null);
        user.setEmail(emailChangeVo.getNewEmail());
        authUserMapper.update(user);
        return null;
    }

    @Override
    public void sendCheckCode(String email) {
        ShiroUser shiroUser = SecurityUtilsExt.getUser();
        AuthUser authUser = Objects.requireNonNull(shiroUser).getAuthUser();
        if(StringUtils.isEmpty(email)) {
            email = authUser.getEmail();
        }
        if(StringUtils.isEmpty(email)) {
            throw new GafException("邮箱为空");
        }
        String checkCode = generateRandomStr(6);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime after10Min = now.plusMinutes(10);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String format = dtf.format(after10Min);
        redisTemplate.opsForValue().set(CacheGroupConstant.CHECK_CODE+":"+ email,checkCode, Duration.ofMinutes(10));
        String context = String.format(EmailConstant.CHECK_CODE_TEXT_TEMPLATE,authUser.getName(),checkCode,"10",format);
        emailService.sendText(email, EmailConstant.CAHANGE_EMAIL,context);
    }

    // 生成几位随机数字 不足以0填充
    private String generateRandomStr(int bit) {
        int randomNum = generateRandomNum(bit);
        return String.format("%0"+bit+"d",randomNum);
    }
    // 生成几位随机数字
    private int generateRandomNum(int bit) {
        Random random = new Random();
        return random.nextInt((int)Math.pow(10,bit));
    }


    /**
     * 随机生成密码
     *
     * @return 密码
     */
    private String generatePassword() {
        if(!StringUtils.isBlank(this.initPassword)){
            return this.initPassword;
        }
        return UUID.randomUUID().toString().replace("-", "").substring(0, USER_PASSWORD_LENGTH);
    }

    private void checkUniqueness(List<AuthUser> authUsers) {
        authUsers.forEach(authUser -> checkUniqueness(authUser, false));
    }

    private boolean checkEmailExit(String email) {
        List<AuthUser> emailAuthUsers = authUserMapper.selectByCombination(AuthUser.builder().status(true).email(email).build());
        return !CollectionUtils.isEmpty(emailAuthUsers);
    }


    /**
     * 唯一性校验
     *
     * @param authUser 用户
     * @param isUpdate 是否为更新，更新时需要排除当前用户
     */
    private void checkUniqueness(AuthUser authUser, boolean isUpdate) {
        // 部门有效性校验
        AuthDepartment authDepartment = authDepartmentService.getById(authUser.getDepartmentId());
        if (null == authDepartment || !authDepartment.getStatus()) {
            throw new GafException("不是有效的部门");
        }
        // 岗位有效性校验
        if (!StringUtils.isEmpty(authUser.getPostId())) {
            AuthPost authPost = authPostService.getById(authUser.getPostId());
            if (null == authPost || !authPost.getStatus()) {
                throw new GafException("不是有效的岗位");
            }
        }
        if (isUpdate) {
            AuthUser oldUser = authUserMapper.select(authUser.getUserId());
            if (!oldUser.getEmail().equals(authUser.getEmail())) {
                List<AuthUser> emailAuthUsers = authUserMapper.selectByCombination(AuthUser.builder().status(true).email(authUser.getEmail()).build());
                if (!CollectionUtils.isEmpty(emailAuthUsers)) {
                    throw new GafException("邮箱已存在");
                }
            }

            if (!oldUser.getName().equals(authUser.getName())) {
                List<AuthUser> nameAuthUsers = authUserMapper.selectByCombination(AuthUser.builder().status(true).name(authUser.getName()).build());
                if (!CollectionUtils.isEmpty(nameAuthUsers)) {
                    throw new GafException("用户名已存在");
                }
            }
        } else {
            // email，全局不重复
            List<AuthUser> emailAuthUsers = authUserMapper.selectByCombination(AuthUser.builder().status(true).email(authUser.getEmail()).build());
            if (!CollectionUtils.isEmpty(emailAuthUsers)) {
                throw new GafException("邮箱已存在");
            }
            List<AuthUser> nameAuthUsers = authUserMapper.selectByCombination(AuthUser.builder().status(true).name(authUser.getName()).build());
            if (!CollectionUtils.isEmpty(nameAuthUsers)) {
                throw new GafException("用户名已存在");
            }
        }
    }
}
