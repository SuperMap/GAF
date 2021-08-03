package com.supermap.gaf.authority.service.impl;

import com.supermap.gaf.authority.commontype.*;
import com.supermap.gaf.constant.CommonConstant;
import com.supermap.gaf.shiro.JJWTUtils;
import com.supermap.gaf.shiro.commontypes.CustomToken;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;


/**
 * @author : duke
 * @since 2021/5/19 9:22 AM
 */
@Service
public class AuthUsernameJwtImpl implements IauthUsername {

    @Override
    public String getAuthUsername(Object o) {
        if (o instanceof CustomToken) {
            CustomToken token = (CustomToken) o;
            HttpServletRequest request = token.getRequest();
            String authHeader = request.getHeader(CommonConstant.JWT_HEADER);
            if (StringUtils.isNoneEmpty(authHeader)) {
                String jwtToken = authHeader.trim();
                if (StringUtils.isNotEmpty(CommonConstant.JWT_PREFIX)) {
                    String[] split = authHeader.trim().split("\\s+");
                    if (split != null && split.length == 2 && split[0].equalsIgnoreCase(CommonConstant.JWT_PREFIX)) {
                        jwtToken = split[1];
                    }
                }
                return JJWTUtils.getUserNameFromJwsUntrusted(jwtToken, CommonConstant.JWT_USER_NAME_FIELD);
            }
        }
        return null;
    }
}
