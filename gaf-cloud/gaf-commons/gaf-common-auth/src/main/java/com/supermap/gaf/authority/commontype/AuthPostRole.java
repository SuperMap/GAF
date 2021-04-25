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
 * 岗位角色
 *
 * @date:2021/3/25
 * @author yangdong
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("岗位角色")
public class AuthPostRole implements Serializable {
    @ApiModelProperty("岗位角色id")
    private String postRoleId;
    @NotNull
    @ApiModelProperty("岗位id")
    private String postId;
    @NotNull
    @ApiModelProperty("角色id")
    private String roleId;
    @ApiModelProperty("创建时间")
    private Date createdTime;
    @ApiModelProperty("创建人")
    private String createdBy;
    @ApiModelProperty("修改时间")
    private Date updatedTime;
    @ApiModelProperty("修改人")
    private String updatedBy;
    @ApiModelProperty("排序序号")
    private Integer sortSn = 1;
    @ApiModelProperty("状态。逻辑删除字段")
    @JSONField(name = "isStatus")
    private Boolean status = true;
}
