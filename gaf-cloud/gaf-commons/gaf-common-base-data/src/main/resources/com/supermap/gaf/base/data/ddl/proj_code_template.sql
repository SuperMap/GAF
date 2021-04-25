-- liquibase formatted sql logicalFilePath:ddl/proj_code_template

-- changeset SYS:20210406-0
CREATE TABLE "proj_code_template" ("proj_code_template_id" VARCHAR(36) NOT NULL, "template_name" VARCHAR(100) NOT NULL, "template_name_cn" VARCHAR(100) NOT NULL, "template_type" SMALLINT NOT NULL, "description" VARCHAR(500), "version" VARCHAR(50), "template_path" VARCHAR(500), "created_time" TIMESTAMP WITHOUT TIME ZONE, "created_by" VARCHAR(255), "updated_time" TIMESTAMP WITHOUT TIME ZONE, "updated_by" VARCHAR(255), "tenant_id" VARCHAR(36), "org_id" VARCHAR(36), "org_code" VARCHAR(100), CONSTRAINT "proj_code_template_pkey" PRIMARY KEY ("proj_code_template_id"));
COMMENT ON TABLE "proj_code_template" IS '代码模板表';
COMMENT ON COLUMN "proj_code_template"."proj_code_template_id" IS '代码模板id。主键,uuid';
COMMENT ON COLUMN "proj_code_template"."template_name" IS '模板名称。';
COMMENT ON COLUMN "proj_code_template"."template_name_cn" IS '模板中文名称。';
COMMENT ON COLUMN "proj_code_template"."template_type" IS '模板类型。11:工程-前端，12：工程-后端，13：工程-前后端，21-表操作-前端，22：表操作-后端，23：表操作-前后端';
COMMENT ON COLUMN "proj_code_template"."description" IS '描述。';
COMMENT ON COLUMN "proj_code_template"."version" IS '版本。';
COMMENT ON COLUMN "proj_code_template"."template_path" IS '模板地址。';
COMMENT ON COLUMN "proj_code_template"."created_time" IS '创建时间。';
COMMENT ON COLUMN "proj_code_template"."created_by" IS '创建人。';
COMMENT ON COLUMN "proj_code_template"."updated_time" IS '修改时间。';
COMMENT ON COLUMN "proj_code_template"."updated_by" IS '修改人。';
COMMENT ON COLUMN "proj_code_template"."tenant_id" IS '租户id。';
COMMENT ON COLUMN "proj_code_template"."org_id" IS '所属组织。';
COMMENT ON COLUMN "proj_code_template"."org_code" IS '组织编码。';