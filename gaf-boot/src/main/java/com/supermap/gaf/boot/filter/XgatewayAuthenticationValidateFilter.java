package com.supermap.gaf.boot.filter;

import com.supermap.gaf.authentication.entity.entity.AuthenticationResult;
import com.supermap.gaf.boot.util.ResponseUtils;
import com.supermap.gaf.gateway.commontypes.ExchangeAuthenticationAttribute;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.supermap.gaf.gateway.commontypes.constant.GatewayConst.CUSTOM_LOGIN_SESSION_NAME;
import static com.supermap.gaf.gateway.commontypes.constant.GatewayConst.EXCHANGE_AUTHENTICATION_ATTRIBUTE_NAME;

/**
 *  该代码对应gaf-microservice-gateway中的同名的filter,功能逻辑等应该保持一致
 *
 *   此过滤器提供用户验证认证信息的逻辑
 *  验证认证信息
 *       1.1.静态资源和公共资源不用验证(index页面必须验证)
 *        1.2.其他都需要验证
 *           1.2.1验证失败需要清除cookie
 *           1.2.2验证失败,返回401
 * @author wxl
 * @date 2021/4/17
 */
public class XgatewayAuthenticationValidateFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        ExchangeAuthenticationAttribute attribute = ((ExchangeAuthenticationAttribute) request.getAttribute(EXCHANGE_AUTHENTICATION_ATTRIBUTE_NAME));
        AuthenticationResult authenticationResult = attribute.getAuthenticationResult();
        if (attribute.getIsPublicUrl()){
            chain.doFilter(request,response);
        }else if (authenticationResult == null
                || StringUtils.isEmpty(authenticationResult.getUsername())
                || StringUtils.isEmpty(authenticationResult.getJwtToken())){
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            removeCookie(httpServletResponse);
            ResponseUtils.unAuth((HttpServletResponse) response,"未获取到资源访问的认证身份");
        }else {
            chain.doFilter(request,response);
        }
    }

    /**
     * 清楚cookie
     * @param httpServletResponse
     */
    private void removeCookie(HttpServletResponse httpServletResponse){
        Cookie cookie = new Cookie(CUSTOM_LOGIN_SESSION_NAME,null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        httpServletResponse.addCookie(cookie);
    }

    @Override
    public void destroy() {

    }
}