package com.supermap.gaf.api.scanner.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CatalogTypeEnum {
    // 平台级表示目录平台可见 租户不可见
    // 租户级 表示租户可见
    /**
     * 模块分组（菜单） 平台级
     */
    MODULE_GROUP_TYPE("1"),
    /**
     * API分组 平台级
     */
    API_GROUP_TYPE("2"),
    /**
     * 角色分组 租户级
     */
    ROLE_GROUP_TYPE("3"),

    /**
     * 菜单分组 平台级
     */
    MENU_GROUP_TYPE("4"),

    /**
     * webgis服务分组， 暂时平台级
     */
    WEBGIS_SERVICE_GROUP_TYPE("5"),

    /**
     * 资源分组（别名,资源目录） 暂时平台级
     * 叶子目录用于挂载图层
     */
    RESOURCE_GROUP_TYPE("6");

    private String value;
}
