/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.authority.dao;

import com.supermap.gaf.authority.commontype.AuthP3MappingRule;
import com.supermap.gaf.authority.vo.AuthP3MappingRuleSelectVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 第三方映射规则mapper
 *
 * @author yangdong
 * @date:2021/3/25
 */
@Mapper
@Component
public interface AuthP3MappingRuleMapper {
    /**
     * 新增第三方映射规则
     *
     * @param authP3MappingRule 第三方映射规则
     * @return 新增的数量
     */
    int insert(AuthP3MappingRule authP3MappingRule);

    /**
     * 根据第三方映射规则id删除第三方映射规则
     *
     * @param mappingRuleId 第三方映射规则id
     * @return 影响的行数即删除的数量
     */
    int delete(@Param("mappingRuleId") String mappingRuleId);

    /**
     * 更新第三方映射规则
     *
     * @param authP3MappingRule 第三方映射规则
     * @return 影响的行数即更新的数量
     */
    int update(AuthP3MappingRule authP3MappingRule);

    /**
     * 根据id查询第三方映射规则
     *
     * @param mappingRuleId 第三方映射规则id
     * @return 第三方映射规则 未查询到则返回null
     */
    AuthP3MappingRule select(@Param("mappingRuleId") String mappingRuleId);

    /**
     * 分页查询第三方映射规则
     *
     * @param selectVo 分页参数 如偏移量，每页条数
     * @return 第三方映射规则集合
     */
    List<AuthP3MappingRule> pageList(AuthP3MappingRuleSelectVo selectVo);

    /**
     * 分页模糊查询第三方映射规则
     *
     * @param selectVo 分页参数 如偏移量，每页条数, 模糊查询字段名 字段值 排序字段名 排序方式
     * @return 第三方映射规则集合
     */
    List<AuthP3MappingRule> searchList(AuthP3MappingRuleSelectVo selectVo);

    /**
     * 单字段等值查询第三方映射规则
     *
     * @param fieldName  字段名
     * @param fieldValue 字段值
     * @return 第三方映射规则集合
     */
    List<AuthP3MappingRule> listByOneField(@Param("fieldName") String fieldName, @Param("fieldValue") Object fieldValue);

    /**
     * 分页查询
     * 针对偏移量过大的分页查询sql优化
     *
     * @param selectVo 分页参数 如偏移量，每页条数
     * @return 第三方映射规则集合
     */
    List<AuthP3MappingRule> bigOffsetPageList(AuthP3MappingRuleSelectVo selectVo);

    /**
     * 查询第三方映射规则总数
     *
     * @return 数量
     */
    int pageListCount();

    /**
     * 批量新增第三方映射规则
     *
     * @param authP3MappingRules 第三方映射规则集合
     * @return 新增的数量
     */
    int batchInsert(List<AuthP3MappingRule> authP3MappingRules);

    /**
     * 根据id集合批量删除第三方映射规则
     *
     * @param mappingRuleIds 第三方映射规则id集合
     * @return 删除的数量
     */
    int batchDelete(List<String> mappingRuleIds);

}
