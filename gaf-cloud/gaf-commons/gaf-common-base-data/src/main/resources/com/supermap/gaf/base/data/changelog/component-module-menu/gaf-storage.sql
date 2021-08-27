-- liquibase formatted sql logicalFilePath:component-module-menu/gaf-storage.sql

-- changeset SYS:20210426-0
INSERT INTO "sys_component"("sys_component_id", "name", "name_cn", "code", "type", "status", "description", "created_time", "created_by", "updated_time", "updated_by", "sort_sn") VALUES ('ffe76607-7781-4d4a-bc9d-714331f563de', 'gaf-storage', '存储管理', NULL, '1', 't', NULL, '2021-03-22 11:44:18.476894', NULL, '2021-03-22 11:44:18.476894', NULL, NULL);
INSERT INTO "sys_catalog"("catalog_id", "parent_id", "sort_sn", "name", "type", "code", "sys_component_id", "icon_url", "description", "status", "created_time", "created_by", "updated_time", "updated_by", "tenant_id", "biz_type_code") VALUES ('5002fa05-74e7-4030-922c-b0466b6a70c6', '0', 14, '存储管理', '2', NULL, 'ffe76607-7781-4d4a-bc9d-714331f563de', NULL, '存储管理默认根目录', 't', '2021-03-22 11:44:18.476894', NULL, '2021-03-22 11:44:18.476894', NULL, NULL, NULL);
INSERT INTO "sys_catalog"("catalog_id", "parent_id", "sort_sn", "name", "type", "code", "sys_component_id", "icon_url", "description", "status", "created_time", "created_by", "updated_time", "updated_by", "tenant_id", "biz_type_code") VALUES ('98ea69c4-1540-456e-aac5-de1a8163ef58', '0', 18, '存储管理', '1', NULL, 'ffe76607-7781-4d4a-bc9d-714331f563de', NULL, '存储管理默认根目录', 't', '2021-03-22 11:44:18.476894', 'SYS', '2021-03-22 11:44:18.476894', 'SYS', 'tenant_000000', NULL);

INSERT INTO "auth_resource_module"("resource_module_id", "sys_component_id", "module_catalog_id", "name", "type", "module_url", "icon_url", "status", "sort_sn", "description", "created_time", "created_by", "updated_time", "updated_by", "path", "target", "name_en") VALUES ('2b6f1cb5-c469-4cc0-9afa-09e76a28f818', 'ffe76607-7781-4d4a-bc9d-714331f563de', '98ea69c4-1540-456e-aac5-de1a8163ef58', '附件管理', '1', '/storage/view/index.html#/AttachmentManagement', NULL, 't', 1, NULL, '2021-03-22 11:46:26.243186', NULL, '2021-03-22 11:46:26.243186', NULL, '/storage/AttachmentManagement', '0', NULL);

INSERT INTO "sys_catalog"("catalog_id", "parent_id", "sort_sn", "name", "type", "code", "sys_component_id", "icon_url", "description", "status", "created_time", "created_by", "updated_time", "updated_by", "tenant_id", "biz_type_code") VALUES ('9b37a916-2374-48e8-ac23-1e2e9f7ef80d', '8ecbef8d-c2a1-453c-9657-ef3b7948829e', 1, '存储管理', '4', NULL, '', NULL, NULL, 't', '2021-03-22 11:47:13.182776', 'SYS', '2021-03-23 13:24:25.995781', 'SYS', 'tenant_000000', NULL);

INSERT INTO "auth_resource_menu"("resource_menu_id", "menu_catalog_id", "name", "resource_module_id", "icon", "visible", "status", "sort_sn", "description", "created_time", "created_by", "updated_time", "updated_by", "params") VALUES ('c7e91d31-c0cf-4758-a247-dcc6398399af', '9b37a916-2374-48e8-ac23-1e2e9f7ef80d', '附件管理', '2b6f1cb5-c469-4cc0-9afa-09e76a28f818', NULL, 't', 't', 1, NULL, '2021-03-22 11:47:46.299534', 'SYS', '2021-03-22 11:47:46.299534', 'SYS', NULL);

INSERT INTO "auth_role_menu"("role_menu_id", "role_id", "resource_module_id", "status", "sort_sn", "description", "created_time", "created_by", "updated_time", "updated_by", "resource_menu_id") VALUES ('b91ad613-83a4-4bda-903f-d0ef1bba24b2', 'role_000000', NULL, 't', 1, NULL, '2021-03-22 11:48:07.982979', 'SYS', '2021-03-22 11:48:07.982979', 'SYS', 'c7e91d31-c0cf-4758-a247-dcc6398399af');
INSERT INTO "auth_role_menu"("role_menu_id", "role_id", "resource_module_id", "status", "sort_sn", "description", "created_time", "created_by", "updated_time", "updated_by", "resource_menu_id") VALUES ('5c3c06c1-ff55-4b0f-8982-ee6cadec2270', 'role_000001', '2b6f1cb5-c469-4cc0-9afa-09e76a28f818', 't', 1, NULL, '2021-03-23 08:26:41.927204', NULL, '2021-03-23 08:26:41.927204', NULL, 'c7e91d31-c0cf-4758-a247-dcc6398399af');

-- changeset SYS:20210810-0
UPDATE "auth_resource_module" SET name='存储配置', module_url = '/storage/view/index.html#/StorageManagement' WHERE resource_module_id = '2b6f1cb5-c469-4cc0-9afa-09e76a28f818';
UPDATE "auth_resource_menu" SET name='存储配置'WHERE resource_menu_id = 'c7e91d31-c0cf-4758-a247-dcc6398399af';

-- changeset SYS:20210810-1
UPDATE "auth_resource_module" SET  path = '/storage/StorageManagement' WHERE resource_module_id = '2b6f1cb5-c469-4cc0-9afa-09e76a28f818';

-- changeset SYS:20210817-8
INSERT INTO "auth_role_menu" ("role_menu_id", "role_id", "resource_module_id", "status", "sort_sn", "description", "created_time", "created_by", "updated_time", "updated_by", "resource_menu_id") VALUES ('eadbaa67-9a3f-4a15-b136-9022ccb92c4c', '8343b450-baac-4d34-902a-49e60284560d', '2b6f1cb5-c469-4cc0-9afa-09e76a28f818', 't', 1, NULL, '2021-08-17 00:58:37.359445', NULL, '2021-08-17 00:58:37.359445', NULL, 'c7e91d31-c0cf-4758-a247-dcc6398399af');