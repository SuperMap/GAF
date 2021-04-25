/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.data.mgt.util;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
* @author:yw
 * @date:2021/3/25
* @Date 2021-3-12
**/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(" 分页实体")
public class Page<E> implements Serializable {
    private static final long serialVersionUID = 1L;
	
    /**
     * 当前页数
     */
	@ApiModelProperty("当前页数")
    private int pageNum;
	
    /**
     * 每页记录数
     */
	@ApiModelProperty("每页记录数")
    private int pageSize;

    /**
     * 总记录数
     */
	@ApiModelProperty("总记录数")
    private int totalCount;

    /**
     * 总页数
     */
	@ApiModelProperty("总页数")
    private int totalPage;
    /**
     * 列表数据
     */
	@ApiModelProperty("列表数据,即分页查询结果")
    private List<E> pageList;

    public static <E> Page<E> create(int pageNum,int pageSize, int totalCount, int totalPage, List<E> pageList) {
        return new Page<>(pageNum,pageSize, totalCount, totalPage, pageList);
    }

}
