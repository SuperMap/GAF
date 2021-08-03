/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.authority.commontype;

import com.alibaba.fastjson.annotation.JSONField;
import com.supermap.gaf.annotation.LogicDeleteField;
import com.supermap.gaf.annotation.ParentIdField;
import com.supermap.gaf.annotation.SortSnField;
import com.supermap.gaf.annotation.UpdatedTimeField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.ws.rs.QueryParam;
import java.io.Serializable;
import java.util.Date;

/**
 * 接口（API）
 *
 * @author zhm
 * @date:2021/3/25
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "接口", description = "接口(API)实体类")
public class AuthResourceApi implements Serializable {
    @QueryParam("resourceApiId")
    @NotNull
    @ApiModelProperty("接口id")
    @Id
    private String resourceApiId;
    @QueryParam("sysComponentId")
    @NotNull
    @ApiModelProperty("所属组件id")
    private String sysComponentId;
    @QueryParam("apiCatalogId")
    @NotNull
    @ApiModelProperty("接口分组id")
    @ParentIdField
    private String apiCatalogId;
    @QueryParam("name")
    @NotNull
    @ApiModelProperty("名称")
    private String name;
    @QueryParam("routeUrl")
    @ApiModelProperty(value = "路由路径, 即请求路径", example = "/authority/auth-posts")
    private String routeUrl;
    @QueryParam("method")
    @ApiModelProperty(value = "方法。1:GET,2:POST,3:PUT,4:DELETE", example = "1", allowableValues = "1,2,3,4")
    private String method;
    @QueryParam("type")
    @ApiModelProperty(value = "类型。1:应用组件资源，2：第三方资源", example = "1", allowableValues = "1,2")
    private String type;
    @QueryParam("status")
    @ApiModelProperty("状态。逻辑删除字段")
    @JSONField(name = "isStatus")
    @LogicDeleteField
    private Boolean status;
    @QueryParam("sortSn")
    @ApiModelProperty(value = "排序序号", example = "1", allowableValues = "range[1,infinity]")
    @SortSnField
    private Integer sortSn;
    @QueryParam("createdTime")
    @ApiModelProperty("创建时间")
    private Date createdTime;
    @QueryParam("createdBy")
    @ApiModelProperty("创建人")
    private String createdBy;
    @QueryParam("updatedTime")
    @ApiModelProperty("修改时间")
    @UpdatedTimeField
    private Date updatedTime;
    @QueryParam("updatedBy")
    @ApiModelProperty("修改人")
    private String updatedBy;
}
