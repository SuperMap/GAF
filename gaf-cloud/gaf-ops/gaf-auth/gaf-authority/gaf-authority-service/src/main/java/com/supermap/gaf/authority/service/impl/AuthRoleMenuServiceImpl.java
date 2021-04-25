/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.authority.service.impl;

import com.supermap.gaf.authority.commontype.AuthResourceMenu;
import com.supermap.gaf.authority.commontype.AuthRoleMenu;
import com.supermap.gaf.authority.constant.CommonConstant;
import com.supermap.gaf.authority.constant.DbFieldNameConstant;
import com.supermap.gaf.authority.dao.AuthRoleMenuMapper;
import com.supermap.gaf.authority.service.AuthResourceMenuService;
import com.supermap.gaf.authority.service.AuthRoleMenuService;
import com.supermap.gaf.authority.service.AuthRoleService;
import com.supermap.gaf.authority.vo.AuthRoleMenuSelectVo;
import com.supermap.gaf.authority.vo.RoleMenuVo;
import com.supermap.gaf.exception.GafException;
import com.supermap.gaf.utils.LogUtil;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 角色菜单服务实现类
 * @author yangdong
 * @date:2021/3/25
 * 
 */
@Service
public class AuthRoleMenuServiceImpl implements AuthRoleMenuService {
    private final AuthRoleMenuMapper authRoleMenuMapper;

    private final AuthRoleService authRoleService;

    private final AuthResourceMenuService authResourceMenuService;

    private static final Logger logger = LogUtil.getLocLogger(AuthRoleMenuServiceImpl.class);

    public AuthRoleMenuServiceImpl(AuthRoleMenuMapper authRoleMenuMapper, AuthRoleService authRoleService, AuthResourceMenuService authResourceMenuService) {
        this.authRoleMenuMapper = authRoleMenuMapper;
        this.authRoleService = authRoleService;
        this.authResourceMenuService = authResourceMenuService;
    }

    @Override
    public AuthRoleMenu getById(String roleMenuId) {
        if (StringUtils.isEmpty(roleMenuId)) {
            throw new IllegalArgumentException("roleMenuId不能为空");
        }
        return authRoleMenuMapper.select(roleMenuId);
    }

    @Override
    public List<AuthRoleMenu> listByRole(String roleId) {
        if (StringUtils.isEmpty(roleId)) {
            return new ArrayList<>();
        }
        return authRoleMenuMapper.listByRole(roleId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void handleRoleMenu(RoleMenuVo roleMenuVo){
        String roleId = roleMenuVo.getRoleId();
        if(roleId == null || StringUtils.isEmpty(roleId)){
            throw new GafException("角色id为空");
        }
        logger.info(roleId);

        if(authRoleService.getById(roleId)==null){
            throw new GafException("角色不存在：" + roleId);
        }
        List<String>  newList = roleMenuVo.getMenuList();
        List<String> oldList = getByRoleId(roleId,true).stream().map(AuthRoleMenu ::getResourceMenuId).collect(Collectors.toList());
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
            List<AuthRoleMenu> addRoleMenuList = new ArrayList<>();
            List<String> addMenuIdList = new LinkedList<>();
            List<String> updateList = new ArrayList<>();
            addList.forEach(item->{
                AuthRoleMenu oldRoleMenu = getByRoleAndMenu(roleId,item,false);
                if(oldRoleMenu!=null){
                    updateList.add(oldRoleMenu.getRoleMenuId());
                }else{
                    AuthRoleMenu authRoleMenu = AuthRoleMenu.builder()
                            .resourceMenuId(item)
                            .roleId(roleId)
                            .status(true)
                            .sortSn(1)
                            .build();
                    addMenuIdList.add(item);
                    addRoleMenuList.add(authRoleMenu);
                }
            });
            //批量修改
            if(!CollectionUtils.isEmpty(updateList)){
                batchUpdate(updateList);
            }
            //批量新增
            if(!CollectionUtils.isEmpty(addRoleMenuList)){
                List<AuthResourceMenu> menus = authResourceMenuService.getByIds(addMenuIdList);
                if (menus != null && menus.size() > 0) {
                    Map< String, String> menuIdAndModule = menus.stream().collect(Collectors.toMap(AuthResourceMenu::getResourceMenuId, AuthResourceMenu::getResourceModuleId));
                    addRoleMenuList.forEach(authRoleMenu -> authRoleMenu.setResourceModuleId(menuIdAndModule.get(authRoleMenu.getResourceMenuId())));
                }
                batchInsertRoleMenu(addRoleMenuList);
            }
        }

        //禁用
        if(!CollectionUtils.isEmpty(removeList)){
            //根据角色id和接口id获取角色接口关联id
            List<String> roleMenuIds = new ArrayList<>();
            removeList.forEach(item->{
                AuthRoleMenu  authRoleMenu = getByRoleAndMenu(roleId,item,true);
                if(authRoleMenu!=null){
                    roleMenuIds.add(authRoleMenu.getRoleMenuId());
                }
            });
            batchDelete(roleMenuIds);
        }
    }

    @Override
    public List<AuthRoleMenu> getByRoleId(String roleId, Boolean status){
        if(StringUtils.isEmpty(roleId)){
            return new ArrayList<>();
        }
        return authRoleMenuMapper.getByRoleId(roleId,status);
    }

    public AuthRoleMenu getByRoleAndMenu(String roleId, String menuId,Boolean status){
        if(StringUtils.isEmpty(roleId) || StringUtils.isEmpty(menuId)){
            return null;
        }
        return authRoleMenuMapper.getByRoleAndMenu(roleId, menuId,status);
    }

    @Override
    public Map<String, Object> pageList(AuthRoleMenuSelectVo authRoleMenuSelectVo) {
        if (authRoleMenuSelectVo.getPageSize() == null || authRoleMenuSelectVo.getPageSize() == 0) {
            authRoleMenuSelectVo.setPageSize(50);
        }
        List<AuthRoleMenu> pageList;
        if (authRoleMenuSelectVo.getOffset() == null || authRoleMenuSelectVo.getOffset() < CommonConstant.OFFSET_MAX_FOR_SQL_BETTER) {
            pageList = authRoleMenuMapper.pageList(authRoleMenuSelectVo);
        } else {
            pageList = authRoleMenuMapper.bigOffsetPageList(authRoleMenuSelectVo);
        }
        int totalCount = authRoleMenuMapper.pageListCount();
        Map<String, Object> result = new HashMap<>(2);
        result.put(DbFieldNameConstant.PAGE_LIST, pageList);
        result.put(DbFieldNameConstant.TOTAL_COUNT, totalCount);
        return result;
    }

    @Override
    public Map<String, Object> searchList(AuthRoleMenuSelectVo authRoleMenuSelectVo) {
        if (authRoleMenuSelectVo.getPageSize() == null || authRoleMenuSelectVo.getPageSize() == 0) {
            authRoleMenuSelectVo.setPageSize(50);
        }
        List<AuthRoleMenu> pageList = authRoleMenuMapper.searchList(authRoleMenuSelectVo);
        Integer totalCount = authRoleMenuMapper.countOneField(authRoleMenuSelectVo.getSearchFieldName(), authRoleMenuSelectVo.getSearchFieldValue());
        Map<String, Object> result = new HashMap<>(2);
        result.put(DbFieldNameConstant.PAGE_LIST, pageList);
        result.put(DbFieldNameConstant.TOTAL_COUNT, totalCount);
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public AuthRoleMenu insertAuthRoleMenu(AuthRoleMenu authRoleMenu) {
        authRoleMenu.setRoleMenuId(UUID.randomUUID().toString());
        // 唯一性校验
        checkUniqueness(authRoleMenu);
        AuthResourceMenu menu = authResourceMenuService.getById(authRoleMenu.getResourceMenuId());
        authRoleMenu.setResourceModuleId(menu.getResourceModuleId());
        authRoleMenuMapper.insert(authRoleMenu);
        return authRoleMenu;
    }


    /**
     * 批量插入
     * @author zhm
     **/
    @Override
    public void batchInsertRoleMenu(List<AuthRoleMenu> authRoleMenus){
        if (authRoleMenus != null && !CollectionUtils.isEmpty(authRoleMenus)) {
            authRoleMenus.forEach(authRoleMenu -> authRoleMenu.setRoleMenuId(UUID.randomUUID().toString()));
            authRoleMenuMapper.batchInsert(authRoleMenus);
        }
    }

    /**
     * 批量逻辑删除
     * @author zhm
     **/
    public void batchUpdate(List<String> authRoleMenuIds){
        authRoleMenuMapper.batchUpdate(authRoleMenuIds);
    }

    @Override
    public void deleteAuthRoleMenu(String roleMenuId) {
        authRoleMenuMapper.delete(roleMenuId);
    }

    @Override
    public void batchDelete(List<String> roleMenuIds) {
        authRoleMenuMapper.batchDelete(roleMenuIds);
    }

    @Override
    public AuthRoleMenu updateAuthRoleMenu(AuthRoleMenu authRoleMenu) {
        authRoleMenuMapper.update(authRoleMenu);
        return authRoleMenu;
    }

    /**
     * 唯一性校验，每个角色与菜单的关系只能出现一次
     *
     * @param authRoleMenu  角色菜单
     */
    private void checkUniqueness(AuthRoleMenu authRoleMenu) {
        List<AuthRoleMenu> authRoleMenus = authRoleMenuMapper.listRoleMenu(authRoleMenu.getRoleId(), authRoleMenu.getResourceMenuId());
        if (!CollectionUtils.isEmpty(authRoleMenus)) {
            boolean isRoleMenuExist = authRoleMenus.size() > 0;
            if (isRoleMenuExist) {
                throw new GafException("该角色已绑定此菜单");
            }
        }
    }
}
