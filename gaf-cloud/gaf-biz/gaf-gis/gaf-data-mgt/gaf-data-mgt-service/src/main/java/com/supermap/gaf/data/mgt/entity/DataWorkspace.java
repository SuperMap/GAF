/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.data.mgt.entity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.*;
import javax.validation.constraints.*;
import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 * 工作空间
 * @author zrc 
 * @date yyyy-mm-dd
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("数据工作空间")
public class DataWorkspace implements Serializable{
    @ApiModelProperty(value = "工作空间id",example = "xxxx")
    private String workspaceId;
    @ApiModelProperty(value = "名称",example = "name")
    private String wsName;
    @NotNull
    @ApiModelProperty(value = "类型",example = "SMWU")
    private String typeCode;
    @NotNull
    @ApiModelProperty(value = "服务器名称",example = "jdbc:postgresql://localhost:5432/demo")
    private String server;
    @ApiModelProperty(value = "数据库名称",example = "test")
    private String database;
    @ApiModelProperty(value = "用户名",example = "root")
    private String userName;
    @ApiModelProperty(value = "密码",example = "root")
    private String password;
    @ApiModelProperty(value = "描述",example = "xxxx")
    private String description;
    @ApiModelProperty(value = "状态",example = "true")
    @JSONField(name="isStatus")
    private Boolean status;
    @ApiModelProperty("所属租户。所属租户id。空则是公共的")
    private String tenantId;
    @ApiModelProperty("创建时间。生成时间不可变更")
    private Date createdTime;
    @ApiModelProperty("创建人。创建人user_id")
    private String createdBy;
    @ApiModelProperty("修改时间。修改时更新")
    private Date updatedTime;
    @ApiModelProperty("修改人。修改人user_id")
    private String updatedBy;
    /**
     * 默认值1：false
     */
    @ApiModelProperty("是否已发布。true:已发布，false:未发布")
    @JSONField(name="isPublished")
    private Boolean published;
}
