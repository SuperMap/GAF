-- liquibase formatted sql logicalFilePath:component-module-menu/gaf-monitor.sql

-- changeset SYS:20210426-0
INSERT INTO "sys_component"("sys_component_id", "name", "name_cn", "code", "type", "status", "description", "created_time", "created_by", "updated_time", "updated_by", "sort_sn") VALUES ('457db623-833c-4500-bf36-23e80300d86b', 'gaf-monitor', '资源监控', '', '4', 't', '资源监控', '2020-12-23 01:55:45.15402', NULL, '2020-12-23 02:34:45.583239', NULL, NULL);
INSERT INTO "sys_catalog"("catalog_id", "parent_id", "sort_sn", "name", "type", "code", "sys_component_id", "icon_url", "description", "status", "created_time", "created_by", "updated_time", "updated_by", "tenant_id", "biz_type_code") VALUES ('46da177a-d861-43e9-9c1c-d2ce5be1d7b1', '0', 10, '资源监控', '2', NULL, '457db623-833c-4500-bf36-23e80300d86b', NULL, '监控组件默认根目录', 't', '2020-12-23 01:55:45.15402', NULL, '2020-12-23 01:56:43.238152', NULL, NULL, NULL);
INSERT INTO "sys_catalog"("catalog_id", "parent_id", "sort_sn", "name", "type", "code", "sys_component_id", "icon_url", "description", "status", "created_time", "created_by", "updated_time", "updated_by", "tenant_id", "biz_type_code") VALUES ('d823e733-5f0f-42a0-94b3-ed47e4e653ed', '0', 17, '资源监控', '1', NULL, '457db623-833c-4500-bf36-23e80300d86b', NULL, '监控组件默认根目录', 't', '2020-12-23 01:55:45.15402', 'SYS', '2020-12-23 01:56:43.238152', 'SYS', 'tenant_000000', NULL);

INSERT INTO "auth_resource_module"("resource_module_id", "sys_component_id", "module_catalog_id", "name", "type", "module_url", "icon_url", "status", "sort_sn", "description", "created_time", "created_by", "updated_time", "updated_by", "path", "target", "name_en") VALUES ('ffbd327e-6c3f-4490-bd3e-5a0bdb8e0039', '457db623-833c-4500-bf36-23e80300d86b', 'd823e733-5f0f-42a0-94b3-ed47e4e653ed', '主机监控', '2', '/monitor/view/index.html#/hosts', 'icon-eye', 't', 2, 'hostsmonitor', '2020-12-23 02:28:39.263535', NULL, '2021-01-15 02:56:42.22177', NULL, '/monitor/hosts', '0', NULL);
INSERT INTO "auth_resource_module"("resource_module_id", "sys_component_id", "module_catalog_id", "name", "type", "module_url", "icon_url", "status", "sort_sn", "description", "created_time", "created_by", "updated_time", "updated_by", "path", "target", "name_en") VALUES ('9c69dd83-0850-41b2-893e-013324dc71c6', '457db623-833c-4500-bf36-23e80300d86b', 'd823e733-5f0f-42a0-94b3-ed47e4e653ed', '容器监控', '2', '/monitor/view/index.html#/containers', 'icon-eye', 't', 1, '容器监控', '2020-12-23 02:29:38.168847', NULL, '2021-01-15 02:56:59.90174', NULL, '/monitor/containers', '0', 'containersmonitor');

INSERT INTO "sys_catalog"("catalog_id", "parent_id", "sort_sn", "name", "type", "code", "sys_component_id", "icon_url", "description", "status", "created_time", "created_by", "updated_time", "updated_by", "tenant_id", "biz_type_code") VALUES ('d60fb7a3-d578-4ed9-aaee-56b721baa247', 'ca4faf63-2836-4e2e-baba-28ccacc5edd3', 4, '资源监控', '4', NULL, '', 'icon-cloud-server', NULL, 't', '2020-12-23 01:50:25.146791', 'SYS', '2021-03-23 13:22:45.664928', 'SYS', 'tenant_000000', NULL);

INSERT INTO "auth_resource_menu"("resource_menu_id", "menu_catalog_id", "name", "resource_module_id", "icon", "visible", "status", "sort_sn", "description", "created_time", "created_by", "updated_time", "updated_by", "params") VALUES ('ef8d8f10-16e7-4cd5-b274-57d9397ef1e9', 'd60fb7a3-d578-4ed9-aaee-56b721baa247', '容器监控', '9c69dd83-0850-41b2-893e-013324dc71c6', NULL, 't', 't', 1, NULL, '2020-12-23 02:35:30.19629', 'SYS', '2020-12-23 02:35:30.19629', 'SYS', NULL);
INSERT INTO "auth_resource_menu"("resource_menu_id", "menu_catalog_id", "name", "resource_module_id", "icon", "visible", "status", "sort_sn", "description", "created_time", "created_by", "updated_time", "updated_by", "params") VALUES ('99d5fdf2-406a-49cf-b47c-b5c2eb978c74', 'd60fb7a3-d578-4ed9-aaee-56b721baa247', '主机监控', 'ffbd327e-6c3f-4490-bd3e-5a0bdb8e0039', NULL, 't', 't', 2, NULL, '2020-12-23 02:36:08.204284', 'SYS', '2020-12-23 02:36:08.204284', 'SYS', NULL);

INSERT INTO "auth_role_menu"("role_menu_id", "role_id", "resource_module_id", "status", "sort_sn", "description", "created_time", "created_by", "updated_time", "updated_by", "resource_menu_id") VALUES ('22f917a2-a16e-4490-b18a-307ca1f2aa6d', 'role_000000', NULL, 't', 1, NULL, '2020-12-23 02:36:44.410325', 'SYS', '2020-12-23 02:36:44.410325', 'SYS', 'ef8d8f10-16e7-4cd5-b274-57d9397ef1e9');
INSERT INTO "auth_role_menu"("role_menu_id", "role_id", "resource_module_id", "status", "sort_sn", "description", "created_time", "created_by", "updated_time", "updated_by", "resource_menu_id") VALUES ('0275ecd0-1e51-457e-a4c0-3cbf2360dd3c', 'role_000000', NULL, 't', 1, NULL, '2020-12-23 02:36:44.410325', 'SYS', '2020-12-23 02:36:44.410325', 'SYS', '99d5fdf2-406a-49cf-b47c-b5c2eb978c74');

