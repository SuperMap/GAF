package com.supermap.gaf.boot.filter;

import com.supermap.gaf.authentication.client.ValidateClient;
import com.supermap.gaf.authentication.entity.entity.AuthenticationParam;
import com.supermap.gaf.authentication.entity.entity.AuthenticationResult;
import com.supermap.gaf.commontypes.MessageResult;
import com.supermap.gaf.gateway.commontypes.ExchangeAuthenticationAttribute;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static com.supermap.gaf.gateway.commontypes.constant.GatewayConst.CUSTOM_LOGIN_SESSION_NAME;
import static com.supermap.gaf.gateway.commontypes.constant.GatewayConst.EXCHANGE_AUTHENTICATION_ATTRIBUTE_NAME;
import static javax.ws.rs.core.HttpHeaders.AUTHORIZATION;

/**
 * 注意： 该代码对应gaf-microservice-gateway中的同名的filter,功能逻辑等应该保持一致
 * <p>
 * 此过滤器提供用户获取认证信息的逻辑
 * 1.获取认证信息
 * 1.1.index首页请求必须获取
 * 1.2.静态资源和公共资源不用获取
 *
 * @author : wxl
 * @date 2021/4/17
 */
public class XgatewayAuthenticationQueryFilter implements Filter {

    private ValidateClient validateClient;

    public XgatewayAuthenticationQueryFilter() {
    }

    public XgatewayAuthenticationQueryFilter(ValidateClient validateClient) {
        this.validateClient = validateClient;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        ExchangeAuthenticationAttribute attribute = ((ExchangeAuthenticationAttribute) servletRequest.getAttribute(EXCHANGE_AUTHENTICATION_ATTRIBUTE_NAME));
        AuthenticationParam authenticationParam = null;
        if (attribute.getIsIndexUrl()) {
            authenticationParam = getAuthenticationParamByHttpServletRequest(((HttpServletRequest) servletRequest));
        }
        if (authenticationParam == null && !attribute.getIsPublicUrl()) {
            authenticationParam = getAuthenticationParamByHttpServletRequest(((HttpServletRequest) servletRequest));
        }

        if (authenticationParam != null) {
            MessageResult<AuthenticationResult> authenticationResultMessageResult = validateClient.authentication(authenticationParam);
            if (authenticationResultMessageResult != null) {
                attribute.setAuthenticationResult(authenticationResultMessageResult.getData());
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    /**
     * request中获取认证参数
     *
     * @param request
     * @return
     */
    public static AuthenticationParam getAuthenticationParamByHttpServletRequest(HttpServletRequest request) {
        String authorization = request.getHeader(AUTHORIZATION);
        Cookie[] cookies = request.getCookies();
        String cookieVal = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (CUSTOM_LOGIN_SESSION_NAME.equals(cookie.getName())) {
                    cookieVal = cookie.getValue();
                }
            }
        }
        AuthenticationParam param = null;
        if (!StringUtils.isEmpty(cookieVal) || !StringUtils.isEmpty(authorization)) {
            param = new AuthenticationParam();
            param.setCustomSessionId(cookieVal);
            param.setAuthorization(authorization);
        }
        return param;
    }

    @Override
    public void destroy() {

    }
}
