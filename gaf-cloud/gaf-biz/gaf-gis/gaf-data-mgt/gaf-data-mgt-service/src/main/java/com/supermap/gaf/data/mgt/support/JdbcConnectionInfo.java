/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.data.mgt.support;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * jdbc连接信息
 * @author wxl
 * @since 2021/7/22
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class JdbcConnectionInfo {
    private String driverClassName;
    private String url;
    private String username;
    private String password;
}
