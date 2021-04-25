/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.shiro.spring;

import com.supermap.gaf.shiro.SecurityUtilsExt;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;


/**
 * @author:yj
 * @date:2021/3/25
*/
public class JWTTokenClientFeignInterceptor implements RequestInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(JWTTokenClientFeignInterceptor.class);

    private static final String AUTHORIZATION_HEADER = "Authorization";


    @Override
    public void apply(RequestTemplate template) {
        String accessToken = SecurityUtilsExt.getJWTToken();
        if (StringUtils.isNoneEmpty(accessToken)) {
            template.header(AUTHORIZATION_HEADER, "Bearer " + accessToken);
        } else {
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (requestAttributes!=null){
                HttpServletRequest request = requestAttributes.getRequest();
                String authorization = request.getHeader(AUTHORIZATION_HEADER);
                template.header(AUTHORIZATION_HEADER, authorization);
            }
        }


    }
}
