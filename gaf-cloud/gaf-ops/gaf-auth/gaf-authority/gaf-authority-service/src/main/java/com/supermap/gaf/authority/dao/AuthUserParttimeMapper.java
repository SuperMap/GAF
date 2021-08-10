/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.authority.dao;

import com.supermap.gaf.authority.commontype.AuthUserParttime;
import com.supermap.gaf.authority.vo.AuthUserParttimeSelectVo;
import com.supermap.gaf.authority.vo.AuthUserParttimeVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 用户兼职mapper
 *
 * @author yangdong
 * @date:2021/3/25
 */
@Mapper
@Component
public interface AuthUserParttimeMapper {
    /**
     * 根据id查询用户兼职
     *
     * @param userParttimeId 用户兼职id
     * @return 用户兼职 未查询到则返回null
     */
    AuthUserParttime select(@Param("userParttimeId") String userParttimeId);


    /**
     * 分页查询用户兼职
     *
     * @param selectVo 分页参数 如偏移量，每页条数
     * @return 用户兼职集合
     */
    List<AuthUserParttime> pageList(AuthUserParttimeSelectVo selectVo);

    /**
     * 分页等值查询接口
     *
     * @param selectVo 分页参数 如偏移量，每页条数, 等值查询字段名 字段值 排序字段名 排序方式
     * @return 用户兼职集合
     */
    List<AuthUserParttime> searchList(AuthUserParttimeSelectVo selectVo);

    /**
     * 分页查询接口
     * 连接部门和岗位表查询
     *
     * @param selectVo 分页参数 如偏移量，每页条数, 等值查询字段名 字段值 排序字段名 排序方式
     * @return 用户兼职集合
     */
    List<AuthUserParttimeVo> searchJoinList(AuthUserParttimeSelectVo selectVo);

    /**
     * 统计条件查询的数量
     * 通过连接部门和岗位查询
     *
     * @param authUserParttimeSelectVo 查询字段名 字段值 部门名
     * @return 查询的数量
     */
    Integer countJoinList(AuthUserParttimeSelectVo authUserParttimeSelectVo);

    /**
     * 分页查询
     * 针对偏移量过大的分页查询sql优化
     *
     * @param selectVo 分页参数 如偏移量，每页条数
     * @return 用户兼职集合
     */
    List<AuthUserParttime> bigOffsetPageList(AuthUserParttimeSelectVo selectVo);

    /**
     * 根据岗位id和用户id查询用户兼职
     *
     * @param postId 岗位id
     * @param userId 用户id
     * @return 用户兼职集合
     */
    List<AuthUserParttime> listUserParttime(@Param("postId") String postId, @Param("userId") String userId);

    /**
     * 查询用户兼职总数
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
     * 新增用户兼职
     *
     * @param authUserParttime 用户兼职
     * @return 新增的数量
     */
    int insert(AuthUserParttime authUserParttime);

    /**
     * 批量新增用户兼职
     *
     * @param authUserParttimes 用户兼职集合
     * @return 新增的数量
     */
    int batchInsert(List<AuthUserParttime> authUserParttimes);

    /**
     * 根据id集合批量删除用户兼职
     *
     * @param userParttimeIds 用户兼职id集合
     * @return 删除的数量
     */
    int batchDelete(List<String> userParttimeIds);

    /**
     * 根据用户兼职id删除用户兼职
     *
     * @param userParttimeId 用户兼职id
     * @return 影响的行数即删除的数量
     */
    int delete(@Param("userParttimeId") String userParttimeId);


    /**
     * 更新用户兼职
     *
     * @param authUserParttime 用户兼职
     * @return 影响的行数即更新的数量
     */
    int update(AuthUserParttime authUserParttime);

    /**
     * 组合等值条件查询
     *
     * @param queryAuthUserParttime 查询条件 至少有一个属性值不为null
     * @return 若未查询到则返回空集合
     */
    List<AuthUserParttime> selectByCombination(AuthUserParttime queryAuthUserParttime);

    /**
     * 通过用户id逻辑删除用户兼职信息
     *
     * @param userId 用户名
     * @author wxl
     */
    void deleteByUserId(String userId);


}
