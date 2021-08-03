/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.authentication.dao;

import org.springframework.stereotype.Component;


/**
 * @author : duke
 * @date:2021/3/25
 */
@Component
public interface UserLoginMapper {

    /**
     * 登陆时，更新数据库用户最后登陆时间
     *
     * @return
     */
    void updateLoginTime(String username);
}
