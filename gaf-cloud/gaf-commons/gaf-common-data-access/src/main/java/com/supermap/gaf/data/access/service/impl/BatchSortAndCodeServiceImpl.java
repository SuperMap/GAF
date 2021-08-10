/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.data.access.service.impl;

import com.google.common.base.CaseFormat;
import com.supermap.gaf.annotation.LogicDeleteField;
import com.supermap.gaf.annotation.ParentIdField;
import com.supermap.gaf.annotation.SortSnField;
import com.supermap.gaf.annotation.UpdatedTimeField;
import com.supermap.gaf.data.access.commontypes.RevisionSortSnParam;
import com.supermap.gaf.data.access.dao.BatchSortAndCodeMapper;
import com.supermap.gaf.data.access.service.BatchSortAndCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


/**
 * @author:yj
 * @date:2021/3/25 The type Batch sort and code service.
 */
@Service
public class BatchSortAndCodeServiceImpl implements BatchSortAndCodeService {

    @Autowired
    private BatchSortAndCodeMapper batchSortAndCodeMapper;

    @Override
    public int revisionSortSn(RevisionSortSnParam revisionSortSnParam) {
        Assert.notNull(revisionSortSnParam.getTableName(), "tableName 不能为null");
        Assert.notNull(revisionSortSnParam.getIdFieldName(), "idFieldName 不能为null");
        Assert.notNull(revisionSortSnParam.getParentIdFieldName(), "parentIdFieldName 不能为null");
        Assert.notNull(revisionSortSnParam.getSortSnFieldName(), "sortSnFieldName 不能为null");
        Assert.notNull(revisionSortSnParam.getUpdatedTimeFieldName(), "updatedTimeFieldName 不能为null");
        if (revisionSortSnParam.getOldSortSn() != null && revisionSortSnParam.getCurSortSn() != null && revisionSortSnParam.getOldSortSn().equals(revisionSortSnParam.getCurSortSn())) {
            return 0;
        }
        return batchSortAndCodeMapper.revisionSortSn(revisionSortSnParam);
    }

    @Override
    public int revisionSortSnForUpdate(Class<?> entityClass, String parentId, Integer oldSortSn, Integer curSortSn) {
        if (oldSortSn != null && curSortSn != null && oldSortSn.equals(curSortSn)) {
            return 0;
        }
        RevisionSortSnParam revisionSortSnParam = new RevisionSortSnParam();
        String tableName = entityClass.getSimpleName();
        Table tableAnnotation = entityClass.getAnnotation(Table.class);
        if (tableAnnotation != null) {
            tableName = tableAnnotation.name();
        } else {
            tableName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, tableName);
        }
        revisionSortSnParam.setTableName(tableName);

        Field[] fields = entityClass.getDeclaredFields();
        Set<Class> founded = new HashSet<>();
        for (Field field : fields) {
            String fieldName = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, field.getName());
            Column column = field.getAnnotation(Column.class);
            if (column != null) {
                fieldName = column.name();
            }
            if (field.isAnnotationPresent(ParentIdField.class)) {
                revisionSortSnParam.setParentIdFieldName(fieldName);
                founded.add(ParentIdField.class);
            }
            if (fieldName.equals("pid") && !founded.contains(ParentIdField.class)) {
                revisionSortSnParam.setParentIdFieldName(fieldName);
            }
            if (field.isAnnotationPresent(Id.class)) {
                revisionSortSnParam.setIdFieldName(fieldName);
                founded.add(Id.class);
            }
            if (fieldName.equals("id") && !founded.contains(Id.class)) {
                revisionSortSnParam.setIdFieldName(fieldName);
            }
            if (field.isAnnotationPresent(SortSnField.class)) {
                revisionSortSnParam.setSortSnFieldName(fieldName);
                founded.add(SortSnField.class);
            }
            if (fieldName.equals("sort_sn") && !founded.contains(SortSnField.class)) {
                revisionSortSnParam.setParentIdFieldName(fieldName);
            }
            if (field.isAnnotationPresent(UpdatedTimeField.class)) {
                revisionSortSnParam.setUpdatedTimeFieldName(fieldName);
                founded.add(UpdatedTimeField.class);
            }
            if (fieldName.equals("updated_time") && !founded.contains(UpdatedTimeField.class)) {
                revisionSortSnParam.setParentIdFieldName(fieldName);
            }
            if (field.isAnnotationPresent(LogicDeleteField.class)) {
                revisionSortSnParam.setLogicDeleteFieldName(fieldName);
                founded.add(ParentIdField.class);
            }
        }
        revisionSortSnParam.setParentId(parentId);
        revisionSortSnParam.setOldSortSn(oldSortSn);
        revisionSortSnParam.setCurSortSn(curSortSn);
        Assert.notNull(revisionSortSnParam.getIdFieldName(), "could not found id property or @Id in class " + entityClass.getSimpleName());
        Assert.notNull(revisionSortSnParam.getParentIdFieldName(), "could not found pid property or @ParentIdField in class " + entityClass.getSimpleName());
        Assert.notNull(revisionSortSnParam.getSortSnFieldName(), "could not found sort_sn property or @SortSnField in class " + entityClass.getSimpleName());
        Assert.notNull(revisionSortSnParam.getUpdatedTimeFieldName(), "could found find updated_time property or @UpdatedTimeField in class " + entityClass.getSimpleName());
        return batchSortAndCodeMapper.revisionSortSn(revisionSortSnParam);
    }

    @Override
    public void revisionSortSnForInsertOrDelete(Class<?> entityClass, Collection<String> parentIds) {
        parentIds.forEach(parentId -> revisionSortSnForUpdate(entityClass, parentId, null, null));
    }
}
