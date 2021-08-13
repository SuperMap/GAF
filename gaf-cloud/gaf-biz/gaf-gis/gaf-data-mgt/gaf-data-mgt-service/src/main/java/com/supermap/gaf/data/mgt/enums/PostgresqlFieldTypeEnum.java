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
    SMALLINT ("postgresql_smallint","smallint"),
    INTEGER("postgresql_integer","integer"),
    BIGINT("postgresql_bigint","bigint"),
    DECIMAL("postgresql_decimal","decimal"),
    NUMERIC("postgresql_numeric","numeric"),
    REAL("postgresql_real","real"),
    DOUBLE("postgresql_double","double"),
    SMALLSERIAL("postgresql_smallserial","smallserial"),
    SERIAL("postgresql_serial","serial"),
    BIGSERIAL("postgresql_bigserial","bigserial"),
    // 货币
    MONEY("postgresql_money","money"),
    // 字符类型
    VARCHAR("postgresql_varchar","varchar"),
    CHAR("postgresql_char","char"),
    TEXT("postgresql_text","text"),
    // 二进制数据类型
    BYTEA("postgresql_bytea","bytea"),
    // 日期/时间类型
    TIMESTAMP("postgresql_timestamp","timestamp"),
    TIMESTAMP_WITH_TIME_ZONE("postgresql_timestamp_with_time_zone","timestamp_with_time_zone"),
    DATE("postgresql_date","date"),
    TIME("postgresql_time","time"),
    TIME_WITH_TIME_ZONE("postgresql_time_with_time_zone","time_with_time_zone"),
    INTERVAL("postgresql_interval","interval"),
    // 布尔类型
    BOOLEAN("postgresql_boolean","boolean");

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
