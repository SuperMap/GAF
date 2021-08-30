package com.supermap.gaf.data.mgt.util;

import com.supermap.data.FieldInfo;
import com.supermap.data.FieldType;
import com.supermap.gaf.commontypes.metadata.FieldMetadataInfo;
import com.supermap.gaf.data.mgt.entity.MmField;

import java.util.function.Function;

public class DatamgtCommonUtils {
    public final static Function<String,String> FIELD_TYPE_2_TYPE_CODE_CONVERT = type->type.substring(type.lastIndexOf("_")+1);


    public static MmField convert2MmField(FieldMetadataInfo fieldMetadataInfo, Function<String,String> typeConvert) {
        String type = fieldMetadataInfo.getSqlType();
        try{
            type = typeConvert.apply(type);
        }catch (Exception e){
            e.printStackTrace();
        }
        return MmField.builder().fieldName(fieldMetadataInfo.getFieldName())
                .fieldAlias(fieldMetadataInfo.getFieldName())
                .fieldLength(fieldMetadataInfo.getColumnSize())
                .fieldPrecision(fieldMetadataInfo.getColumnSize())
                .fieldScale(fieldMetadataInfo.getScale())
                .fieldNotNull(!fieldMetadataInfo.isNullable())
                .fieldType(type)
                .fieldDefault(fieldMetadataInfo.getDefaultValue())
                .description(fieldMetadataInfo.getRemarks())
                .pkSeq(fieldMetadataInfo.getPkSeq())
                .fieldPrimaryKey(fieldMetadataInfo.isPrimaryKey())
                .build();
    }

    public static FieldMetadataInfo convert2FieldMetadataInfo(MmField mmField, Function<String,String> typeConvert) {
        String type = mmField.getFieldType();
        try{
            type = typeConvert.apply(mmField.getFieldType());
        }catch (Exception e){
            e.printStackTrace();
        }
        return FieldMetadataInfo.builder().fieldName(mmField.getFieldName())
                .isPrimaryKey(mmField.getFieldPrimaryKey()==null?false:mmField.getFieldPrimaryKey())
                .scale(mmField.getFieldScale())
                .remarks(mmField.getDescription())
                .defaultValue(mmField.getFieldDefault())
                .sqlType(type)
                .columnSize(mmField.getFieldLength())
                .pkSeq(mmField.getPkSeq())
                .isNullable(!(mmField.getFieldNotNull()==null?true:mmField.getFieldNotNull()))
                .build();
    }

    /**
     * Convert mm field.
     *
     * @param fieldInfo the field info
     * @return the mm field
     */
    public static MmField convert2MmField(FieldInfo fieldInfo, Function<String,String> typeConvert){
        String type = fieldInfo.getType().name().toLowerCase();
        try{
            type = typeConvert.apply(type);
        }catch (Exception e){
            e.printStackTrace();
        }
        MmField re = new MmField();
        re.setFieldName(fieldInfo.getName());
        re.setFieldType(type);
        re.setFieldLength(fieldInfo.getMaxLength());
        re.setFieldAlias(fieldInfo.getCaption());
        re.setFieldDefault(fieldInfo.getDefaultValue());
        re.setFieldNotNull(fieldInfo.isRequired());
        return re;
    }

    /**
     * Convert field info.
     *
     * @param mmField the mm field
     * @return the field info
     */
    public static FieldInfo convert2FieldInfo(MmField mmField, Function<String,String> typeConvert){
        String type = mmField.getFieldType();
        try{
            type = typeConvert.apply(type);
        }catch (Exception e){
            e.printStackTrace();
        }
        FieldInfo re = new FieldInfo();
        re.setName(mmField.getFieldName());
        if(mmField.getFieldAlias()!=null){
            re.setCaption(mmField.getFieldAlias());
        }
        if(mmField.getFieldDefault()!=null){
            re.setDefaultValue(mmField.getFieldDefault());
        }
        if(mmField.getFieldLength()!=null){
            re.setMaxLength(mmField.getFieldLength());
        }
        if(mmField.getFieldNotNull()!=null){
            re.setRequired(mmField.getFieldNotNull());
        }
        FieldType fieldType = (FieldType) FieldType.parse(FieldType.class, type.toUpperCase());
        re.setType(fieldType);
        return re;
    }

}
