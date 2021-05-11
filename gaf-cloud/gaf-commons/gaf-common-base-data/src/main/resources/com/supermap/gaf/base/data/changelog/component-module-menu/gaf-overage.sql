-- liquibase formatted sql logicalFilePath:component-module-menu/gaf-overage.sql

-- changeset SYS:20210426-0
-- gaf-docs
INSERT INTO "sys_component"("sys_component_id", "name", "name_cn", "code", "type", "status", "description", "created_time", "created_by", "updated_time", "updated_by", "sort_sn") VALUES ('component_0006_prod_manual', 'gaf-docs', '产品手册', '0001', '4', 't', '产品手册', '2020-11-13 08:41:43.494901', 'SYS', '2020-11-18 14:26:15.206961', 'SYS', NULL);
INSERT INTO "sys_catalog"("catalog_id", "parent_id", "sort_sn", "name", "type", "code", "sys_component_id", "icon_url", "description", "status", "created_time", "created_by", "updated_time", "updated_by", "tenant_id", "biz_type_code") VALUES ('2d224859-5aba-44c1-9ab9-740f850294e2', '0', 6, '产品手册', '2', '0001', 'component_0006_prod_manual', 'icon-question', 'productManual', 't', '2020-11-13 09:05:58.207838', 'SYS', '2020-11-13 09:05:58.207838', 'SYS', 'tenant_000000', NULL);
INSERT INTO "sys_catalog"("catalog_id", "parent_id", "sort_sn", "name", "type", "code", "sys_component_id", "icon_url", "description", "status", "created_time", "created_by", "updated_time", "updated_by", "tenant_id", "biz_type_code") VALUES ('a0f63d1d-c89b-11e9-8b31-223e840cdc84', '0', 12, '产品手册', '1', '0001', 'component_0006_prod_manual', 'icon-question', 'productManual', 't', '2020-11-12 12:22:08.108094', 'SYS', '2020-11-13 07:51:38.610524', 'SYS', 'tenant_000000', NULL);
INSERT INTO "auth_resource_module"("resource_module_id", "sys_component_id", "module_catalog_id", "name", "type", "module_url", "icon_url", "status", "sort_sn", "description", "created_time", "created_by", "updated_time", "updated_by", "path", "target", "name_en") VALUES ('5cd6c73f-c89f-11e9-8b31-223e840cdc84', 'component_0006_prod_manual', 'a0f63d1d-c89b-11e9-8b31-223e840cdc84', 'API&SDK', '2', 'docs/api/doc.html', 'icon-tags', 't', 2, 'productAPI', '2020-11-12 12:52:47.966', 'SYS', '2021-02-05 08:32:07.063069', 'SYS', '/productAPI', '1', NULL);
INSERT INTO "auth_resource_menu"("resource_menu_id", "menu_catalog_id", "name", "resource_module_id", "icon", "visible", "status", "sort_sn", "description", "created_time", "created_by", "updated_time", "updated_by", "params") VALUES ('5cd6c73f-c89f-11e9-8b31-223e840cdc84', 'ma0f63d1d-c89b-11e9-8b31-223e840cdc8', 'API&SDK', '5cd6c73f-c89f-11e9-8b31-223e840cdc84', 'icon-tags', 't', 't', 2, 'productAPI', '2020-11-12 13:08:51.783', 'SYS', '2020-11-13 04:21:26.41991', 'SYS', NULL);
INSERT INTO "auth_role_menu"("role_menu_id", "role_id", "resource_module_id", "status", "sort_sn", "description", "created_time", "created_by", "updated_time", "updated_by", "resource_menu_id") VALUES ('fc1a50f7-a09a-4122-af43-ad2660ed0aca', 'role_000000', NULL, 't', 1, NULL, '2020-11-23 02:31:40.951231', 'SYS', '2020-11-23 02:31:40.951231', 'SYS', '5cd6c73f-c89f-11e9-8b31-223e840cdc84');

-- remain menu
INSERT INTO "sys_catalog"("catalog_id", "parent_id", "sort_sn", "name", "type", "code", "sys_component_id", "icon_url", "description", "status", "created_time", "created_by", "updated_time", "updated_by", "tenant_id", "biz_type_code") VALUES ('fc55b7c5-6dfd-40bc-9d08-9d20ad7f35b3', '0', 15, '所有菜单', '4', '0014', '', NULL, '菜单分组根目录，该分组不会用于菜单显示', 't', '2020-11-07 18:33:50', 'SYS', '2021-01-15 16:06:12.920249', 'SYS', 'tenant_000000', NULL);
INSERT INTO "sys_catalog"("catalog_id", "parent_id", "sort_sn", "name", "type", "code", "sys_component_id", "icon_url", "description", "status", "created_time", "created_by", "updated_time", "updated_by", "tenant_id", "biz_type_code") VALUES ('251c9892-cf95-45ed-8f0a-c3ba9d6880eb', 'ca4faf63-2836-4e2e-baba-28ccacc5edd3', 1, '系统管理', '4', '00140002', NULL, NULL, NULL, 't', '2020-11-08 21:13:31.770001', 'SYS', '2021-03-23 13:21:28.784943', 'SYS', 'tenant_000000', NULL);
INSERT INTO "sys_catalog"("catalog_id", "parent_id", "sort_sn", "name", "type", "code", "sys_component_id", "icon_url", "description", "status", "created_time", "created_by", "updated_time", "updated_by", "tenant_id", "biz_type_code") VALUES ('ma0f63d1d-c89b-11e9-8b31-223e840cdc8', 'fc55b7c5-6dfd-40bc-9d08-9d20ad7f35b3', 6, '帮助指南', '4', '0001', 'comp_xxx', 'icon-question', 'productManual', 't', '2020-11-12 13:22:59.047851', 'SYS', '2021-03-24 01:50:35.095124', 'SYS', 'tenant_000000', NULL);
INSERT INTO "sys_catalog"("catalog_id", "parent_id", "sort_sn", "name", "type", "code", "sys_component_id", "icon_url", "description", "status", "created_time", "created_by", "updated_time", "updated_by", "tenant_id", "biz_type_code") VALUES ('d91fbd49-504d-4b6a-b1d0-1321d3581f8c', 'fc55b7c5-6dfd-40bc-9d08-9d20ad7f35b3', 5, 'GIS开发', '4', NULL, '', NULL, NULL, 't', '2021-03-23 13:25:45.509341', 'SYS', '2021-03-23 13:28:39.913806', 'SYS', 'tenant_000000', NULL);
INSERT INTO "sys_catalog"("catalog_id", "parent_id", "sort_sn", "name", "type", "code", "sys_component_id", "icon_url", "description", "status", "created_time", "created_by", "updated_time", "updated_by", "tenant_id", "biz_type_code") VALUES ('ca4faf63-2836-4e2e-baba-28ccacc5edd3', 'fc55b7c5-6dfd-40bc-9d08-9d20ad7f35b3', 2, '运维支撑', '4', NULL, '', NULL, NULL, 't', '2021-03-23 13:21:14.083817', 'SYS', '2021-03-24 01:46:41.114765', 'SYS', 'tenant_000000', NULL);
INSERT INTO "sys_catalog"("catalog_id", "parent_id", "sort_sn", "name", "type", "code", "sys_component_id", "icon_url", "description", "status", "created_time", "created_by", "updated_time", "updated_by", "tenant_id", "biz_type_code") VALUES ('8ecbef8d-c2a1-453c-9657-ef3b7948829e', 'fc55b7c5-6dfd-40bc-9d08-9d20ad7f35b3', 3, '通用业务', '4', NULL, '', NULL, NULL, 't', '2021-03-23 13:24:07.546188', 'SYS', '2021-03-24 01:46:54.213723', 'SYS', 'tenant_000000', NULL);


