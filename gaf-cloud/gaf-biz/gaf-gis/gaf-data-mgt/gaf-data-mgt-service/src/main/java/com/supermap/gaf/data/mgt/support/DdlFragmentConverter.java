package com.supermap.gaf.data.mgt.support;

import com.supermap.gaf.data.mgt.entity.MmField;

/**
 * 将字段信息转换为对应的ddl片段
 *
 */
public interface DdlFragmentConverter {

    String convertToDdlFragment(MmField mmField);

    default String getCommonFragment(MmField mmField) {
        StringBuilder sb = new StringBuilder();
        if (mmField.getFieldNotNull()) {
            sb.append(" NOT NULL ");
        }
        String fieldDefault = mmField.getFieldDefault();
        if (fieldDefault != null && !fieldDefault.isEmpty()) {
            sb.append(" DEFAULT ").append(fieldDefault);
        }
        String description = mmField.getDescription();
        if (description != null && !description.isEmpty()) {
            sb.append("comment '").append(description).append("' ");
        }
        return sb.toString();
    }

    default String getLengthPrecision(MmField mmField) {
        Integer fieldLength = mmField.getFieldLength();
        Integer fieldPrecision = mmField.getFieldPrecision();
        StringBuilder sb = new StringBuilder();
        if (fieldLength != null) {
            sb.append("(").append(fieldLength);
            if(fieldPrecision != null) {
                sb.append(",").append(fieldPrecision);
            }
            sb.append(")");
        }
        return sb.toString();
    }
}
