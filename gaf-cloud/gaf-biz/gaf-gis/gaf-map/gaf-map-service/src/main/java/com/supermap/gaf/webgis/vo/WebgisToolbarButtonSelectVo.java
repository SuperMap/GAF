/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.webgis.vo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.*;
import javax.validation.constraints.*;
import com.alibaba.fastjson.annotation.JSONField;

import javax.ws.rs.QueryParam;

/**
* 工具条按钮 条件查询实体
 * @date:2021/3/25
* @author zhurongcheng
*/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("工具条按钮 条件查询实体")
public class WebgisToolbarButtonSelectVo {
	@ApiModelProperty("工具条按钮id。主键,uuid")
	@QueryParam("toolbarButtonId")
	private String toolbarButtonId;
	@ApiModelProperty("工具条。主键,uuid")
	@QueryParam("toolbarId")
	private String toolbarId;
	@ApiModelProperty("按钮。")
	@QueryParam("buttonId")
	private String buttonId;
	@ApiModelProperty("图标。若不为空则覆盖按钮的icon属性")
	@QueryParam("icon")
	private String icon;
	@ApiModelProperty("参数。params: [ 参数1 ,  参数2 ], 为方法时可以传递的参数")
	@QueryParam("params")
	private String params;
	@ApiModelProperty("其它方法。actions: [ func1 ,  func2 ],当加载组件或者执行方法时要执行的其他工具类方法")
	@QueryParam("actions")
	private String actions;
	@ApiModelProperty("二次单击关闭。toggle: true false 启用则单次激活功能并高亮图标，二次单击关闭功能并取消高亮")
	@JSONField(name="isToggle")
	@QueryParam("toggle")
	private Boolean toggle;
	@ApiModelProperty("关闭其他面板。closePanel: true false 打开时是否关闭其它打开的面板")
	@JSONField(name="isCloseOtherPanel")
	@QueryParam("closeOtherPanel")
	private Boolean closeOtherPanel;
	@ApiModelProperty("更多属性。自定义属性，json")
	@QueryParam("moreProperties")
	private String moreProperties;
	@ApiModelProperty("排序序号。同工具条中的序号")
	@QueryParam("sortSn")
	private Integer sortSn;
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
	@ApiModelProperty("子工具条。关联webgis_toolbar的一个工具条id.不为空则为一个子工具条，不能是该按钮所属的工具条")
	@QueryParam("subToolbarId")
	private String subToolbarId;
	@QueryParam("equalFieldName")
	@ApiModelProperty("等值查询字段名 可查null")
	private String equalFieldName;
	@QueryParam("equalFieldValue")
	@ApiModelProperty("等值查询值 可查null")
	private String equalFieldValue;
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
}
