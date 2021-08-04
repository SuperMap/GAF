/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.authority.commontype;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 角色接口
 *
 * @author yangdong
 * @date:2021/3/25
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "角色接口", description = "角色接口关联实体类")
public class AuthRoleApi implements Serializable {
    @ApiModelProperty("角色接口id")
    private String roleApiId;
    @NotNull
    @ApiModelProperty("角色id")
    private String roleId;
    @NotNull
    @ApiModelProperty("接口id")
    private String resourceApiId;
    @ApiModelProperty("状态。逻辑删除字段")
    @JSONField(name = "isStatus")
    private Boolean status = true;
    @ApiModelProperty("排序序号")
    private Integer sortSn = 1;
    @ApiModelProperty("描述")
    private String description;
    @ApiModelProperty("创建时间")
    private Date createdTime;
    @ApiModelProperty("创建人")
    private String createdBy;
    @ApiModelProperty("修改时间")
    private Date updatedTime;
    @ApiModelProperty("修改人")
    private String updatedBy;
}
