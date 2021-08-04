/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.data.mgt.entity;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * @author h_sha
 * @version 1.0
 * @date:2021/3/25
 * @created 20-4月-2019 7:25:47
 */
@Data
@Builder
@ApiModel("地图模板")
public class MapTemplate {
    @ApiModelProperty("地图模板id")
    public String id;
    @ApiModelProperty("地图模板名称")
    public String name;
    @ApiModelProperty("地图模板类型")
    public Type type;
    @ApiModelProperty("地图模板描述")
    public String description;
    /**
     * 顺序号
     */
    @ApiModelProperty("地图模板顺序号")
    public int order;
    /**
     * 内容
     */
    @ApiModelProperty("地图模板内容")
    public String content;

    public MapTemplate() {

    }

    public MapTemplate(String id, String name, Type type, String description, int order, String content) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.description = description;
        this.order = order;
        this.content = content;
    }

    @Override
    public void finalize() throws Throwable {

    }

    public enum Type {
        /**
         * 矢量单面
         **/
        VECTOR_SINGLE,
        /**
         * 矢量多面
         **/
        VECTOR_MULTIPLE,
        /**
         * 影像
         **/
        IMAGE,
        /**
         * 自定义
         **/
        CUSTOM
    }

}
