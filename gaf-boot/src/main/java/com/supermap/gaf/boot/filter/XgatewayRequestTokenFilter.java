package com.supermap.gaf.boot.filter;

import com.supermap.gaf.authentication.entity.entity.AuthenticationResult;
import com.supermap.gaf.gateway.commontypes.ExchangeAuthenticationAttribute;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static com.supermap.gaf.gateway.commontypes.constant.GatewayConst.BEARER;
import static com.supermap.gaf.gateway.commontypes.constant.GatewayConst.EXCHANGE_AUTHENTICATION_ATTRIBUTE_NAME;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

/**
 * 该代码对应gaf-microservice-gateway中的同名的filter,功能逻辑等应该保持一致
 *
 * 此拦截器主要在获取到认证信息后，将认证信息token添加到请求头中
 * @author wxl
 * @date 2021/4/17
 */
public class XgatewayRequestTokenFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        ExchangeAuthenticationAttribute attribute = (ExchangeAuthenticationAttribute) httpServletRequest.getAttribute(EXCHANGE_AUTHENTICATION_ATTRIBUTE_NAME);
        AuthenticationResult authenticationResult = attribute.getAuthenticationResult();
        if (null != authenticationResult && !StringUtils.isEmpty(authenticationResult.getJwtToken())){
            String token = authenticationResult.getJwtToken();
            token = removeTokenBeareHead(token);
            HeaderRequestWrapper requestWrapper = new HeaderRequestWrapper(httpServletRequest);
            requestWrapper.putHeader(AUTHORIZATION, BEARER + " " + token);
            requestWrapper.putHeader("SourceHost", httpServletRequest.getHeader("Host"));
            chain.doFilter(requestWrapper,response);
        }else {
            chain.doFilter(httpServletRequest,response);
        }
    }

    private String removeTokenBeareHead(String token){
        if (token != null && token.startsWith(BEARER)){
            token = token.substring(BEARER.length()).trim();
        }
        return token;
    }

    @Override
    public void destroy() {

    }
}
