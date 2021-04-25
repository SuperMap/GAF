package com.supermap.gaf.authority.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 树原始节点类型枚举值
 * @author wxl
 */
@AllArgsConstructor
@Getter
public enum NodeTypeEnum {
    /**
     * 租户
     */
    TENANT(1),
    /**
     * 部门
     */
    DEPARTMENT(2),
    /**
     * 岗位
     */
    POST(3),
    /**
     * 用户
     */
    USER(4),
    /**
     * 角色
     */
    ROLE(5),
    /**
     * 组件
     */
    COMPONENT(6),
    /**
     * 模块
     */
    MODULE(7),
    /**
     * 接口分组
     */
    API_GROUP(8),
    /**
     * 接口
     */
    API(9),
    /**
     * 未知类型
     */
    UNKNOWN(10),
    /**
     * 目录分类
     */
    CATALOG_CLASSIFICATION(11),
    /**
     * 目录
     */
    CATALOG(12),
    /**
     * 菜单
     */
    MENU(13),

    /**
     * 字典
     */
    DICTIONARY(14);

    /**
     * 类型值
     */
    private final Integer value;

}
