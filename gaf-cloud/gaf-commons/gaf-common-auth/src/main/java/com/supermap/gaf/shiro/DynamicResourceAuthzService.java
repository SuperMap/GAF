/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.shiro;

import java.util.List;


/**
 * @author:yj
 * @date:2021/3/25
*/
public interface DynamicResourceAuthzService{

    String supportedType();
    
    boolean hasDynamicPermission(String dymicPermissonId, String userName, List<String> roles, List<String> groups); 
}
