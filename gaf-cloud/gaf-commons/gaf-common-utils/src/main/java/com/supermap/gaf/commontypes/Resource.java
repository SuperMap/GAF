/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.commontypes;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Set;

/**
 * @ClassName: Resource
 * @Description: 资源实体类
 * @author: wangyan
 * @date: 2019年7月11日 下午4:05:02
 * @Copyright:
 */
@ApiModel(description = "资源实体类")
public class Resource {
    public Resource() {
    }

    /**
     * 唯一标识
     */
    @ApiModelProperty(value = "资源的唯一标识")
    private String id;
    /**
     * 资源名称
     */
    @ApiModelProperty(value = "资源名称")
    private String name;
    /**
     * 标题
     */
    @ApiModelProperty(value = "资源标题")
    private String caption;
    /**
     * 资源地址
     */
    @ApiModelProperty(value = "资源地址")
    private String url;
    /**
     * 资源类型
     */
    @ApiModelProperty(value = "资源类型")
    private String type;
    /**
     * 菜单分组
     */
    @ApiModelProperty(value = "菜单分组")
    private String groupType;
    /**
     * 对资源可操作的范围
     */
    Set<String> scopes;
    /**
     * 图标路径
     */
    @ApiModelProperty(value = "图标路径")
    private String iconUrl;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 菜单的层级路径
     */
    private String menuPath;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public Set<String> getScopes() {
        return scopes;
    }

    public void setScopes(Set<String> scopes) {
        this.scopes = scopes;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getMenuPath() {
        return menuPath;
    }

    public void setMenuPath(String menuPath) {
        this.menuPath = menuPath;
    }
}
