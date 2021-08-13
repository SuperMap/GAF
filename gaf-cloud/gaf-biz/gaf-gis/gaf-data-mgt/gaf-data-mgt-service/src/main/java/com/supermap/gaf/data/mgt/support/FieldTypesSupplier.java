package com.supermap.gaf.data.mgt.support;

import com.supermap.gaf.data.mgt.model.FieldTypeInfo;

import java.util.List;

/**
 * 获取字段的数据类型
 */
public interface FieldTypesSupplier {
    /**
     * 获取对应的字段数据类型编码和名称映射的集合
     * @return 段数据类型编码和名称映射的集合
     */
    List<FieldTypeInfo> getFieldTypes();
}
