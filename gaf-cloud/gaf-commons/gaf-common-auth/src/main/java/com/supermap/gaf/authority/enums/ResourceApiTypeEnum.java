package com.supermap.gaf.authority.enums;

/**
 * API类型
 * @author wxl
 * @since 2021/12/3
 */
public enum ResourceApiTypeEnum {
    
    SYSTEM("1","系统资源"),
    THIRD("2","第三方资源");


    /**
     * 类型编码
     * */
    private String code;

    /**类型名*/
    private String name;


    ResourceApiTypeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }


    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
