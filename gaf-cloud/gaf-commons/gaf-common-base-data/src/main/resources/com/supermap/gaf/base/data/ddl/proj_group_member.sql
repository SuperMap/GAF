-- liquibase formatted sql logicalFilePath:ddl/proj_group_member

-- changeset SYS:20210406-0
CREATE TABLE "proj_group_member" ("proj_group_member_id" VARCHAR(36) NOT NULL, "proj_group_id" VARCHAR(36) NOT NULL, "user_id" VARCHAR(36) NOT NULL, "proj_role" SMALLINT NOT NULL, "expiration_time" TIMESTAMP WITHOUT TIME ZONE, "status" BOOLEAN, "created_time" TIMESTAMP WITHOUT TIME ZONE, "created_by" VARCHAR(255), "updated_time" TIMESTAMP WITHOUT TIME ZONE, "updated_by" VARCHAR(255), CONSTRAINT "proj_group_member_pkey" PRIMARY KEY ("proj_group_member_id"));
COMMENT ON TABLE "proj_group_member" IS '工程组成员表';
COMMENT ON COLUMN "proj_group_member"."proj_group_member_id" IS '工程组成员id。主键,uuid';
COMMENT ON COLUMN "proj_group_member"."proj_group_id" IS '工程组id。';
COMMENT ON COLUMN "proj_group_member"."user_id" IS '成员id。';
COMMENT ON COLUMN "proj_group_member"."proj_role" IS '工程角色。1:Reporter报告者,2:Developer开发者，3:Maintainer维护者,4:Owner所有者';
COMMENT ON COLUMN "proj_group_member"."expiration_time" IS '过期时间。';
COMMENT ON COLUMN "proj_group_member"."status" IS '状态。true:有效，false:无效';
COMMENT ON COLUMN "proj_group_member"."created_time" IS '创建时间。授权时间';
COMMENT ON COLUMN "proj_group_member"."created_by" IS '创建人。';
COMMENT ON COLUMN "proj_group_member"."updated_time" IS '修改时间。最近变更授权时间';
COMMENT ON COLUMN "proj_group_member"."updated_by" IS '修改人。';