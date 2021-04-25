-- liquibase formatted sql logicalFilePath:dict/data_source_type

-- changeset SYS:20210406-0
INSERT INTO "sys_dict"("data_dict_id", "dict_code", "dict_name", "dict_value", "dict_class", "seq", "pid", "dict_desc", "status", "created_time", "created_by", "updated_time", "updated_by", "tenant_id", "visibility", "catalog_id", "ext_properties") VALUES ('b06615e0-0d2c-43e1-a0a3-c8a50baaa539', 'DataSourceType', '文件型', 'file', NULL, 1, 'd2e75d6f-5511-4968-956c-9a61083b0f85', NULL, 't', '2021-02-05 06:17:28.667141', 'SYS', '2021-02-08 01:09:57.153719', 'SYS', 'tenant_000000', 't', NULL, NULL);
INSERT INTO "sys_dict"("data_dict_id", "dict_code", "dict_name", "dict_value", "dict_class", "seq", "pid", "dict_desc", "status", "created_time", "created_by", "updated_time", "updated_by", "tenant_id", "visibility", "catalog_id", "ext_properties") VALUES ('ad8e1c82-c5bd-468b-b09f-2f7a5739fe0e', 'DataSourceType', '数据库型', 'database', NULL, 2, 'd2e75d6f-5511-4968-956c-9a61083b0f85', NULL, 't', '2021-02-05 06:18:04.323802', 'SYS', '2021-02-08 01:10:02.143967', 'SYS', 'tenant_000000', 't', NULL, NULL);
INSERT INTO "sys_dict"("data_dict_id", "dict_code", "dict_name", "dict_value", "dict_class", "seq", "pid", "dict_desc", "status", "created_time", "created_by", "updated_time", "updated_by", "tenant_id", "visibility", "catalog_id", "ext_properties") VALUES ('8f52f05a-8065-44c1-871e-82d5f4e5f577', 'DataSourceType', 'udbx', 'UDBX', NULL, 2, 'b06615e0-0d2c-43e1-a0a3-c8a50baaa539', NULL, 't', '2021-02-05 06:18:30.631138', 'SYS', '2021-03-19 06:05:07.891295', 'SYS', 'tenant_000000', 't', NULL, NULL);
INSERT INTO "sys_dict"("data_dict_id", "dict_code", "dict_name", "dict_value", "dict_class", "seq", "pid", "dict_desc", "status", "created_time", "created_by", "updated_time", "updated_by", "tenant_id", "visibility", "catalog_id", "ext_properties") VALUES ('dc18737e-a2b5-40c3-9054-6e1d9efd5c45', 'DataSourceType', 'PostgreSQL', 'POSTGRESQL', NULL, 1, 'ad8e1c82-c5bd-468b-b09f-2f7a5739fe0e', NULL, 't', '2021-02-05 06:19:38.195326', 'SYS', '2021-02-07 03:17:29.454735', 'SYS', 'tenant_000000', 't', NULL, NULL);
INSERT INTO "sys_dict"("data_dict_id", "dict_code", "dict_name", "dict_value", "dict_class", "seq", "pid", "dict_desc", "status", "created_time", "created_by", "updated_time", "updated_by", "tenant_id", "visibility", "catalog_id", "ext_properties") VALUES ('5aa40a68-85f2-4697-a5b0-011869b19185', 'DataSourceType', 'MySQL', 'MYSQL', NULL, 2, 'ad8e1c82-c5bd-468b-b09f-2f7a5739fe0e', NULL, 't', '2021-02-05 06:19:54.325459', 'SYS', '2021-02-07 03:17:59.639593', 'SYS', 'tenant_000000', 't', NULL, NULL);
INSERT INTO "sys_dict"("data_dict_id", "dict_code", "dict_name", "dict_value", "dict_class", "seq", "pid", "dict_desc", "status", "created_time", "created_by", "updated_time", "updated_by", "tenant_id", "visibility", "catalog_id", "ext_properties") VALUES ('81921ff4-f80e-42b5-ab9a-0f389ad82832', 'DataSourceType', 'Hbase', 'HBASE', NULL, 3, 'ad8e1c82-c5bd-468b-b09f-2f7a5739fe0e', NULL, 't', '2021-02-05 06:20:07.279098', 'SYS', '2021-02-07 03:17:45.061098', 'SYS', 'tenant_000000', 't', NULL, NULL);
INSERT INTO "sys_dict"("data_dict_id", "dict_code", "dict_name", "dict_value", "dict_class", "seq", "pid", "dict_desc", "status", "created_time", "created_by", "updated_time", "updated_by", "tenant_id", "visibility", "catalog_id", "ext_properties") VALUES ('5d82c362-775b-4847-95af-525ced496ae4', 'DataSourceType', 'MongoDB', 'MONGODB', NULL, 4, 'ad8e1c82-c5bd-468b-b09f-2f7a5739fe0e', NULL, 't', '2021-02-05 06:20:33.787232', 'SYS', '2021-03-19 06:47:01.002178', 'SYS', 'tenant_000000', 't', NULL, NULL);
INSERT INTO "sys_dict"("data_dict_id", "dict_code", "dict_name", "dict_value", "dict_class", "seq", "pid", "dict_desc", "status", "created_time", "created_by", "updated_time", "updated_by", "tenant_id", "visibility", "catalog_id", "ext_properties") VALUES ('d2e75d6f-5511-4968-956c-9a61083b0f85', 'DataSourceType', '数据源类型', NULL, NULL, 2, '0', NULL, 't', '2021-02-05 06:15:21.027105', 'SYS', '2021-02-05 06:15:21.027105', 'SYS', 'tenant_000000', 't', '64b6f34d-7f8e-42d8-b50c-590b653b2107', NULL);
INSERT INTO "sys_dict"("data_dict_id", "dict_code", "dict_name", "dict_value", "dict_class", "seq", "pid", "dict_desc", "status", "created_time", "created_by", "updated_time", "updated_by", "tenant_id", "visibility", "catalog_id", "ext_properties") VALUES ('97ba7659-f2ca-43ac-b7b7-3bb9ce5d5b18', 'DataSourceType', 'Oracle', 'ORACLE', NULL, 5, 'ad8e1c82-c5bd-468b-b09f-2f7a5739fe0e', NULL, 't', '2021-02-08 03:12:01.222949', 'SYS', '2021-02-08 03:12:01.222949', 'SYS', 'tenant_000000', 't', NULL, NULL);
INSERT INTO "sys_dict"("data_dict_id", "dict_code", "dict_name", "dict_value", "dict_class", "seq", "pid", "dict_desc", "status", "created_time", "created_by", "updated_time", "updated_by", "tenant_id", "visibility", "catalog_id", "ext_properties") VALUES ('49ed9d7c-3b8c-464c-8e17-bdd494484b54', 'DataSourceType', 'SqlServer', 'SQL_SERVER', NULL, 6, 'ad8e1c82-c5bd-468b-b09f-2f7a5739fe0e', NULL, 't', '2021-02-08 03:14:23.189784', 'SYS', '2021-02-08 03:14:23.189784', 'SYS', 'tenant_000000', 't', NULL, NULL);
INSERT INTO "sys_dict"("data_dict_id", "dict_code", "dict_name", "dict_value", "dict_class", "seq", "pid", "dict_desc", "status", "created_time", "created_by", "updated_time", "updated_by", "tenant_id", "visibility", "catalog_id", "ext_properties") VALUES ('1acc7ad0-2d57-4d4b-b879-591f62288644', 'DataSourceType', 'udb', 'UDB', NULL, 1, 'b06615e0-0d2c-43e1-a0a3-c8a50baaa539', NULL, 't', '2021-02-05 06:18:20.281141', 'SYS', '2021-03-19 06:04:43.691025', 'SYS', 'tenant_000000', 't', NULL, NULL);
