-- liquibase formatted sql logicalFilePath:ddl/proj_group

-- changeset SYS:20210406-0
CREATE TABLE "proj_group" ("proj_group_id" VARCHAR(36) NOT NULL, "tenant_id" VARCHAR(36) NOT NULL, "proj_group_name" VARCHAR(100) NOT NULL, "web_url" VARCHAR(200), "visibility" VARCHAR(20), "avatar_url" VARCHAR(200), "description" VARCHAR(500), "parent_id" VARCHAR(200), "created_time" TIMESTAMP WITHOUT TIME ZONE, "created_by" VARCHAR(255), "updated_time" TIMESTAMP WITHOUT TIME ZONE, "updated_by" VARCHAR(255), "org_id" VARCHAR(36), "org_code" VARCHAR(100), "brief_name" VARCHAR(100), "code_base_group_id" VARCHAR(100), CONSTRAINT "proj_group_pkey" PRIMARY KEY ("proj_group_id"));
COMMENT ON TABLE "proj_group" IS '工程组表';
COMMENT ON COLUMN "proj_group"."proj_group_id" IS '工程组id。主键,uuid';
COMMENT ON COLUMN "proj_group"."tenant_id" IS '租户id。';
COMMENT ON COLUMN "proj_group"."proj_group_name" IS '工程组名称。';
COMMENT ON COLUMN "proj_group"."web_url" IS '工程组url。http://域名/租户英文名简称/工程组名简称';
COMMENT ON COLUMN "proj_group"."visibility" IS '可见性。private：私有,public：公有
1：组织内(某级组织)，2：租户内，3：全局
（gitlab实现：1/2-private：私有,3-public：公有）';
COMMENT ON COLUMN "proj_group"."avatar_url" IS '头像。';
COMMENT ON COLUMN "proj_group"."description" IS '描述。';
COMMENT ON COLUMN "proj_group"."parent_id" IS '租户工程组id。由后端填充';
COMMENT ON COLUMN "proj_group"."created_time" IS '创建时间。';
COMMENT ON COLUMN "proj_group"."created_by" IS '创建人。';
COMMENT ON COLUMN "proj_group"."updated_time" IS '修改时间。';
COMMENT ON COLUMN "proj_group"."updated_by" IS '修改人。';
COMMENT ON COLUMN "proj_group"."org_id" IS '所属组织';
COMMENT ON COLUMN "proj_group"."org_code" IS '组织编码';
COMMENT ON COLUMN "proj_group"."brief_name" IS '工程组名缩写(工程组路径中使用)';
COMMENT ON COLUMN "proj_group"."code_base_group_id" IS '代码库工程组id';