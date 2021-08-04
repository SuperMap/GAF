package com.supermap.gaf.storage.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.ws.rs.QueryParam;
import java.util.Date;

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
public class SpaceSelectVo {
    @QueryParam("targetType")
    @ApiModelProperty("P, T，U")
    private String targetType;
    @QueryParam("target")
    @ApiModelProperty("平台或者应用或者用户")
    private String target;
    @QueryParam("storageName")
    @ApiModelProperty("物理名称（目录或者bucketName)")
    private String storageName;
    @QueryParam("id")
    @ApiModelProperty("逻辑空间id")
    private String id;
    private String name;
    @QueryParam("parentSpaceId")
    @ApiModelProperty("父空间。s3server id或者space id")
    private String parentSpaceId;
    @QueryParam("spaceType")
    @ApiModelProperty("B,D")
    private String createdType;
    @QueryParam("description")
    @ApiModelProperty("描述")
    private String description;
    @QueryParam("totalSize")
    @ApiModelProperty("空间大小")
    private Long totalSize;
    @QueryParam("createdTime")
    @ApiModelProperty("创建时间。生成时间不可变更")
    private Date createdTime;
    @QueryParam("updatedTime")
    @ApiModelProperty("修改时间。修改时更新")
    private Date updatedTime;
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
}