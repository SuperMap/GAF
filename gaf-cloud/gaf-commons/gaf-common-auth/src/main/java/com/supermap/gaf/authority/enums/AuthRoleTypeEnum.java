package com.supermap.gaf.authority.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author wxl
 */
@AllArgsConstructor
@Getter
public enum AuthRoleTypeEnum {
    /**
     * 组件内置，租户可见
     */
    Component("1"),
    /**
     * 平台级，租户不可见
     */
    Platform("2"),
    /**
     * 租户级
     */
    Tenant("3");

    public final String typeValue;
}
