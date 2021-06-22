/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.webgis.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.supermap.gaf.validator.StringRange;
import com.supermap.gaf.webgis.entity.WebgisService;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.ws.rs.QueryParam;
import java.util.Date;

/**
* GIS服务 条件查询实体
 * @date:2021/3/25
* @author wangxiaolong
*/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("GIS服务 条件查询实体")
public class WebgisServiceSelectVo {
	@ApiModelProperty("GIS服务id。主键,uuid")
	@QueryParam("gisServiceId")
	private String gisServiceId;
	@ApiModelProperty("名称。中文名称")
	@QueryParam("name")
	private String name;
	@ApiModelProperty("英文名称。")
	@QueryParam("nameEn")
	private String nameEn;
	@ApiModelProperty("类别。选自数据字典：服务类别，存各级code斜级分隔(level1code/level2code...)")
	@QueryParam("typeCode")
	private String typeCode;
	@ApiModelProperty("API资源id。服务名称、地址从api资源表中获取(name,route_url)，资源表中建立根目录webgis子目录为此类别，资源表中类型为第三方")
	@QueryParam("resourceApiId")
	private String resourceApiId;
	@ApiModelProperty("地址。resource_api_id为空则服务地址存于此字段")
	@QueryParam("address")
	private String address;
	@ApiModelProperty("时态。年月日,yyyyMMdd")
	@QueryParam("timeAttribute")
	private Date timeAttribute;
	@ApiModelProperty("扩展属性。自定义属性,json数据格式，接口读出转为json时平铺放到服务属性中去")
	@QueryParam("moreProperties")
	private String moreProperties;
	@ApiModelProperty("描述。")
	@QueryParam("description")
	private String description;
	@ApiModelProperty("状态。true:有效，false:停用")
	@JSONField(name="isStatus")
	private Boolean status;
	@ApiModelProperty("创建时间。生成时间不可变更")
	@QueryParam("createdTime")
	private Date createdTime;
	@ApiModelProperty("创建人。创建人user_id")
	@QueryParam("createdBy")
	private String createdBy;
	@ApiModelProperty("修改时间。修改时更新")
	@QueryParam("updatedTime")
	private Date updatedTime;
	@ApiModelProperty("修改人。修改人user_id")
	@QueryParam("updatedBy")
	private String updatedBy;
	@ApiModelProperty("天地图服务类型。")
	@QueryParam("tiandituServiceType")
	private String tiandituServiceType;
	@ApiModelProperty("行政区划。选自数据字典：行政区划，存各级code斜级分隔(level1code/level2code...)")
	@QueryParam("regionCode")
	private String regionCode;
	@ApiModelProperty("显示属性。json:{[...]}二维地图服务，设置最小以及最大显示级别,[{minimumTerrainLevel:5,maximumTerrainLevel:7}];三维地图服务，设置最小以及最大显示相机高度,[{name: 现状建筑@三维转换成果 ，visibleDistanceMin:0,visibleDistanceMax:2000}...]")
	@QueryParam("displayAttrs")
	private String displayAttrs;
	@ApiModelProperty("选自数据字典：服务接口类型，存各级code斜级分隔(level1code/level2code...)。可有多个接口类型编码，英文逗号分隔")
	@QueryParam("intfTypeCode")
	private String intfTypeCode;
	@QueryParam("equalFieldName")
	@ApiModelProperty("等值查询字段名 可查null")
	private String equalFieldName;
	@QueryParam("equalFieldValue")
	@ApiModelProperty("等值查询值 可查null")
	private String equalFieldValue;
	@QueryParam("searchFieldName")
	@ApiModelProperty("查询字段名")
	@StringRange(entityClass = WebgisService.class,message = "不在指定的字段名范围内")
    private String searchFieldName;
	@QueryParam("searchFieldValue")
	@ApiModelProperty("查询字段值")
	private String searchFieldValue;
	@QueryParam("orderFieldName")
	@ApiModelProperty("排序字段名")
	@StringRange(entityClass = WebgisService.class,message = "不在指定的字段名范围内")
    private String orderFieldName;
	@QueryParam("orderMethod")
	@ApiModelProperty("排序方法")
	@StringRange(value = {"asc","desc"},message = "不在指定的范围[asc,desc]内")
    private String orderMethod;
}
