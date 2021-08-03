/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.commontypes;

import java.io.Serializable;
import java.util.List;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * @author:yj
 * @date:2021/3/25
 */

@ApiModel(description = "页面查询的基类")
public class Page<T> implements Serializable {
    /**
     * @since 8.1.0
     */
    private static final long serialVersionUID = -8123601244937188076L;
    @ApiModelProperty(value = "页面规格")
    private int pageSize;
    @ApiModelProperty(value = "页面索引")
    private int pageIndex;
    @ApiModelProperty(value = "总页面数")
    private int totalPage;
    @ApiModelProperty(value = "总数")
    private int total;
    @ApiModelProperty(value = "内容")
    private List<T> content;
    @ApiModelProperty(value = "查询参数")
    protected SearchParameter searchParameter;


    public Page() {
        this.pageSize = 10;
        this.pageIndex = 1;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
        calculateTotalPage();
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
        calculateTotalPage();
    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void calculateTotalPage() {
        if ((this.pageSize > 0) && (this.total > 0)) {
            double count = this.total;
            totalPage = (int) Math.ceil(count / pageSize);
        }
    }

    public int calculateStartIndex() {
        return ((this.pageIndex - 1) * this.pageSize);
    }

    public SearchParameter getSearchParameter() {
        return this.searchParameter;
    }

    public void setSearchParameter(SearchParameter params) {
        if (params == null) {
            return;
        }
        this.pageIndex = params.getPageIndex();
        this.pageSize = params.getPageSize();
        this.searchParameter = params;
    }

    @Override
    public String toString() {
        return "Page [pageSize=" + pageSize + ", pageIndex=" + pageIndex + ", totalPage=" + totalPage + ", total=" + total + ", content=" + content
                + ", searchParameter=" + searchParameter + "]";
    }

}
