package com.supermap.gaf.authority.constant;

/**
 * 有关目录的常量信息
 */
public final class SysCatalogConstant {

    private SysCatalogConstant() {
    }

    /**
     * 数据库目录表字段名,表示是目录名
     */
    public static final String NAME = "name";
    /**
     * 表示平台级而非租户级 租户级用户不应该看见平台级的一些资源
     */
    public static final String PLATFORM_LEVEL = "0";
    /**
     * 表示租户级
     */
    public static final String TENANT_LEVEL = "1";
}
