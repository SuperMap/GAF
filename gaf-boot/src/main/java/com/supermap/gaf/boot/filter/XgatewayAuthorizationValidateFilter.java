package com.supermap.gaf.boot.filter;

import com.supermap.gaf.authentication.client.ValidateClient;
import com.supermap.gaf.authentication.entity.entity.AuthenticationResult;
import com.supermap.gaf.authentication.entity.entity.AuthorizationParam;
import com.supermap.gaf.authority.client.AuthUserClient;
import com.supermap.gaf.authority.commontype.AuthRole;
import com.supermap.gaf.authority.enums.ResourceApiMethodEnum;
import com.supermap.gaf.boot.util.ResponseUtils;
import com.supermap.gaf.gateway.commontypes.ExchangeAuthenticationAttribute;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static com.supermap.gaf.gateway.commontypes.constant.GatewayConst.EXCHANGE_AUTHENTICATION_ATTRIBUTE_NAME;

/**
 * 注意： 该代码对应gaf-microservice-gateway中的同名的filter,功能逻辑等应该保持一致
 * @author wxl
 * @since 2021/7/7
 */
public class XgatewayAuthorizationValidateFilter implements Filter {

    private static final String STORAGE_FILTER_URL_REGIX = "^/storage/api/tenant[^/]*/.*";
    private static final String STORAGE_PERMISSION_HEADER = "PERMISSION";

    private ValidateClient validateClient;

    private AuthUserClient authUserClient;

    public XgatewayAuthorizationValidateFilter(ValidateClient validateClient,AuthUserClient authUserClient) {
        this.validateClient = validateClient;
        this.authUserClient = authUserClient;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        ExchangeAuthenticationAttribute attribute = ((ExchangeAuthenticationAttribute) request.getAttribute(EXCHANGE_AUTHENTICATION_ATTRIBUTE_NAME));
        boolean apiAuthzEnabled = attribute.getGatewaySecurityProperties().isApiAuthzEnable();
        if (attribute.getIsPublicUrl() || !apiAuthzEnabled) {
            chain.doFilter(request, response);
        }
        AuthenticationResult authenticationResult = attribute.getAuthenticationResult();
        AuthorizationParam authorizationParam = new AuthorizationParam();
        authorizationParam.setUsername(authenticationResult.getUsername());
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        authorizationParam.setUri(httpServletRequest.getRequestURI());
        authorizationParam.setMethod(ResourceApiMethodEnum.valueOf(httpServletRequest.getMethod()).getValue());

        // 文件权限
        if (authorizationParam.getUri().matches(STORAGE_FILTER_URL_REGIX)) {
            List<AuthRole> authRoles = authUserClient.selectUserRoles(authenticationResult.getUsername()).getData();
            HeaderRequestWrapper requestWrapper = new HeaderRequestWrapper(httpServletRequest);
            requestWrapper.putHeader(STORAGE_PERMISSION_HEADER, authRoles.stream().map(item -> item.getRoleId()).collect(Collectors.joining(",")));
            chain.doFilter(requestWrapper, response);
        }

        Boolean result = validateClient.authorization(authorizationParam);
        if (Boolean.FALSE.equals(result)) {
            ResponseUtils.unAuth((HttpServletResponse) response, "API资源访问权限不足");
        } 
        chain.doFilter(request, response);
    }

}
