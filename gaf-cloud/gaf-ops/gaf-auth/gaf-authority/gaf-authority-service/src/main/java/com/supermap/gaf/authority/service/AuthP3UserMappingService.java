/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.authority.service;

import com.supermap.gaf.authority.commontype.AuthP3UserMapping;
import com.supermap.gaf.authority.vo.AuthP3UserMappingSelectVo;

import java.util.List;
import java.util.Map;

/**
 * 第三方用户映射服务类
 *
 * @author yangdong
 * @date:2021/3/25
 */
public interface AuthP3UserMappingService {
    /**
     * 新增第三方用户映射
     *
     * @param authP3UserMapping 第三方用户映射
     * @return 第三方用户映射
     */
    AuthP3UserMapping insertAuthP3UserMapping(AuthP3UserMapping authP3UserMapping);

    /**
     * 根据id删除第三方用户映射
     *
     * @param userMappingId 三方用户映射id
     */
    void deleteAuthP3UserMapping(String userMappingId);

    /**
     * 更新第三方用户映射
     *
     * @param authP3UserMapping 第三方用户映射
     * @return 第三方用户映射
     */
    AuthP3UserMapping updateAuthP3UserMapping(AuthP3UserMapping authP3UserMapping);

    /**
     * 根据id查询第三方用户映射
     *
     * @param userMappingId 第三方用户映射id
     * @return 第三方用户映射  若未查询到则返回null
     */
    AuthP3UserMapping selectById(String userMappingId);

    /**
     * 根据第三方组件id和用户id查询第三方用户映射
     *
     * @param p3ComponentId 第三方组件id
     * @param userId        用户id
     * @return 第三方用户映射
     */
    AuthP3UserMapping getByUserId(String p3ComponentId, String userId);

    /**
     * 分页查询第三方用户映射
     *
     * @param authP3UserMappingSelectVo 分页参数 如偏移量，每页条数
     * @return 分页结果 如查询到的总数 分页后的结果
     */
    Map<String, Object> pageList(AuthP3UserMappingSelectVo authP3UserMappingSelectVo);

    /**
     * 分页模糊查询第三方用户映射
     *
     * @param authP3UserMappingSelectVo 分页参数 如偏移量，每页条数, 模糊查询字段名 字段值 排序字段名 排序方式
     * @return 分页结果 如查询到的总数 分页后的结果
     */
    Map<String, Object> searchList(AuthP3UserMappingSelectVo authP3UserMappingSelectVo);

    /**
     * 批量插入第三方用户映射
     *
     * @param authP3UserMappings 第三方用户映射集合
     */
    void batchInsert(List<AuthP3UserMapping> authP3UserMappings);

    /**
     * 根据第三方用户映射id集合批量删除
     *
     * @param userMappingIds 第三方用户映射id集合
     */
    void batchDelete(List<String> userMappingIds);
}
