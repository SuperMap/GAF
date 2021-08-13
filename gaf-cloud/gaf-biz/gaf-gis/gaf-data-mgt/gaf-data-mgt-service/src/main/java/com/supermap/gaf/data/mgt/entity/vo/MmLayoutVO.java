package com.supermap.gaf.data.mgt.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 模型画布接口响应数据
 * @author dqc
 * @date yyyy-mm-dd
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("模型画布数据VO")
public class MmLayoutVO implements Serializable{

    @ApiModelProperty("节点数据")
    private List<MmTableVO> nodes;
    @ApiModelProperty("链接数据")
    private List<MmLinksVO> links;

}