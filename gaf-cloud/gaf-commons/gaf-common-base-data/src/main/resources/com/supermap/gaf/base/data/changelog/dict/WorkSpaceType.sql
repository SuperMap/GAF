-- liquibase formatted sql logicalFilePath:dict/WorkSpaceType

-- changeset SYS:20210406-0
INSERT INTO "sys_dict"("data_dict_id", "dict_code", "dict_name", "dict_value", "dict_class", "seq", "pid", "dict_desc", "status", "created_time", "created_by", "updated_time", "updated_by", "tenant_id", "visibility", "catalog_id", "ext_properties") VALUES ('2bca9bc2-afdb-4c95-8e9c-3f56cde907d7', 'WorkSpaceType', 'MYSQL', 'MYSQL', NULL, 2, 'bba2a8ba-05cd-4137-89a8-ad73cc69acf1', NULL, 't', '2021-02-23 08:44:23.492802', 'SYS', '2021-03-22 07:40:24.918058', 'SYS', 'tenant_000000', 't', NULL, NULL);
INSERT INTO "sys_dict"("data_dict_id", "dict_code", "dict_name", "dict_value", "dict_class", "seq", "pid", "dict_desc", "status", "created_time", "created_by", "updated_time", "updated_by", "tenant_id", "visibility", "catalog_id", "ext_properties") VALUES ('b201ce73-d66b-4499-a483-eaaa14f2ffa7', 'WorkSpaceType', 'SMWU', 'SMWU', NULL, 2, 'f5b15440-b8ed-4efd-9da6-0d879719943b', NULL, 't', '2021-02-23 08:43:16.432092', 'SYS', '2021-02-25 07:51:22.333814', 'SYS', 'tenant_000000', 't', NULL, NULL);
INSERT INTO "sys_dict"("data_dict_id", "dict_code", "dict_name", "dict_value", "dict_class", "seq", "pid", "dict_desc", "status", "created_time", "created_by", "updated_time", "updated_by", "tenant_id", "visibility", "catalog_id", "ext_properties") VALUES ('bba2a8ba-05cd-4137-89a8-ad73cc69acf1', 'WorkSpaceType', '数据库型', 'database', NULL, 2, '6c6be794-badb-4f4f-9ef0-d4a3a8138bfc', NULL, 't', '2021-02-23 08:41:19.096582', 'SYS', '2021-02-23 08:41:19.096582', 'SYS', 'tenant_000000', 't', NULL, NULL);
INSERT INTO "sys_dict"("data_dict_id", "dict_code", "dict_name", "dict_value", "dict_class", "seq", "pid", "dict_desc", "status", "created_time", "created_by", "updated_time", "updated_by", "tenant_id", "visibility", "catalog_id", "ext_properties") VALUES ('6c6be794-badb-4f4f-9ef0-d4a3a8138bfc', 'WorkSpaceType', '工作空间类型', NULL, NULL, 4, '0', NULL, 't', '2021-02-23 08:40:07.912719', 'SYS', '2021-02-23 08:40:07.912719', 'SYS', 'tenant_000000', 't', '64b6f34d-7f8e-42d8-b50c-590b653b2107', NULL);
INSERT INTO "sys_dict"("data_dict_id", "dict_code", "dict_name", "dict_value", "dict_class", "seq", "pid", "dict_desc", "status", "created_time", "created_by", "updated_time", "updated_by", "tenant_id", "visibility", "catalog_id", "ext_properties") VALUES ('ce47baea-272f-4436-8441-e1ccb284a02d', 'WorkSpaceType', 'PGSQL', 'PGSQL', NULL, 1, 'bba2a8ba-05cd-4137-89a8-ad73cc69acf1', NULL, 't', '2021-02-23 08:43:57.263908', 'SYS', '2021-03-22 07:27:10.985024', 'SYS', 'tenant_000000', 't', NULL, NULL);
INSERT INTO "sys_dict"("data_dict_id", "dict_code", "dict_name", "dict_value", "dict_class", "seq", "pid", "dict_desc", "status", "created_time", "created_by", "updated_time", "updated_by", "tenant_id", "visibility", "catalog_id", "ext_properties") VALUES ('f5b15440-b8ed-4efd-9da6-0d879719943b', 'WorkSpaceType', '文件型', 'file', NULL, 1, '6c6be794-badb-4f4f-9ef0-d4a3a8138bfc', NULL, 't', '2021-02-23 08:40:53.373936', 'SYS', '2021-03-29 09:17:25.215394', 'SYS', 'tenant_000000', 't', NULL, NULL);
INSERT INTO "sys_dict"("data_dict_id", "dict_code", "dict_name", "dict_value", "dict_class", "seq", "pid", "dict_desc", "status", "created_time", "created_by", "updated_time", "updated_by", "tenant_id", "visibility", "catalog_id", "ext_properties") VALUES ('479927ec-b136-48e6-a44d-392116246a52', 'WorkSpaceType', 'SXWU', 'SXWU', NULL, 1, 'f5b15440-b8ed-4efd-9da6-0d879719943b', NULL, 't', '2021-02-23 08:42:52.996172', 'SYS', '2021-02-25 07:51:16.247399', 'SYS', 'tenant_000000', 't', NULL, NULL);
INSERT INTO "sys_dict"("data_dict_id", "dict_code", "dict_name", "dict_value", "dict_class", "seq", "pid", "dict_desc", "status", "created_time", "created_by", "updated_time", "updated_by", "tenant_id", "visibility", "catalog_id", "ext_properties") VALUES ('eb570e24-6f0d-4fe7-a4b3-83f07502451c', 'WorkSpaceType', 'ORACLE', 'ORACLE', NULL, 8, 'bba2a8ba-05cd-4137-89a8-ad73cc69acf1', NULL, 't', '2021-02-25 07:57:52.787997', 'SYS', '2021-02-25 07:57:52.787997', 'SYS', 'tenant_000000', 't', NULL, NULL);
INSERT INTO "sys_dict"("data_dict_id", "dict_code", "dict_name", "dict_value", "dict_class", "seq", "pid", "dict_desc", "status", "created_time", "created_by", "updated_time", "updated_by", "tenant_id", "visibility", "catalog_id", "ext_properties") VALUES ('3a595d7e-c592-428d-98a1-f25b7cd56782', 'WorkSpaceType', 'DM', 'DM', NULL, 4, 'bba2a8ba-05cd-4137-89a8-ad73cc69acf1', NULL, 't', '2021-02-25 07:57:10.256747', 'SYS', '2021-02-25 07:57:10.256747', 'SYS', 'tenant_000000', 't', NULL, NULL);
INSERT INTO "sys_dict"("data_dict_id", "dict_code", "dict_name", "dict_value", "dict_class", "seq", "pid", "dict_desc", "status", "created_time", "created_by", "updated_time", "updated_by", "tenant_id", "visibility", "catalog_id", "ext_properties") VALUES ('f8f69f0e-39c2-4822-a082-b0c74d32c9fa', 'WorkSpaceType', 'SQL', 'SQL', NULL, 3, 'bba2a8ba-05cd-4137-89a8-ad73cc69acf1', NULL, 't', '2021-02-25 07:56:57.998709', 'SYS', '2021-02-25 07:56:57.998709', 'SYS', 'tenant_000000', 't', NULL, NULL);
INSERT INTO "sys_dict"("data_dict_id", "dict_code", "dict_name", "dict_value", "dict_class", "seq", "pid", "dict_desc", "status", "created_time", "created_by", "updated_time", "updated_by", "tenant_id", "visibility", "catalog_id", "ext_properties") VALUES ('c701c03a-543e-4ddb-a621-367c61f530f3', 'WorkSpaceType', 'MONGO', 'MONGO', NULL, 5, 'bba2a8ba-05cd-4137-89a8-ad73cc69acf1', NULL, 't', '2021-02-25 07:57:20.435593', 'SYS', '2021-02-25 07:57:20.435593', 'SYS', 'tenant_000000', 't', NULL, NULL);
INSERT INTO "sys_dict"("data_dict_id", "dict_code", "dict_name", "dict_value", "dict_class", "seq", "pid", "dict_desc", "status", "created_time", "created_by", "updated_time", "updated_by", "tenant_id", "visibility", "catalog_id", "ext_properties") VALUES ('b30ebd77-2512-470f-9869-d4b15edf0a6a', 'WorkSpaceType', 'PGIS', 'PGIS', NULL, 6, 'bba2a8ba-05cd-4137-89a8-ad73cc69acf1', NULL, 't', '2021-02-25 07:57:29.027773', 'SYS', '2021-02-25 07:57:29.027773', 'SYS', 'tenant_000000', 't', NULL, NULL);
INSERT INTO "sys_dict"("data_dict_id", "dict_code", "dict_name", "dict_value", "dict_class", "seq", "pid", "dict_desc", "status", "created_time", "created_by", "updated_time", "updated_by", "tenant_id", "visibility", "catalog_id", "ext_properties") VALUES ('5409f832-b430-497b-be96-50836ff0c654', 'WorkSpaceType', 'HWPG', 'HWPG', NULL, 7, 'bba2a8ba-05cd-4137-89a8-ad73cc69acf1', NULL, 't', '2021-02-25 07:57:39.410428', 'SYS', '2021-02-25 07:57:39.410428', 'SYS', 'tenant_000000', 't', NULL, NULL);
