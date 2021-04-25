-- liquibase formatted sql logicalFilePath:ddl/auth_post_role

-- changeset SYS:20210406-0
CREATE TABLE "auth_post_role" ("post_role_id" VARCHAR(36) NOT NULL, "post_id" VARCHAR(36) NOT NULL, "role_id" VARCHAR(36) NOT NULL, "created_time" TIMESTAMP WITHOUT TIME ZONE, "created_by" VARCHAR(255), "updated_time" TIMESTAMP WITHOUT TIME ZONE, "updated_by" VARCHAR(255), "sort_sn" INTEGER, "status" BOOLEAN DEFAULT TRUE, CONSTRAINT "auth_post_role_pkey" PRIMARY KEY ("post_role_id"));
COMMENT ON TABLE "auth_post_role" IS '岗位角色表';
COMMENT ON COLUMN "auth_post_role"."post_role_id" IS '岗位角色id。主键,uuid';
COMMENT ON COLUMN "auth_post_role"."post_id" IS '岗位id。';
COMMENT ON COLUMN "auth_post_role"."role_id" IS '角色id。';
COMMENT ON COLUMN "auth_post_role"."created_time" IS '创建时间。生成时间不可变更';
COMMENT ON COLUMN "auth_post_role"."created_by" IS '创建人。创建人user_id';
COMMENT ON COLUMN "auth_post_role"."updated_time" IS '修改时间。修改时更新';
COMMENT ON COLUMN "auth_post_role"."updated_by" IS '修改人。修改人user_id';
COMMENT ON COLUMN "auth_post_role"."sort_sn" IS '排序序号。';
COMMENT ON COLUMN "auth_post_role"."status" IS '状态。true:有效，false:无效';