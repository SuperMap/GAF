package com.supermap.gaf.data.mgt.support;

import com.supermap.gaf.data.mgt.entity.MmField;

/**
 * 将字段信息转换为对应的ddl片段
 *
 */
public interface DdlFragmentConverter {

    String convertToDdlFragment(MmField mmField);

    default String getCommonFragment(MmField mmField) {
        return getNotNull(mmField).append(getDefault(mmField)).append(getComment(mmField)).toString();
    }

    default StringBuilder getNotNull(MmField mmField) {
        StringBuilder sb = new StringBuilder();
        if (mmField.getFieldNotNull()) {
            sb.append(" NOT NULL ");
        }
        return sb;
    }

    default StringBuilder getDefault(MmField mmField) {
        StringBuilder sb = new StringBuilder();
        String fieldDefault = mmField.getFieldDefault();
        if (fieldDefault != null && !fieldDefault.isEmpty()) {
            sb.append(" DEFAULT ").append(fieldDefault).append(" ");
        }
        return sb;
    }

    default StringBuilder getComment(MmField mmField) {
        StringBuilder sb = new StringBuilder();
        String description = mmField.getDescription();
        if (description != null && !description.isEmpty()) {
            sb.append(" comment '").append(description).append("' ");
        }
        return sb;
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
