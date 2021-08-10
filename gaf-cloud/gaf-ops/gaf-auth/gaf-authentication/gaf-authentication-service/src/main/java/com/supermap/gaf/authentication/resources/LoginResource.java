/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.authentication.resources;

import com.supermap.gaf.authentication.entity.entity.po.ThirdPartyAssociation;
import com.supermap.gaf.authentication.entity.entity.properties.LoginOidcClientProperties;
import com.supermap.gaf.authentication.service.CustomLoginService;
import com.supermap.gaf.authentication.service.ThirdPartyAssociationService;
import com.supermap.gaf.authentication.utils.HttpRequestUtils;
import com.supermap.gaf.authentication.utils.JjwtUtils;
import com.supermap.gaf.authentication.utils.ThirdPartyLoginTypePropertiesHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;

import java.util.Map;
import java.util.UUID;

import static com.supermap.gaf.authentication.entity.constant.LoginConstant.*;
import static com.supermap.gaf.authentication.entity.constant.LoginConstant.CUSTOM_LOGIN_SESSION_NAME;
import static com.supermap.gaf.authentication.entity.constant.LoginConstant.LOGIN_SUCCESS_REDIRECT;

/**
 * @author dqc
 * @date:2021/3/25 /authentication/login
 */
@Path("/")
@Api("登录接口")
@Component
public class LoginResource {

    @Autowired
    private ThirdPartyLoginTypePropertiesHolder thirdPartyLoginTypePropertiesHolder;
    @Autowired
    private ThirdPartyAssociationService thirdPartyAssociationService;
    @Autowired
    private CustomLoginService customLoginService;


    @ApiOperation(value = "跳转登录页", notes = "如果无Session和Jwt信息,跳转到登录页面，使用账号密码登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "straight", value = "如果为true直接跳转到登录页面", paramType = "query", dataType = "boolean", example = "true")
    })
    @GET
    @Path("/username_password")
    public void loginWithOauth2(@Context HttpServletRequest request,
                                @Context HttpServletResponse response,
                                @QueryParam("straight") @DefaultValue("false") boolean straight) throws Exception {
        if (!straight && HttpRequestUtils.containsJwtOrSession(request)) {
            response.sendRedirect(LOGIN_WITH_JWT_SEESION);
            return;
        }
        response.sendRedirect(LOGIN_URL);
    }


    @ApiOperation(value = "单点登录统一跳转页面", notes = "预留的客户端如果有多种登录模式，提供此接口自动跳转到配置的登录模式")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "third_party_manually", value = "手动指定第三方登录模式", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "straight", value = "如果为true直接跳转", paramType = "query", dataType = "boolean", example = "true")
    })
    @GET
    @Path("/center")
    public void loginCenter(@Context HttpServletRequest request,
                            @Context HttpServletResponse response,
                            @QueryParam("third_party_manually") String thirdPartyManually,
                            @QueryParam("straight") @DefaultValue("false") boolean straight) throws Exception {
        if (!straight && HttpRequestUtils.containsJwtOrSession(request)) {
            response.sendRedirect(LOGIN_WITH_JWT_SEESION);
            return;
        }

        boolean enableThird = false;

        boolean enableThirdManually = thirdPartyManually != null && thirdPartyLoginTypePropertiesHolder.getThirdPartyContext().get(thirdPartyManually.toLowerCase()) != null;
        String enabledThirdParty = thirdPartyLoginTypePropertiesHolder.getEnabledThirdParty();
        if (null != enabledThirdParty) {
            enableThird = true;
        }
        if (enableThirdManually) {
            enableThird = true;
            enabledThirdParty = thirdPartyManually;
        }

        if (enableThird) {
            Object clientInfo = thirdPartyLoginTypePropertiesHolder.getThirdPartyContext().get(enabledThirdParty.toLowerCase());
            if (clientInfo instanceof LoginOidcClientProperties.OidcClientInfo) {
                LoginOidcClientProperties.OidcClientInfo oidcClientInfo = (LoginOidcClientProperties.OidcClientInfo) clientInfo;

                LoginOidcClientProperties.Provider provider = oidcClientInfo.getProvider();
                LoginOidcClientProperties.Registration registration = oidcClientInfo.getRegistration();

                UriComponents uriComponents =
                        UriComponentsBuilder
                                .fromHttpUrl(provider.getAuthorizationUri())
                                .query("client_id=" + registration.getClientId())
                                .query("scope=" + LoginOidcClientProperties.Registration.getScopeString(registration.getScope()))
                                .query("response_type=" + "code")
                                .query("state=" + enabledThirdParty)
                                .query("redirect_uri=" + registration.getRedirectUri())
                                .encode()
                                .build();
                response.sendRedirect(uriComponents.toString());
                return;
            }
        }
        response.sendRedirect(LOGIN_WITH_USERNAME_PASSWORD);
    }


    /**
     * oidc回调接口
     *
     * @param response
     * @param sessionState
     * @param code
     * @param state
     * @throws Exception
     */
    @ApiOperation(value = "第三方oidc登录模式回调地址", notes = "第三方oidc登录模式回调地址")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "session_state", value = "session_state", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "code", value = "code", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "state", value = "state", paramType = "query", dataType = "string")
    })
    @GET
    @Path("/callback/oidc")
    public void oidcCallback(@Context HttpServletResponse response,
                             @QueryParam("session_state") String sessionState,
                             @QueryParam("code") String code,
                             @QueryParam("state") String state) throws Exception {
        Map<String, String> tokenRes = customLoginService.getThirdPartyTokenByCode(code, state);
        String idToken = tokenRes.get("id_token");
        if (StringUtils.isEmpty(idToken)) {
            throw new IllegalStateException("【login_center: oidc id_token error】");
        }

        LoginOidcClientProperties.OidcClientInfo oidcClientInfo = (LoginOidcClientProperties.OidcClientInfo) thirdPartyLoginTypePropertiesHolder.getThirdPartyContext().get(state.toLowerCase());
        LoginOidcClientProperties.Provider provider = oidcClientInfo.getProvider();
        String oidcUserName = JjwtUtils.getUserNameFromJwsUntrusted(idToken, provider.getUserNameAttribute());

        //查数据库，通过目前的登录方式筛选，thirdpartyusername是否有对应username
        ThirdPartyAssociation thirdPartyAssociation = thirdPartyAssociationService.getByEnabledThirdPartyAndThirdPartyUserName(state, oidcUserName);
        //如果有，则生成token为key和session为key，value 为 session 、username、token、thirdtoken的结构到redis。并生成session-key返回前端
        //如果没有，跳转到登录界面，并带上回掉地址参数
        String sessionId = UUID.randomUUID().toString().replace("-", "");
        if (null != thirdPartyAssociation) {
            String username = thirdPartyAssociation.getUsername();
            OAuth2AccessToken oAuth2AccessToken = customLoginService.createOauth2AccessTokenWithoutPassword(username);
            customLoginService.storeLoginSession(sessionId, username, oAuth2AccessToken, tokenRes, state, oidcUserName);
            Cookie cookie = new Cookie(CUSTOM_LOGIN_SESSION_NAME, sessionId);
            cookie.setPath("/");
            response.addCookie(cookie);
            response.sendRedirect(LOGIN_SUCCESS_REDIRECT);
        } else {
            customLoginService.storeLoginSession(sessionId, null, null, tokenRes, state, oidcUserName);
            response.sendRedirect(String.format(LOGIN_WITH_USERNAME_PASSWORD + "?CUSTOM_SESSION_ID=%s", sessionId));
        }
    }

    /**
     * oidc验证登录状态接口
     *
     * @param response
     * @param username
     * @param customSessionId
     * @throws Exception
     */
    @ApiOperation(value = "第三方oidc登录校验", notes = "第三方oidc登录校验")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "CUSTOM_SESSION_ID", value = "认证系统自定义的Session", paramType = "query", dataType = "string")
    })
    @GET
    @Path("/callback/oidc/validated")
    public void oidcCallbackValidated(@Context HttpServletResponse response,
                                      @QueryParam("username") String username,
                                      @QueryParam("CUSTOM_SESSION_ID") String customSessionId) throws Exception {
        OAuth2AccessToken oAuth2AccessToken = customLoginService.createOauth2AccessTokenWithoutPassword(username);
        customLoginService.storeLoginSession(customSessionId, username, oAuth2AccessToken, null, null, null);

        Map<String, Object> sessionInfoMap = customLoginService.getLoginSessionById(customSessionId);
        ThirdPartyAssociation thirdPartyAssociation = new ThirdPartyAssociation();
        thirdPartyAssociation.setEnabledThirdParty(sessionInfoMap.get("enabledThirdParty").toString());
        thirdPartyAssociation.setThirdPartyUsername(sessionInfoMap.get("thirdPartyUsername").toString());
        thirdPartyAssociation.setUsername(username);
        thirdPartyAssociationService.addThirdPartyAssociation(thirdPartyAssociation);

        Cookie cookie = new Cookie(CUSTOM_LOGIN_SESSION_NAME, customSessionId);
        cookie.setPath("/");
        response.addCookie(cookie);
        response.sendRedirect(LOGIN_SUCCESS_REDIRECT);
    }


}
