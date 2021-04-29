/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.webgis.entity;

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
import java.io.Serializable;
import java.util.Date;

/**
 * 图层
 * @author wangxiaolong 
 * @date 2020-12-05
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("图层")
public class WebgisCatalogLayer implements Serializable{
    @Id
    @NotNull
    @ApiModelProperty("图层目录id")
    private String catalogLayerId;
    @NotNull
    @ConfigName("resourceName")
    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("所属目录")
    @ParentIdField
    @ConfigName("pid")
    private String layerCatalogId;
    @ConfigName("resourceId")
    @ApiModelProperty("GIS服务")
    private String gisServiceId;

    @ApiModelProperty("服务名称")
    private String serviceName;
    @ApiModelProperty("服务英文名称")
    private String serviceNameEn;
    @ApiModelProperty("服务访问地址")
    @ConfigName("resourceLocation")
    private String address;
    @ApiModelProperty("服务类型")
    @ConfigName({"resourceTag","sourceType"})
    private String typeCode;
    @ApiModelProperty(value = "扩展属性", notes = "自定义属性,json,数据只加一层而不加多层，接口读出转为json时平铺放到服务属性中去")
    @ConfigName(value = "moreProperties",expand = true)
    private String moreProperties;

    @SortSnField
    @ConfigName("sortSn")
    @ApiModelProperty("排序序号")
    private Integer sortSn;
    @ApiModelProperty("初始加载")
    @ConfigName("initLoad")
    @JSONField(name="isInitLoad")
    private Boolean initLoad;
    @ApiModelProperty("描述")
    private String description;
    @LogicDeleteField
    @ApiModelProperty("状态")
    @JSONField(name="isStatus")
    private Boolean status;
    @ApiModelProperty("创建时间")
    private Date createdTime;
    @ApiModelProperty("创建人")
    private String createdBy;
    @UpdatedTimeField
    @ApiModelProperty("修改时间")
    private Date updatedTime;
    @ApiModelProperty("修改人")
    private String updatedBy;


    @ApiModelProperty(value = "有效性",notes = "计算属性，无效表示关联的webgis服务已被删除，有效表是关联的webgis服务")
    public boolean isValid() {
        return serviceName != null && !"".equals(serviceName);
    }

    //    @ApiModelProperty("图层服务")
//    private WebgisService webgisService;
}
