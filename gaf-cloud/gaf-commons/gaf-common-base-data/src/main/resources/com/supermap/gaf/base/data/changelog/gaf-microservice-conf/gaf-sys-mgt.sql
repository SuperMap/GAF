-- liquibase formatted sql logicalFilePath:gaf-microservice-conf/gaf-sys-mgt

-- changeset SYS:20210406-0
INSERT INTO "public"."config_properties" ( "id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description" )
VALUES
	( 'd00200', 'fluentd.enable', 'true', 'gaf-sys-mgt', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL );
INSERT INTO "public"."config_properties" ( "id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description" )
VALUES
	( 'd00201', 'fluentd.host', '${GAF_ENV_FLUENTD_HOST:localhost}', 'gaf-sys-mgt', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL );
INSERT INTO "public"."config_properties" ( "id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description" )
VALUES
	( 'd00202', 'fluentd.port', '${GAF_ENV_FLUENTD_PORT:24224}', 'gaf-sys-mgt', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL );
INSERT INTO "public"."config_properties" ( "id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description" )
VALUES
	( 'd00203', 'fluentd.tagPrefix', '${GAF_ENV_FLUENTD_TAG:gaf}', 'gaf-sys-mgt', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL );
INSERT INTO "public"."config_properties" ( "id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description" )
VALUES
	( 'd00204', 'gaf.database.secretKey', '1q2w3e4r5t6y7u8i', 'gaf-sys-mgt', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL );
INSERT INTO "public"."config_properties" ( "id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description" )
VALUES
	( 'd00205', 'gaf.redis.database', '3', 'gaf-sys-mgt', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL );
INSERT INTO "public"."config_properties" ( "id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description" )
VALUES
	( 'd00206', 'gaf.redis.enable', 'true', 'gaf-sys-mgt', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL );
INSERT INTO "public"."config_properties" ( "id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description" )
VALUES
	( 'd00207', 'gaf.redis.host', '${GAF_ENV_REDIS_HOST:localhost}', 'gaf-sys-mgt', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL );
INSERT INTO "public"."config_properties" ( "id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description" )
VALUES
	( 'd00208', 'gaf.redis.maxActive', '16', 'gaf-sys-mgt', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL );
INSERT INTO "public"."config_properties" ( "id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description" )
VALUES
	( 'd00209', 'gaf.redis.maxIdle', '-1', 'gaf-sys-mgt', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL );
INSERT INTO "public"."config_properties" ( "id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description" )
VALUES
	( 'd00210', 'gaf.redis.maxWait', '16', 'gaf-sys-mgt', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL );
INSERT INTO "public"."config_properties" ( "id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description" )
VALUES
	( 'd00211', 'gaf.redis.minIdle', '0', 'gaf-sys-mgt', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL );
INSERT INTO "public"."config_properties" ( "id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description" )
VALUES
	( 'd00212', 'gaf.redis.port', '${GAF_ENV_REDIS_PORT:6379}', 'gaf-sys-mgt', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL );
INSERT INTO "public"."config_properties" ( "id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description" )
VALUES
	( 'd00213', 'management.endpoints.web.exposure.include', '*', 'gaf-sys-mgt', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL );
INSERT INTO "public"."config_properties" ( "id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description" )
VALUES
	( 'd00214', 'mybatis.configuration.map-underscore-to-camel-case', 'true', 'gaf-sys-mgt', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL );
INSERT INTO "public"."config_properties" ( "id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description" )
VALUES
	( 'd00215', 'mybatis.mapper-locations', 'classpath*:com/supermap/gaf/**/database/postgresql/*.xml', 'gaf-sys-mgt', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL );
INSERT INTO "public"."config_properties" ( "id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description" )
VALUES
	( 'd00216', 'mybatis.type-aliases-package', 'com.supermap.gaf.sys.mgt.commontype', 'gaf-sys-mgt', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL );
INSERT INTO "public"."config_properties" ( "id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description" )
VALUES
	( 'd00217', 'pagehelper.reasonable', 'true', 'gaf-sys-mgt', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL );
INSERT INTO "public"."config_properties" ( "id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description" )
VALUES
	( 'd00218', 'shiro.loginUrl', '/login', 'gaf-sys-mgt', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL );
INSERT INTO "public"."config_properties" ( "id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description" )
VALUES
	( 'd00219', 'shiro.serviceUrl', '/', 'gaf-sys-mgt', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL );
INSERT INTO "public"."config_properties" ( "id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description" )
VALUES
	( 'd00220', 'shiro.successUrl', '/portal/view/index.html', 'gaf-sys-mgt', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL );
INSERT INTO "public"."config_properties" ( "id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description" )
VALUES
	( 'd00221', 'shiro.urlFilters[0]', '/login, keycloakSecurityFilter', 'gaf-sys-mgt', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL );
INSERT INTO "public"."config_properties" ( "id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description" )
VALUES
	( 'd00222', 'shiro.urlFilters[1]', '/callback, callbackFilter', 'gaf-sys-mgt', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL );
INSERT INTO "public"."config_properties" ( "id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description" )
VALUES
	( 'd00223', 'shiro.urlFilters[2]', '/portal/manager/**, authc', 'gaf-sys-mgt', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL );
INSERT INTO "public"."config_properties" ( "id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description" )
VALUES
	( 'd00224', 'shiro.urlFilters[3]', '/portal/manager**, authc', 'gaf-sys-mgt', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL );
INSERT INTO "public"."config_properties" ( "id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description" )
VALUES
	( 'd00225', 'shiro.urlFilters[4]', '/portal/view/**, anon', 'gaf-sys-mgt', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL );
INSERT INTO "public"."config_properties" ( "id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description" )
VALUES
	( 'd00226', 'shiro.urlFilters[5]', '/portal/view**, anon', 'gaf-sys-mgt', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL );
INSERT INTO "public"."config_properties" ( "id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description" )
VALUES
	( 'd00227', 'shiro.urlFilters[6]', '/portal/**, anon', 'gaf-sys-mgt', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL );
INSERT INTO "public"."config_properties" ( "id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description" )
VALUES
	( 'd00228', 'shiro.urlFilters[7]', '/portal**, anon', 'gaf-sys-mgt', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL );
INSERT INTO "public"."config_properties" ( "id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description" )
VALUES
	( 'd00229', 'spring.datasource.driver-class-name', '${GAF_ENV_DATASOURCE_DRIVER:org.postgresql.Driver}', 'gaf-sys-mgt', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL );
INSERT INTO "public"."config_properties" ( "id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description" )
VALUES
	( 'd00230', 'spring.datasource.password', '${GAF_ENV_DATASOURCE_PASSWORD:}', 'gaf-sys-mgt', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL );
INSERT INTO "public"."config_properties" ( "id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description" )
VALUES
	( 'd00231', 'spring.datasource.url', '${GAF_ENV_DATASOURCE_URL:jdbc:postgresql://localhost:5432/postgres}', 'gaf-sys-mgt', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL );
INSERT INTO "public"."config_properties" ( "id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description" )
VALUES
	( 'd00232', 'spring.datasource.username', '${GAF_ENV_DATASOURCE_USERNAME:postgres}', 'gaf-sys-mgt', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL );
INSERT INTO "public"."config_properties" ( "id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description" )
VALUES
	( 'd00233', 'spring.sleuth.sampler.probability', '1', 'gaf-sys-mgt', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL );
INSERT INTO "public"."config_properties" ( "id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description" )
VALUES
	( 'd00234', 'spring.zipkin.base-url', '${GAF_ENV_ZIPKIN_URL:http://localhost:9411}', 'gaf-sys-mgt', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL );
INSERT INTO "public"."config_properties" ( "id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description" )
VALUES
	( 'd00235', 'spring.zipkin.sender.type', 'web', 'gaf-sys-mgt', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL );

-- changeset SYS:20210423-0
INSERT INTO config_properties (id, pro_key, pro_value, application, profile, "label", tenant_id, created_time, created_by, updated_time, updated_by, description) VALUES('i9b023', 'management.endpoint.health.show-details', 'always', 'gaf-sys-mgt', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL);
