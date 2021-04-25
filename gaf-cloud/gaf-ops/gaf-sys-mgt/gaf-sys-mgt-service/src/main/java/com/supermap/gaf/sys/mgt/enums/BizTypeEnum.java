package com.supermap.gaf.sys.mgt.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author wxl
 */
@AllArgsConstructor
@Getter
public enum BizTypeEnum {
    /**
     * 一般类型
     */
    BASIS("1", "一般类型"),
    /**
     * 专项
     */
    SPECIAL("2", "专项");

    private final String value;
    private final String name;
}
