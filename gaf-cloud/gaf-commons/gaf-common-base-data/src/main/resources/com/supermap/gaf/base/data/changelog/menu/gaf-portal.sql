-- liquibase formatted sql logicalFilePath:menu/gaf-portal.sql

-- changeset SYS:20210426-0
INSERT INTO "auth_resource_menu" ("resource_menu_id", "parent_id", "name", "target", "path", "url", "icon", "sort_sn", "status", "description", "created_time", "created_by", "updated_time", "updated_by") VALUES ('c2c3557d-9a16-45eb-8d74-df3f4daba0fb', NULL, '我的门户', '0', NULL, NULL, 'home', 1, 't', '门户管理', '2020-11-12 12:20:24.205822', 'SYS', '2021-03-24 01:45:02.246333', 'SYS');

INSERT INTO "auth_resource_menu" ("resource_menu_id", "parent_id", "name", "target", "path", "url", "icon", "sort_sn", "status", "description", "created_time", "created_by", "updated_time", "updated_by") VALUES ('4fc3e149-cedb-11e9-8b31-223e840cdc84', 'c2c3557d-9a16-45eb-8d74-df3f4daba0fb', '门户订制', '0', '/portal/customization', NULL, 'setting', 1, 't', 'customization', '2020-11-12 13:08:51.749532', 'SYS', '2020-11-13 07:10:55.36237', 'SYS');
INSERT INTO "auth_resource_menu" ("resource_menu_id", "parent_id", "name", "target", "path", "url", "icon", "sort_sn", "status", "description", "created_time", "created_by", "updated_time", "updated_by") VALUES ('18663cb8-0e5f-4359-ba07-9585dcd9afcb', 'c2c3557d-9a16-45eb-8d74-df3f4daba0fb', '个人中心', '0', '/authority/authuserinfo', NULL, 'user', 2, 't', NULL, '2020-11-23 06:32:26.09883', 'SYS', '2020-11-23 06:32:40.820546', 'SYS');

INSERT INTO "auth_role_menu" ("role_menu_id", "role_id", "status", "sort_sn", "description", "created_time", "created_by", "updated_time", "updated_by", "resource_menu_id") VALUES ('29ff081e-1b9a-4476-8055-0401beb50e6e', 'role_000000', 't', 1, NULL, '2020-11-23 06:33:06.430681', 'SYS', '2020-11-23 06:33:06.430681', 'SYS', '18663cb8-0e5f-4359-ba07-9585dcd9afcb');
INSERT INTO "auth_role_menu" ("role_menu_id", "role_id", "status", "sort_sn", "description", "created_time", "created_by", "updated_time", "updated_by", "resource_menu_id") VALUES ('aac907b4-7d98-44cd-ba50-b0c49ae705f4', 'role_000000', 't', 1, NULL, '2020-11-23 02:31:40.951231', 'SYS', '2020-11-23 02:31:40.951231', 'SYS', '4fc3e149-cedb-11e9-8b31-223e840cdc84');

INSERT INTO "auth_role_menu" ("role_menu_id", "role_id", "status", "sort_sn", "description", "created_time", "created_by", "updated_time", "updated_by", "resource_menu_id") VALUES ('981cd181-67d4-4203-9ebc-2bbf7d58f22d', 'role_000001', 't', 1, NULL, '2020-11-23 08:02:41.989366', 'SYS', '2020-11-23 08:02:41.989366', 'SYS', '18663cb8-0e5f-4359-ba07-9585dcd9afcb');
INSERT INTO "auth_role_menu" ("role_menu_id", "role_id", "status", "sort_sn", "description", "created_time", "created_by", "updated_time", "updated_by", "resource_menu_id") VALUES ('29bff236-6d1d-4b7e-9f77-be6cb31417de', 'role_000001', 't', 1, NULL, '2020-12-22 02:23:57.453939', 'SYS', '2020-12-22 02:23:57.453939', 'SYS', '4fc3e149-cedb-11e9-8b31-223e840cdc84');

INSERT INTO "auth_role_menu" ("role_menu_id", "role_id", "status", "sort_sn", "description", "created_time", "created_by", "updated_time", "updated_by", "resource_menu_id") VALUES ('cd0e9414-1aac-4c9b-8453-45db22722b40', '8343b450-baac-4d34-902a-49e60284560d', 't', 1, NULL, '2021-08-17 00:58:37.359445', 'SYS', '2021-08-17 00:58:37.359445', 'SYS', '4fc3e149-cedb-11e9-8b31-223e840cdc84');
INSERT INTO "auth_role_menu" ("role_menu_id", "role_id", "status", "sort_sn", "description", "created_time", "created_by", "updated_time", "updated_by", "resource_menu_id") VALUES ('d2fdc606-f338-4e7b-be53-c7dee0ee932e', '8343b450-baac-4d34-902a-49e60284560d', 't', 1, NULL, '2021-08-17 00:58:37.359445', 'SYS', '2021-08-17 00:58:37.359445', 'SYS', '18663cb8-0e5f-4359-ba07-9585dcd9afcb');