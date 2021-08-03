/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.authority.service;

import com.supermap.gaf.authority.commontype.AuthResourceApi;
import com.supermap.gaf.authority.vo.AuthResourceApiSelectVo;

import java.util.List;
import java.util.Map;

/**
 * 接口(api)服务类
 *
 * @author zhm
 * @date:2021/3/25
 */
public interface AuthResourceApiService {

    /**
     * 根据id查询接口(api)
     *
     * @param resourceApiId 接口(api)id
     * @return 接口(api)
     */
    AuthResourceApi getById(String resourceApiId);

    /**
     * 分页查询接口(api)
     *
     * @param authResourceApiSelectVo 分页参数 如偏移量，每页条数
     * @return 分页结果 如查询到的总数 分页后的结果
     */
    Map<String, Object> pageList(AuthResourceApiSelectVo authResourceApiSelectVo);

    /**
     * 分页模糊查询接口(api)
     *
     * @param authResourceApiSelectVo 分页参数 如偏移量，每页条数, 模糊查询字段名 字段值 排序字段名 排序方式
     * @return 分页结果 如查询到的总数 分页后的结果
     */
    Map<String, Object> searchList(AuthResourceApiSelectVo authResourceApiSelectVo);

    /**
     * 新增接口(api)
     *
     * @param authResourceApi 接口(api)
     * @return 接口(api)
     */
    AuthResourceApi insertAuthResourceApi(AuthResourceApi authResourceApi);

    /**
     * 批量插入接口(api)
     *
     * @param authResourceApis 接口(api)集合
     */
    void batchInsert(List<AuthResourceApi> authResourceApis);

    /**
     * 根据id删除接口(api)
     *
     * @param resourceApiId 接口(api)id
     */
    void deleteAuthResourceApi(String resourceApiId);

    /**
     * 批量删除接口(api)
     *
     * @param resourceApiIds 接口(api)id集合
     */
    void batchDelete(List<String> resourceApiIds);

    /**
     * 更新接口(api)
     *
     * @param authResourceApi 接口(api)id
     * @return 更新接口(api)
     */
    AuthResourceApi updateAuthResourceApi(AuthResourceApi authResourceApi);

    /**
     * 查询某接口(api)分组下的接口(api)
     *
     * @param catalogId 分组目录id
     * @return 接口(api)集合 若未查询到则返回null
     */
    List<AuthResourceApi> listByCatalogId(String catalogId);

    /**
     * 查询某接口(api)分组下的接口(api)数量
     *
     * @param apiCatalogId 分组目录id
     * @return 数量
     */
    Integer countByApiCatalogId(String apiCatalogId);

    /**
     * 等值条件查询
     *
     * @param query 查询条件
     * @return 查询的接口(api)集合，若未查询到则返回空集合
     */
    List<AuthResourceApi> list(AuthResourceApi query);
}
