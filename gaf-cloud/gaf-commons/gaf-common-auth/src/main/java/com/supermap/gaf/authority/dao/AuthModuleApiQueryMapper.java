/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.authority.dao;

import com.supermap.gaf.authority.commontype.AuthModuleApi;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @date:2021/3/25
 * @author dqc
 */
@Component
public interface AuthModuleApiQueryMapper {

    /**
     * @author dqc
     **/
    List<AuthModuleApi> getByModuleId(@Param("moduleId") String moduleId, @Param("status") Boolean status);

}
