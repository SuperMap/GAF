-- liquibase formatted sql logicalFilePath:menu/gaf-sys-mgt.sql

-- changeset SYS:20210426-0
INSERT INTO "auth_resource_menu" ("resource_menu_id", "parent_id", "name", "target", "path", "url", "icon", "sort_sn", "status", "description", "created_time", "created_by", "updated_time", "updated_by") VALUES ('7f3f8160-e031-4072-acd5-fad1b77b0527', '251c9892-cf95-45ed-8f0a-c3ba9d6880eb', '字典管理', '0', '/sysmgt/SysDics', NULL, 'book', 3, 't', NULL, '2020-12-22 01:51:29.125487', 'SYS', '2020-12-22 01:51:29.125487', 'SYS');

INSERT INTO "auth_role_menu"("role_menu_id", "role_id","status", "sort_sn", "description", "created_time", "created_by", "updated_time", "updated_by", "resource_menu_id") VALUES ('8ddb4f11-2504-49f5-aaa8-8d5e2cfeb45c', 'role_000000', 't', 1, NULL, '2020-12-22 01:53:11.469052', 'SYS', '2020-12-22 01:53:11.469052', 'SYS', '7f3f8160-e031-4072-acd5-fad1b77b0527');


