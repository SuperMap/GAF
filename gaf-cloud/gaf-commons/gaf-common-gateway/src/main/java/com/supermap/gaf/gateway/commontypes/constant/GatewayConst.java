package com.supermap.gaf.gateway.commontypes.constant;

/**
 * @program: gaf-commons-modules
 * @description:常量类
 * @author: lidong
 * @create: 2019/07/31
 */
public class GatewayConst {
    /**
     * 存入redis的key
     */
    public static final String GATEWAY_ROUTES = "gateway:routes";
    /**
     * 默认的标识：可为租户id可为其他
     */
    public static final String DEAFULT_KEY = "common";



    public static final String BEARER = "Bearer";


    public static final int GATEWAY_AUTHENTICATION_QUERY_FILTER_ORDER = -180;
    public static final int GATEWAY_AUTHENTICATION_VALIDATE_FILTER_ORDER = -170;
    public static final int GATEWAY_AUTHORIZATION_VALIDATE_FILTER_ORDER = -165;
    public static final int GATEWAY_REQUEST_TOKEN_FILTER_ORDER = -160;
    public static final int GATEWAY_TRACE_FILTER_ORDER = -150;


    public static final String CUSTOM_LOGIN_SESSION_NAME = "CUSTOM_SESSION_ID";

    public static final String EXCHANGE_AUTHENTICATION_ATTRIBUTE_NAME = "ExchangeAuthenticationAttr";

    public static final String SEPARATOR = "/";
}
