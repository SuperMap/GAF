-- liquibase formatted sql logicalFilePath:component-module-menu/gaf-authentication.sql

-- changeset SYS:20210426-0

INSERT INTO "sys_component"("sys_component_id", "name", "name_cn", "code", "type", "status", "description", "created_time", "created_by", "updated_time", "updated_by", "sort_sn") VALUES ('22d5c254-ffef-4b28-be57-bcc3f9b9897b', 'gaf-authentication', '认证中心', NULL, '2', 't', NULL, '2021-02-07 11:09:25.043576', NULL, '2021-04-23 07:08:22.811807', NULL, NULL);
INSERT INTO "sys_catalog"("catalog_id", "parent_id", "sort_sn", "name", "type", "code", "sys_component_id", "icon_url", "description", "status", "created_time", "created_by", "updated_time", "updated_by", "tenant_id", "biz_type_code") VALUES ('64646f21-1239-41dd-89c9-e5368265408c', '0', 13, '认证中心', '2', NULL, '22d5c254-ffef-4b28-be57-bcc3f9b9897b', NULL, '系统与权限控制默认根目录', 't', '2021-02-07 11:09:25.043576', NULL, '2021-04-23 07:08:22.811807', NULL, NULL, NULL);


