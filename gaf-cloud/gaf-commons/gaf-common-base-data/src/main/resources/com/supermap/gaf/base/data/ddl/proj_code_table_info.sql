-- liquibase formatted sql logicalFilePath:ddl/proj_code_table_info

-- changeset SYS:20210406-0
CREATE TABLE "proj_code_table_info" ("proj_code_table_id" VARCHAR(36) NOT NULL, "proj_id" VARCHAR(36), "table_name" VARCHAR(100) NOT NULL, "table_name_cn" VARCHAR(100) NOT NULL, "entity_cls_name" VARCHAR(100) NOT NULL, "author" VARCHAR(36), "table_template_id" VARCHAR(36) NOT NULL, "status" SMALLINT, "codegen_time" TIMESTAMP WITHOUT TIME ZONE, "description" VARCHAR(500), "created_time" TIMESTAMP WITHOUT TIME ZONE, "created_by" VARCHAR(255), "updated_time" TIMESTAMP WITHOUT TIME ZONE, "updated_by" VARCHAR(255), CONSTRAINT "proj_code_table_info_pkey" PRIMARY KEY ("proj_code_table_id"));
COMMENT ON TABLE "proj_code_table_info" IS '代码生成的表信息表';
COMMENT ON COLUMN "proj_code_table_info"."proj_code_table_id" IS '工程表id。主键,uuid';
COMMENT ON COLUMN "proj_code_table_info"."proj_id" IS '工程id。';
COMMENT ON COLUMN "proj_code_table_info"."table_name" IS '表名称。';
COMMENT ON COLUMN "proj_code_table_info"."table_name_cn" IS '表中文名称。';
COMMENT ON COLUMN "proj_code_table_info"."entity_cls_name" IS '实体类名称。';
COMMENT ON COLUMN "proj_code_table_info"."author" IS '作者。';
COMMENT ON COLUMN "proj_code_table_info"."table_template_id" IS '代码模板id。';
COMMENT ON COLUMN "proj_code_table_info"."status" IS '状态。1:保存设置未生成代码，2：已生成代码';
COMMENT ON COLUMN "proj_code_table_info"."codegen_time" IS '生成代码时间。允许多次生成的操作';
COMMENT ON COLUMN "proj_code_table_info"."description" IS '描述。';
COMMENT ON COLUMN "proj_code_table_info"."created_time" IS '创建时间。';
COMMENT ON COLUMN "proj_code_table_info"."created_by" IS '创建人。';
COMMENT ON COLUMN "proj_code_table_info"."updated_time" IS '修改时间。';
COMMENT ON COLUMN "proj_code_table_info"."updated_by" IS '修改人。';