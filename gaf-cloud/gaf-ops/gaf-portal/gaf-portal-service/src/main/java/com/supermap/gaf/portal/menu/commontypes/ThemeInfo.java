/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.portal.menu.commontypes;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author:yw
 * @date:2021/3/25
 * @Date 2021-3-12
 **/
@ApiModel("布局设置数据信息")
public class ThemeInfo {
    @ApiModelProperty(value = "主题唯一标识", example = "xxxxx")
    private String id;
    @ApiModelProperty(value = "主题色", example = "#545c64")
    private String color;
    @ApiModelProperty(value = "是否启用多页签模式", example = "t为true，f为false")
    private boolean multipleTabs;
    @ApiModelProperty(value = "用户名", example = "xxxx")
    private String user;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean getMultipleTabs() {
        return multipleTabs;
    }

    public void setMultipleTabs(boolean multipleTabs) {
        this.multipleTabs = multipleTabs;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setNull2Default() {
        if (color == null) {
            color = "red";
        }
    }

}
