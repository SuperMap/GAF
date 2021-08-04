package com.supermap.gaf.api.scanner.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ResourceApiMethodEnum {
    /**
     * 获取
     */
    GET("1"),
    /**
     * 添加
     */
    POST("2"),
    /**
     * 更新
     */
    PUT("3"),
    /**
     * 删除
     */
    DELETE("4");

    private String value;

    public static ResourceApiMethodEnum getByValue(String value) {
        for (ResourceApiMethodEnum e : ResourceApiMethodEnum.values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        return null;
    }
}
