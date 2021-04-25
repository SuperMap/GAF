package com.supermap.gaf.authority.enums;

import java.util.LinkedList;
import java.util.List;

/**
 * @author wxl
 */
public enum CodeBaseRoleEnum {

    /**
     * 应用开发者
     */
    APP_DEVELOPER,
    /**
     * 应用维护者
     */
    APP_MAINTAINER,
    /**
     * 应用管理员
     */
    APP_ADMIN;

    public static List<String> getAllNames() {
        List<String> names = new LinkedList<>();
        CodeBaseRoleEnum[] values = values();
        for (CodeBaseRoleEnum value : values) {
            names.add(value.name());
        }
        return names;
    }
}
