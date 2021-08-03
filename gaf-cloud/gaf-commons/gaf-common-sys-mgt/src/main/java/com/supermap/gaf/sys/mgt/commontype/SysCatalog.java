/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.sys.mgt.commontype;

import com.alibaba.fastjson.annotation.JSONField;
import com.supermap.gaf.annotation.*;
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
 * 目录
 *
 * @author wangxiaolong
 * @date:2021/3/25
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("目录")
public class SysCatalog implements Serializable {
    @QueryParam("catalogId")
    @NotNull
    @ApiModelProperty("目录id")
    @Id
    @ConfigName("resourceId")
    private String catalogId;
    @QueryParam("parentId")
    @NotNull
    @ApiModelProperty("上级目录id")
    @ParentIdField
    @ConfigName("pid")
    private String parentId;
    @ConfigName("sortSn")
    @QueryParam("sortSn")
    @ApiModelProperty(value = "排序序号", example = "1", allowableValues = "range[1,infinity]")
    @SortSnField
    private Integer sortSn;
    @QueryParam("name")
    @NotNull
    @ApiModelProperty(value = "目录名称", example = "xxx分组")
    @ConfigName("resourceName")
    private String name;
    @QueryParam("type")
    @NotNull
    @ApiModelProperty(value = "类别。1：模块分组，2：API分组，3：角色分组，4：菜单分组，5:webgis服务分组，6：资源目录,7:字典目录",
            allowableValues = "1,2,3,4,5,6,7", example = "1"
    )
    private String type;
    @QueryParam("code")
    @ApiModelProperty("编码。暂时无用")
    private String code;
    @QueryParam("sysComponentId")
    @NotNull
    @ApiModelProperty("所属组件")
    private String sysComponentId;
    @QueryParam("iconUrl")
    @ApiModelProperty("图标地址")
    private String iconUrl;
    @QueryParam("description")
    @ApiModelProperty("描述")
    private String description;
    @QueryParam("status")
    @NotNull
    @ApiModelProperty("状态。逻辑删除字段")
    @JSONField(name = "isStatus")
    @LogicDeleteField
    private Boolean status;
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
    @QueryParam("tenantId")
    @ApiModelProperty("租户id")
    private String tenantId;
    @QueryParam("bizTypeCode")
    @ApiModelProperty("业务类别。业务类型字典码，资源目录等业务数据目录使用")
    private String bizTypeCode;
}
