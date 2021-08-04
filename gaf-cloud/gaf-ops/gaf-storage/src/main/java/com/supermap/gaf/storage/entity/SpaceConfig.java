package com.supermap.gaf.storage.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("")
@Data
public class SpaceConfig {
    private static final long serialVersionUID = 1L;

    private String serviceEndpoint;

    private String accessKey;

    private String secretKey;

    private String bucketName;

    private String name;

    private String id;

    private Long totalSize;

    private String description;
    @ApiModelProperty("创建时间。生成时间不可变更")
    private Date createdTime;
    /**
     * 默认值1：now()
     */
    @ApiModelProperty("修改时间。修改时更新")
    private Date updatedTime;
}
