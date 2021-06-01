/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.authentication.entity.entity;


import lombok.Data;

import java.io.Serializable;

/**
 * @author : duke
 * @date:2021/3/25
 * @since 2020/11/23 11:01 AM
 */
@Data
public class AuthorizationParam implements Serializable {
    private static final long serialVersionUID = -1L;

    private String username;
    private String uri;
    private String method;


}
