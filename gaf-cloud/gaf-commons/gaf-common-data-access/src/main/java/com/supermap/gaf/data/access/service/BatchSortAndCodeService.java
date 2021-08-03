/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.data.access.service;


import java.util.Collection;


/**
 * @author:yj
 * @date:2021/3/25
 */
public interface BatchSortAndCodeService {

    @Deprecated
    int revisionSortSn(com.supermap.gaf.data.access.commontypes.RevisionSortSnParam revisionSortSnParam);

    int revisionSortSnForUpdate(Class<?> entityClass, String parentId, Integer oldSortSn, Integer curSortSn);

    void revisionSortSnForInsertOrDelete(Class<?> entityClass, Collection<String> parentIds);
}
