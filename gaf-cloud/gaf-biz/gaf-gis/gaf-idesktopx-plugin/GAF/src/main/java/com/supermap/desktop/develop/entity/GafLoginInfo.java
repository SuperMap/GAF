/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.desktop.develop.entity;

import com.alibaba.fastjson.JSONObject;
import com.supermap.desktop.core.Interface.IHistoryNode;
import com.supermap.desktop.core.utilties.PasswordUtilities;

/**
 * @author heykb
 * @date:2021/3/25
 */
public class GafLoginInfo implements IHistoryNode {
    private String gafServer;
    private String username;
    private String password;

    @Override
    public String toFileString() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("gafServer", this.gafServer);
        jsonObject.put("userName", this.username);
        jsonObject.put("passWord", PasswordUtilities.encode(this.password));
        return jsonObject.toString();
    }

    @Override
    public String getViewText() {
        return null;
    }
}
