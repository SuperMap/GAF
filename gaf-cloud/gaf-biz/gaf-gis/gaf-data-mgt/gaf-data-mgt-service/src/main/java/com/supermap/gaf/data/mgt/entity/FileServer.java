/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.data.mgt.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author:yw
 * @date:2021/3/25
 * @Date 2021-3-12
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("文件型数据源连接信息")
public class FileServer {
    @ApiModelProperty(value = "文件地址", example = "/xx/xx")
    private String filePath;
    @ApiModelProperty(value = "用户名")
    private String username;
    @ApiModelProperty(value = "密码")
    private String password;

}
