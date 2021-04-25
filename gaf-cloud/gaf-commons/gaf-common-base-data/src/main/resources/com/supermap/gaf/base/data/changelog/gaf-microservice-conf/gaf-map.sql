-- liquibase formatted sql logicalFilePath:gaf-microservice-conf/gaf-map

-- changeset SYS:20210406-0
INSERT INTO "config_properties"("id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description") VALUES ('qIz6ne', 'mybatis.mapper-locations', 'classpath*:com/supermap/gaf/**/database/postgresql/*.xml', 'gaf-map', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', '');
INSERT INTO "config_properties"("id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description") VALUES ('efccb9', 'fluentd.enable', 'true', 'gaf-map', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL);
INSERT INTO "config_properties"("id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description") VALUES ('3f4b66', 'fluentd.tagPrefix', '${GAF_ENV_FLUENTD_TAG:gaf}', 'gaf-map', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL);
INSERT INTO "config_properties"("id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description") VALUES ('8358f4', 'fluentd.host', '${GAF_ENV_FLUENTD_HOST:localhost}', 'gaf-map', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL);
INSERT INTO "config_properties"("id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description") VALUES ('fb2a00', 'fluentd.port', '${GAF_ENV_FLUENTD_PORT:24224}', 'gaf-map', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL);
INSERT INTO "config_properties"("id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description") VALUES ('c8b1d1', 'gaf.service-catalog.enable', 'true', 'gaf-map', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL);
INSERT INTO "config_properties"("id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description") VALUES ('d66276', 'gaf.service-layer.enable', 'true', 'gaf-map', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL);
INSERT INTO "config_properties"("id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description") VALUES ('db94f5', 'require.security', 'false', 'gaf-map', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL);
INSERT INTO "config_properties"("id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description") VALUES ('d48e40', 'shiro.loginUrl', '/login', 'gaf-map', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL);
INSERT INTO "config_properties"("id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description") VALUES ('f8d6ff', 'shiro.publicUrls[0]', '/map/view/**', 'gaf-map', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL);
INSERT INTO "config_properties"("id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description") VALUES ('ae890d', 'shiro.publicUrls[1]', '/map/view**', 'gaf-map', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL);
INSERT INTO "config_properties"("id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description") VALUES ('484a45', 'shiro.publicUrls[2]', '/map/authdefines/**', 'gaf-map', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL);
INSERT INTO "config_properties"("id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description") VALUES ('bdab6d', 'shiro.publicUrls[3]', '/favicon.ico', 'gaf-map', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL);
INSERT INTO "config_properties"("id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description") VALUES ('0258c2', 'shiro.serviceUrl', '/', 'gaf-map', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL);
INSERT INTO "config_properties"("id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description") VALUES ('5116eb', 'shiro.successUrl', '/', 'gaf-map', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL);
INSERT INTO "config_properties"("id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description") VALUES ('72634c', 'info.dependencies.service-dir', 'service-dir', 'gaf-map', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL);
INSERT INTO "config_properties"("id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description") VALUES ('c89f99', 'info.dependencies.service-gis', 'service-gis', 'gaf-map', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL);
INSERT INTO "config_properties"("id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description") VALUES ('854328', 'shiro.apiauthz.enable', 'false', 'gaf-map', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL);
INSERT INTO "config_properties"("id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description") VALUES ('ef1cb8', 'shiro.publicUrls[4]', '/index.html**', 'gaf-map', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL);
INSERT INTO "config_properties"("id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description") VALUES ('c5ae57', 'shiro.urlFilters[0]', '/login, keycloakSecurityFilter', 'gaf-map', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL);
INSERT INTO "config_properties"("id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description") VALUES ('6f6d8c', 'shiro.urlFilters[1]', '/callback, callbackFilter', 'gaf-map', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL);
INSERT INTO "config_properties"("id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description") VALUES ('4e5c75', 'shiro.urlFilters[2]', '/** anon', 'gaf-map', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL);
INSERT INTO "config_properties"("id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description") VALUES ('aq2Yjq', 'webgis.host', 'http://192.168.11.118:31111', 'gaf-map', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL);
INSERT INTO "config_properties"("id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description") VALUES ('fYNFBz', 'spring.datasource.driver-class-name', '${GAF_ENV_DATASOURCE_DRIVER:org.postgresql.Driver}', 'gaf-map', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', '');
INSERT INTO "config_properties"("id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description") VALUES ('rIruqi', 'spring.datasource.password', '${GAF_ENV_DATASOURCE_PASSWORD:}', 'gaf-map', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', '');
INSERT INTO "config_properties"("id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description") VALUES ('MNJ7na', 'spring.datasource.username', '${GAF_ENV_DATASOURCE_USERNAME:postgres}', 'gaf-map', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', '');
INSERT INTO "config_properties"("id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description") VALUES ('BBvIJ3', 'spring.mail.default-encoding', 'utf-8', 'gaf-map', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', '');
INSERT INTO "config_properties"("id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description") VALUES ('6edb25', 'feign.bigdata.url', '1x1', 'gaf-map', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL);
INSERT INTO "config_properties"("id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description") VALUES ('bdd63b', 'feign.security.url', '1x1', 'gaf-map', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL);
INSERT INTO "config_properties"("id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description") VALUES ('aqeQNn', 'mybatis.configuration.map-underscore-to-camel-case', 'true', 'gaf-map', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL);
INSERT INTO "config_properties"("id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description") VALUES ('bdd63a', 'pagehelper.reasonable', 'true', 'gaf-map', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL);
INSERT INTO "config_properties"("id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description") VALUES ('Mnq2Mb', 'spring.datasource.url', '${GAF_ENV_DATASOURCE_URL:jdbc:postgresql://localhost:5432/postgres}', 'gaf-map', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', '');
INSERT INTO "config_properties"("id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description") VALUES ('baa538', 'gaf.map.fly-manager.file-path', '/public/map/fpf', 'gaf-map', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL);


-- changeset SYS:20210423-0
INSERT INTO config_properties (id, pro_key, pro_value, application, profile, "label", tenant_id, created_time, created_by, updated_time, updated_by, description) VALUES('m9b023', 'management.endpoint.health.show-details', 'always', 'gaf-map', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL);


-- changeset SYS:20210425-0
INSERT INTO "config_properties"("id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description") VALUES ('8eeb11', 'spring.redis.database', '5', 'gaf-map', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL);
INSERT INTO "config_properties"("id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description") VALUES ('18xvbe', 'spring.redis.host', '${GAF_ENV_REDIS_HOST:localhost}', 'gaf-map', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL);
INSERT INTO "config_properties"("id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description") VALUES ('92gcea', 'spring.redis.jedis.pool.max-active', '8', 'gaf-map', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL);
INSERT INTO "config_properties"("id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description") VALUES ('4815r2', 'spring.redis.port', '${GAF_ENV_REDIS_PORT:6379}', 'gaf-map', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL);