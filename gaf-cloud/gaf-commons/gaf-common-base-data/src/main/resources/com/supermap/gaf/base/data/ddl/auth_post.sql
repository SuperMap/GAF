-- liquibase formatted sql logicalFilePath:ddl/auth_post

-- changeset SYS:20210406-0
CREATE TABLE "auth_post" ("post_id" VARCHAR(36) NOT NULL, "tenant_id" VARCHAR(36) NOT NULL, "department_id" VARCHAR(36) NOT NULL, "sort_sn" INTEGER, "post_name" VARCHAR(255) NOT NULL, "name_en" VARCHAR(255), "code" VARCHAR(100), "status" BOOLEAN DEFAULT TRUE, "description" VARCHAR(500), "created_time" TIMESTAMP WITHOUT TIME ZONE, "created_by" VARCHAR(255), "updated_time" TIMESTAMP WITHOUT TIME ZONE, "updated_by" VARCHAR(255), CONSTRAINT "auth_post_pkey" PRIMARY KEY ("post_id"));
COMMENT ON TABLE "auth_post" IS '岗位表';
COMMENT ON COLUMN "auth_post"."post_id" IS '岗位id。主键,uuid';
COMMENT ON COLUMN "auth_post"."tenant_id" IS '租户id。';
COMMENT ON COLUMN "auth_post"."department_id" IS '部门id。';
COMMENT ON COLUMN "auth_post"."sort_sn" IS '排序序号。同级中的序号';
COMMENT ON COLUMN "auth_post"."post_name" IS '名称。中文名称';
COMMENT ON COLUMN "auth_post"."name_en" IS '英文名称。';
COMMENT ON COLUMN "auth_post"."code" IS '编码。部门编码+4位编码';
COMMENT ON COLUMN "auth_post"."status" IS '状态。true:有效，false:过期';
COMMENT ON COLUMN "auth_post"."description" IS '描述。';
COMMENT ON COLUMN "auth_post"."created_time" IS '创建时间。';
COMMENT ON COLUMN "auth_post"."created_by" IS '创建人。';
COMMENT ON COLUMN "auth_post"."updated_time" IS '修改时间。';
COMMENT ON COLUMN "auth_post"."updated_by" IS '修改人。';