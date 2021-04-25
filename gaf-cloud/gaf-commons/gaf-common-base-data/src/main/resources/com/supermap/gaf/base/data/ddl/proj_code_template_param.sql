-- liquibase formatted sql logicalFilePath:ddl/proj_code_template_param

-- changeset SYS:20210406-0
CREATE TABLE "proj_code_template_param" ("proj_code_template_param_id" VARCHAR(36) NOT NULL, "code_template_id" VARCHAR(36) NOT NULL, "param_name" VARCHAR(100) NOT NULL, "param_name_cn" VARCHAR(100) NOT NULL, "description" VARCHAR(500), "default_value" VARCHAR(200), "created_time" TIMESTAMP WITHOUT TIME ZONE, "created_by" VARCHAR(255), "updated_time" TIMESTAMP WITHOUT TIME ZONE, "updated_by" VARCHAR(255), "param_group_id" VARCHAR(36), CONSTRAINT "proj_code_template_param_pkey" PRIMARY KEY ("proj_code_template_param_id"));
COMMENT ON TABLE "proj_code_template_param" IS '代码模板参数表';
COMMENT ON COLUMN "proj_code_template_param"."proj_code_template_param_id" IS '代码模板参数id。主键,uuid';
COMMENT ON COLUMN "proj_code_template_param"."code_template_id" IS '模板id。该值为000000，则为参数样板';
COMMENT ON COLUMN "proj_code_template_param"."param_name" IS '参数名称。';
COMMENT ON COLUMN "proj_code_template_param"."param_name_cn" IS '参数中文名称。';
COMMENT ON COLUMN "proj_code_template_param"."description" IS '描述。';
COMMENT ON COLUMN "proj_code_template_param"."default_value" IS '默认值。';
COMMENT ON COLUMN "proj_code_template_param"."created_time" IS '创建时间。';
COMMENT ON COLUMN "proj_code_template_param"."created_by" IS '创建人。';
COMMENT ON COLUMN "proj_code_template_param"."updated_time" IS '修改时间。';
COMMENT ON COLUMN "proj_code_template_param"."updated_by" IS '修改人。';
COMMENT ON COLUMN "proj_code_template_param"."param_group_id" IS '配置参数分组id。';