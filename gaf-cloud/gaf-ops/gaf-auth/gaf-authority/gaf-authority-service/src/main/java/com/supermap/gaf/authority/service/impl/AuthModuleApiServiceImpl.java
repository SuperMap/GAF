/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.authority.service.impl;

import com.supermap.gaf.authority.commontype.AuthModuleApi;
import com.supermap.gaf.authority.constant.CommonConstant;
import com.supermap.gaf.authority.dao.AuthModuleApiMapper;
import com.supermap.gaf.authority.service.AuthModuleApiService;
import com.supermap.gaf.authority.vo.AuthModuleApiSelectVo;
import com.supermap.gaf.authority.vo.ModuleApiVo;
import com.supermap.gaf.exception.GafException;
import com.supermap.gaf.data.access.service.BatchSortAndCodeService;
import com.supermap.gaf.utils.LogUtil;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;


/**
 * 模块接口(API)关联服务实现类
 * @author zhm
 * @date:2021/3/25
 * 
 */
@Service
public class AuthModuleApiServiceImpl implements AuthModuleApiService{
    private final AuthModuleApiMapper authModuleApiMapper;
    private final BatchSortAndCodeService batchSortAndCodeService;

    private static final Logger logger = LogUtil.getLocLogger(AuthModuleApiServiceImpl.class);

    public AuthModuleApiServiceImpl(AuthModuleApiMapper authModuleApiMapper, BatchSortAndCodeService batchSortAndCodeService) {
        this.authModuleApiMapper = authModuleApiMapper;
        this.batchSortAndCodeService = batchSortAndCodeService;
    }

    @Override
    public AuthModuleApi getById(String moduleApiId){
        if(moduleApiId == null){
            throw new IllegalArgumentException("moduleApiId不能为空");
        }
        return authModuleApiMapper.select(moduleApiId);
    }


    @Override
    public List<AuthModuleApi> getByModuleId(String moduleId,Boolean status){
        if(StringUtils.isEmpty(moduleId)){
            return new ArrayList<>();
        }
        return authModuleApiMapper.getByModuleId(moduleId,status);
    }


    public AuthModuleApi getByModuleAndApi(String moduleId, String apiId,Boolean status){
        if(StringUtils.isEmpty(moduleId) || StringUtils.isEmpty(apiId)){
            return null;
        }
        return authModuleApiMapper.getByModuleAndApi(moduleId, apiId,status);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handleModuleApi(ModuleApiVo moduleApiVo){
        String moduleId = moduleApiVo.getModuleId();
        if(moduleId == null || StringUtils.isEmpty(moduleId)){
            throw new GafException("模块id为空");
        }
        logger.info(moduleApiVo.getModuleId());

        List<String>  newList = moduleApiVo.getApiList();

        List<String> oldList = getByModuleId(moduleApiVo.getModuleId(),true).stream().map(AuthModuleApi ::getResourceApiId).collect(Collectors.toList());


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
            List<AuthModuleApi> addModuleApiList = new ArrayList<>();
            List<String> updateList = new ArrayList<>();
            Set<String> parentIdsInUpdateList = new HashSet<>();
            addList.forEach(item->{
                AuthModuleApi oldModuleApi = getByModuleAndApi(moduleId,item,false);
                if(oldModuleApi!=null){
                    updateList.add(oldModuleApi.getModuleApiId());
                    parentIdsInUpdateList.add(oldModuleApi.getResourceModuleId());
                }else{
                    AuthModuleApi authModuleApi = AuthModuleApi.builder()
                            .resourceApiId(item)
                            .resourceModuleId(moduleId)
                            .status(true)
                            .sortSn(1)
                            .build();
                    addModuleApiList.add(authModuleApi);
                }
            });
            //批量修改
            if(!CollectionUtils.isEmpty(updateList)){
                batchUpdate(updateList,parentIdsInUpdateList);
            }
            //批量新增
            if(!CollectionUtils.isEmpty(addModuleApiList)){
                batchInsert(addModuleApiList);
            }
        }

        //禁用
        if(!CollectionUtils.isEmpty(removeList)){
            //根据模块id和接口id获取模块接口关联id
            List<String> moduleApiIds = new ArrayList<>();
            removeList.forEach(item->{
                AuthModuleApi  authModuleApi = getByModuleAndApi(moduleId,item,true);
                if(authModuleApi!=null){
                    moduleApiIds.add(authModuleApi.getModuleApiId());
                }
            });
            batchDelete(moduleApiIds);
        }
    }



	@Override
    public Map<String,Object> pageList(AuthModuleApiSelectVo authModuleApiSelectVo){
        if(authModuleApiSelectVo.getPageSize()==null || authModuleApiSelectVo.getPageSize()==0){
           authModuleApiSelectVo.setPageSize(50);
       }
        List<AuthModuleApi> pageList;
        if(authModuleApiSelectVo.getOffset() == null || authModuleApiSelectVo.getOffset() < CommonConstant.OFFSET_MAX_FOR_SQL_BETTER){
            pageList = authModuleApiMapper.pageList(authModuleApiSelectVo);
        }else{
            pageList = authModuleApiMapper.bigOffsetPageList(authModuleApiSelectVo);
        }
		int totalCount=authModuleApiMapper.pageListCount();
        Map<String, Object> result = new HashMap<>(2);
		result.put("pageList", pageList);
		result.put("totalCount", totalCount);
        return result;
    }
	
	@Override
    public Map<String,Object> searchList(AuthModuleApiSelectVo authModuleApiSelectVo){
        if(authModuleApiSelectVo.getPageSize()==null || authModuleApiSelectVo.getPageSize()==0){
           authModuleApiSelectVo.setPageSize(50);
       }
        List<AuthModuleApi> pageList;
		pageList = authModuleApiMapper.searchList(authModuleApiSelectVo);
		Integer totalCount = authModuleApiMapper.countOneField(authModuleApiSelectVo.getSearchFieldName(),authModuleApiSelectVo.getSearchFieldValue());
        Map<String, Object> result = new HashMap<>(2);
		result.put("pageList", pageList);
		result.put("totalCount", totalCount);
        return result;
    }


	@Override
    @Transactional(rollbackFor = Exception.class)
    public void batchInsert(List<AuthModuleApi> authModuleApis){
		if (authModuleApis != null && !CollectionUtils.isEmpty(authModuleApis)) {
            Set<String> parentIds = new HashSet<>();
	        authModuleApis.forEach(authModuleApi -> {
				authModuleApi.setModuleApiId(UUID.randomUUID().toString());
                parentIds.add(authModuleApi.getResourceModuleId());
            });
            authModuleApiMapper.batchInsert(authModuleApis);
            batchSortAndCodeService.revisionSortSnForInsertOrDelete(AuthModuleApi.class,parentIds);
        }
    }

	@Override
    public void batchDelete(List<String> moduleApiIds){
        authModuleApiMapper.batchDelete(moduleApiIds);
    }

    @Transactional(rollbackFor = Exception.class)
    public void batchUpdate(List<String> authModuleApiIds,Set<String> parentIds){
        authModuleApiMapper.batchUpdate(authModuleApiIds);
        batchSortAndCodeService.revisionSortSnForInsertOrDelete(AuthModuleApi.class,parentIds);
    }

}
