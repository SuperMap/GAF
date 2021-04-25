/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.sys.mgt.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.ws.rs.QueryParam;
import java.util.Date;

/**
* 字典条件查询实体
 * @date:2021/3/25
* @author wangxiaolong
*/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("字典条件查询实体")
public class SysDictSelectVo  {
	@QueryParam("searchFieldName")
	@ApiModelProperty("查询字段名")
    private String searchFieldName;
	@QueryParam("searchFieldValue")
	@ApiModelProperty("查询字段值")
    private String searchFieldValue;
	@QueryParam("orderFieldName")
	@ApiModelProperty("排序字段名")
    private String orderFieldName;
	@QueryParam("orderMethod")
	@ApiModelProperty("排序方法")
    private String orderMethod;

	@QueryParam("dataDictId")
	@ApiModelProperty("字典id")
	private String dataDictId;
	@QueryParam("dictCode")
	@ApiModelProperty("编码")
	private String dictCode;
	@QueryParam("dictName")
	@ApiModelProperty("名称")
	private String dictName;
	@QueryParam("dictValue")
	@ApiModelProperty("值")
	private String dictValue;
	@QueryParam("seq")
	@ApiModelProperty("序号")
	private Integer seq;
	@QueryParam("pid")
	@ApiModelProperty("父级id")
	private String pid;
	@QueryParam("dictDesc")
	@ApiModelProperty("描述")
	private String dictDesc;
	@QueryParam("createdTime")
	@ApiModelProperty("创建时间")
	private Date createdTime;
	@QueryParam("createdBy")
	@ApiModelProperty("创建人")
	private String createdBy;
	@QueryParam("updatedTime")
	@ApiModelProperty("修改时间")
	private Date updatedTime;
	@QueryParam("updatedBy")
	@ApiModelProperty("修改人")
	private String updatedBy;
	@QueryParam("tenantId")
	@ApiModelProperty("租户id")
	private String tenantId;
	@QueryParam("visibility")
	@ApiModelProperty("可见性")
	private Boolean visibility;
	@QueryParam("catalogId")
	@ApiModelProperty("字典目录")
	private String catalogId;
	@QueryParam("extProperties")
	@ApiModelProperty("扩展属性。自定义属性，json格式")
	private String extProperties;

}
