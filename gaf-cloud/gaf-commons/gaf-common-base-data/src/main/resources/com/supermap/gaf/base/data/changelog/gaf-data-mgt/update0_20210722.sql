-- liquibase formatted sql logicalFilePath:gaf-data-mgt/update0_20210722

-- changeset SYS:20210722-2
UPDATE "sys_resource_datasource" SET type_code = 'HBase' WHERE type_code = 'Hbase';
UPDATE "sys_resource_datasource" SET type_code = 'SQLPlus' WHERE type_code = 'SQL_SERVER';