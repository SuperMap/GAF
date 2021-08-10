/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.data.access.commontypes;

import com.supermap.gaf.commontypes.RevisionSortSnParam;

import java.util.List;

/**
 * @author wxl
 * <p>
 * conditions 是sql的where子句条件集合 ，条件 例如 id = ‘1’ 例如 name like '%xxx%'
 * @date 2021/1/15
 */
public class ExtendSortSnParam extends RevisionSortSnParam {
    private List<String> conditions;

    public List<String> getConditions() {
        return conditions;
    }

    public void setConditions(List<String> conditions) {
        this.conditions = conditions;
    }
}
