/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.shiro;

import io.buji.pac4j.engine.ShiroCallbackLogic;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.pac4j.core.context.J2EContext;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.exception.HttpAction;

import javax.servlet.http.HttpServletRequest;


/**
 * @author:yj
 * @date:2021/3/25
 */
public class RedirectShiroCallbackLogic<R, C extends WebContext> extends ShiroCallbackLogic<R, C> {

    @Override
    protected HttpAction redirectToOriginallyRequestedUrl(C context, String defaultUrl) {
        if (context instanceof J2EContext) {
            HttpServletRequest request = ((J2EContext) context).getRequest();
            SavedRequest originRequest = WebUtils.getSavedRequest(request);
            if (originRequest != null && StringUtils.isNotEmpty(originRequest.getRequestUrl())) {
                String preUrl = originRequest.getRequestUrl();
                if (preUrl.indexOf("login") == -1 || preUrl.indexOf("keycloak") == -1 || preUrl.indexOf("cas") == -1) {
                    return HttpAction.redirect(context, preUrl);
                }
            }
        }
        if (StringUtils.isEmpty(defaultUrl)) {
            return super.redirectToOriginallyRequestedUrl(context, defaultUrl);
        }
        return HttpAction.redirect(context, defaultUrl);
    }
}
