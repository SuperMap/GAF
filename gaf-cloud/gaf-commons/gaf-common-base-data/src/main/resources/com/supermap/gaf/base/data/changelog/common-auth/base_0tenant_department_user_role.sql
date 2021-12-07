-- liquibase formatted sql logicalFilePath:common-auth/tenant_department_user_role

-- changeset SYS:20210406-0
INSERT INTO "auth_tenant"("tenant_id", "tenant_name", "description", "type", "name_en", "brief_name_en", "code", "admin_id", "created_time", "created_by", "updated_time", "updated_by", "status") VALUES ('tenant_000000', 'GAF(平台内置)', '平台内置租户,勿删', '1', 'gaf', 'gaf', '0000', '7aee9f52-791d-4a53-b7b0-6635ef551cca', '2020-10-29 22:38:04', 'SYS', '2020-11-13 03:10:49.908265', 'SYS', 't');
INSERT INTO "auth_department"("department_id", "tenant_id", "parent_id", "sort_sn", "department_type", "department_name", "name_en", "brief_name", "code", "status", "description", "admin_id", "is_third_party", "created_time", "created_by", "updated_time", "updated_by") VALUES ('department_000000', 'tenant_000000', '0', 1, '1', 'gaf平台内置部门', 'gaf', 'gaf', NULL, 't', '平台内置部门，勿删', NULL, NULL, '2020-10-29 22:42:01', 'SYS', '2021-04-01 02:21:32.365886', 'SYS');
INSERT INTO "sys_catalog"("catalog_id", "parent_id", "sort_sn", "name", "type", "code", "icon_url", "description", "status", "created_time", "created_by", "updated_time", "updated_by", "tenant_id", "biz_type_code") VALUES ('catalog_000000', '0', 7, '平台内置角色组', '3', '0000', NULL, 'gaf角色组根目录，勿删', 't', '2020-10-30 10:42:21', 'SYS', '2020-11-13 03:10:49.872292', 'SYS', 'tenant_000000', NULL);
INSERT INTO "sys_catalog"("catalog_id", "parent_id", "sort_sn", "name", "type", "code", "icon_url", "description", "status", "created_time", "created_by", "updated_time", "updated_by", "tenant_id", "biz_type_code") VALUES ('catalog_sys_authority_000000', 'catalog_000000', 1, '权限控制组', '3', '00000000', NULL, '权限控制角色组', 't', '2020-11-09 12:43:31.928363', 'SYS', '2020-11-09 12:43:31.928363', 'SYS', 'tenant_000000', NULL);
INSERT INTO "sys_catalog"("catalog_id", "parent_id", "sort_sn", "name", "type", "code", "icon_url", "description", "status", "created_time", "created_by", "updated_time", "updated_by", "tenant_id", "biz_type_code") VALUES ('08cfd76e-f6c1-4cae-865c-3f708fe8e296', 'catalog_000000', 2, '工程开发组', '3', '00000001', NULL, NULL, 't', '2020-11-12 03:23:20.816463', 'SYS', '2020-11-18 14:41:07.874197', 'SYS', 'tenant_000000', NULL);
INSERT INTO "auth_role"("role_id", "tenant_id", "role_catalog_id", "sort_sn", "role_name", "name_en", "code", "status", "description", "created_time", "created_by", "updated_time", "updated_by", "type") VALUES ('role_000000', 'tenant_000000', 'catalog_sys_authority_000000', 1, '平台管理员', 'platform_admin', NULL, 't', '平台内置角色，勿删', '2020-10-29 22:25:39', 'SYS', NULL, 'SYS', '2');
INSERT INTO "auth_role"("role_id", "tenant_id", "role_catalog_id", "sort_sn", "role_name", "name_en", "code", "status", "description", "created_time", "created_by", "updated_time", "updated_by", "type") VALUES ('role_000001', 'tenant_000000', 'catalog_sys_authority_000000', 2, '租户管理员', 'tenant_admin', NULL, 't', '权限控制组件内置角色，勿删', '2020-10-29 22:30:08', 'zhuhongming', NULL, NULL, '1');
INSERT INTO "auth_user"("user_id", "tenant_id", "department_id", "sort_sn", "name", "password", "real_name", "id_number", "mobile_number", "email", "address", "post_id", "expiration_time", "is_third_party", "status", "description", "last_login_time", "created_time", "created_by", "updated_time", "updated_by") VALUES ('user_000000', 'tenant_000000', 'department_000000', 1, 'sys_admin', '$2a$10$MHqyBinUz4ZU7X4OZlh/FuLU1CiQLwh9R3YbPhTTt.PhvDkJSJ4BK', '平台管理者', NULL, NULL, 'gaf_sys_admin@163.com', NULL, NULL, NULL, 'f', 't', '平台管理员用户，勿删', '2021-04-01 21:11:05.942007', '2020-10-29 22:45:13', 'SYS', '2020-11-03 13:58:48', 'SYS');
INSERT INTO "auth_user"("user_id", "tenant_id", "department_id", "sort_sn", "name", "password", "real_name", "id_number", "mobile_number", "email", "address", "post_id", "expiration_time", "is_third_party", "status", "description", "last_login_time", "created_time", "created_by", "updated_time", "updated_by") VALUES ('262abacb-bbce-4886-a622-fafd82a2bee6', 'tenant_000000', 'department_000000', 2, 'SYS', '$2a$10$MHqyBinUz4ZU7X4OZlh/FuLU1CiQLwh9R3YbPhTTt.PhvDkJSJ4BK', '平台基础数据管理者', NULL, NULL, 'xx@xx.com', NULL, NULL, NULL, 'f', 't', NULL, '2021-04-01 21:09:13.239281', '2021-04-01 13:07:55.890409', 'SYS', '2021-04-01 13:07:55.890409', 'SYS');
INSERT INTO "auth_user_role"("user_role_id", "user_id", "role_id", "status", "sort_sn", "created_time", "created_by", "updated_time", "updated_by", "tenant_id") VALUES ('user_role_000000', 'user_000000', 'role_000000', 't', 1, '2020-10-30 08:38:18', 'SYS', NULL, 'SYS', 'tenant_000000');
INSERT INTO "auth_user_role"("user_role_id", "user_id", "role_id", "status", "sort_sn", "created_time", "created_by", "updated_time", "updated_by", "tenant_id") VALUES ('c40ab9d1-037e-47b4-91f7-a94ba04d48a1', '262abacb-bbce-4886-a622-fafd82a2bee6', 'role_000000', 't', NULL, '2021-04-01 13:08:11.427137', 'SYS', '2021-04-01 13:08:11.427137', 'SYS', 'tenant_000000');

-- changeset SYS:20210817-0
INSERT INTO "auth_role" ("role_id", "tenant_id", "role_catalog_id", "sort_sn", "role_name", "name_en", "code", "status", "description", "created_time", "created_by", "updated_time", "updated_by", "type") VALUES ('8343b450-baac-4d34-902a-49e60284560d', 'tenant_000000', 'catalog_sys_authority_000000', 3, '普通用户', NULL, '003', 't', NULL, '2021-08-17 00:57:50.745538', 'sys_admin', '2021-08-17 02:02:56.355186', NULL, '3');
INSERT INTO "auth_user" ("user_id", "tenant_id", "department_id", "sort_sn", "name", "password", "real_name", "id_number", "mobile_number", "email", "address", "post_id", "expiration_time", "is_third_party", "status", "description", "last_login_time", "created_time", "created_by", "updated_time", "updated_by") VALUES ('dbfdd91d-0dbc-4097-9ea7-99ac6c1a2652', 'tenant_000000', 'department_000000', 4, 'case', '$2a$10$kfsCTJ1/7bjZAiaQrLpb5OMP6h9HRlOh7J9An2ADxe88iwBKOMDjO', '演示', NULL, NULL, 'wxlwxl3210@gmail.com', NULL, NULL, NULL, 'f', 't', NULL, '2021-08-17 02:06:29.706198', '2021-08-17 02:00:56.553909', 'sys_admin', '2021-08-17 02:07:35.098895', NULL);
INSERT INTO "auth_user" ("user_id", "tenant_id", "department_id", "sort_sn", "name", "password", "real_name", "id_number", "mobile_number", "email", "address", "post_id", "expiration_time", "is_third_party", "status", "description", "last_login_time", "created_time", "created_by", "updated_time", "updated_by") VALUES ('ca8c9848-1d54-420c-aaf0-77581c372103', 'tenant_000000', 'department_000000', 3, 'test', '$2a$10$5D3OAsiZR8uEsDddE.nLleg5kIj78O8LHufrMt2wH5jl83sAMHsvW', '测试', NULL, NULL, '524447938@qq.com', NULL, NULL, NULL, 'f', 't', NULL, '2021-08-17 02:21:06.557918', '2021-08-17 01:52:05.763968', 'sys_admin', '2021-08-17 02:17:17.989778', NULL);
INSERT INTO "auth_user_role" ("user_role_id", "user_id", "role_id", "status", "sort_sn", "created_time", "created_by", "updated_time", "updated_by", "tenant_id") VALUES ('ca3d9a49-0a82-4daa-9f9a-f2c894a43a21', 'dbfdd91d-0dbc-4097-9ea7-99ac6c1a2652', '8343b450-baac-4d34-902a-49e60284560d', 't', NULL, '2021-08-17 02:03:31.362039', NULL, '2021-08-17 02:03:31.362039', NULL, NULL);
INSERT INTO "auth_user_role" ("user_role_id", "user_id", "role_id", "status", "sort_sn", "created_time", "created_by", "updated_time", "updated_by", "tenant_id") VALUES ('7bbce59b-b647-46e9-83cf-1470f7f710dd', 'ca8c9848-1d54-420c-aaf0-77581c372103', 'role_000000', 't', NULL, '2021-08-17 01:52:20.515276', NULL, '2021-08-17 01:52:20.515276', NULL, NULL);
