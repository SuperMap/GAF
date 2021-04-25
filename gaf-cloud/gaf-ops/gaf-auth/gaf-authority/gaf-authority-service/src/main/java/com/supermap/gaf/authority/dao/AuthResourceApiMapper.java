/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.authority.dao;

import com.supermap.gaf.authority.commontype.AuthResourceApi;
import com.supermap.gaf.authority.vo.AuthResourceApiSelectVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
/**
 * 接口mapper
 * @date:2021/3/25
 * @author zhm
 */
@Mapper
@Component
public interface AuthResourceApiMapper{
    /**
     * 根据id查询接口
     * @param resourceApiId 接口id
     * @return 接口 未查询到则返回null
     */
    AuthResourceApi select(@Param("resourceApiId") String resourceApiId);

    /**
     * 分页查询接口
     * @param selectVo 分页参数 如偏移量，每页条数
     * @return 接口集合
     */
    List<AuthResourceApi> pageList(AuthResourceApiSelectVo selectVo);

    /**
     * 分页模糊查询接口
     * @param selectVo 分页参数 如偏移量，每页条数, 模糊查询字段名 字段值 排序字段名 排序方式
     * @return 接口集合
     */
    List<AuthResourceApi> searchList(AuthResourceApiSelectVo selectVo);
    
    /**
     * 分页查询
     * 针对偏移量过大的分页查询sql优化
     * @param selectVo 分页参数 如偏移量，每页条数
     * @return 接口集合
     */
    List<AuthResourceApi> bigOffsetPageList(AuthResourceApiSelectVo selectVo);

    /**
     * 查询接口总数
     * @return 数量
     */
    int pageListCount();
    /**
     * 统计单字段模糊匹配条件查询后的结果数量
     * @param fieldName 字段名
     * @param fieldValue 字段值
     * @return 数量
     */
	Integer countOneField(@Param("fieldName") String fieldName, @Param("fieldValue") Object fieldValue);
    /**
     * 新增接口
     * @param authResourceApi 接口
     * @return 新增的数量
     */
    int insert(AuthResourceApi authResourceApi);
    /**
     * 批量新增接口
     * @param authResourceApis  接口集合
     * @return 新增的数量
     */
    int batchInsert(List<AuthResourceApi> authResourceApis);

    /**
     * 根据id集合批量删除接口
     * @param resourceApiIds 接口id集合
     * @return 删除的数量
     */
    int batchDelete(List<String> resourceApiIds);
    /**
     * 根据接口id删除接口
     * @param resourceApiId 接口id
     * @return 影响的行数即删除的数量
     */
    int delete(@Param("resourceApiId") String resourceApiId);
    /**
     * 更新接口
     * @param authResourceApi   接口
     * @return 影响的行数即更新的数量
     */
    int update(AuthResourceApi authResourceApi);

    /**
     * 查询某分组目录下的接口
     * @param catalogId  目录id
     * @return 接口集合
     */
    List<AuthResourceApi> listByCatalogId(String catalogId);

    /**
     * 统计等值条件查询的结果数量
     * @param queryApi 查询条件 属性值只是有一个不为null
     * @return 若未查询到则返回0
     */
    Integer countByCombination(AuthResourceApi queryApi);

    /**
     * 等值条件查询
     *
     * @param queryCondition 查询条件至少有一个属性不能为空
     * @return 若未查询到则返回空集合
     */
    List<AuthResourceApi> selectByCombination(AuthResourceApi queryCondition);
}
