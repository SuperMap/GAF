package com.supermap.gaf.data.mgt.entity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 服务来源关联表
 * @author zrc 
 * @date yyyy-mm-dd
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("服务来源关联表")
public class ServiceSource implements Serializable{
    @NotNull
    @ApiModelProperty("服务来源关联记录id")
    private String serviceSourceId;
    @NotNull
    @ApiModelProperty("来源id")
    private String sourceId;
    @NotNull
    @ApiModelProperty("服务id")
    private String serviceId;
    @NotNull
    @ApiModelProperty("来源类型。1:工作空间；2:瓦片")
    private Integer sourceType;
}