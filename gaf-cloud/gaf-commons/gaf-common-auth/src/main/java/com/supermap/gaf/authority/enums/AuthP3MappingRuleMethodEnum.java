package com.supermap.gaf.authority.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @auther : yd
 * @since : 2020-11-26
 * 第三方数据映射方式
 */
@AllArgsConstructor
@Getter
public enum AuthP3MappingRuleMethodEnum {
    /**
     * 推
     */
    PUSH("1"),
    /**
     * 拉
     */
    PULL("2");

    private final String value;

    public static AuthP3MappingRuleMethodEnum getByValue(String value) {
        for (AuthP3MappingRuleMethodEnum e : AuthP3MappingRuleMethodEnum.values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        return null;
    }

}
