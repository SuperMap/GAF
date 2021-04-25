/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.authentication.utils;

import com.supermap.gaf.authentication.entity.enums.ThirdPartyLoginTypeEumn;
import com.supermap.gaf.authentication.entity.entity.properties.LoginOidcClientProperties;
import com.supermap.gaf.authentication.entity.entity.properties.LoginProperties;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * @author : duke
 * @date:2021/3/25
 * @since 2020/11/16 10:57 AM
 */
@Component
@EnableConfigurationProperties({LoginOidcClientProperties.class, LoginProperties.class})
public class ThirdPartyLoginTypePropertiesHolder implements InitializingBean {

    @Autowired
    private LoginProperties loginProperties;
    @Autowired
    private LoginOidcClientProperties loginOidcClientProperties;


    private String enabledThirdParty;

    private Map<String,Object> thirdPartyContext;

    public String getEnabledThirdParty() {
        return enabledThirdParty;
    }

    public Map<String, Object> getThirdPartyContext() {
        return thirdPartyContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.enabledThirdParty = loginProperties.getEnabledThirdParty();
        this.thirdPartyContext = new HashMap<>(16);
        //组装所有注册的第三方登录信息
        //oidc
        Map<String, LoginOidcClientProperties.OidcClientInfo> oidcClientInfoMap = loginOidcClientProperties.getOidc();
        Set<String> oidcKeySet = oidcClientInfoMap.keySet();
        for (String key : oidcKeySet){
            String newKey = ThirdPartyLoginTypeEumn.OIDC.name() + ":" + key;
            thirdPartyContext.put(newKey.toLowerCase(), oidcClientInfoMap.get(key));
        }

        if (StringUtils.isEmpty(this.enabledThirdParty)){
            return;
        }
        Object clientInfo = thirdPartyContext.get(this.enabledThirdParty.toLowerCase());
        if (null == clientInfo){
            throw new IllegalStateException("third-party dot not exist info :" + this.enabledThirdParty);
        }
    }
}
