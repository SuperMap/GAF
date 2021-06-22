/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.data.mgt.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.supermap.gaf.data.mgt.entity.DataWorkspace;
import com.supermap.gaf.validator.StringRange;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.ws.rs.QueryParam;
import java.util.Date;

/**
 * 工作空间 条件查询实体
 *
 * @author zrc
 * @date:2021/3/25
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("工作空间 条件查询实体")
public class DataWorkspaceSelectVo {
    @ApiModelProperty("工作空间id。主键,uuid")
    @QueryParam("workspaceId")
    private String workspaceId;
    @ApiModelProperty("名称。")
    @QueryParam("wsName")
    private String wsName;
    @ApiModelProperty("类型。选自数据字典:工作空间类型，存各级code斜级分隔(level1code/level2code...)： 1.文件型-1:smwu,2:sxwx… 2.数据库型-1.PostgreSQL,2.MySQL,3.SQLPlus,4.MongoDB,5.PostGIS,6.OraclePlus 驱动信息(driver)存于字典的扩展字段")
    @QueryParam("typeCode")
    private String typeCode;
    @ApiModelProperty("服务器名称。第1类文件路径+文件名，第2类服务器ip+端口或域名")
    @QueryParam("server")
    private String server;
    @ApiModelProperty("数据库名称。")
    @QueryParam("database")
    private String database;
    @ApiModelProperty("用户名。")
    @QueryParam("userName")
    private String userName;
    @ApiModelProperty("密码。暂不保存（加密可解密）")
    @QueryParam("password")
    private String password;
    @ApiModelProperty("描述。")
    @QueryParam("description")
    private String description;
    @ApiModelProperty("状态。true:有效，false:无效")
    @JSONField(name = "isStatus")
    private Boolean status;
    @ApiModelProperty("所属租户。所属租户id。空则是公共的")
    @QueryParam("tenantId")
    private String tenantId;
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
    @ApiModelProperty("是否已发布。true:已发布，false:未发布")
    @JSONField(name = "isPublished")
    @QueryParam("published")
    private Boolean published;
    @QueryParam("equalFieldName")
    @ApiModelProperty("等值查询字段名 可查null")
    private String equalFieldName;
    @QueryParam("equalFieldValue")
    @ApiModelProperty("等值查询值 可查null")
    private String equalFieldValue;
    @QueryParam("searchFieldName")
    @ApiModelProperty("查询字段名")
    @StringRange(entityClass = DataWorkspace.class, message = "不在指定的字段名范围内")
    private String searchFieldName;
    @QueryParam("searchFieldValue")
    @ApiModelProperty("查询字段值")
    private String searchFieldValue;
    @QueryParam("orderFieldName")
    @ApiModelProperty("排序字段名")
    @StringRange(entityClass = DataWorkspace.class, message = "不在指定的字段名范围内")
    private String orderFieldName;
    @QueryParam("orderMethod")
    @ApiModelProperty("排序方法")
    @StringRange(value = {"asc", "desc"}, message = "不在指定的范围[asc,desc]内")
    private String orderMethod;
}
