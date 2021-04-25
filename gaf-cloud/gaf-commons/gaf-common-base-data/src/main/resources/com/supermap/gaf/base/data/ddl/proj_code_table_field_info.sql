-- liquibase formatted sql logicalFilePath:ddl/proj_code_table_field_info

-- changeset SYS:20210406-0
CREATE TABLE "proj_code_table_field_info" ("proj_code_table_field_id" VARCHAR(36) NOT NULL, "proj_table_id" VARCHAR(36) NOT NULL, "field_name" VARCHAR(100) NOT NULL, "field_name_cn" VARCHAR(100) NOT NULL, "type_db" VARCHAR(20) NOT NULL, "type_java" VARCHAR(20), "field_name_java" VARCHAR(100) NOT NULL, "is_pk" BOOLEAN, "is_required" BOOLEAN, "page_input_type" SMALLINT, "description" VARCHAR(500), "created_time" TIMESTAMP WITHOUT TIME ZONE, "created_by" VARCHAR(255), "updated_time" TIMESTAMP WITHOUT TIME ZONE, "updated_by" VARCHAR(255), "min_value" numeric(32, 4), "max_value" numeric(32, 4), "field_length" SMALLINT, CONSTRAINT "proj_code_table_field_info_pkey" PRIMARY KEY ("proj_code_table_field_id"));
COMMENT ON TABLE "proj_code_table_field_info" IS '代码生成的表字段信息表';
COMMENT ON COLUMN "proj_code_table_field_info"."proj_code_table_field_id" IS '工程表字段id。主键,uuid';
COMMENT ON COLUMN "proj_code_table_field_info"."proj_table_id" IS '工程表id。';
COMMENT ON COLUMN "proj_code_table_field_info"."field_name" IS '字段名称。';
COMMENT ON COLUMN "proj_code_table_field_info"."field_name_cn" IS '字段中文名称。';
COMMENT ON COLUMN "proj_code_table_field_info"."type_db" IS '字段类型。';
COMMENT ON COLUMN "proj_code_table_field_info"."type_java" IS 'Java类型。';
COMMENT ON COLUMN "proj_code_table_field_info"."field_name_java" IS 'Java字段名。';
COMMENT ON COLUMN "proj_code_table_field_info"."is_pk" IS '是否主键。';
COMMENT ON COLUMN "proj_code_table_field_info"."is_required" IS '是否必填。';
COMMENT ON COLUMN "proj_code_table_field_info"."page_input_type" IS '页面输入类型。1:文本框，2：文本域，3：下拉框，4：单选框，5：复选框，6：日期组件…';
COMMENT ON COLUMN "proj_code_table_field_info"."description" IS '描述。';
COMMENT ON COLUMN "proj_code_table_field_info"."created_time" IS '创建时间。';
COMMENT ON COLUMN "proj_code_table_field_info"."created_by" IS '创建人。';
COMMENT ON COLUMN "proj_code_table_field_info"."updated_time" IS '修改时间。';
COMMENT ON COLUMN "proj_code_table_field_info"."updated_by" IS '修改人。';
COMMENT ON COLUMN "proj_code_table_field_info"."min_value" IS '数值最小值';
COMMENT ON COLUMN "proj_code_table_field_info"."max_value" IS '数据最大值';
COMMENT ON COLUMN "proj_code_table_field_info"."field_length" IS '字段长度';