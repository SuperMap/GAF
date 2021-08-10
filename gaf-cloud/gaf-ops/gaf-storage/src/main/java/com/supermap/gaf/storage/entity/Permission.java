package com.supermap.gaf.storage.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author zrc
 * @date yyyy-mm-dd
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("")
public class Permission implements Serializable {
    private static final long serialVersionUID = 1L;
    @NotNull
    @ApiModelProperty("资源。如xx/xx.jpg, xx/")
    private String resource;
    @NotNull
    @ApiModelProperty("范围。download,upload,share,delete")
    private String scope;
    @NotNull
    @ApiModelProperty("权限id")
    private String id;
    @NotNull
    @ApiModelProperty("所有者")
    private String ower;
    /**
     * 默认值1：''::character varying
     */
    @ApiModelProperty("存储配置name")
    private String configName;
    @NotNull
    @ApiModelProperty("父级资源")
    private String parentResource;

}