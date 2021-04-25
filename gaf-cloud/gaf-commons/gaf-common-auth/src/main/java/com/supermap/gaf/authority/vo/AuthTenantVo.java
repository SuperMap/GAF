/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.authority.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;


/**
 * @author:yj
 * @date:2021/3/25
*/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("租户vo")
public class AuthTenantVo implements Serializable {
    @ApiModelProperty("租户id")
    private String tenantId;
    @ApiModelProperty("租户名称")
    private String tenantName;
    @ApiModelProperty("租户描述")
    private String description;
    @ApiModelProperty("租户类别")
    private String type;
    @ApiModelProperty("英文名称")
    private String nameEn;
    @ApiModelProperty("英文简称")
    private String briefNameEn;
    @ApiModelProperty("编码")
    private String code;
    @ApiModelProperty("状态")
    private Boolean status;
    @ApiModelProperty("创建时间")
    private Date createdTime;
    @ApiModelProperty("创建人")
    private String createdBy;
    @ApiModelProperty("修改时间")
    private Date updatedTime;
    @ApiModelProperty("修改人")
    private String updatedBy;
}
