/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.data.mgt.enums;

import com.supermap.gaf.data.mgt.entity.MmField;
import com.supermap.gaf.data.mgt.model.FieldTypeInfo;
import com.supermap.gaf.data.mgt.support.DdlFragmentConverter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * postgresql 数据库字段数据类型
 * @author wxl
 * @since 2021-8-12
 */
public enum PostgresqlFieldTypeEnum implements  DdlFragmentConverter {
    // 数值类型
    SMALLINT ("postgresql_smallint","smallint") {
        @Override
        public String convertToDdlFragment(MmField mmField) {

            return mmField.getFieldName() + " smallint " + getCommonFragment(mmField);
        }
    },
    // 数值类型
    INT2 ("postgresql_smallint","int2") {
        @Override
        public String convertToDdlFragment(MmField mmField) {

            return mmField.getFieldName() + " int2 " + getCommonFragment(mmField);
        }
    },
    // 数值类型
    INT4 ("postgresql_smallint","int4") {
        @Override
        public String convertToDdlFragment(MmField mmField) {

            return mmField.getFieldName() + " int4 " + getCommonFragment(mmField);
        }
    },
    INT8 ("postgresql_smallint","int8") {
        @Override
        public String convertToDdlFragment(MmField mmField) {

            return mmField.getFieldName() + " int8 " + getCommonFragment(mmField);
        }
    },
    INTEGER("postgresql_integer","integer") {
        @Override
        public String convertToDdlFragment(MmField mmField) {
            return mmField.getFieldName() + " integer " + getCommonFragment(mmField);
        }
    },
    BIGINT("postgresql_bigint","bigint") {
        @Override
        public String convertToDdlFragment(MmField mmField) {
            return mmField.getFieldName() + " bigint " + getCommonFragment(mmField);
        }
    },
    DECIMAL("postgresql_decimal","decimal") {
        @Override
        public String convertToDdlFragment(MmField mmField) {
            return mmField.getFieldName() + " decimal"+ getLengthPrecision(mmField)+" "+ getCommonFragment(mmField);
        }
    },
    NUMERIC("postgresql_numeric","numeric") {
        @Override
        public String convertToDdlFragment(MmField mmField) {
            return mmField.getFieldName() + " numeric" + getLengthPrecision(mmField)+" "+ getCommonFragment(mmField);
        }
    },
    REAL("postgresql_real","real") {
        @Override
        public String convertToDdlFragment(MmField mmField) {
            return mmField.getFieldName() + " real " + getCommonFragment(mmField);
        }
    },
    DOUBLE("postgresql_double","double precision") {
        @Override
        public String convertToDdlFragment(MmField mmField) {
            return mmField.getFieldName() + " double precision " + getCommonFragment(mmField);
        }
    },
    SMALLSERIAL("postgresql_smallserial","smallserial") {
        @Override
        public String convertToDdlFragment(MmField mmField) {
            return mmField.getFieldName() + " smallserial ";
        }
    },
    SERIAL("postgresql_serial","serial") {
        @Override
        public String convertToDdlFragment(MmField mmField) {
            return mmField.getFieldName() + " serial ";
        }
    },
    BIGSERIAL("postgresql_bigserial","bigserial"){
        @Override
        public String convertToDdlFragment(MmField mmField) {
            return mmField.getFieldName() + " bigserial ";
        }
    },
    // 货币
    MONEY("postgresql_money","money") {
        @Override
        public String convertToDdlFragment(MmField mmField) {
            return mmField.getFieldName() + " money " + getCommonFragment(mmField);
        }
    },
    // 字符类型
    VARCHAR("postgresql_varchar","varchar") {
        @Override
        public String convertToDdlFragment(MmField mmField) {
            Integer fieldLength = mmField.getFieldLength();
            String length =  fieldLength == null ? "": "("+ fieldLength +")";
            StringBuilder sb = getNotNull(mmField);
            String fieldDefault = mmField.getFieldDefault();
            if (fieldDefault != null && !fieldDefault.isEmpty()) {
                sb.append(" DEFAULT '").append(fieldDefault).append("' ");
            }
            sb.append(getComment(mmField));
            return mmField.getFieldName() + " varchar" + length + " " + sb;
        }
    },
    CHAR("postgresql_char","char") {
        @Override
        public String convertToDdlFragment(MmField mmField) {
            Integer fieldLength = mmField.getFieldLength();
            String length =  fieldLength == null ? "": "("+ fieldLength +")";
            StringBuilder sb = getNotNull(mmField);
            String fieldDefault = mmField.getFieldDefault();
            if (fieldDefault != null && !fieldDefault.isEmpty()) {
                sb.append(" DEFAULT '").append(fieldDefault).append("' ");
            }
            sb.append(getComment(mmField));
            return mmField.getFieldName() + " char " + length + " " + sb;
        }
    },
    TEXT("postgresql_text","text") {
        @Override
        public String convertToDdlFragment(MmField mmField) {
            StringBuilder sb = getNotNull(mmField);
            String fieldDefault = mmField.getFieldDefault();
            if (fieldDefault != null && !fieldDefault.isEmpty()) {
                sb.append(" DEFAULT '").append(fieldDefault).append("' ");
            }
            sb.append(getComment(mmField));
            return mmField.getFieldName() + " text " +  sb;
        }
    },
    // 二进制数据类型 默认值或者输入格式 十六进制格式 E'\\xDEADBEEF 或者'\x5c303436'::bytea 转义格式 E'\\047' 或者 '\\047'::bytea
    BYTEA("postgresql_bytea","bytea") {
        @Override
        public String convertToDdlFragment(MmField mmField) {
            return mmField.getFieldName() + " bytea " + getCommonFragment(mmField);
        }
    },
    // 日期/时间类型
    TIMESTAMP("postgresql_timestamp","timestamp") {
        @Override
        public String convertToDdlFragment(MmField mmField) {
            Integer fieldPrecision = mmField.getFieldPrecision();
            String precision = "";
            if(fieldPrecision != null) {
                if (fieldPrecision  < 0) {
                    precision = "(0)";
                } else if (fieldPrecision > 6) {
                    precision = "(6)";
                } else {
                    precision = "(" + fieldPrecision +")";
                }
            }

            return mmField.getFieldName() + " timestamp" + precision + getCommonFragment(mmField);
        }
    },
    TIMESTAMP_WITH_TIME_ZONE("postgresql_timestamp_with_time_zone","timestamp_with_time_zone") {
        @Override
        public String convertToDdlFragment(MmField mmField) {
            Integer fieldPrecision = mmField.getFieldPrecision();
            String precision = "";
            if(fieldPrecision != null) {
                if (fieldPrecision < 0) {
                    precision = "(0)";
                } else if (fieldPrecision > 6) {
                    precision = "(6)";
                } else {
                    precision = "(" + fieldPrecision +")";
                }
            }
            return mmField.getFieldName() + " timestamp" + precision + " with time zone "+ getCommonFragment(mmField);
        }
    },
    DATE("postgresql_date","date") {
        @Override
        public String convertToDdlFragment(MmField mmField) {
            return mmField.getFieldName() + " date" + getCommonFragment(mmField);
        }
    },
    TIME("postgresql_time","time") {
        @Override
        public String convertToDdlFragment(MmField mmField) {
            Integer fieldPrecision = mmField.getFieldPrecision();
            String precision = "";
            if(fieldPrecision != null) {
                if (fieldPrecision < 0) {
                    precision = "(0)";
                } else if (fieldPrecision > 6) {
                    precision = "(6)";
                } else {
                    precision = "(" + fieldPrecision +")";
                }
            }

            return mmField.getFieldName() + " time" + precision + getCommonFragment(mmField);
        }
    },
    TIME_WITH_TIME_ZONE("postgresql_time_with_time_zone","time_with_time_zone") {
        @Override
        public String convertToDdlFragment(MmField mmField) {
            Integer fieldPrecision = mmField.getFieldPrecision();
            String precision = "";
            if(fieldPrecision != null) {
                if (fieldPrecision < 0) {
                    precision = "(0)";
                } else if (fieldPrecision > 6) {
                    precision = "(6)";
                } else {
                    precision = "(" + fieldPrecision +")";
                }
            }
            return mmField.getFieldName() + " time" + precision + " with time zone " + getCommonFragment(mmField);
        }
    },
    INTERVAL("postgresql_interval","interval") {
        @Override
        public String convertToDdlFragment(MmField mmField) {
            Integer fieldPrecision = mmField.getFieldPrecision();
            String precision = "";
            if(fieldPrecision != null) {
                if (fieldPrecision < 0) {
                    precision = "(0)";
                } else if (fieldPrecision > 6) {
                    precision = "(6)";
                } else {
                    precision = "(" + fieldPrecision +")";
                }
            }
            return mmField.getFieldName() + " interval" + precision + getCommonFragment(mmField);
        }
    },
    // 布尔类型
    BOOLEAN("postgresql_boolean","boolean") {
        @Override
        public String convertToDdlFragment(MmField mmField) {
            return mmField.getFieldName() + " boolean "  + getCommonFragment(mmField);
        }
    },
    // 布尔类型
    BOOL("postgresql_bool","bool") {
        @Override
        public String convertToDdlFragment(MmField mmField) {
            return mmField.getFieldName() + " bool "  + getCommonFragment(mmField);
        }
    };

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

    public static PostgresqlFieldTypeEnum fromCode(String code) {
        for (PostgresqlFieldTypeEnum postgresqlFieldTypeEnum : PostgresqlFieldTypeEnum.values()) {
            if (postgresqlFieldTypeEnum.getCode().equals(code)) {
                return postgresqlFieldTypeEnum;
            }
        }
        throw new IllegalArgumentException("不支持的类型编码:" + code);
    }
}
