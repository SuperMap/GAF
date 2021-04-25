-- liquibase formatted sql logicalFilePath:ddl/auth_user_role

-- changeset SYS:20210406-0
CREATE TABLE "auth_user_role" ("user_role_id" VARCHAR(36) NOT NULL, "user_id" VARCHAR(36) NOT NULL, "role_id" VARCHAR(36) NOT NULL, "status" BOOLEAN DEFAULT TRUE, "sort_sn" INTEGER, "created_time" TIMESTAMP WITHOUT TIME ZONE, "created_by" VARCHAR(255), "updated_time" TIMESTAMP WITHOUT TIME ZONE, "updated_by" VARCHAR(255), "tenant_id" VARCHAR(36), CONSTRAINT "auth_user_role_pkey" PRIMARY KEY ("user_role_id"));
COMMENT ON TABLE "auth_user_role" IS '用户角色表';
COMMENT ON COLUMN "auth_user_role"."user_role_id" IS '用户角色id。主键,uuid';
COMMENT ON COLUMN "auth_user_role"."user_id" IS '用户id。';
COMMENT ON COLUMN "auth_user_role"."role_id" IS '角色id。';
COMMENT ON COLUMN "auth_user_role"."status" IS '状态。true:有效，false:无效';
COMMENT ON COLUMN "auth_user_role"."sort_sn" IS '排序序号。同级中的序号';
COMMENT ON COLUMN "auth_user_role"."created_time" IS '创建时间。生成时间不可变更';
COMMENT ON COLUMN "auth_user_role"."created_by" IS '创建人。创建人user_id';
COMMENT ON COLUMN "auth_user_role"."updated_time" IS '修改时间。修改时更新';
COMMENT ON COLUMN "auth_user_role"."updated_by" IS '修改人。修改人user_id';
COMMENT ON COLUMN "auth_user_role"."tenant_id" IS '所属租户。所属租户id';