/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.data.access.dao;

import com.supermap.gaf.data.access.commontypes.ExtendSortSnParam;
import com.supermap.gaf.data.access.commontypes.RevisionSortSnParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;


/**
 * @author:yj
 * @date:2021/3/25
 */
@Mapper
@Component
public interface BatchSortAndCodeMapper {
    void batchModifySortSNWhenAdd(@Param("tableName") String tableName, @Param("parentIdFieldName") String parentIdFieldName, @Param("parentId") String parentId, @Param("idFieldName") String idFieldName, @Param("curId") String curId, @Param("curSN") int curSN);

    void batchModifySortSNWhenDown(@Param("tableName") String tableName, @Param("parentIdFieldName") String parentIdFieldName, @Param("parentId") String parentId, @Param("idFieldName") String idFieldName, @Param("curId") String curId, @Param("oldSN") int oldSn, @Param("curSN") int curSN);

    void batchModifySortSNWhenUp(@Param("tableName") String tableName, @Param("parentIdFieldName") String parentIdFieldName, @Param("parentId") String parentId, @Param("idFieldName") String idFieldName, @Param("curId") String curId, @Param("oldSN") int oldSn, @Param("curSN") int curSN);

    void modifyCurSortSN(String tableName, String idFieldName, String curId, int oldSn, int curSN);

    void batchResetSortSN(String tableName, String parentId);

    int revisionSortSn(RevisionSortSnParam revisionSortSnParam);

    int revisionSortSnMutiCondition(ExtendSortSnParam extendSortSnParam);
}
