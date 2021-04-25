/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.authority.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


/**
 *  角色菜单关联对象
 * @date:2021/3/25
 * @author zhm
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(" 角色菜单关联对象")
public class RoleMenuVo {
    @ApiModelProperty("角色id")
    private String roleId;

    @ApiModelProperty("菜单id列表")
    private List<String> menuList;
}
