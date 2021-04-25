package com.supermap.gaf.authority.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author wxl
 */
@AllArgsConstructor
@Getter
public enum ComponentTypeEnum {
    /**
     * 前端
     */
    FRONTEND("1"),
    /**
     * 后端
     */
    BACKEND("2"),
    /**
     * 移动端
     */
    MOBILE("3"),
    /**
     * 前后端
     */
    FRONT_BACK_END("4");

    private final String value;
}
