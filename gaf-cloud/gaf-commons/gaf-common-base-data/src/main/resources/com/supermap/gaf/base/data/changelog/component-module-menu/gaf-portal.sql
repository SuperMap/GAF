-- liquibase formatted sql logicalFilePath:component-module-menu/gaf-portal.sql

-- changeset SYS:20210426-0
INSERT INTO "sys_component"("sys_component_id", "name", "name_cn", "code", "type", "status", "description", "created_time", "created_by", "updated_time", "updated_by", "sort_sn") VALUES ('component_0004_portal', 'gaf-portal', '门户管理', '0001', '4', 't', '门户管理', '2020-11-13 08:41:43.336268', 'SYS', '2021-04-23 07:11:57.753715', 'SYS', NULL);
INSERT INTO "sys_catalog"("catalog_id", "parent_id", "sort_sn", "name", "type", "code", "sys_component_id", "icon_url", "description", "status", "created_time", "created_by", "updated_time", "updated_by", "tenant_id", "biz_type_code") VALUES ('2760fc79-838e-4463-bfef-e6b593de7113', '0', 1, '门户管理', '2', '0001', 'component_0004_portal', 'icon-home', '门户管理', 't', '2020-11-13 09:02:34.691571', 'SYS', '2021-01-15 15:37:17.404749', 'SYS', 'tenant_000000', NULL);
INSERT INTO "sys_catalog"("catalog_id", "parent_id", "sort_sn", "name", "type", "code", "sys_component_id", "icon_url", "description", "status", "created_time", "created_by", "updated_time", "updated_by", "tenant_id", "biz_type_code") VALUES ('374f8923-cedb-11e9-8b31-223e840cdc84', '0', 5, '门户管理', '1', '0001', 'component_0004_portal', 'icon-home', '门户管理', 't', '2020-11-12 12:20:24.205822', 'SYS', '2021-01-15 15:26:01.613215', 'SYS', 'tenant_000000', NULL);

INSERT INTO "auth_resource_module"("resource_module_id", "sys_component_id", "module_catalog_id", "name", "type", "module_url", "icon_url", "status", "sort_sn", "description", "created_time", "created_by", "updated_time", "updated_by", "path", "target", "name_en") VALUES ('4fc3e149-cedb-11e9-8b31-223e840cdc84', 'component_0004_portal', '374f8923-cedb-11e9-8b31-223e840cdc84', '门户定制', '1', '/customization', 'icon-setting', 't', 3, 'customization', '2020-11-12 12:52:47.941', 'SYS', '2021-03-09 06:39:30.274018', 'SYS', '/portal/customization', '0', NULL);
INSERT INTO "auth_resource_module"("resource_module_id", "sys_component_id", "module_catalog_id", "name", "type", "module_url", "icon_url", "status", "sort_sn", "description", "created_time", "created_by", "updated_time", "updated_by", "path", "target", "name_en") VALUES ('f5c2ade5-cee5-43ce-a7b5-93cd6b05ba34', 'component_0004_portal', '374f8923-cedb-11e9-8b31-223e840cdc84', '个人中心', '2', '/authority/view/index.html#/AuthUserInfo', 'user', 't', 4, NULL, '2020-11-23 06:29:16.275084', NULL, '2021-01-14 01:29:57.139123', NULL, '/authority/authuserinfo', '0', NULL);

INSERT INTO "sys_catalog"("catalog_id", "parent_id", "sort_sn", "name", "type", "code", "sys_component_id", "icon_url", "description", "status", "created_time", "created_by", "updated_time", "updated_by", "tenant_id", "biz_type_code") VALUES ('m74f8923-cedb-11e9-8b31-223e840cdc8', 'fc55b7c5-6dfd-40bc-9d08-9d20ad7f35b3', 1, '我的门户', '4', '0001', 'comp_xxx', 'icon-home', '门户管理', 't', '2020-11-12 12:20:24.205822', 'SYS', '2021-03-24 01:45:02.246333', 'SYS', 'tenant_000000', NULL);

INSERT INTO "auth_resource_menu"("resource_menu_id", "menu_catalog_id", "name", "resource_module_id", "icon", "visible", "status", "sort_sn", "description", "created_time", "created_by", "updated_time", "updated_by", "params") VALUES ('18663cb8-0e5f-4359-ba07-9585dcd9afcb', 'm74f8923-cedb-11e9-8b31-223e840cdc8', '个人中心', 'f5c2ade5-cee5-43ce-a7b5-93cd6b05ba34', 'user', 't', 't', 2, NULL, '2020-11-23 06:32:26.09883', 'SYS', '2020-11-23 06:32:40.820546', 'SYS', NULL);
INSERT INTO "auth_resource_menu"("resource_menu_id", "menu_catalog_id", "name", "resource_module_id", "icon", "visible", "status", "sort_sn", "description", "created_time", "created_by", "updated_time", "updated_by", "params") VALUES ('4fc3e149-cedb-11e9-8b31-223e840cdc84', 'm74f8923-cedb-11e9-8b31-223e840cdc8', '门户订制', '4fc3e149-cedb-11e9-8b31-223e840cdc84', 'icon-setting', 't', 't', 1, 'customization', '2020-11-12 13:08:51.749532', 'SYS', '2020-11-13 07:10:55.36237', 'SYS', NULL);

INSERT INTO "auth_role_menu"("role_menu_id", "role_id", "resource_module_id", "status", "sort_sn", "description", "created_time", "created_by", "updated_time", "updated_by", "resource_menu_id") VALUES ('29ff081e-1b9a-4476-8055-0401beb50e6e', 'role_000000', NULL, 't', 1, NULL, '2020-11-23 06:33:06.430681', 'SYS', '2020-11-23 06:33:06.430681', 'SYS', '18663cb8-0e5f-4359-ba07-9585dcd9afcb');
INSERT INTO "auth_role_menu"("role_menu_id", "role_id", "resource_module_id", "status", "sort_sn", "description", "created_time", "created_by", "updated_time", "updated_by", "resource_menu_id") VALUES ('aac907b4-7d98-44cd-ba50-b0c49ae705f4', 'role_000000', NULL, 't', 1, NULL, '2020-11-23 02:31:40.951231', 'SYS', '2020-11-23 02:31:40.951231', 'SYS', '4fc3e149-cedb-11e9-8b31-223e840cdc84');
