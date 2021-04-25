-- liquibase formatted sql logicalFilePath:gaf-microservice-conf/gaf-microservice-gateway

-- changeset SYS:20210406-0
INSERT INTO "config_properties"("id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description") VALUES ('c0450a', 'eureka.instance.metadataMap.prometheus.path', '/actuator/prometheus', 'gaf-microservice-gateway', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL);
INSERT INTO "config_properties"("id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description") VALUES ('a8d285', 'eureka.instance.metadataMap.prometheus.port', '8080', 'gaf-microservice-gateway', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL);
INSERT INTO "config_properties"("id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description") VALUES ('ca79a0', 'eureka.instance.metadataMap.prometheus.scrape', 'true', 'gaf-microservice-gateway', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL);
INSERT INTO "config_properties"("id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description") VALUES ('689f73', 'gateway.security.publicUrls', '/viewer/**,/monitor/**,/grafana/**,/**/actuator/**,/authentication/**,/authority/tenant/**,/base/fileservice/file/upload/swagger/*,/cloud-portal/**,/logout,/oauth/**,/oauth2/**,/login/**,/api/**,/**/view/**, /**/*.js,/*.*, /lang/**,/img/**,/**/static/**,/**/_static/**, /docs/**,/security/token/**,/config/**,/_static/**,/samples/**,/portal/customization**,/portal/user/profile**,/es/**,/log/env/logFluentdEsUrl,/map/webgis-view/**,/map/webgis-apps/*/config', 'gaf-microservice-gateway', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL);
INSERT INTO "config_properties"("id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description") VALUES ('0a6884', 'gateway.security.indexUrl', '/viewer/#/', 'gaf-microservice-gateway', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL);
INSERT INTO "config_properties"("id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description") VALUES ('5068f7', 'gateway.security.centerLoginUrl', '/authentication/login/center', 'gaf-microservice-gateway', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL);
INSERT INTO "config_properties"("id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description") VALUES ('3e9d96', 'gateway.security.centerLogoutUrl', '/authentication/logout', 'gaf-microservice-gateway', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL);
INSERT INTO "config_properties"("id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description") VALUES ('7515e2', 'gateway.security.gatewayLoginUrl', '/login', 'gaf-microservice-gateway', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL);
INSERT INTO "config_properties"("id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description") VALUES ('58c2a4', 'gateway.security.gatewayLogoutUrl', '/logout', 'gaf-microservice-gateway', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL);
INSERT INTO "config_properties"("id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description") VALUES ('5b616e', 'mybatis.configuration.map-underscore-to-camel-case', 'true', 'gaf-microservice-gateway', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL);
INSERT INTO "config_properties"("id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description") VALUES ('5664eb', 'mybatis.mapper-locations', 'classpath*:com/supermap/gaf/**/postgresql/*Mapper.xml', 'gaf-microservice-gateway', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL);
INSERT INTO "config_properties"("id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description") VALUES ('fdab17', 'spring.datasource.driver-class-name', '${GAF_ENV_DATASOURCE_DRIVER:org.postgresql.Driver}', 'gaf-microservice-gateway', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL);
INSERT INTO "config_properties"("id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description") VALUES ('b7fd9c', 'spring.datasource.password', '${GAF_ENV_DATASOURCE_PASSWORD:}', 'gaf-microservice-gateway', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL);
INSERT INTO "config_properties"("id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description") VALUES ('9b6ab2', 'spring.datasource.username', '${GAF_ENV_DATASOURCE_USERNAME:postgres}', 'gaf-microservice-gateway', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL);
INSERT INTO "config_properties"("id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description") VALUES ('6fbd2c', 'tenantId', 'system', 'gaf-microservice-gateway', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL);
INSERT INTO "config_properties"("id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description") VALUES ('78411f', 'fluentd.tagPrefix', '${GAF_ENV_FLUENTD_TAG:gaf}', 'gaf-microservice-gateway', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL);
INSERT INTO "config_properties"("id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description") VALUES ('8f5288', 'management.endpoint.health.show-details', 'always', 'gaf-microservice-gateway', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL);
INSERT INTO "config_properties"("id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description") VALUES ('8846bd', 'management.endpoint.metrics.enabled', 'true', 'gaf-microservice-gateway', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL);
INSERT INTO "config_properties"("id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description") VALUES ('0a117b', 'management.endpoint.prometheus.enabled', 'true', 'gaf-microservice-gateway', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL);
INSERT INTO "config_properties"("id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description") VALUES ('145dcd', 'management.endpoints.web.exposure.include', '*', 'gaf-microservice-gateway', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL);
INSERT INTO "config_properties"("id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description") VALUES ('302bd5', 'management.metrics.export.prometheus.enabled', 'true', 'gaf-microservice-gateway', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL);
INSERT INTO "config_properties"("id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description") VALUES ('773ca1', 'management.metrics.tags.application', '${spring.application.name}', 'gaf-microservice-gateway', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL);
INSERT INTO "config_properties"("id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description") VALUES ('6be848', 'ribbon.ConnectTimeout', '5000', 'gaf-microservice-gateway', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL);
INSERT INTO "config_properties"("id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description") VALUES ('f03bb8', 'ribbon.ReadTimeout', '50000', 'gaf-microservice-gateway', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL);
INSERT INTO "config_properties"("id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description") VALUES ('230c3e', 'fluentd.enable', 'true', 'gaf-microservice-gateway', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL);
INSERT INTO "config_properties"("id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description") VALUES ('6a4134', 'fluentd.host', '${GAF_ENV_FLUENTD_HOST:localhost}', 'gaf-microservice-gateway', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL);
INSERT INTO "config_properties"("id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description") VALUES ('42bf8d', 'fluentd.port', '${GAF_ENV_FLUENTD_PORT:24224}', 'gaf-microservice-gateway', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL);
INSERT INTO "config_properties"("id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description") VALUES ('5a08ec', 'spring.sleuth.sampler.probability', '1', 'gaf-microservice-gateway', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL);
INSERT INTO "config_properties"("id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description") VALUES ('75f51d', 'spring.zipkin.sender.type', 'web', 'gaf-microservice-gateway', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL);
INSERT INTO "config_properties"("id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description") VALUES ('903959', 'spring.zipkin.base-url', '${GAF_ENV_ZIPKIN_URL:http://localhost:9411}', 'gaf-microservice-gateway', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL);
INSERT INTO "config_properties"("id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description") VALUES ('a7c142', 'spring.datasource.url', '${GAF_ENV_DATASOURCE_URL:jdbc:postgresql://localhost:5432/postgres}', 'gaf-microservice-gateway', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL);

