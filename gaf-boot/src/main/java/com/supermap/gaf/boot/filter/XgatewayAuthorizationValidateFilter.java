package com.supermap.gaf.boot.filter;

import com.supermap.gaf.authentication.client.ValidateClient;
import com.supermap.gaf.authentication.entity.entity.AuthenticationResult;
import com.supermap.gaf.authentication.entity.entity.AuthorizationParam;
import com.supermap.gaf.authority.client.AuthUserClient;
import com.supermap.gaf.authority.enums.ResourceApiMethodEnum;
import com.supermap.gaf.boot.util.ResponseUtils;
import com.supermap.gaf.gateway.commontypes.ExchangeAuthenticationAttribute;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

import static com.supermap.gaf.gateway.commontypes.constant.GatewayConst.EXCHANGE_AUTHENTICATION_ATTRIBUTE_NAME;

/**
 * 注意： 该代码对应gaf-microservice-gateway中的同名的filter,功能逻辑等应该保持一致
 * @author wxl
 * @since 2021/7/7
 */
public class XgatewayAuthorizationValidateFilter implements Filter {

    private static final String STORAGE_FILTER_URL_REGIX = "^/storage/api/tenant[^/]*/.*";
    private static final String STORAGE_PERMISSION_HEADER = "PERMISSION";
    private static final String STORAGE_TENANTID_HEADER = "TENANT_ID";

    private ValidateClient validateClient;

    private AuthUserClient authUserClient;

    public XgatewayAuthorizationValidateFilter(ValidateClient validateClient,AuthUserClient authUserClient) {
        this.validateClient = validateClient;
        this.authUserClient = authUserClient;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        ExchangeAuthenticationAttribute attribute = ((ExchangeAuthenticationAttribute) request.getAttribute(EXCHANGE_AUTHENTICATION_ATTRIBUTE_NAME));
        if (attribute.getIsPublicUrl() || attribute.getIsIndexUrl() || attribute.getIsProfileUrl()){
            chain.doFilter(request, response);
        }else{
            AuthenticationResult authenticationResult = attribute.getAuthenticationResult();
            AuthorizationParam authorizationParam = new AuthorizationParam();
            authorizationParam.setUsername(authenticationResult.getUsername());
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            authorizationParam.setUri(httpServletRequest.getRequestURI());
            authorizationParam.setMethod(ResourceApiMethodEnum.valueOf(httpServletRequest.getMethod()).getValue());
            // 文件权限
            if (authorizationParam.getUri().startsWith("/storage/")) {
                Map<String,Object> infos = authUserClient.someInfo(authenticationResult.getUsername()).getData();
                String roleIds = (String) infos.get(AuthUserClient.SOME_INFO_ROLE_IDS_KEY);
                String tenantId = (String) infos.get(AuthUserClient.SOME_INFO_TENANT_ID_KEY);
                HeaderRequestWrapper requestWrapper = new HeaderRequestWrapper(httpServletRequest);
                requestWrapper.putHeader(STORAGE_TENANTID_HEADER, tenantId);
                if(authorizationParam.getUri().matches(STORAGE_FILTER_URL_REGIX)){
                    if(!StringUtils.isEmpty(roleIds)){
                        requestWrapper.putHeader(STORAGE_PERMISSION_HEADER,roleIds);
                    }
                    chain.doFilter(requestWrapper, response);
                }
                request = requestWrapper;
            }
            boolean apiAuthzEnabled = attribute.getGatewaySecurityProperties().isApiAuthzEnable();
            if (!apiAuthzEnabled) {
                chain.doFilter(request, response);
            } else {
                Boolean result = validateClient.authorization(authorizationParam);
                if (Boolean.FALSE.equals(result)) {
                    ResponseUtils.unAuth((HttpServletResponse) response, "API资源访问权限不足");
                } else {
                    chain.doFilter(request, response);
                }
            }
        }
    }

}
