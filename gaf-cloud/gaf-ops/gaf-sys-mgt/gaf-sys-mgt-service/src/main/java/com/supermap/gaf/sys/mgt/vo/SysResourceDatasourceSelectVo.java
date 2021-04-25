/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.sys.mgt.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.ws.rs.QueryParam;
import java.util.Date;
import java.util.List;

/**
 * 数据源 条件查询实体
 *
 * @date:2021/3/25
 * @author wangxiaolong
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("数据源 条件查询实体")
public class SysResourceDatasourceSelectVo {
	@QueryParam("startTimeStamp")
	@ApiParam("开始时间戳")
	@ApiModelProperty("开始时间戳")
	private Long startTimeStamp;
	@QueryParam("endTimeStamp")
	@ApiParam("结束时间戳")
	@ApiModelProperty("结束时间戳")
	private Long endTimeStamp;
	@QueryParam("isSdx")
	@ApiParam("是否是空间数据源")
	@ApiModelProperty("是否是空间数据源")
	private Boolean isSdx;
	@QueryParam("catalogCode")
	@ApiParam("数据源分类")
	@ApiModelProperty("数据源分类")
	private String catalogCode;
	@QueryParam("typeCodes")
	@ApiParam("数据源类型集合")
	@ApiModelProperty("数据源类型集合")
	private List<String> typeCodes;
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

	public List<String> getTypeCodes() {
		return typeCodes;
	}

	public void setTypeCodes(List<String> typeCodes) {
		this.typeCodes = typeCodes;
	}


	public Long getStartTimeStamp() {
		return startTimeStamp;
	}

	public void setStartTimeStamp(Long startTimeStamp) {
		this.startTimeStamp = startTimeStamp;
	}

	public Long getEndTimeStamp() {
		return endTimeStamp;
	}

	public void setEndTimeStamp(Long endTimeStamp) {
		this.endTimeStamp = endTimeStamp;
	}

	public Boolean getSdx() {
		return isSdx;
	}

	public void setSdx(Boolean sdx) {
		isSdx = sdx;
	}

	public String getCatalogCode() {
		return catalogCode;
	}

	public void setCatalogCode(String catalogCode) {
		this.catalogCode = catalogCode;
	}

	public String getSearchFieldName() {
		return searchFieldName;
	}

	public void setSearchFieldName(String searchFieldName) {
		this.searchFieldName = searchFieldName;
	}

	public String getSearchFieldValue() {
		return searchFieldValue;
	}

	public void setSearchFieldValue(String searchFieldValue) {
		this.searchFieldValue = searchFieldValue;
	}

	public String getOrderFieldName() {
		return orderFieldName;
	}

	public void setOrderFieldName(String orderFieldName) {
		this.orderFieldName = orderFieldName;
	}

	public String getOrderMethod() {
		return orderMethod;
	}

	public void setOrderMethod(String orderMethod) {
		this.orderMethod = orderMethod;
	}

	/**
	 * 计算属性 将endTimeStamp时间戳转为Date
	 *
	 * @return
	 */
	public Date getEndTime() {
		return endTimeStamp == null ? null : new Date(this.endTimeStamp);
	}

	/**
	 * 计算属性 将startTimeStamp时间戳转为Date
	 *
	 * @return
	 */
	public Date getStartTime() {
		return startTimeStamp == null ? null : new Date(this.startTimeStamp);
	}
}
