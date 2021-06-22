/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.webgis.vo;

import com.supermap.gaf.validator.StringRange;
import com.supermap.gaf.webgis.entity.WebgisToolbar;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.ws.rs.QueryParam;
import java.util.Date;

/**
* 工具条 条件查询实体
 * @date:2021/3/25
* @author zhurongcheng
*/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("工具条 条件查询实体")
public class WebgisToolbarSelectVo {

	@QueryParam("toolbarId")
	@ApiModelProperty("工具条id。主键,uuid")
	private String toolbarId;
	@QueryParam("name")
	@ApiModelProperty("名称。")
	private String name;
	@ApiModelProperty("位置。1：顶平，2：左纵上，3：左纵上,4：底平，5：右纵上，6.右纵下（字典表？）")
	@QueryParam("toolbarLocation")
	private String toolbarLocation;
	@ApiModelProperty("描述。")
	@QueryParam("description")
	private String description;
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
	@ApiModelProperty("类别。1:基础类,2:业务类(工具条只能选用同种类型按钮)")
	@QueryParam("type")
	private String type;
	@QueryParam("equalFieldName")
	@ApiModelProperty("等值查询字段名 可查null")
	private String equalFieldName;
	@QueryParam("equalFieldValue")
	@ApiModelProperty("等值查询值 可查null")
	private String equalFieldValue;
	@QueryParam("searchFieldName")
	@ApiModelProperty("查询字段名")
	@StringRange(entityClass = WebgisToolbar.class,message = "不在指定的字段名范围内")
    private String searchFieldName;
	@QueryParam("searchFieldValue")
	@ApiModelProperty("查询字段值")
	private String searchFieldValue;
	@QueryParam("orderFieldName")
	@ApiModelProperty("排序字段名")
    @StringRange(entityClass = WebgisToolbar.class,message = "不在指定的字段名范围内")
    private String orderFieldName;
	@QueryParam("orderMethod")
	@ApiModelProperty("排序方法")
    @StringRange(value = {"asc","desc"},message = "不在指定的范围[asc,desc]内")
    private String orderMethod;
}
