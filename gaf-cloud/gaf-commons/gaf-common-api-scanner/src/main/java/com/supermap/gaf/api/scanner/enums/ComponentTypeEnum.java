package com.supermap.gaf.api.scanner.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

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

    private String value;
}
