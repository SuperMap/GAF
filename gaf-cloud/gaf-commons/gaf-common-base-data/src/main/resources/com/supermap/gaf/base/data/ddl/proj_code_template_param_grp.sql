-- liquibase formatted sql logicalFilePath:ddl/proj_code_template_param_grp

-- changeset SYS:20210406-0
CREATE TABLE "proj_code_template_param_grp" ("code_template_param_grp_id" VARCHAR(36) NOT NULL, "code_template_id" VARCHAR(36) NOT NULL, "param_group_name" VARCHAR(100) NOT NULL, "param_group_name_cn" VARCHAR(100) NOT NULL, "description" VARCHAR(500), "referred_group_id" VARCHAR(36), "created_time" TIMESTAMP WITHOUT TIME ZONE, "created_by" VARCHAR(255), "updated_time" TIMESTAMP WITHOUT TIME ZONE, "updated_by" VARCHAR(255), "is_required" BOOLEAN, "check_type" VARCHAR(3), CONSTRAINT "proj_code_template_param_grp_copy1_pkey" PRIMARY KEY ("code_template_param_grp_id"));
COMMENT ON TABLE "proj_code_template_param_grp" IS '代码模板参数分组表';
COMMENT ON COLUMN "proj_code_template_param_grp"."code_template_param_grp_id" IS '代码模板参数分组id。主键,uuid';
COMMENT ON COLUMN "proj_code_template_param_grp"."code_template_id" IS '模板id。该值为000000，则为参数样板分组';
COMMENT ON COLUMN "proj_code_template_param_grp"."param_group_name" IS '分组名称。';
COMMENT ON COLUMN "proj_code_template_param_grp"."param_group_name_cn" IS '参数中文名称。';
COMMENT ON COLUMN "proj_code_template_param_grp"."description" IS '描述。';
COMMENT ON COLUMN "proj_code_template_param_grp"."referred_group_id" IS '参考分组id。参考的样板分组id';
COMMENT ON COLUMN "proj_code_template_param_grp"."created_time" IS '创建时间。';
COMMENT ON COLUMN "proj_code_template_param_grp"."created_by" IS '创建人。';
COMMENT ON COLUMN "proj_code_template_param_grp"."updated_time" IS '修改时间。';
COMMENT ON COLUMN "proj_code_template_param_grp"."updated_by" IS '修改人。';
COMMENT ON COLUMN "proj_code_template_param_grp"."is_required" IS '是否必填。必填的分组，界面上默认选中';
COMMENT ON COLUMN "proj_code_template_param_grp"."check_type" IS '校验类型。自数据字典，1：数据源，2：缓存环境，3：文件存储...';