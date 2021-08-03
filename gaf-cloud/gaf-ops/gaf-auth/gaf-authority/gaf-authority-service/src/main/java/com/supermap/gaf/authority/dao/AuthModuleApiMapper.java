/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.authority.dao;

import com.supermap.gaf.authority.commontype.AuthModuleApi;
import com.supermap.gaf.authority.vo.AuthModuleApiSelectVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 模块接口mapper
 *
 * @author zhm
 * @date:2021/3/25
 */
@Mapper
@Component
public interface AuthModuleApiMapper {

    /**
     * 根据id查询模块接口
     *
     * @param moduleApiId 模块接口id
     * @return 模块接口 未查询到则返回null
     */
    AuthModuleApi select(@Param("moduleApiId") String moduleApiId);

    /**
     * 根据模块id和接口id及状态查询模块接口
     *
     * @param moduleId 模块id
     * @param apiId    接口id
     * @param status   状态
     * @return 模块接口
     */
    AuthModuleApi getByModuleAndApi(@Param("moduleId") String moduleId, @Param("apiId") String apiId, @Param("status") Boolean status);


    /**
     * 查询某模块相关的模块接口
     * 根据模块id和状态查询模块接口
     *
     * @param moduleId 模块id
     * @param status   状态
     * @return 模块接口
     */
    List<AuthModuleApi> getByModuleId(@Param("moduleId") String moduleId, @Param("status") Boolean status);

    /**
     * 分页查询模块接口
     *
     * @param selectVo 分页参数 如偏移量，每页条数
     * @return 模块接口集合
     */
    List<AuthModuleApi> pageList(AuthModuleApiSelectVo selectVo);

    /**
     * 分页模糊查询模块接口
     *
     * @param selectVo 分页参数 如偏移量，每页条数, 模糊查询字段名 字段值 排序字段名 排序方式
     * @return 模块接口集合
     */
    List<AuthModuleApi> searchList(AuthModuleApiSelectVo selectVo);

    /**
     * 分页查询
     * 针对偏移量过大的分页查询sql优化
     *
     * @param selectVo 分页参数 如偏移量，每页条数
     * @return 模块接口集合
     */
    List<AuthModuleApi> bigOffsetPageList(AuthModuleApiSelectVo selectVo);

    /**
     * 查询模块接口总数
     *
     * @return 数量
     */
    int pageListCount();

    /**
     * 统计单字段模糊匹配条件查询后的结果数量
     *
     * @param fieldName  字段名
     * @param fieldValue 字段值
     * @return 数量
     */
    Integer countOneField(@Param("fieldName") String fieldName, @Param("fieldValue") Object fieldValue);

    /**
     * 新增模块接口
     *
     * @param authModuleApi 模块接口
     * @return 新增的数量
     */
    int insert(AuthModuleApi authModuleApi);

    /**
     * 批量新增模块接口
     *
     * @param authModuleApis 模块接口集合
     * @return 新增的数量
     */
    int batchInsert(List<AuthModuleApi> authModuleApis);

    /**
     * 批量将模块接口状态置为true
     *
     * @param authModuleApiIds 模块接口id集合
     * @return 影响的行数及更新的数量
     */
    int batchUpdate(List<String> authModuleApiIds);

    /**
     * 根据id集合批量删除模块接口
     *
     * @param moduleApiIds 模块接口id集合
     * @return 删除的数量
     */
    int batchDelete(List<String> moduleApiIds);

    /**
     * 根据模块接口id删除模块接口
     *
     * @param moduleApiId 模块接口id
     * @return 影响的行数即删除的数量
     */
    int delete(@Param("moduleApiId") String moduleApiId);

    /**
     * 更新模块接口
     *
     * @param authModuleApi 模块接口
     * @return 影响的行数即更新的数量
     */
    int update(AuthModuleApi authModuleApi);
}
