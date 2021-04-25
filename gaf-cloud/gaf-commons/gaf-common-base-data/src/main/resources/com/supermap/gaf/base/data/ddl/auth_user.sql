-- liquibase formatted sql logicalFilePath:ddl/auth_user

-- changeset SYS:20210406-0
CREATE TABLE "auth_user" ("user_id" VARCHAR(36) NOT NULL, "tenant_id" VARCHAR(36) NOT NULL, "department_id" VARCHAR(36) NOT NULL, "sort_sn" INTEGER, "name" VARCHAR(50) NOT NULL, "password" VARCHAR(100), "real_name" VARCHAR(50) NOT NULL, "id_number" VARCHAR(30), "mobile_number" VARCHAR(30), "email" VARCHAR(100), "address" VARCHAR(200), "post_id" VARCHAR(36), "expiration_time" TIMESTAMP WITHOUT TIME ZONE, "is_third_party" BOOLEAN, "status" BOOLEAN DEFAULT TRUE, "description" VARCHAR(500), "last_login_time" TIMESTAMP WITHOUT TIME ZONE, "created_time" TIMESTAMP WITHOUT TIME ZONE, "created_by" VARCHAR(255), "updated_time" TIMESTAMP WITHOUT TIME ZONE, "updated_by" VARCHAR(255), CONSTRAINT "auth_user_pkey" PRIMARY KEY ("user_id"));
COMMENT ON TABLE "auth_user" IS '用户表';
COMMENT ON COLUMN "auth_user"."user_id" IS '用户id。主键,uuid';
COMMENT ON COLUMN "auth_user"."tenant_id" IS '租户id。';
COMMENT ON COLUMN "auth_user"."department_id" IS '部门id。';
COMMENT ON COLUMN "auth_user"."sort_sn" IS '排序序号。同级中的序号';
COMMENT ON COLUMN "auth_user"."name" IS '登录用户名。登录名，可含字母数字下划线';
COMMENT ON COLUMN "auth_user"."password" IS '密码。存到认证中心，db可不存';
COMMENT ON COLUMN "auth_user"."real_name" IS '真实姓名。中文名称';
COMMENT ON COLUMN "auth_user"."id_number" IS '身份证号。';
COMMENT ON COLUMN "auth_user"."mobile_number" IS '手机号。';
COMMENT ON COLUMN "auth_user"."email" IS '邮箱。';
COMMENT ON COLUMN "auth_user"."address" IS '地址。';
COMMENT ON COLUMN "auth_user"."post_id" IS '岗位id。';
COMMENT ON COLUMN "auth_user"."expiration_time" IS '授权截止时间。为空则永不过期';
COMMENT ON COLUMN "auth_user"."is_third_party" IS '是否第三方。从所属部门继承该属性';
COMMENT ON COLUMN "auth_user"."status" IS '状态。true:有效，false:过期/无效';
COMMENT ON COLUMN "auth_user"."description" IS '描述。';
COMMENT ON COLUMN "auth_user"."last_login_time" IS '上次登录时间。';
COMMENT ON COLUMN "auth_user"."created_time" IS '创建时间。生成时间不可变更';
COMMENT ON COLUMN "auth_user"."created_by" IS '创建人。创建人user_id';
COMMENT ON COLUMN "auth_user"."updated_time" IS '修改时间。修改时更新';
COMMENT ON COLUMN "auth_user"."updated_by" IS '修改人。修改人user_id';