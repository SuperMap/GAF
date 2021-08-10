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
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;

/**
 * @author dqc
 * @date:2021/3/25
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("用户")
public class AuthUser implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TENANT_ADMIN_ROLE_ID = "role_000001";
    @Id
    @ApiModelProperty("用户id")
    private String userId;
    @ApiModelProperty("租户id")
    private String tenantId;
    @NotNull
    @ApiModelProperty("部门id")
    @ParentIdField
    private String departmentId;
    @ApiModelProperty(value = "排序序号", example = "1", allowableValues = "range[1,infinity]")
    @SortSnField
    private Integer sortSn;
    @NotNull
    @ApiModelProperty("登录用户名")
    private String name;
    @ApiModelProperty("密码")
    private String password;
    @NotNull
    @ApiModelProperty("真实姓名")
    private String realName;
    @ApiModelProperty("身份证号")
    private String idNumber;
    @ApiModelProperty("手机号")
    private String mobileNumber;
    @NotNull
    @Pattern(regexp = "^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*\\.[a-zA-Z0-9]{2,6}$", message = "不满足邮箱格式")
    @ApiModelProperty("邮箱")
    private String email;
    @ApiModelProperty("地址")
    private String address;
    @ApiModelProperty("岗位id")
    private String postId;
    @ApiModelProperty("授权截止时间")
    private Date expirationTime;
    @ApiModelProperty(value = "是否第三方", allowableValues = "true,false")
    @JSONField(name = "isThirdParty")
    private Boolean isThirdParty;
    @ApiModelProperty("状态。逻辑删除字段")
    @JSONField(name = "isStatus")
    @LogicDeleteField
    private Boolean status = true;
    @ApiModelProperty("描述")
    private String description;
    @ApiModelProperty("上次登录时间")
    private Date lastLoginTime;
    @ApiModelProperty("创建时间")
    private Date createdTime;
    @ApiModelProperty("创建人")
    private String createdBy;
    @ApiModelProperty("修改时间")
    @UpdatedTimeField
    private Date updatedTime;
    @ApiModelProperty("修改人")
    private String updatedBy;

    // transient
    @ApiModelProperty("部门名称")
    private String departmentName;
    @ApiModelProperty("岗位名称")
    private String postName;
    @ApiModelProperty("是否是挂职")
    private Boolean belongsParttime;


}
