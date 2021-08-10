/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.shiro.config;

import java.util.List;

import com.supermap.gaf.shiro.commontypes.ShiroUser;


/**
 * @author:yj
 * @date:2021/3/25
 */
public class DevConfig {

    private boolean enable = true;

    private List<ShiroUser> users;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public List<ShiroUser> getUsers() {
        return users;
    }

    public void setUsers(List<ShiroUser> users) {
        this.users = users;
    }
}
