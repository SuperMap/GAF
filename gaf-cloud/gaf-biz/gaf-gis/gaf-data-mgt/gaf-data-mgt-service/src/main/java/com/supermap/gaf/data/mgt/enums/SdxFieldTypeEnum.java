package com.supermap.gaf.data.mgt.enums;

import com.supermap.gaf.data.mgt.model.FieldTypeInfo;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 空间数据库字段的数据类型枚举
 * 对应com.supermap.data.FieldType
 * @author wxl
 * @since 2021/8/12
 */
public enum SdxFieldTypeEnum{
     BOOLEAN("sdx_boolean","布尔"),
     BYTE ("sdx_byte","字节"),
     INT16("sdx_int16","64位整型"),
     INT32("sdx_int32","32位整型"),
     INT64("sdx_int64","64位整型"),
     SINGLE("sdx_single","单精度"),
     DOUBLE("sdx_double","双精度"),
     DATETIME("sdx_datetime","日期"),
     //DATE("sdx_date",""),
     //TIME("sdx_time",""),
     LONGBINARY("sdx_longbinary","二进制型"),
     TEXT("sdx_text","文本型"),
     CHAR("sdx_char","字符型"),
     WTEXT("sdx_wtext","宽字符");
     //JSONB("sdx_jsonb","");
    private String code;
    private String name;

    SdxFieldTypeEnum(String code,String name) {
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
        return Arrays.stream(SdxFieldTypeEnum.values()).map(type -> {
            FieldTypeInfo fieldTypeInfo = new FieldTypeInfo();
            fieldTypeInfo.setCode(type.getCode());
            fieldTypeInfo.setName(type.getName());
            fieldTypeInfo.setDescrption(type.getName());
            return fieldTypeInfo;
        }).collect(Collectors.toList());
    }


}
