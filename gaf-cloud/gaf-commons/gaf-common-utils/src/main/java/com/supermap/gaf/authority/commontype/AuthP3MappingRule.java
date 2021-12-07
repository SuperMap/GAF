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
import java.util.Date;

/**
 * 第三方映射规则
 *
 * @author yangdong
 * @date:2021/3/25
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("第三方映射规则")
public class AuthP3MappingRule {
    @ApiModelProperty("映射id")
    private String mappingRuleId;
    @NotNull
    @ApiModelProperty(value = "映射规则名称", example = "同步sinfcloud租户")
    private String mappingRuleName;

    // todo: wxl 应该没用了
    @NotNull
    @ApiModelProperty("第三方组件")
    private String p3ComponentId;

    @NotNull
    @ApiModelProperty(value = "映射类型。1:租户，2：部门，3：用户", example = "1", allowableValues = "1,2,3")
    private String mappingType;
    @NotNull
    @ApiModelProperty(value = "同步方式。1:推push,2:拉pull", example = "1", allowableValues = "1,2")
    private String mappingMethod;
    @ApiModelProperty("映射实现类。包路径.类名。统一反射方式实现，根据映射方式调用实现类的pull或push方法")
    private String mappingImpl;
    @ApiModelProperty("状态。逻辑删除字段")
    @JSONField(name = "isStatus")
    private Boolean status = true;
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
    @ApiModelProperty("其他参数")
    private String extraParamJson;

    // todo: wxl 应该没用了
    // transient
    private String p3ComponentName;
    private boolean isMapped = false;
}
