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

import javax.validation.constraints.NotNull;


/**
 * @author:yj
 * @date:2021/3/25
*/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("数据源前端选择器可选项")
public class DatasourceOptionVo {
    @ApiModelProperty(value = "选项值",required=true)
    private Object value;
    @ApiModelProperty(value = "选项显示值",required = true)
    private String label;
    @ApiModelProperty(value = "选项是否禁用")
    private Boolean disabled;
    @ApiModelProperty(value = "选项关键字", notes = "Vue使用的")
    private Object key;
    @ApiModelProperty(value = "标题")
    private String title;

    @NotNull
    @ApiModelProperty("数据源id")
    private String datasourceId;
    @NotNull
    @ApiModelProperty("数据源名称")
    private String dsName;
    @ApiModelProperty("类型")
    private String type;
    @NotNull
    @ApiModelProperty("地址")
    private String addr;
    @ApiModelProperty("数据库名称")
    private String dbName;
    @ApiModelProperty("用户名")
    private String userName;
    @ApiModelProperty("密码")
    private String password;
    @ApiModelProperty(value = "数据源类型。选自数据字典", example = "udb",
            allowableValues = "1.文件型-1:udb,2:udbx…\n2.数据库型-1.PostgreSQL,2.MySQL,3.Hbase,4.MongoDB…")
    private String typeCode;
    @ApiModelProperty("是否空间数据库。true:是，false:否")
    private Boolean isSdx = false;
}
