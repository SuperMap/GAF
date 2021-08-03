/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.sys.mgt.commontype;

import com.alibaba.fastjson.annotation.JSONField;
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
 * 字典
 *
 * @author wangxiaolong
 * @date 2021-01-08
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("字典")
public class SysDict implements Serializable {
    @NotNull
    @ApiModelProperty("字典id")
    private String dataDictId;
    @ApiModelProperty(value = "编码。用于存储字典类别编码")
    private String dictCode;
    @NotNull
    @ApiModelProperty("名称。当字典类别名或者字典数据名")
    private String dictName;
    @ApiModelProperty("值。用于存储字典值")
    private String dictValue;
    @ApiModelProperty("类别")
    private String dictClass;
    @ApiModelProperty("序号")
    private Integer seq;
    @ApiModelProperty(value = "父级id。等于0时表示字典类别（DictType类），否则表示字典数据（DictData类）")
    private String pid;
    @ApiModelProperty("描述")
    private String dictDesc;
    /**
     * 默认值1：true
     */
    @ApiModelProperty("状态。逻辑删除字段")
    @JSONField(name = "isStatus")
    private Boolean status;
    @ApiModelProperty("创建时间")
    private Date createdTime;
    @ApiModelProperty("创建人")
    private String createdBy;
    @ApiModelProperty("修改时间")
    private Date updatedTime;
    @ApiModelProperty("修改人")
    private String updatedBy;
    @ApiModelProperty("租户id")
    private String tenantId;
    @ApiModelProperty("可见性")
    private Boolean visibility;
    @ApiModelProperty("字典目录id")
    private String catalogId;
    @ApiModelProperty("扩展属性。自定义属性，json格式")
    private String extProperties;
}
