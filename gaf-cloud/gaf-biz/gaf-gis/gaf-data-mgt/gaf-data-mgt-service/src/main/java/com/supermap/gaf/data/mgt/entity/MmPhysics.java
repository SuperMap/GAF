package com.supermap.gaf.data.mgt.entity;

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
 * 物理
 * @author wxl 
 * @date yyyy-mm-dd
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("物理")
public class MmPhysics implements Serializable{
    @NotNull
    @ApiModelProperty("主键")
    private String physicsId;
    @ApiModelProperty("物理表名")
    private String physicsName;
    @NotNull
    @ApiModelProperty("逻辑表id")
    private String tableId;
    @NotNull
    @ApiModelProperty("数据库地址")
    private String server;
    @ApiModelProperty("数据库名")
    private String dbName;
    @ApiModelProperty("用户名")
    private String username;
    @ApiModelProperty("密码")
    private String password;
    @ApiModelProperty("数据源类型")
    private String type;
    /**
    * 默认值1：now()
    */
    @ApiModelProperty("创建时间")
    private Date createdTime;
    @ApiModelProperty("创建人")
    private String createdBy;
    @ApiModelProperty("更新时间")
    private Date updatedTime;
    @ApiModelProperty("更新人")
    private String updatedBy;
}