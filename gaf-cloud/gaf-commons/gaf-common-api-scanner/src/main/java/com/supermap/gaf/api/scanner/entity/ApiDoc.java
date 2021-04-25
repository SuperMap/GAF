/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.api.scanner.entity;

import com.supermap.gaf.annotation.UpdatedTimeField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * api文档
 * @date:2021/3/25
 * @author dqc
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("api文档")
public class ApiDoc implements Serializable {

    private static final long serialVersionUID = -6882943437766854102L;

    @ApiModelProperty("id")
    private String id;
    @ApiModelProperty("文档名称")
    private String name;
    @ApiModelProperty("文档内容")
    private String data;
    @ApiModelProperty("创建时间")
    private Date createdTime;
    @ApiModelProperty("创建人")
    private String createdBy;
    @ApiModelProperty("修改时间")
    private Date updatedTime;
    @ApiModelProperty("修改人")
    private String updatedBy;

    public ApiDoc(String name, String data) {
        this.name = name;
        this.data = data;
    }
}
