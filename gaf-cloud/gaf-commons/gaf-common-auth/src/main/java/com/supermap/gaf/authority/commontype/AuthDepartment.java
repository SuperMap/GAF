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
import java.io.Serializable;
import java.util.Date;

/**
 * 部门
 *
 * @date:2021/3/25
 * @author zhm
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("部门")
public class AuthDepartment implements Serializable {
    @ApiModelProperty("部门id")
    @Id
    private String departmentId;
    @ApiModelProperty("租户id")
    private String tenantId;
    @ApiModelProperty("父级id")
    @ParentIdField
    private String parentId;
    @ApiModelProperty(value = "序号", example = "1", allowableValues = "range[1,infinity]")
    @SortSnField
    private Integer sortSn;
    @ApiModelProperty(value = "部门类型。存字典code,定义：0无、1管理、2研发…", example = "0")
    private String departmentType;
    @NotNull
    @ApiModelProperty(value = "名称", example = "开发部")
    private String departmentName;
    @ApiModelProperty(value ="英文名称", example = "development")
    private String nameEn;
    @ApiModelProperty(value = "简称", example = "dept")
    private String briefName;
    @ApiModelProperty("编码")
    private String code;
    @ApiModelProperty("状态。逻辑删除字段")
    @LogicDeleteField
    @JSONField(name = "isStatus")
    private Boolean status = true;
    @ApiModelProperty("描述")
    private String description;
    @ApiModelProperty("管理员id")
    private String adminId;
    @ApiModelProperty(value = "是否第三方", example = "false", allowableValues = "true,false")
    @JSONField(name = "isThirdParty")
    private Boolean isThirdParty;
    @ApiModelProperty("创建时间")
    private Date createdTime;
    @ApiModelProperty("创建人")
    private String createdBy;
    @UpdatedTimeField
    @ApiModelProperty("修改时间")
    private Date updatedTime;
    @ApiModelProperty("修改人")
    private String updatedBy;
}
