/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.portal.menu.commontypes;

import com.supermap.gaf.annotation.UpdatedTimeField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
* @author:yw
 * @date:2021/3/25
* @Date 2021-3-12
**/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("门户订制对象")
public class CustomationInfo  implements Serializable {

    public static final String DEFUALT_TENANT_ID = "tenant_000000";

    @ApiModelProperty(value = "登录地址",example = "/login")
    private String loginAdress;
    @ApiModelProperty(value = "登出地址",example = "/logout")
    private String logoutAdress;
    @ApiModelProperty(value = "个人信息地址",example = "/portal/user/profile")
    private String profileAdress;
    @ApiModelProperty(value = "logo图标",example = "./img/logo.png")
    private String logo;
    @ApiModelProperty(value = "门户标题",example = "GAF")
    private String title;
    @ApiModelProperty(value = "主题色",example = "#545c64")
    private String color;
    @ApiModelProperty(value = "是否启用多页签模式",example = "t为true，f为false")
    private boolean multipleTabs;
    @ApiModelProperty(value = "用户名称",example = "xxxxx")
    private String user;
    @ApiModelProperty(value = "菜单布局方式",example = "group(默认)、top（顶部）、left(侧栏)、top-left(顶部-侧栏)选一")
    private String layoutType;
    @ApiModelProperty(value = "定制信息")
    private String configInfo;
    @ApiModelProperty(value = "默认定制信息")
    private String defaultConfigInfo;
    @ApiModelProperty(value = "租户id")
    private String tenantId;
    @ApiModelProperty(value = "创建时间",notes = "创建时间")
    private Date createdTime;
    @ApiModelProperty(value = "创建人",notes = "创建人")
    private String createdBy;
    @ApiModelProperty(value = "修改时间",notes = "修改时间")
    @UpdatedTimeField
    private Date updatedTime;
    @ApiModelProperty(value = "修改人",notes = "修改人")
    private String updatedBy;
    @ApiModelProperty(value = "id", notes = "定制id")
    private String id;

    public void setNull2Default(){
        if (loginAdress == null) {
            loginAdress = "/login";
        }
        if (logoutAdress == null) {
            logoutAdress = "/logout";
        }
        if (profileAdress == null) {
            profileAdress = "/profile";
        }
        if (logo == null) {
            logo = "gaf";
        }
        if (title == null) {
            title = "GAF";
        }
        if (color == null) {
            color = "gray";
        }
        if (user == null) {
            user = "admin";
        }
        if (layoutType == null) {
            layoutType = "group";
        }
    }

}
