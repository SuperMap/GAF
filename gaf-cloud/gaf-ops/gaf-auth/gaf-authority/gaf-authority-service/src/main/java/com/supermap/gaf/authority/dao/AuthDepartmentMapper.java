/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.authority.dao;

import com.supermap.gaf.authority.commontype.AuthDepartment;
import com.supermap.gaf.authority.vo.AuthDepartmentSelectVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * 部门mapper
 *
 * @author zhm
 * @date:2021/3/25
 */
@Mapper
@Component
public interface AuthDepartmentMapper {

    /**
     * 根据id查询部门
     *
     * @param departmentId 部门id
     * @return 部门 未查询到则返回null
     */
    AuthDepartment select(@Param("departmentId") String departmentId);

    /**
     * 分页查询部门
     *
     * @param selectVo 分页参数 如偏移量，每页条数
     * @return 部门集合
     */
    List<AuthDepartment> pageList(AuthDepartmentSelectVo selectVo);

    /**
     * 分页模糊查询某租户下的部门
     *
     * @param selectVo 分页参数 如偏移量，每页条数, 模糊查询字段名 字段值 排序字段名 排序方式
     * @param tenantId 租户id
     * @return 部门集合
     */
    List<AuthDepartment> searchList(@Param("selectVo") AuthDepartmentSelectVo selectVo, @Param("tenantId") String tenantId);

    /**
     * 分页等值查询某租户下的部门
     *
     * @param selectVo 分页参数 如偏移量，每页条数, 等值查询字段名 字段值 排序字段名 排序方式
     * @param tenantId 租户id
     * @return 部门集合
     */
    List<AuthDepartment> searchWholeMatchList(@Param("selectVo") AuthDepartmentSelectVo selectVo, @Param("tenantId") String tenantId);

    /**
     * 分页查询
     * 针对偏移量过大的分页查询sql优化
     *
     * @param selectVo 分页参数 如偏移量，每页条数
     * @return 部门集合
     */
    List<AuthDepartment> bigOffsetPageList(AuthDepartmentSelectVo selectVo);

    /**
     * 查询部门总数
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
     * 新增部门
     *
     * @param authDepartment 部门
     * @return 新增的数量
     */
    int insert(AuthDepartment authDepartment);

    /**
     * 批量新增部门
     *
     * @param authDepartments 部门集合
     * @return 新增的数量
     */
    int batchInsert(List<AuthDepartment> authDepartments);

    /**
     * 根据id集合批量删除部门
     *
     * @param departmentIds 部门id集合
     * @return 删除的数量
     */
    int batchDelete(List<String> departmentIds);

    /**
     * 根据部门id删除部门
     *
     * @param departmentId 部门id
     * @return 影响的行数即删除的数量
     */
    int delete(@Param("departmentId") String departmentId);

    /**
     * 更新部门
     *
     * @param authDepartment 部门
     * @return 影响的行数即更新的数量
     */
    int update(AuthDepartment authDepartment);

    /**
     * 组合条件 等值查询
     *
     * @param queryDepartment 查询参数 至少有一个属性值不为null
     * @return 若未查询到数据则返回空集合
     */
    List<AuthDepartment> selectByCombination(AuthDepartment queryDepartment);

    /**
     * 根据组合条件 更新范围内的部门 不包含边界
     *
     * @param queryCondition 查询条件 注意queryCondition 至少有一个属性不为null
     * @param addOrSub       序号+1 还是 -1
     * @param startSortSn    开始序号
     * @param endSortSn      结束序号
     * @return 影响的行数即更新的数量
     */
    Integer updateSortSnByCombination(@Param("queryCondition") AuthDepartment queryCondition, @Param("addOrSub") Boolean addOrSub, @Param("startSortSn") Integer startSortSn, @Param("endSortSn") Integer endSortSn);

    /**
     * 组合条件 等值查询
     *
     * @param queryDepartment 查询参数 至少有一个属性值不为null
     * @return 若未查询到数据则返回
     */
    Integer countByCombination(AuthDepartment queryDepartment);

    /**
     * 根据id集合批量获取有效部门信息
     *
     * @param departmentIds 部门id集合
     * @return 若未查询到则返回空集合
     */
    List<AuthDepartment> selectByIds(Set<String> departmentIds);

    /**
     * 获取所有的父部门的id集合
     *
     * @param tenantId 租户id
     * @return 父部门的id集合
     */
    Set<String> selectAllParentIds(String tenantId);
}
