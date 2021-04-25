/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.authority.service;

import com.supermap.gaf.authority.commontype.AuthUserParttime;
import com.supermap.gaf.authority.vo.AuthUserParttimeSelectVo;

import java.util.List;
import java.util.Map;

/**
 * 用户挂职服务类
 * @date:2021/3/25
 * @author yangdong
 */
public interface AuthUserParttimeService {

    /**
     * 根据id查询用户挂职
     * @param userParttimeId  用户挂职id
     * @return 用户挂职
     */
    AuthUserParttime getById(String userParttimeId);

    /**
     * 分页查询用户挂职
     * @param authUserParttimeSelectVo 分页参数 如偏移量，每页条数
     * @return 分页结果 如查询到的总数 分页后的结果
     */
    Map<String,Object> pageList(AuthUserParttimeSelectVo authUserParttimeSelectVo);

    /**
     * 分页模糊查询用户挂职
     * @param authUserParttimeSelectVo 分页参数 如偏移量，每页条数, 模糊查询字段名 字段值 排序字段名 排序方式
     * @return 分页结果 如查询到的总数 分页后的结果
     */
    Map<String,Object> searchList(AuthUserParttimeSelectVo authUserParttimeSelectVo);

    /**
     * 新增用户挂职
     * @param authUserParttime 用户挂职
     * @return 用户挂职
     */
    AuthUserParttime insertAuthUserParttime(AuthUserParttime authUserParttime);

    /**
     * 批量插入用户挂职
     * @param authUserParttimes 用户挂职集合
     */
    void batchInsert(List<AuthUserParttime> authUserParttimes);

    /**
     * 根据用户挂职id删除用户挂职
     * @param userParttimeId 用户挂职id
     */
    void deleteAuthUserParttime(String userParttimeId);

    /**
     * 更新用户挂职
     * @param authUserParttime 用户挂职
     * @return 用户挂职
     */
    AuthUserParttime updateAuthUserParttime(AuthUserParttime authUserParttime);

    /**
     * 通过用户id查询挂职信息
     * @param userId 用户id
     * @return 若未查询到则返回为空
     */
    List<AuthUserParttime> getByUserId(String userId);

    /**
     * 通过用户id删除挂职信息
     * @param userId 用户id
     */
    void deleteByUserId(String userId);
}
