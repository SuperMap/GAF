/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.authority.vo;

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
 * @date:2021/3/25
 * @author wxl
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("用户挂职 视图层对象")
public class AuthUserParttimeVo implements Serializable {
    @ApiModelProperty("用户挂职id")
    private String userParttimeId;
    @NotNull
    @ApiModelProperty("用户id")
    private String userId;
    @NotNull
    @ApiModelProperty("挂职部门id")
    private String departmentId;
    @NotNull
    @ApiModelProperty("岗位id")
    private String postId;
    @ApiModelProperty("岗位类别")
    private String postType;
    @ApiModelProperty("授权截止时间")
    private Date expirationTime;
    @ApiModelProperty("状态")
    @JSONField(name = "isStatus")
    private Boolean status = true;
    @ApiModelProperty("排序序号")
    private Integer sortSn = 1;
    @ApiModelProperty("创建时间")
    private Date createdTime;
    @ApiModelProperty("创建人")
    private String createdBy;
    @ApiModelProperty("修改时间")
    private Date updatedTime;
    @ApiModelProperty("修改人")
    private String updatedBy;

    @ApiModelProperty("部门名称")
    private String departmentName;
    @ApiModelProperty("岗位名")
    private String postName;

}
