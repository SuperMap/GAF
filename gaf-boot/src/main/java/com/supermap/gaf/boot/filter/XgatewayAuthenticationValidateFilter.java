package com.supermap.gaf.boot.filter;

import com.supermap.gaf.authentication.entity.entity.AuthenticationResult;
import com.supermap.gaf.gateway.commontypes.ExchangeAuthenticationAttribute;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.supermap.gaf.gateway.commontypes.constant.GatewayConst.CUSTOM_LOGIN_SESSION_NAME;
import static com.supermap.gaf.gateway.commontypes.constant.GatewayConst.EXCHANGE_AUTHENTICATION_ATTRIBUTE_NAME;

/**
 *  该代码对应gaf-microservice-gateway中的同名的filter,功能逻辑等应该保持一致
 *
 *   此过滤器提供用户验证认证信息的逻辑
 *  验证认证信息
 *       1.1.静态资源和公共资源不用验证
 *        1.2.其他都需要验证
 *           1.2.1验证失败需要清除cookie
 *           1.2.2验证失败如果是index首页，跳转index首页
 *           1.2.3验证失败如果不是index首页，跳转到登录页
 * @author wxl
 * @date 2021/4/17
 */
public class XgatewayAuthenticationValidateFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        ExchangeAuthenticationAttribute attribute = ((ExchangeAuthenticationAttribute) httpServletRequest.getAttribute(EXCHANGE_AUTHENTICATION_ATTRIBUTE_NAME));
        AuthenticationResult authenticationResult = attribute.getAuthenticationResult();
        if (attribute.getIsPublicUrl() && !attribute.getIsIndexUrl()){
            chain.doFilter(httpServletRequest,httpServletResponse);
        }else if (authenticationResult == null
                || StringUtils.isEmpty(authenticationResult.getUsername())
                || StringUtils.isEmpty(authenticationResult.getJwtToken())){
            Cookie cookie = new Cookie(CUSTOM_LOGIN_SESSION_NAME, null);
            cookie.setPath("/");
            cookie.setMaxAge(0);
            httpServletResponse.addCookie(cookie);
            if (attribute.getIsIndexUrl()){
                chain.doFilter(httpServletRequest,httpServletResponse);
            }else {
                httpServletResponse.setStatus(HttpStatus.FOUND.value());
                httpServletResponse.sendRedirect(attribute.getGatewaySecurityProperties().getCenterLoginUrl());
            }
        }else {
            chain.doFilter(httpServletRequest,httpServletResponse);
        }
    }

    @Override
    public void destroy() {

    }
}
