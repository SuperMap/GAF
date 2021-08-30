package com.supermap.gaf.data.mgt.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("元数据")
public class MetadataVo {
    private String recordName;
    // postgre udb
    private String catalogCode;
    private String recordType;
    private String datasourceId;
    private String datasourceName;
    private String owner;
    private Object spatialInfo;

    @ApiModelProperty("创建时间。生成时间不可变更")
    private Date createdTime;
    @ApiModelProperty("创建人。创建人user_id")
    private String createdBy;
    @ApiModelProperty("修改时间。修改时更新")
    private Date updatedTime;
    @ApiModelProperty("修改人。修改人user_id")
    private String updatedBy;

}
