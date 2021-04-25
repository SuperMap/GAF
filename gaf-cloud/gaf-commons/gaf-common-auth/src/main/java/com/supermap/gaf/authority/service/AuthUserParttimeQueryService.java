/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.authority.service;

import com.supermap.gaf.authority.commontype.AuthUserParttime;

import java.util.List;

/**
 * @author dqc
 * @date:2021/3/25
 * 用户挂职服务类
 */
public interface AuthUserParttimeQueryService {

	List<AuthUserParttime> listByUserId(String userId);

}
