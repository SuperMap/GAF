/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.data.mgt.enums;

import com.supermap.gaf.data.mgt.model.FieldTypeInfo;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * postgresql 数据库字段数据类型
 * @author wxl
 * @since 2021-8-12
 */
public enum PostgresqlFieldTypeEnum {
    // 数值类型
    SMALLINT ("postgresql_smallint","smallint") ,
    // 数值类型
    INT2 ("postgresql_smallint","int2"),
    // 数值类型
    INT4 ("postgresql_smallint","int4") ,
    INT8 ("postgresql_smallint","int8") ,
    INTEGER("postgresql_integer","integer") ,
    BIGINT("postgresql_bigint","bigint") ,
    DECIMAL("postgresql_decimal","decimal") ,
    NUMERIC("postgresql_numeric","numeric"),
    REAL("postgresql_real","real"),
    DOUBLE("postgresql_double","double precision") ,
    SMALLSERIAL("postgresql_smallserial","smallserial") ,
    SERIAL("postgresql_serial","serial"),
    BIGSERIAL("postgresql_bigserial","bigserial"),
    // 货币
    MONEY("postgresql_money","money") ,
    // 字符类型
    VARCHAR("postgresql_varchar","varchar") ,
    CHAR("postgresql_char","char"),
    TEXT("postgresql_text","text"),
    // 二进制数据类型 默认值或者输入格式 十六进制格式 E'\\xDEADBEEF 或者'\x5c303436'::bytea 转义格式 E'\\047' 或者 '\\047'::bytea
    BYTEA("postgresql_bytea","bytea") ,
    // 日期/时间类型
    TIMESTAMP("postgresql_timestamp","timestamp") ,
    TIMESTAMP_WITH_TIME_ZONE("postgresql_timestamp_with_time_zone","timestamp_with_time_zone") ,
    DATE("postgresql_date","date") ,
    TIME("postgresql_time","time") ,
    TIME_WITH_TIME_ZONE("postgresql_time_with_time_zone","time_with_time_zone"),
    INTERVAL("postgresql_interval","interval") ,
    // 布尔类型
    BOOLEAN("postgresql_boolean","boolean") ,
    // 布尔类型
    BOOL("postgresql_bool","bool") ;

    private String code;
    private String name;
    private String descrption;

    PostgresqlFieldTypeEnum(String code,String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getDescrption() {
        return null;
    }

    public static List<FieldTypeInfo> toFieldTypeInfoList() {
       return Arrays.stream(PostgresqlFieldTypeEnum.values()).map(type -> {
           FieldTypeInfo fieldTypeInfo = new FieldTypeInfo();
           fieldTypeInfo.setCode(type.getCode());
           fieldTypeInfo.setName(type.getName());
           fieldTypeInfo.setDescrption(type.getName());
           return fieldTypeInfo;
       }).collect(Collectors.toList());
    }

}
