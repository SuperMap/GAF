package com.supermap.gaf.storage.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.ws.rs.QueryParam;

/**
 * 条件查询实体
 *
 * @author zrc
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("条件查询实体")
public class StoragePermissionSelectVo {
    @QueryParam("searchFieldName")
    @ApiModelProperty("模糊查询字段名")
    private String searchFieldName;
    @QueryParam("searchFieldValue")
    @ApiModelProperty("模糊查询字段值")
    private String searchFieldValue;
    @QueryParam("equalFieldName")
    @ApiModelProperty("等值查询字段名")
    private String equalFieldName;
    @QueryParam("equalFieldValue")
    @ApiModelProperty("等值查询字段值")
    private String equalFieldValue;
    @QueryParam("orderFieldName")
    @ApiModelProperty("排序字段名")
    private String orderFieldName;
    @QueryParam("orderMethod")
    @ApiModelProperty("排序方法")
    private String orderMethod;
    @QueryParam("resource")
    @ApiModelProperty("资源。如xx/xx.jpg, xx/")
    private String resource;
    @QueryParam("scope")
    @ApiModelProperty("范围。download,upload,share,delete")
    private String scope;
    @QueryParam("id")
    @ApiModelProperty("权限id")
    private String id;
    @QueryParam("ower")
    @ApiModelProperty("所有者")
    private String ower;
    @QueryParam("configName")
    @ApiModelProperty("存储配置name")
    private String configName;
    @QueryParam("parentResource")
    @ApiModelProperty("父级资源")
    private String parentResource;
}