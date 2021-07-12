-- liquibase formatted sql logicalFilePath:component-module-menu/gaf-boot.sql

-- changeset SYS:20210712-0
INSERT INTO "sys_component" ("sys_component_id", "name", "name_cn", "code", "type", "status", "description", "created_time", "created_by", "updated_time", "updated_by", "sort_sn") VALUES ('a987dbc6-563d-4d27-a989-d64274629f81', 'gaf-boot', 'gaf', NULL, '2', 't', NULL, '2021-07-11 21:44:11.023193', NULL, '2021-07-11 21:44:11.023193', NULL, NULL);

INSERT INTO "public"."sys_catalog" ("catalog_id", "parent_id", "sort_sn", "name", "type", "code", "sys_component_id", "icon_url", "description", "status", "created_time", "created_by", "updated_time", "updated_by", "tenant_id", "biz_type_code") VALUES ('3e4cf4c7-bc87-4003-94b4-c5915fb5dfdd', '0', 8, 'gaf', '2', NULL, 'a987dbc6-563d-4d27-a989-d64274629f81', NULL, 'gaf默认根目录', 't', '2021-07-11 21:44:11.023193', NULL, '2021-07-11 21:44:11.023193', NULL, NULL, NULL);
