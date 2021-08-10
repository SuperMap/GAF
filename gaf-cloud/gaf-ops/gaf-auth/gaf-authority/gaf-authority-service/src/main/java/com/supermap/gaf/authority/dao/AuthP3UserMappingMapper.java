/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.authority.dao;

import com.supermap.gaf.authority.commontype.AuthP3UserMapping;
import com.supermap.gaf.authority.vo.AuthP3UserMappingSelectVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 第三方用户映射mapper
 *
 * @author yangdong
 * @date:2021/3/25
 */
@Mapper
@Component
public interface AuthP3UserMappingMapper {
    /**
     * 新增第三方用户映射
     *
     * @param authP3UserMapping 第三方用户映射
     * @return 新增的数量
     */
    int insert(AuthP3UserMapping authP3UserMapping);

    /**
     * 根据第三方用户映射id删除第三方用户映射
     *
     * @param userMappingId 第三方用户映射id
     * @return 影响的行数即删除的数量
     */
    int delete(@Param("userMappingId") String userMappingId);

    /**
     * 更新第三方用户映射
     *
     * @param authP3UserMapping 第三方用户映射
     * @return 影响的行数即更新的数量
     */
    int update(AuthP3UserMapping authP3UserMapping);

    /**
     * 根据id查询第三方用户映射
     *
     * @param userMappingId 第三方用户映射id
     * @return 第三方用户映射 未查询到则返回null
     */
    AuthP3UserMapping select(@Param("userMappingId") String userMappingId);

    /**
     * 根据第三方组件id和用户id查询第三方用户映射
     *
     * @param p3ComponentId 第三方组件id
     * @param userId        用户id
     * @return 第三方用户映射
     */
    AuthP3UserMapping getByUserId(@Param("p3ComponentId") String p3ComponentId, @Param("userId") String userId);

    /**
     * 分页查询第三方用户映射
     *
     * @param selectVo 分页参数 如偏移量，每页条数
     * @return 第三方用户映射集合
     */
    List<AuthP3UserMapping> pageList(AuthP3UserMappingSelectVo selectVo);

    /**
     * 分页模糊查询第三方用户映射
     *
     * @param selectVo 分页参数 如偏移量，每页条数, 模糊查询字段名 字段值 排序字段名 排序方式
     * @return 第三方用户映射集合
     */
    List<AuthP3UserMapping> searchList(AuthP3UserMappingSelectVo selectVo);

    /**
     * 分页查询
     * 针对偏移量过大的分页查询sql优化
     *
     * @param selectVo 分页参数 如偏移量，每页条数
     * @return 第三方用户映射集合
     */
    List<AuthP3UserMapping> bigOffsetPageList(AuthP3UserMappingSelectVo selectVo);

    /**
     * 查询第三方用户映射总数
     *
     * @return 数量
     */
    int pageListCount();

    /**
     * 批量新增第三方用户映射
     *
     * @param authP3UserMappings 第三方用户映射集合
     * @return 新增的数量
     */
    int batchInsert(List<AuthP3UserMapping> authP3UserMappings);

    /**
     * 根据id集合批量删除第三方用户映射
     *
     * @param userMappingIds 第三方用户映射id集合
     * @return 删除的数量
     */
    int batchDelete(List<String> userMappingIds);

}
