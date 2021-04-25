package com.supermap.gaf.srv.governance.constant;

/**
 * @author : duke
 * @since 2020/11/20 2:32 PM
 */
public class SrvConstant {

    public static final String FLUENT_LOG_PREFIX = "fluentd*";
    public static final String TRACE_PREFIX = "zipkin*";

    public static final int MAX_RESULT_WINDOW = 10000;

    public static final String FLUENT_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSSX";

    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String MILLIS_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

    public static final String FLUENT_TIMESTAMP_FILED_NAME = "@timestamp";
    public static final String FLUENT_APPLICATION_FILED_NAME = "application";
    public static final String FLUENT_LEVEL_FILED_NAME = "level";
    public static final String FLUENT_LOGGER_FILED_NAME = "logger";
    public static final String FLUENT_MESSAGE_FILED_NAME = "message";

    public static final String TRACE_TIMESTAMP_FILED_NAME = "timestamp_millis";
    public static final String TRACE_MUST_EXIST_FILED_NAME = "traceId";
    public static final String TRACE_MUST_NOT_EXIST_FILED_NAME = "parentId";
    public static final String TRACE_KIND_FILED_NAME = "kind";
    public static final String TRACE_KIND_SERVER = "SERVER";
    public static final String TRACE_SERVICENAME_FIELD_NAME = "localEndpoint.serviceName";
    public static final String TRACE_USERNAME_FIELD_NAME = "tags.username";
    public static final String TRACE_ID_FIELD_NAME = "id";
    public static final String TRACE_PARENTID_FIELD_NAME = "parentId";

    public static final String TRACE_KIND_FIELD_NAME = "kind";
    public static final String TRACE_TIMESTAMP_MILLIS_FIELD_NAME = "timestamp_millis";
    public static final String TRACE_DURATION_FIELD_NAME = "duration";
    public static final String TRACE_LOCALENDPOINT_FIELD_NAME = "localEndpoint";
    public static final String TRACE_HTTP_METHOD_FIELD_NAME = "http.method";
    public static final String TRACE_HTTP_PATH_FIELD_NAME = "http.path";

    public static final String SERVICENAME = "serviceName";
    public static final String IPV4 = "ipv4";
    public static final String TAGS = "tags";
    public static final String ERROR = "error";
    public static final String USERNAME = "username";

}
