/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.sys.mgt.commontype;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 数据源
 *
 * @author wangxiaolong
 * @date:2021/3/25
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("数据源")
public class SysResourceDatasource implements Serializable {
    @NotNull
    @ApiModelProperty("数据源id")
    private String datasourceId;
    @NotNull
    @ApiModelProperty("数据源名称")
    private String dsName;
    @ApiModelProperty("类型。暂时无用。已被typeCode取代")
    private String type;

    @ApiModelProperty("排序序号。暂时无用")
    private Integer sortSn;
    @ApiModelProperty("地址。数据库类型格式应该是ip(或域名):端口,文件类型应该是文件路径")
    private String addr;
    @ApiModelProperty("端口。暂时无用。")
    private Integer port;
    @ApiModelProperty("数据库名称")
    private String dbName;
    @ApiModelProperty("用户名")
    private String userName;
    @ApiModelProperty("密码")
    private String password;
    @ApiModelProperty("描述")
    private String description;
    @ApiModelProperty("状态。逻辑删除字段")
    private Boolean status;
    @ApiModelProperty("创建时间")
    private Date createdTime;
    @ApiModelProperty("创建人")
    private String createdBy;
    @ApiModelProperty("修改时间")
    private Date updatedTime;
    @ApiModelProperty("修改人")
    private String updatedBy;
    @ApiModelProperty("所属租户")
    private String tenantId;
    @ApiModelProperty(value = "数据源类型。选自数据字典", example = "udb",
            allowableValues = "1.文件型-1:udb,2:udbx…\n2.数据库型-1.PostgreSQL,2.MySQL,3.Hbase,4.MongoDB…")
    private String typeCode;
    @ApiModelProperty(value = "数据源分类。选自数据字典:数据源分类", example = "GuiHua1")
    private String catalogCode;
    @ApiModelProperty(value = "行政区划。选自数据字典,存各级code斜级分隔(level1code/level2code...)", example = "level1code/level2code")
    private String regionCode;
    @ApiModelProperty("时态。年月日,yyyyMMdd")
    private Date timeAttribute;
    @ApiModelProperty("是否空间数据库。true:是，false:否")
    private Boolean isSdx = false;

}
