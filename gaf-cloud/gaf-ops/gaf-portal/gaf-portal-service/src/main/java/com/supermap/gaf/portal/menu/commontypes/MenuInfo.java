/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.portal.menu.commontypes;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
* @author:yw
 * @date:2021/3/25
* @Date 2021-3-12
**/
@ApiModel("菜单数据对象")
@EqualsAndHashCode
public class MenuInfo {
    @ApiModelProperty(value = "菜单唯一标识",notes = "菜单唯一标识")
    private String id;
    @ApiModelProperty(value = "父菜单标识"  ,notes = "可以为空")
    private String pid;
    @ApiModelProperty(value = "菜单资源路径"  )
    private String path;
    @ApiModelProperty(value = "菜单名称"  )
    private String name;
    @ApiModelProperty(value = "菜单打开方式"  )
    private String target;
    @ApiModelProperty(value = "菜单图标")
    private String icon;
    @ApiModelProperty(value = "在父菜单下的索引位置"  )
    private Integer order;
    @ApiModelProperty(value = "是否可见"  )
    private boolean visible = true;
    @ApiModelProperty(value = "是否外链"  )
    private boolean isEmbed = false;
    @ApiModelProperty(value = "外链地址"  )
    private String embedUrl;
    @ApiModelProperty(value = "子菜单"  )
    private List<MenuInfo> children= new ArrayList<>();
    @ApiModelProperty(value = ""  )
    /** eg: ppid.pid.id... select...like flag */
    private String flag;

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public boolean getEmbed() {
        return isEmbed;
    }

    public void setEmbed(boolean embed) {
        isEmbed = embed;
    }

    public String getEmbedUrl() {
        return embedUrl;
    }

    public void setEmbedUrl(String embedUrl) {
        this.embedUrl = embedUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public boolean getVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public List<MenuInfo> getChildren() { // 菜单列表没有维护在内存，只能通过数据库查询子菜单列表
        return children;
    }

    public void setChildren(List<MenuInfo> children) {
        this.children = children;
    }

    public void setNull2Default(){
        if (pid == null) {
            pid = "";
        }
        if (path == null) {
            path = "";
        }
        if (name == null) {
            name = "";
        }
        if (target == null) {
            target = "";
        }
        if (icon == null) {
            icon = "";
        }
        if (order == null) {
            order = 0;
        }
        if (embedUrl == null) {
            embedUrl = "";
        }
        if (flag == null) {
            flag = "";
        }
    }


}
