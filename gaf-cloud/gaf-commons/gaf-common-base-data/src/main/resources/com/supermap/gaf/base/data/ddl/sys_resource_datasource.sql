-- liquibase formatted sql logicalFilePath:ddl/sys_resource_datasource

-- changeset SYS:20210406-0
CREATE TABLE "sys_resource_datasource" ("datasource_id" VARCHAR(36) NOT NULL, "ds_name" VARCHAR(100) NOT NULL, "type" VARCHAR(3), "sort_sn" INTEGER, "addr" VARCHAR(255), "port" INTEGER, "db_name" VARCHAR(255), "user_name" VARCHAR(100), "password" VARCHAR(100), "description" VARCHAR(500), "status" BOOLEAN, "created_time" TIMESTAMP WITHOUT TIME ZONE, "created_by" VARCHAR(50), "updated_time" TIMESTAMP WITHOUT TIME ZONE, "updated_by" VARCHAR(50), "tenant_id" VARCHAR(36), "type_code" VARCHAR(50), "catalog_code" VARCHAR(100), "region_code" VARCHAR(100), "time_attribute" TIMESTAMP WITHOUT TIME ZONE, "is_sdx" BOOLEAN, CONSTRAINT "sys_resource_datasource_pkey" PRIMARY KEY ("datasource_id"));
COMMENT ON TABLE "sys_resource_datasource" IS '数据源表';
COMMENT ON COLUMN "sys_resource_datasource"."datasource_id" IS '数据源id。主键,uuid';
COMMENT ON COLUMN "sys_resource_datasource"."ds_name" IS '数据源名称。';
COMMENT ON COLUMN "sys_resource_datasource"."type" IS '类型。(无用字段)';
COMMENT ON COLUMN "sys_resource_datasource"."sort_sn" IS '排序序号。';
COMMENT ON COLUMN "sys_resource_datasource"."addr" IS '地址。第1类文件路径，第2类服务器ip或域名';
COMMENT ON COLUMN "sys_resource_datasource"."port" IS '端口。';
COMMENT ON COLUMN "sys_resource_datasource"."db_name" IS '数据库名称。';
COMMENT ON COLUMN "sys_resource_datasource"."user_name" IS '用户名。';
COMMENT ON COLUMN "sys_resource_datasource"."password" IS '密码。（加密可解密）';
COMMENT ON COLUMN "sys_resource_datasource"."description" IS '描述。';
COMMENT ON COLUMN "sys_resource_datasource"."status" IS '状态。true:有效，false:无效';
COMMENT ON COLUMN "sys_resource_datasource"."created_time" IS '创建时间。生成时间不可变更';
COMMENT ON COLUMN "sys_resource_datasource"."created_by" IS '创建人。创建人user_id';
COMMENT ON COLUMN "sys_resource_datasource"."updated_time" IS '修改时间。修改时更新';
COMMENT ON COLUMN "sys_resource_datasource"."updated_by" IS '修改人。修改人user_id';
COMMENT ON COLUMN "sys_resource_datasource"."tenant_id" IS '所属租户。空则是公共的';
COMMENT ON COLUMN "sys_resource_datasource"."type_code" IS '数据源类型。选自数据字典:数据源类型)：
1.文件型-1:udb,2:udbx…
2.数据库型-1.PostgreSQL,2.MySQL,3.Hbase,4.MongoDB…';
COMMENT ON COLUMN "sys_resource_datasource"."catalog_code" IS '数据源分类。选自数据字典:数据源分类
自定义';
COMMENT ON COLUMN "sys_resource_datasource"."region_code" IS '行政区划。选自数据字典：行政区划，存各级code斜级分隔(level1code/level2code...)';
COMMENT ON COLUMN "sys_resource_datasource"."time_attribute" IS '时态。年月日,yyyyMMdd';
COMMENT ON COLUMN "sys_resource_datasource"."is_sdx" IS '是否空间数据库。true:是，false:否';

-- changeset SYS:20210723-0
ALTER TABLE sys_resource_datasource ADD is_template BOOLEAN default false;
COMMENT ON COLUMN "sys_resource_datasource"."is_template" IS '是否是空间数据库模板。true:是, false:否';