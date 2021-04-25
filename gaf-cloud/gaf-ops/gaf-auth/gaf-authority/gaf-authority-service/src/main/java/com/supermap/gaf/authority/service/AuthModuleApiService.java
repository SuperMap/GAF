/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.authority.service;

import com.supermap.gaf.authority.commontype.AuthModuleApi;
import com.supermap.gaf.authority.vo.AuthModuleApiSelectVo;
import com.supermap.gaf.authority.vo.ModuleApiVo;

import java.util.List;
import java.util.Map;

/**
 * 模块接口(API)关联服务类
 * @date:2021/3/25
 * @author zhm
 */
public interface AuthModuleApiService {

    /**
     * 根据id查询模块接口(API)
     * @param moduleApiId 模块接口(API)id
     * @return 模块接口(API)
     */
    AuthModuleApi getById(String moduleApiId);

    /**
     * 根据模块id查询模块接口(API)
     * @param moduleId 模块id
     * @param status  true有效 false 无效
     * @return 模块接口(API)集合 未查询到则返回空集合
     */
    List<AuthModuleApi> getByModuleId(String moduleId, Boolean status);

    /**
     * 传入模块id和接口(API)的id列表，查询到该模块已绑定的接口(API)，与传入的接口(API)的id列表做对比更新
     * @param moduleApiVo 模块接口(api)关联对象
     */
    void handleModuleApi(ModuleApiVo moduleApiVo);
	
    /**
     * 分页查询模块接口(API)
     * @param authModuleApiSelectVo 分页参数 如偏移量，每页条数
     * @return 分页结果 如查询到的总数 分页后的结果
     */
    Map<String,Object> pageList(AuthModuleApiSelectVo authModuleApiSelectVo);
	
    /**
     * 分页模糊查询部门
     * @param authModuleApiSelectVo 分页参数 如偏移量，每页条数, 模糊查询字段名 字段值 排序字段名 排序方式
     * @return 分页结果 如查询到的总数 分页后的结果
     */
    Map<String,Object> searchList(AuthModuleApiSelectVo authModuleApiSelectVo);


    /**
     * 批量新增模块接口(API)
     * @param authModuleApis  模块接口(API)集合
     */
    void batchInsert(List<AuthModuleApi> authModuleApis);

    /**
     * 批量删除模块接口(API)
     * @param moduleApiIds 模块接口(API)id集合
     */
    void batchDelete(List<String> moduleApiIds);

}
