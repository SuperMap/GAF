-- liquibase formatted sql logicalFilePath:gaf-microservice-conf/gaf-microservice-api

-- changeset SYS:20210406-0
INSERT INTO "config_properties"("id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description") VALUES ('7a772e', 'mybatis.configuration.map-underscore-to-camel-case', 'true', 'gaf-microservice-api', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL);
INSERT INTO "config_properties"("id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description") VALUES ('e7bb1a', 'mybatis.mapper-locations', 'classpath*:com/supermap/gaf/**/postgresql/*Mapper.xml', 'gaf-microservice-api', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL);
INSERT INTO "config_properties"("id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description") VALUES ('ed680c', 'swagger-file-path', '/swagger', 'gaf-microservice-api', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL);
INSERT INTO "config_properties"("id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description") VALUES ('23ba45', 'spring.datasource.driver-class-name', '${GAF_ENV_DATASOURCE_DRIVER:org.postgresql.Driver}', 'gaf-microservice-api', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL);
INSERT INTO "config_properties"("id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description") VALUES ('0db560', 'spring.datasource.password', '${GAF_ENV_DATASOURCE_PASSWORD:}', 'gaf-microservice-api', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL);
INSERT INTO "config_properties"("id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description") VALUES ('225eb8', 'spring.datasource.url', '${GAF_ENV_DATASOURCE_URL:jdbc:postgresql://localhost:5432/postgres}', 'gaf-microservice-api', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL);
INSERT INTO "config_properties"("id", "pro_key", "pro_value", "application", "profile", "label", "tenant_id", "created_time", "created_by", "updated_time", "updated_by", "description") VALUES ('14293c', 'spring.datasource.username', '${GAF_ENV_DATASOURCE_USERNAME:postgres}', 'gaf-microservice-api', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL);

-- changeset SYS:20210423-0
INSERT INTO config_properties (id, pro_key, pro_value, application, profile, "label", tenant_id, created_time, created_by, updated_time, updated_by, description) VALUES('k9b023', 'management.endpoint.health.show-details', 'always', 'gaf-microservice-api', 'prod', 'master', 'tenant_000000', NULL, 'SYS', NULL, 'SYS', NULL);