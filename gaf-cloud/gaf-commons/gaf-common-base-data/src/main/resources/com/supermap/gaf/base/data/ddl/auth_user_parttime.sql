-- liquibase formatted sql logicalFilePath:ddl/auth_user_parttime

-- changeset SYS:20210406-0
CREATE TABLE "auth_user_parttime" ("user_parttime_id" VARCHAR(36) NOT NULL, "user_id" VARCHAR(36) NOT NULL, "department_id" VARCHAR(36) NOT NULL, "post_id" VARCHAR(36) NOT NULL, "post_type" VARCHAR(3), "expiration_time" TIMESTAMP WITHOUT TIME ZONE, "status" BOOLEAN DEFAULT TRUE, "sort_sn" INTEGER, "created_time" TIMESTAMP WITHOUT TIME ZONE, "created_by" VARCHAR(255), "updated_time" TIMESTAMP WITHOUT TIME ZONE, "updated_by" VARCHAR(255), "tenant_id" VARCHAR(36) NOT NULL, CONSTRAINT "auth_user_parttime_pkey" PRIMARY KEY ("user_parttime_id"));
COMMENT ON TABLE "auth_user_parttime" IS '用户挂职表';
COMMENT ON COLUMN "auth_user_parttime"."user_parttime_id" IS '用户挂职id。主键,uuid';
COMMENT ON COLUMN "auth_user_parttime"."user_id" IS '用户id。';
COMMENT ON COLUMN "auth_user_parttime"."department_id" IS '挂职部门id。';
COMMENT ON COLUMN "auth_user_parttime"."post_id" IS '岗位id。';
COMMENT ON COLUMN "auth_user_parttime"."post_type" IS '岗位类别。存字典code,定义：实习、研发、运维、管理…';
COMMENT ON COLUMN "auth_user_parttime"."expiration_time" IS '授权截止时间。为空则永不过期';
COMMENT ON COLUMN "auth_user_parttime"."status" IS '状态。true:有效，false:无效';
COMMENT ON COLUMN "auth_user_parttime"."sort_sn" IS '排序序号。同级中的序号';
COMMENT ON COLUMN "auth_user_parttime"."created_time" IS '创建时间。生成时间不可变更';
COMMENT ON COLUMN "auth_user_parttime"."created_by" IS '创建人。创建人user_id';
COMMENT ON COLUMN "auth_user_parttime"."updated_time" IS '修改时间。修改时更新';
COMMENT ON COLUMN "auth_user_parttime"."updated_by" IS '修改人。修改人user_id';
COMMENT ON COLUMN "auth_user_parttime"."tenant_id" IS '挂职租户。租户id';