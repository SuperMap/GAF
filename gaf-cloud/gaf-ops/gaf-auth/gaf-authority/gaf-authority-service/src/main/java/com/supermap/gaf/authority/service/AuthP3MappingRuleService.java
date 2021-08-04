/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.authority.service;

import com.supermap.gaf.authority.commontype.AuthP3MappingRule;
import com.supermap.gaf.authority.vo.AuthP3MappingRuleSelectVo;

import java.util.List;
import java.util.Map;

/**
 * 第三方映射规则服务类
 *
 * @author yangdong
 * @date:2021/3/25
 */
public interface AuthP3MappingRuleService {
    /**
     * 新增第三方映射规则
     *
     * @param authP3MappingRule 第三方映射规则
     * @return 第三方映射规则
     */
    AuthP3MappingRule insertAuthP3MappingRule(AuthP3MappingRule authP3MappingRule);

    /**
     * 根据id删除第三方映射规则
     *
     * @param mappingRuleId 第三方映射规则id
     */
    void deleteAuthP3MappingRule(String mappingRuleId);

    /**
     * 更新第三方映射规则
     *
     * @param authP3MappingRule 第三方映射规则
     * @return 第三方映射规则
     */
    AuthP3MappingRule updateAuthP3MappingRule(AuthP3MappingRule authP3MappingRule);


    /**
     * 根据id查询第三方映射规则
     *
     * @param mappingRuleId 第三方映射规则id
     * @return 第三方映射规则 若未查询到则返回null
     */
    AuthP3MappingRule selectById(String mappingRuleId);

    /**
     * 分页查询第三方映射规则
     *
     * @param authP3MappingRuleSelectVo 分页参数 如偏移量，每页条数
     * @return 分页结果 如查询到的总数 分页后的结果
     */
    Map<String, Object> pageList(AuthP3MappingRuleSelectVo authP3MappingRuleSelectVo);

    /**
     * 分页模糊查询第三方映射规则
     *
     * @param authP3MappingRuleSelectVo 分页参数 如偏移量，每页条数, 模糊查询字段名 字段值 排序字段名 排序方式
     * @return 分页结果 如查询到的总数 分页后的结果
     */
    Map<String, Object> searchList(AuthP3MappingRuleSelectVo authP3MappingRuleSelectVo);

    /**
     * 根据映射类型、资源id查询第三方映射规则。映射类型暂时有1:租户，2：部门，3：用户,而资源id则是对应的id,例如租户id、部门id、用户id
     *
     * @param mappingType 映射类型.暂时有1:租户，2：部门，3：用户
     * @param id          资源id。对应映射类型,例如租户id、部门id、用户id
     * @return 三方映射规则集合 若未查询到则返回空集合
     */
    List<AuthP3MappingRule> listByMappingType(String mappingType, String id);

    /**
     * 批量新增第三方映射规则
     *
     * @param authP3MappingRules 第三方映射规则集合
     */
    void batchInsert(List<AuthP3MappingRule> authP3MappingRules);

    /**
     * 根据id集合批量删除第三方映射规则
     *
     * @param mappingRuleIds 第三方映射规则id集合
     */
    void batchDelete(List<String> mappingRuleIds);
}
