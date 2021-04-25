-- liquibase formatted sql logicalFilePath:ddl/proj_code_base_user

-- changeset SYS:20210406-0
CREATE TABLE "proj_code_base_user" ("proj_code_base_user_id" VARCHAR(36) NOT NULL, "user_id" VARCHAR(36), "password" VARCHAR(30), "expiration_time" TIMESTAMP WITHOUT TIME ZONE, "status" BOOLEAN, "created_time" TIMESTAMP WITHOUT TIME ZONE, "created_by" VARCHAR(255), "updated_time" TIMESTAMP WITHOUT TIME ZONE, "updated_by" VARCHAR(255), "user_name" VARCHAR(255), "email" VARCHAR(255), "code_base_user_id" VARCHAR(100), CONSTRAINT "proj_code_base_user_pkey" PRIMARY KEY ("proj_code_base_user_id"));
COMMENT ON TABLE "proj_code_base_user" IS '代码库成员表';
COMMENT ON COLUMN "proj_code_base_user"."proj_code_base_user_id" IS '工程组成员id。主键,uuid';
COMMENT ON COLUMN "proj_code_base_user"."user_id" IS '用户id。代码库用户名与gaf用户名一样';
COMMENT ON COLUMN "proj_code_base_user"."password" IS '代码库密码。用于代码库pull/push代码使用（用户登录gaf可重置，也可不做保存）';
COMMENT ON COLUMN "proj_code_base_user"."expiration_time" IS '过期时间。';
COMMENT ON COLUMN "proj_code_base_user"."status" IS '状态。true:有效，false:无效';
COMMENT ON COLUMN "proj_code_base_user"."created_time" IS '创建时间。授权时间';
COMMENT ON COLUMN "proj_code_base_user"."created_by" IS '创建人。';
COMMENT ON COLUMN "proj_code_base_user"."updated_time" IS '修改时间。最近变更授权时间';
COMMENT ON COLUMN "proj_code_base_user"."updated_by" IS '修改人。';
COMMENT ON COLUMN "proj_code_base_user"."user_name" IS '用户名称。';
COMMENT ON COLUMN "proj_code_base_user"."email" IS '邮箱。';
COMMENT ON COLUMN "proj_code_base_user"."code_base_user_id" IS '代码库用户id。';