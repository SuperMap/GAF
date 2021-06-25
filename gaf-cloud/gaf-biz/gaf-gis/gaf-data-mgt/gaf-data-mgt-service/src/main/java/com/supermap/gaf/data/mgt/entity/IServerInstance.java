package com.supermap.gaf.data.mgt.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author : yd
 * @date : 2021-06-25
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("iserver服务实例")
public class IServerInstance implements Serializable {
    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("服务类型")
    private String type;
    @ApiModelProperty("服务地址")
    private String url;
    @ApiModelProperty("状态")
    private String status;
    @ApiModelProperty("是否可用")
    private boolean enabled;
}
