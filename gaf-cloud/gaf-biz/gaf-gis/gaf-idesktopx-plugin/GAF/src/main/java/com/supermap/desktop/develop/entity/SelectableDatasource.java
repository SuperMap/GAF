/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.desktop.develop.entity;

import com.supermap.data.Datasource;

/**
 * @date:2021/3/25
 * @author heykb
 */
public class SelectableDatasource{
    private Datasource datasource;
    private boolean select;

    public SelectableDatasource(Datasource datasource) {
        this.datasource = datasource;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public Datasource getDatasource() {
        return datasource;
    }

    public void setDatasource(Datasource datasource) {
        this.datasource = datasource;
    }
}
