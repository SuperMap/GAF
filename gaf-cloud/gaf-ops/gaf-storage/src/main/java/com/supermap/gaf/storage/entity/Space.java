package com.supermap.gaf.storage.entity;

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
 * 
 * @author zrc 
 * @date yyyy-mm-dd
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("")
public class Space implements Serializable{
    @NotNull
    @ApiModelProperty("P, T，U")
    private String targetType;
    @ApiModelProperty("平台或者应用或者用户")
    private String target;
    @NotNull
    @ApiModelProperty("物理名称（目录或者bucketName)")
    private String storageName;
    @ApiModelProperty("逻辑空间id")
    private String id;
    @ApiModelProperty("空间名称")
    private String name;
    @NotNull
    @ApiModelProperty("父空间。s3server id或者space id")
    private String parentSpaceId;
    @NotNull
    @ApiModelProperty("B,D")
    private String createdType;
    @ApiModelProperty("描述")
    private String description;
    /**
    * 默认值1：'-1'::integer
    */
    @ApiModelProperty("空间大小")
    private Long totalSize;

    /**
     * 默认值1：now()
     */
    @ApiModelProperty("创建时间。生成时间不可变更")
    private Date createdTime;
    /**
     * 默认值1：now()
     */
    @ApiModelProperty("修改时间。修改时更新")
    private Date updatedTime;
}