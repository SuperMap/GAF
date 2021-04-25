-- liquibase formatted sql logicalFilePath:ddl/proj_info

-- changeset SYS:20210406-0
CREATE TABLE "proj_info" ("proj_id" VARCHAR(36) NOT NULL, "proj_name" VARCHAR(100) NOT NULL, "description" VARCHAR(500), "proj_template_id" VARCHAR(36), "setting_params" VARCHAR(5000), "visibility" VARCHAR(25), "org_id" VARCHAR(36), "git_prj_group" VARCHAR(100), "git_url" VARCHAR(500), "status" SMALLINT, "created_time" TIMESTAMP WITHOUT TIME ZONE, "created_by" VARCHAR(255), "updated_time" TIMESTAMP WITHOUT TIME ZONE, "updated_by" VARCHAR(255), "template_name" VARCHAR(255), "git_proj_id" VARCHAR(100), "git_group_name" VARCHAR(100), "web_url" VARCHAR(500), "proj_group_id" VARCHAR(36), "code_base_proj_id" VARCHAR(100), CONSTRAINT "proj_info_pkey" PRIMARY KEY ("proj_id"));
COMMENT ON TABLE "proj_info" IS '工程信息表';
COMMENT ON COLUMN "proj_info"."proj_id" IS '工程id。主键,uuid';
COMMENT ON COLUMN "proj_info"."proj_name" IS '工程名称。';
COMMENT ON COLUMN "proj_info"."description" IS '工程描述。';
COMMENT ON COLUMN "proj_info"."proj_template_id" IS '工程模板id。';
COMMENT ON COLUMN "proj_info"."setting_params" IS '工程设置(参数)。map json格式{"key1":"value1",...}';
COMMENT ON COLUMN "proj_info"."visibility" IS '权限。private:私有,public:公有';
COMMENT ON COLUMN "proj_info"."org_id" IS '所属组织。';
COMMENT ON COLUMN "proj_info"."git_prj_group" IS '(不用该字段)工程组。';
COMMENT ON COLUMN "proj_info"."git_url" IS '(需变为用web_url)代码地址。';
COMMENT ON COLUMN "proj_info"."status" IS '状态。1:保存设置未生成工程代码，2：已生成工程代码';
COMMENT ON COLUMN "proj_info"."created_time" IS '创建时间。';
COMMENT ON COLUMN "proj_info"."created_by" IS '创建人。';
COMMENT ON COLUMN "proj_info"."updated_time" IS '修改时间。';
COMMENT ON COLUMN "proj_info"."updated_by" IS '修改人。';
COMMENT ON COLUMN "proj_info"."template_name" IS '模板名称。';
COMMENT ON COLUMN "proj_info"."git_proj_id" IS '(需变为用code_base_proj_id)gitlab工程id，与工程id一一对应。';
COMMENT ON COLUMN "proj_info"."git_group_name" IS '(不用该字段)';
COMMENT ON COLUMN "proj_info"."web_url" IS '代码地址。';
COMMENT ON COLUMN "proj_info"."proj_group_id" IS '工程组id。开发者仅可选取他所属的工程组';
COMMENT ON COLUMN "proj_info"."code_base_proj_id" IS '代码库工程id。';