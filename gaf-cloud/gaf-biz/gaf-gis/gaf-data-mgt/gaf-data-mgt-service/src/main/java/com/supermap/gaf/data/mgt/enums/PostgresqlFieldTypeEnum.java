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
public enum PostgresqlFieldTypeEnum implements DdlFragmentConverter {
    // 数值类型
    SMALLINT ("postgresql_smallint","smallint") {
        @Override
        public String convertToDdlFragment(MmField mmField) {
            return mmField.getFieldName() + " smallint " + getCommonFragment(mmField);
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
            return null;
        }
    },
    // 字符类型
    VARCHAR("postgresql_varchar","varchar") {
        @Override
        public String convertToDdlFragment(MmField mmField) {
            return null;
        }
    },
    CHAR("postgresql_char","char") {
        @Override
        public String convertToDdlFragment(MmField mmField) {
            return null;
        }
    },
    TEXT("postgresql_text","text") {
        @Override
        public String convertToDdlFragment(MmField mmField) {
            return null;
        }
    },
    // 二进制数据类型
    BYTEA("postgresql_bytea","bytea") {
        @Override
        public String convertToDdlFragment(MmField mmField) {
            return null;
        }
    },
    // 日期/时间类型
    TIMESTAMP("postgresql_timestamp","timestamp") {
        @Override
        public String convertToDdlFragment(MmField mmField) {
            return null;
        }
    },
    TIMESTAMP_WITH_TIME_ZONE("postgresql_timestamp_with_time_zone","timestamp_with_time_zone") {
        @Override
        public String convertToDdlFragment(MmField mmField) {
            return null;
        }
    },
    DATE("postgresql_date","date") {
        @Override
        public String convertToDdlFragment(MmField mmField) {
            return null;
        }
    },
    TIME("postgresql_time","time") {
        @Override
        public String convertToDdlFragment(MmField mmField) {
            return null;
        }
    },
    TIME_WITH_TIME_ZONE("postgresql_time_with_time_zone","time_with_time_zone") {
        @Override
        public String convertToDdlFragment(MmField mmField) {
            return null;
        }
    },
    INTERVAL("postgresql_interval","interval") {
        @Override
        public String convertToDdlFragment(MmField mmField) {
            return null;
        }
    },
    // 布尔类型
    BOOLEAN("postgresql_boolean","boolean") {
        @Override
        public String convertToDdlFragment(MmField mmField) {
            return null;
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
