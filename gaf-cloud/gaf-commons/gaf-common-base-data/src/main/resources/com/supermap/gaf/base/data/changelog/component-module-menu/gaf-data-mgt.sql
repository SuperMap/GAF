-- liquibase formatted sql logicalFilePath:component-module-menu/gaf-data-mgt.sql

-- changeset SYS:20210426-0
INSERT INTO "sys_component"("sys_component_id", "name", "name_cn", "code", "type", "status", "description", "created_time", "created_by", "updated_time", "updated_by", "sort_sn") VALUES ('17acfca9-ef7e-4557-bffb-08d0102e9a1b', 'gaf-data-mgt', '数据管理', NULL, '4', 't', NULL, '2021-03-17 03:34:40.84569', NULL, '2021-04-23 06:55:59.511582', NULL, NULL);

INSERT INTO "sys_catalog"("catalog_id", "parent_id", "sort_sn", "name", "type", "code", "sys_component_id", "icon_url", "description", "status", "created_time", "created_by", "updated_time", "updated_by", "tenant_id", "biz_type_code") VALUES ('f9772eb7-40fd-4373-b6f5-7d2ace6bf156', '0', 4, '数据管理', '2', NULL, '17acfca9-ef7e-4557-bffb-08d0102e9a1b', NULL, '数据管理默认根目录', 't', '2021-03-17 03:34:40.84569', NULL, '2021-03-17 03:34:40.84569', NULL, NULL, NULL);
INSERT INTO "sys_catalog"("catalog_id", "parent_id", "sort_sn", "name", "type", "code", "sys_component_id", "icon_url", "description", "status", "created_time", "created_by", "updated_time", "updated_by", "tenant_id", "biz_type_code") VALUES ('29821edb-df77-4739-a131-565370deab4e', '0', 9, '数据管理', '1', NULL, '17acfca9-ef7e-4557-bffb-08d0102e9a1b', NULL, '数据管理默认根目录', 't', '2021-03-17 03:34:40.84569', 'SYS', '2021-03-17 03:34:40.84569', 'SYS', 'tenant_000000', NULL);
INSERT INTO "auth_resource_module"("resource_module_id", "sys_component_id", "module_catalog_id", "name", "type", "module_url", "icon_url", "status", "sort_sn", "description", "created_time", "created_by", "updated_time", "updated_by", "path", "target", "name_en") VALUES ('fe88b763-f584-481d-a2a4-06ac52f8fe10', '17acfca9-ef7e-4557-bffb-08d0102e9a1b', '29821edb-df77-4739-a131-565370deab4e', '空间数据源', '1', '/dataMgt/view/index.html#/SpaceDatasource', NULL, 't', 1, NULL, '2021-03-23 03:13:03.737718', NULL, '2021-03-23 03:13:03.737718', NULL, '/dataMgt/SpaceDatasource', '0', NULL);
INSERT INTO "auth_resource_module"("resource_module_id", "sys_component_id", "module_catalog_id", "name", "type", "module_url", "icon_url", "status", "sort_sn", "description", "created_time", "created_by", "updated_time", "updated_by", "path", "target", "name_en") VALUES ('d375a7fe-7b6b-4de3-b12a-bf944bcfeda2', '17acfca9-ef7e-4557-bffb-08d0102e9a1b', '29821edb-df77-4739-a131-565370deab4e', '工作空间管理', '1', '/dataMgt/view/index.html#/dataworkspace', NULL, 't', 2, NULL, '2021-03-17 03:38:37.386952', NULL, '2021-03-17 03:39:57.855243', NULL, '/dataMgt/dataworkspace', '0', 'dataworkspace');

INSERT INTO "sys_catalog"("catalog_id", "parent_id", "sort_sn", "name",  "type", "code", "sys_component_id", "icon_url", "description", "status", "created_time", "created_by", "updated_time", "updated_by", "tenant_id", "biz_type_code") VALUES ('266180ca-6c88-4fa2-b4eb-92c91d9504f4', 'd91fbd49-504d-4b6a-b1d0-1321d3581f8c', 1, '数据管理', '4', NULL, '', NULL, NULL, 't', '2021-03-17 03:40:22.847339', 'SYS', '2021-03-23 13:26:03.41472', 'SYS', 'tenant_000000', NULL);
INSERT INTO "auth_resource_menu"("resource_menu_id", "menu_catalog_id", "name", "resource_module_id", "icon", "visible", "status", "sort_sn", "description", "created_time", "created_by", "updated_time", "updated_by", "params") VALUES ('852694d8-3141-48a7-802f-d220a0b3839f', '266180ca-6c88-4fa2-b4eb-92c91d9504f4', '工作空间管理', 'd375a7fe-7b6b-4de3-b12a-bf944bcfeda2', NULL, 't', 't', 2, NULL, '2021-03-17 03:41:25.495468', 'SYS', '2021-03-17 03:41:25.495468', 'SYS', NULL);
INSERT INTO "auth_resource_menu"("resource_menu_id", "menu_catalog_id", "name", "resource_module_id", "icon", "visible", "status", "sort_sn", "description", "created_time", "created_by", "updated_time", "updated_by", "params") VALUES ('6cdb36d1-a7ca-424d-b783-5b11713825f8', '266180ca-6c88-4fa2-b4eb-92c91d9504f4', '空间数据源管理', 'fe88b763-f584-481d-a2a4-06ac52f8fe10', NULL, 't', 't', 3, NULL, '2021-03-23 03:13:35.419825', 'SYS', '2021-03-24 01:58:56.693056', 'SYS', NULL);
INSERT INTO "auth_role_menu"("role_menu_id", "role_id", "resource_module_id", "status", "sort_sn", "description", "created_time", "created_by", "updated_time", "updated_by", "resource_menu_id") VALUES ('5ce0c4be-0c20-45e8-9397-2f4566a46edd', 'role_000000', NULL, 't', 1, NULL, '2021-03-17 03:42:27.809188', 'SYS', '2021-03-17 03:42:27.809188', 'SYS', '852694d8-3141-48a7-802f-d220a0b3839f');
INSERT INTO "auth_role_menu"("role_menu_id", "role_id", "resource_module_id", "status", "sort_sn", "description", "created_time", "created_by", "updated_time", "updated_by", "resource_menu_id") VALUES ('137728fe-8f4b-48b5-9062-094067113130', 'role_000000', NULL, 't', 1, NULL, '2021-03-23 03:13:53.758629', 'SYS', '2021-03-23 03:13:53.758629', 'SYS', '6cdb36d1-a7ca-424d-b783-5b11713825f8');

-- changeset SYS:20210512-0
INSERT INTO "auth_role_menu"("role_menu_id", "role_id", "resource_module_id", "status", "sort_sn", "description", "created_time", "created_by", "updated_time", "updated_by", "resource_menu_id") VALUES ('1e42d6da-f4a6-44f6-8658-de76f36b665c', 'role_000001', 'd375a7fe-7b6b-4de3-b12a-bf944bcfeda2', 't', 1, NULL, '2021-03-17 03:42:36.271189', NULL, '2021-03-17 03:42:36.271189', NULL, '852694d8-3141-48a7-802f-d220a0b3839f');
INSERT INTO "auth_role_menu"("role_menu_id", "role_id", "resource_module_id", "status", "sort_sn", "description", "created_time", "created_by", "updated_time", "updated_by", "resource_menu_id") VALUES ('b80e6162-c5e2-4cfa-9755-505ab1911d86', 'role_000001', 'fe88b763-f584-481d-a2a4-06ac52f8fe10', 't', 1, NULL, '2021-03-24 03:07:03.00393', NULL, '2021-03-24 03:07:03.00393', NULL, '6cdb36d1-a7ca-424d-b783-5b11713825f8');

