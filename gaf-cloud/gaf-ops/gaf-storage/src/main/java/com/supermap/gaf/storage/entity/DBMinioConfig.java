package com.supermap.gaf.storage.entity;

import com.supermap.gaf.common.storage.entity.MinioConfig;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DBMinioConfig extends MinioConfig {
    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("根父级空间id")
    private String rootId;
}
