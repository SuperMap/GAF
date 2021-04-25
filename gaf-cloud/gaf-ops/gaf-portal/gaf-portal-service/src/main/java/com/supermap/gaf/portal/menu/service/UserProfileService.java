/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.portal.menu.service;

import java.util.Map;

/**
* @author:yw
 * @date:2021/3/25
* @Date 2021-3-12
**/
public interface UserProfileService {
    /**
     * 查看用户简要信息
     * @return
     */
    String queryUserProfile();

    /**
     * 查看用户个人完整信息
     * @return
     */
    Map<String, Object> getLoginUserInfo();
}
