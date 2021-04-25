/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.authority.service.impl;

import com.supermap.gaf.authority.commontype.AuthRoleModule;
import com.supermap.gaf.authority.constant.CacheGroupConstant;
import com.supermap.gaf.authority.constant.CommonConstant;
import com.supermap.gaf.authority.constant.DbFieldNameConstant;
import com.supermap.gaf.authority.dao.AuthRoleModuleMapper;
import com.supermap.gaf.authority.service.AuthRoleModuleService;
import com.supermap.gaf.authority.service.AuthRoleService;
import com.supermap.gaf.authority.vo.AuthRoleModuleSelectVo;
import com.supermap.gaf.authority.vo.RoleModuleVo;
import com.supermap.gaf.exception.GafException;
import com.supermap.gaf.utils.LogUtil;
import org.slf4j.Logger;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 角色模块服务实现类
 * @author yangdong
 * @date:2021/3/25
 * 
 */
@Service
public class AuthRoleModuleServiceImpl implements AuthRoleModuleService {
    private final AuthRoleModuleMapper authRoleModuleMapper;

    private final AuthRoleService authRoleService;

    private static final Logger logger = LogUtil.getLocLogger(AuthRoleModuleServiceImpl.class);

    public AuthRoleModuleServiceImpl(AuthRoleModuleMapper authRoleModuleMapper, AuthRoleService authRoleService) {
        this.authRoleModuleMapper = authRoleModuleMapper;
        this.authRoleService = authRoleService;
    }

    @Override
    public AuthRoleModule getById(String roleModuleId) {
        if (StringUtils.isEmpty(roleModuleId)) {
            throw new IllegalArgumentException("roleModuleId不能为空");
        }
        return authRoleModuleMapper.select(roleModuleId);
    }

    @Cacheable(value = CacheGroupConstant.ROLE_MODULE, key = "#roleId", unless = "#result == null")
    @Override
    public List<AuthRoleModule> listByRole(String roleId) {
        if (StringUtils.isEmpty(roleId)) {
            return new ArrayList<>();
        }
        return authRoleModuleMapper.listByRole(roleId);
    }

    @Override
    public void handleRoleModule(RoleModuleVo roleModuleVo){
        String roleId = roleModuleVo.getRoleId();
        if(roleId == null || StringUtils.isEmpty(roleId)){
            throw new GafException("角色id为空");
        }
        logger.info(roleId);

        if(authRoleService.getById(roleId)==null){
            throw new GafException("角色不存在：" + roleId);
        }
        List<String>  newList = roleModuleVo.getModuleList();
        List<String> oldList = getByRoleId(roleId,true).stream().map(AuthRoleModule ::getResourceModuleId).collect(Collectors.toList());
        List<String> addList = new ArrayList<>();
        List<String> removeList = new ArrayList<>();
        newList.forEach(item ->{
            if(!oldList.contains(item)){
                addList.add(item);
            }
        });
        logger.info("toadd:");
        oldList.forEach(item ->{
            if(!newList.contains(item)){
                removeList.add(item);
            }
        });
        logger.info("toremove:");

        //新增或修改
        if(!CollectionUtils.isEmpty(addList)){
            List<AuthRoleModule> addRoleModuleList = new ArrayList<>();
            List<String> updateList = new ArrayList<>();
            addList.forEach(item->{
                AuthRoleModule oldRoleModule = getByRoleAndModule(roleId,item,false);
                if(oldRoleModule!=null){
                    updateList.add(oldRoleModule.getRoleModuleId());
                }else{
                    AuthRoleModule authRoleModule = AuthRoleModule.builder()
                            .resourceModuleId(item)
                            .roleId(roleId)
                            .status(true)
                            .sortSn(1)
                            .build();
                    addRoleModuleList.add(authRoleModule);
                }
            });
            //批量修改
            if(!CollectionUtils.isEmpty(updateList)){
                batchUpdate(updateList);
            }
            //批量新增
            if(!CollectionUtils.isEmpty(addRoleModuleList)){
                batchInsertRoleModule(addRoleModuleList);
            }
        }

        //禁用
        if(!CollectionUtils.isEmpty(removeList)){
            //根据角色id和接口id获取角色接口关联id
            List<String> roleModuleIds = new ArrayList<>();
            removeList.forEach(item->{
                AuthRoleModule  authRoleModule = getByRoleAndModule(roleId,item,true);
                if(authRoleModule!=null){
                    roleModuleIds.add(authRoleModule.getRoleModuleId());
                }
            });
            batchDelete(roleModuleIds);
        }
    }

    @Override
    public List<AuthRoleModule> getByRoleId(String roleId, Boolean status){
        if(StringUtils.isEmpty(roleId)){
            return new ArrayList<>();
        }
        return authRoleModuleMapper.getByRoleId(roleId,status);
    }

    /**
     * 根据角色id和接口id查询
     * @return 角色接口
     */
    public AuthRoleModule getByRoleAndModule(String roleId, String moduleId,Boolean status){
        if(StringUtils.isEmpty(roleId) || StringUtils.isEmpty(moduleId)){
            return null;
        }
        return authRoleModuleMapper.getByRoleAndModule(roleId, moduleId,status);
    }

    @Override
    public Map<String, Object> pageList(AuthRoleModuleSelectVo authRoleModuleSelectVo) {
        if (authRoleModuleSelectVo.getPageSize() == null || authRoleModuleSelectVo.getPageSize() == 0) {
            authRoleModuleSelectVo.setPageSize(50);
        }
        List<AuthRoleModule> pageList;
        if (authRoleModuleSelectVo.getOffset() == null || authRoleModuleSelectVo.getOffset() < CommonConstant.OFFSET_MAX_FOR_SQL_BETTER) {
            pageList = authRoleModuleMapper.pageList(authRoleModuleSelectVo);
        } else {
            pageList = authRoleModuleMapper.bigOffsetPageList(authRoleModuleSelectVo);
        }
        int totalCount = authRoleModuleMapper.pageListCount();
        Map<String, Object> result = new HashMap<>(2);
        result.put(DbFieldNameConstant.PAGE_LIST, pageList);
        result.put(DbFieldNameConstant.TOTAL_COUNT, totalCount);
        return result;
    }

    @Override
    public Map<String, Object> searchList(AuthRoleModuleSelectVo authRoleModuleSelectVo) {
        if (authRoleModuleSelectVo.getPageSize() == null || authRoleModuleSelectVo.getPageSize() == 0) {
            authRoleModuleSelectVo.setPageSize(50);
        }
        List<AuthRoleModule> pageList = authRoleModuleMapper.searchList(authRoleModuleSelectVo);
        Integer totalCount = authRoleModuleMapper.countOneField(authRoleModuleSelectVo.getSearchFieldName(), authRoleModuleSelectVo.getSearchFieldValue());
        Map<String, Object> result = new HashMap<>(2);
        result.put(DbFieldNameConstant.PAGE_LIST, pageList);
        result.put(DbFieldNameConstant.TOTAL_COUNT, totalCount);
        return result;
    }

    @CacheEvict(value = CacheGroupConstant.ROLE_MODULE, key = "#authRoleModule.roleId")
    @Override
    public AuthRoleModule insertAuthRoleModule(AuthRoleModule authRoleModule) {
        authRoleModule.setRoleModuleId(UUID.randomUUID().toString());

        // 唯一性校验
        checkUniqueness(authRoleModule);

        authRoleModuleMapper.insert(authRoleModule);
        return authRoleModule;
    }


    @Override
    public void batchInsertRoleModule(List<AuthRoleModule> authRoleModules){
        if (authRoleModules != null && !CollectionUtils.isEmpty(authRoleModules)) {
            authRoleModules.forEach(authRoleModule -> authRoleModule.setRoleModuleId(UUID.randomUUID().toString()));
            authRoleModuleMapper.batchInsert(authRoleModules);
        }
    }
    /**
     * 批量逻辑删除
     * @author zhm
     **/
    public void batchUpdate(List<String> authRoleModuleIds){
        authRoleModuleMapper.batchUpdate(authRoleModuleIds);
    }

    @Override
    public void deleteAuthRoleModule(String roleModuleId) {
        authRoleModuleMapper.delete(roleModuleId);
    }

    @Override
    public void batchDelete(List<String> roleModuleIds) {
        authRoleModuleMapper.batchDelete(roleModuleIds);
    }

    @CacheEvict(value = CacheGroupConstant.ROLE_MODULE, key = "#authRoleModule.roleId")
    @Override
    public AuthRoleModule updateAuthRoleModule(AuthRoleModule authRoleModule) {
        authRoleModuleMapper.update(authRoleModule);
        return authRoleModule;
    }

    /**
     * 唯一性校验，每个角色与模块的关系只能出现一次
     * @param authRoleModule 角色模块
     */
    private void checkUniqueness(AuthRoleModule authRoleModule) {
        List<AuthRoleModule> authRoleModules = authRoleModuleMapper.listRoleModule(authRoleModule.getRoleId(), authRoleModule.getResourceModuleId());
        if (!CollectionUtils.isEmpty(authRoleModules)) {
            boolean isRoleModuleExist = authRoleModules.size() > 0;
            if (isRoleModuleExist) {
                throw new GafException("该角色已绑定此模块");
            }
        }
    }
}
