package com.supermap.gaf.storage.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.ws.rs.QueryParam;
import java.util.Date;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("条件查询实体")
public class SpaceConfigSelectVo{
    private static final long serialVersionUID = 1L;
    private String id;
    @QueryParam("serviceEndpoint")
    private String serviceEndpoint;
    @QueryParam("accessKey")
    private String accessKey;
    @QueryParam("secretKey")
    private String secretKey;
    @QueryParam("bucketName")
    private String bucketName;
    @QueryParam("name")
    private String name;
    @QueryParam("totalSize")
    private Long totalSize;
    @QueryParam("target")
    private String target;
    @QueryParam("targetType")
    @ApiModelProperty("P, T，U")
    private String targetType;
    @QueryParam("description")
    private String description;
    @QueryParam("parentSpaceId")
    @ApiModelProperty("父空间。s3server id或者space id")
    private String parentSpaceId;
    @QueryParam("spaceType")
    @ApiModelProperty("B,D")
    private String createdType;
    /**
     * 默认值1：now()
     */
    @ApiModelProperty("创建时间。生成时间不可变更")
    @QueryParam("updatedTime")
    private Date createdTime;
    /**
     * 默认值1：now()
     */
    @ApiModelProperty("修改时间。修改时更新")
    @QueryParam("updatedTime")
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

//    private String storageName;
//    private String id;
}
