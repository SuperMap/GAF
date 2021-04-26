-- liquibase formatted sql logicalFilePath:component-module-menu/gaf-sys-mgt.sql

-- changeset SYS:20210426-0
INSERT INTO "sys_component"("sys_component_id", "name", "name_cn", "code", "type", "status", "description", "created_time", "created_by", "updated_time", "updated_by", "sort_sn") VALUES ('a42395e5-d116-4003-a83b-efc34ac35d7d', 'gaf-sys-mgt', '系统管理', NULL, '2', 't', NULL, '2021-04-23 04:26:51.57588', NULL, '2021-04-23 04:26:51.57588', NULL, NULL);

INSERT INTO "sys_catalog"("catalog_id", "parent_id", "sort_sn", "name", "type", "code", "sys_component_id", "icon_url", "description", "status", "created_time", "created_by", "updated_time", "updated_by", "tenant_id", "biz_type_code") VALUES ('c953872d-b4a7-4cbe-9943-7275a3bef81f', '0', 15, '系统管理', '2', NULL, 'a42395e5-d116-4003-a83b-efc34ac35d7d', NULL, '系统管理默认根目录', 't', '2021-04-23 04:26:51.57588', NULL, '2021-04-23 04:26:51.57588', NULL, NULL, NULL);
INSERT INTO "sys_catalog"("catalog_id", "parent_id", "sort_sn", "name", "type", "code", "sys_component_id", "icon_url", "description", "status", "created_time", "created_by", "updated_time", "updated_by", "tenant_id", "biz_type_code") VALUES ('61120fca-0f67-4ac6-a4bb-af670ebcd0d2', '0', 15, '系统管理', '1', NULL, 'a42395e5-d116-4003-a83b-efc34ac35d7d', NULL, '系统管理默认根目录', 't', '2021-04-23 04:26:51.57588', NULL, '2021-04-23 04:26:51.57588', NULL, NULL, NULL);

INSERT INTO "auth_resource_module"("resource_module_id", "sys_component_id", "module_catalog_id", "name", "type", "module_url", "icon_url", "status", "sort_sn", "description", "created_time", "created_by", "updated_time", "updated_by", "path", "target", "name_en") VALUES ('bbbbb1ef-a047-43bb-baab-0b086400b65a', 'a42395e5-d116-4003-a83b-efc34ac35d7d', '61120fca-0f67-4ac6-a4bb-af670ebcd0d2', '字典管理', '2', '/sys-mgt/view/index.html#/dics', 'book', 't', 1, NULL, '2021-04-23 04:43:26.724239', NULL, '2021-04-23 04:43:26.724239', NULL, '/sysmgt/SysDics', '0', 'DictionaryManage');

INSERT INTO "auth_resource_menu"("resource_menu_id", "menu_catalog_id", "name", "resource_module_id", "icon", "visible", "status", "sort_sn", "description", "created_time", "created_by", "updated_time", "updated_by", "params") VALUES ('7f3f8160-e031-4072-acd5-fad1b77b0527', '251c9892-cf95-45ed-8f0a-c3ba9d6880eb', '字典管理', 'bbbbb1ef-a047-43bb-baab-0b086400b65a', NULL, 't', 't', 9, NULL, '2020-12-22 01:51:29.125487', 'SYS', '2020-12-22 01:51:29.125487', 'SYS', NULL);

INSERT INTO "auth_role_menu"("role_menu_id", "role_id", "resource_module_id", "status", "sort_sn", "description", "created_time", "created_by", "updated_time", "updated_by", "resource_menu_id") VALUES ('8ddb4f11-2504-49f5-aaa8-8d5e2cfeb45c', 'role_000000', NULL, 't', 1, NULL, '2020-12-22 01:53:11.469052', 'SYS', '2020-12-22 01:53:11.469052', 'SYS', '7f3f8160-e031-4072-acd5-fad1b77b0527');


