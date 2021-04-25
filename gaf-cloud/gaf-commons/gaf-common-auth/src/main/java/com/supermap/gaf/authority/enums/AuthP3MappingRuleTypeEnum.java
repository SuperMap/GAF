package com.supermap.gaf.authority.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @auther : yd
 * @since : 2020-12-02
 */
@AllArgsConstructor
@Getter
public enum AuthP3MappingRuleTypeEnum {

    TENANT("1"),
    DEPARTMENT("2"),
    USER("3");


    private final String value;

    public static AuthP3MappingRuleTypeEnum getByValue(String value) {
        for (AuthP3MappingRuleTypeEnum e : AuthP3MappingRuleTypeEnum.values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        return null;
    }

}
